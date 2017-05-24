package com.octopus.services.impl;

import com.amazonaws.util.IOUtils;
import com.google.common.base.Optional;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.domain.*;
import com.octopus.exception.ConfigurationException;
import com.octopus.services.*;
import feign.FeignException;
import feign.Logger;
import feign.Response;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * Implementation of the Octopus client
 */
@Component
public class OctopusClientImpl implements OctopusClient {

    private final FeignService feignService;
    private final FileService fileService;
    private final LookupService lookupService;
    private final CommonTaskService commonTaskService;

    @Inject
    public OctopusClientImpl(@NotNull final FeignService feignService,
                             @NotNull final FileService fileService,
                             @NotNull final LookupService lookupService,
                             @NotNull final CommonTaskService commonTaskService) {
        checkNotNull(feignService, "feignService cannot be null");
        checkNotNull(fileService, "fileService cannot be null");
        checkNotNull(commonTaskService, "commonTaskService cannot be null");
        checkNotNull(lookupService, "lookupService cannot be null");

        this.feignService = feignService;
        this.fileService = fileService;
        this.commonTaskService = commonTaskService;
        this.lookupService = lookupService;
    }

    public boolean pushPackage(@NotNull final LoggerService logger,
                               @NotNull final feign.Logger buildLogger,
                               @NotNull final String host,
                               @NotNull final String apiKey,
                               @NotNull final String pattern,
                               @NotNull final File workingDirectory,
                               final boolean forceUpload,
                               final boolean verboseLogging) {
        checkNotNull(logger);
        checkNotNull(buildLogger);
        checkNotNull(workingDirectory);
        checkArgument(StringUtils.isNotBlank(host));
        checkArgument(StringUtils.isNotBlank(apiKey));
        checkArgument(StringUtils.isNotBlank(pattern));


        /*
            Create the API client that we will use to call the Octopus REST API.
            We allow any request that is forced to be retried, as this request
            is essentially idempotent.
         */
        final RestAPI restAPI = feignService.createClient(
                buildLogger,
                host,
                apiKey,
                verboseLogging,
                forceUpload);

        final List<File> files = fileService.getMatchingFile(workingDirectory, pattern);

        /*
            Fail if no files were matched
         */
        if (files.isEmpty()) {
            logger.logError("OCTOPUS-BAMBOO-ERROR-0003: The pattern " + pattern
                    + " failed to match any files");
            return false;
        }

        /*
            Contact the API to upload the files
         */
        try {
            for (final File file : files) {
                final Response result = restAPI.packagesRaw(forceUpload, file);

                /*
                    Mocked responses have no body
                 */
                if (result.body() != null) {
                    final String body = IOUtils.toString(result.body().asInputStream());
                    logger.logMessage(body);
                }

                /*
                    Make sure the response code indicates success
                 */
                if (result.status() < OctoConstants.START_HTTP_OK_RANGE || result.status() > OctoConstants.END_HTTP_OK_RANGE) {
                    if (result.status() == HttpStatus.SC_UNAUTHORIZED) {
                        logger.logError("OCTOPUS-BAMBOO-ERROR-0001: Status code "
                                + result.status()
                                + " indicates an authorization error! Make sure the API key is correct.");
                    } else if (result.status() == HttpStatus.SC_CONFLICT) {
                        logger.logError("OCTOPUS-BAMBOO-ERROR-0005: Status code "
                                + result.status()
                                + " most likely means you are trying to push a file that already exists, "
                                + " and you have not enabled the \"Force the package upload\" option!");
                    } else {
                        logger.logError("OCTOPUS-BAMBOO-ERROR-0002: Status code "
                                + result.status()
                                + " indicates an error!");
                    }

                    return false;
                }
            }
        } catch (final Exception ex) {
            /*
                Any upload errors mean this task has failed.
             */
            logger.logError("OCTOPUS-BAMBOO-ERROR-0008: The package could not be pushed.");
            logger.logError(ex.toString());
            return false;
        }

        /*
            We're all good, so let bamboo know that the task was a success.
         */
        return true;
    }

    @Override
    public Optional<Release> createRelease(@NotNull LoggerService logger,
                                           @NotNull Logger buildLogger,
                                           @NotNull String host,
                                           @NotNull String apiKey,
                                           @NotNull String projectName,
                                           String channelName,
                                           @NotNull String releaseVersion,
                                           boolean ignoreExisting,
                                           boolean verboseLogging) {
        checkNotNull(logger);
        checkNotNull(buildLogger);
        checkArgument(StringUtils.isNotBlank(host));
        checkArgument(StringUtils.isNotBlank(apiKey));
        checkState(StringUtils.isNotBlank(projectName));
        checkState(StringUtils.isNotBlank(releaseVersion));

        /*
            Build up the release info
         */
        final Release release = new Release();
        release.setVersion(releaseVersion);
        final List<SelectedPackages> selectedPackages = new ArrayList<SelectedPackages>();
        release.setSelectedPackages(selectedPackages);

        /*
            Create the API client that we will use to call the Octopus REST API.
            We allow any request that includes a version number to be retried, as this request
            is essentially idempotent.
         */
        final RestAPI restAPI = feignService.createClient(
                buildLogger,
                host,
                apiKey,
                verboseLogging,
                false);

        try {
            /*
                Find the project and channel ids that we need to create a release
             */
            final Project project = populateProjectAndChannelID(logger,
                    buildLogger,
                    host,
                    apiKey,
                    release,
                    projectName,
                    channelName,
                    verboseLogging);

            /*
                See if a release already exists
             */
            final Optional<Release> existingRelease = lookupService.getRelease(
                    logger,
                    buildLogger,
                    host,
                    apiKey,
                    releaseVersion,
                    project,
                    verboseLogging);

            if (existingRelease.isPresent()) {
                if (BooleanUtils.isTrue(ignoreExisting)) {
                    return existingRelease;
                } else {
                    logger.logError("OCTOPUS-BAMBOO-INPUT-ERROR-0007: "
                            + "The release version already exists, "
                            + "and the ignore existing releases option was not selected.");
                    return Optional.absent();
                }
            }

            /*
                Set the package versions for the steps associated with the project
             */
            lookupService.populateSelectedPackages(logger,
                    buildLogger,
                    host,
                    apiKey,
                    release,
                    project,
                    verboseLogging);

            /*
                Create the release
             */
            final Release releaseEntity =
                    restAPI.createRelease(false, release);


            /*
                All went well, so return a successful result
             */
            return Optional.of(releaseEntity);
        } catch (final ConfigurationException | IllegalStateException ex) {
            logger.logError(ex.getMessage());

        } catch (final FeignException ex) {
            logger.logError(
                    "OCTOPUS-BAMBOO-INPUT-ERROR-0004: The release could not be created. "
                            + "Make sure the release version number of \"" + releaseVersion + "\" is a "
                            + "valid semver version string.");
            logger.logError(ex.toString());
        }

        return Optional.absent();
    }

    @Override
    public boolean deployRelease(@NotNull final LoggerService logger,
                                 @NotNull final Logger buildLogger,
                                 @NotNull final String host,
                                 @NotNull final String apiKey,
                                 @NotNull final String projectName,
                                 @NotNull final String environmentName,
                                 @NotNull final String releaseVersion,
                                 final boolean verboseLogging) {
        checkNotNull(logger);
        checkNotNull(buildLogger);
        checkArgument(StringUtils.isNotBlank(host));
        checkArgument(StringUtils.isNotBlank(apiKey));
        checkArgument(StringUtils.isNotBlank(projectName));
        checkArgument(StringUtils.isNotBlank(environmentName));
        checkArgument(StringUtils.isNotBlank(releaseVersion));

        try {
            /*
                Map project to id
             */
            final Optional<Project> project = lookupService.getProject(logger,
                    buildLogger,
                    host,
                    apiKey,
                    projectName,
                    verboseLogging);
            if (!project.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0001: Project named " + projectName + " was not found");
            }

            /*
                Match release to id
             */
            final Optional<Release> release = lookupService.getRelease(logger,
                    buildLogger,
                    host,
                    apiKey,
                    releaseVersion,
                    project.get(),
                    verboseLogging);
            if (!release.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0006: Release with version " + releaseVersion + " was not found");
            }

            /*
                Match environment name to id
             */
            final Optional<Environment> environmentEntity = lookupService.getEnvironment(logger,
                    buildLogger,
                    host,
                    apiKey,
                    environmentName,
                    verboseLogging);
            if (!environmentEntity.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0005: Environment named " + environmentName + " was not found");
            }

            /*
                Create the deployment
             */

            final Deployment deployment = new Deployment();
            deployment.setReleaseId(release.get().getId());
            deployment.setEnvironmentId(environmentEntity.get().getId());

            final RestAPI restAPI = feignService.createClient(buildLogger,
                    host,
                    apiKey,
                    verboseLogging,
                    false);
            restAPI.createDeployment(deployment);

            return true;
        } catch (final ConfigurationException | IllegalStateException ex) {
            logger.logError(ex.getMessage());
            return false;
        } catch (final FeignException ex) {
            logger.logError("OCTOPUS-BAMBOO-ERROR-0007: The release could not be deployed.");
            logger.logError(ex.toString());
            return false;
        }
    }

    /**
     * Map the project and channel names to their IDs
     *
     * @param taskContext The bamboo task context
     * @param release     The release to be populated with the id values
     * @param projectName The name of the project
     * @param channelName The name of the channel
     */
    private Project populateProjectAndChannelID(@NotNull LoggerService logger,
                                                @NotNull Logger buildLogger,
                                                @NotNull String host,
                                                @NotNull String apiKey,
                                                @NotNull final Release release,
                                                @NotNull final String projectName,
                                                final String channelName,
                                                final boolean verboseLogging) {
        checkNotNull(logger);
        checkNotNull(buildLogger);
        checkArgument(StringUtils.isNotBlank(host));
        checkArgument(StringUtils.isNotBlank(apiKey));
        checkNotNull(release);
        checkArgument(StringUtils.isNotBlank(projectName));

        final Optional<Project> project = lookupService.getProject(logger,
                buildLogger,
                host,
                apiKey,
                projectName,
                verboseLogging);
        if (!project.isPresent()) {
            throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0001: Project named " + projectName + " was not found");
        }
        release.setProjectId(project.get().getId());

        if (StringUtils.isNotBlank(channelName)) {
            final Optional<String> channelId = lookupService.getChannel(logger,
                    buildLogger,
                    host,
                    apiKey,
                    project.get(),
                    channelName,
                    verboseLogging);
            if (!channelId.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0002: Channel named " + channelName + " was not found");
            }
            release.setChannelId(channelId.get());
        } else {
            final Optional<String> channelId = lookupService.getDefaultChannel(logger,
                    buildLogger,
                    host,
                    apiKey,
                    project.get(),
                    verboseLogging);
            if (!channelId.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0003: Default channel ID was not found");
            }
            release.setChannelId(channelId.get());
        }

        return project.get();
    }
}
