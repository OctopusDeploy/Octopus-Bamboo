package com.octopus.bamboo.plugins.task.createrelease;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.google.common.base.Optional;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.domain.Project;
import com.octopus.domain.Release;
import com.octopus.domain.SelectedPackages;
import com.octopus.exception.ConfigurationException;
import com.octopus.services.CommonTaskService;
import com.octopus.services.FeignService;
import com.octopus.services.LookupService;
import feign.FeignException;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * The Bamboo Task that is used to create releases in Octopus Deploy
 */
@Component
@ExportAsService({CreateReleaseTask.class})
@Named("createReleaseTask")
public class CreateReleaseTask implements TaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReleaseTask.class);
    private final FeignService feignService;
    private final CommonTaskService commonTaskService;
    private final LookupService lookupService;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param feignService      The service that is used to create feign clients
     * @param commonTaskService The service used for common task operations
     */
    @Inject
    public CreateReleaseTask(@NotNull final FeignService feignService,
                             @NotNull final CommonTaskService commonTaskService,
                             @NotNull final LookupService lookupService) {
        checkNotNull(feignService, "feignService cannot be null");
        checkNotNull(commonTaskService, "commonTaskService cannot be null");

        this.feignService = feignService;
        this.commonTaskService = commonTaskService;
        this.lookupService = lookupService;
    }


    @NotNull
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

        final String projectName = taskContext.getConfigurationMap().get(OctoConstants.PROJECT_NAME);
        final String channelName = taskContext.getConfigurationMap().get(OctoConstants.CHANNEL_NAME);
        final String releaseVersion = taskContext.getConfigurationMap().get(OctoConstants.RELEASE_VERSION);
        final Boolean ignoreExisting = BooleanUtils.toBooleanObject(
                taskContext.getConfigurationMap().get(OctoConstants.IGNORE_EXISTING_RELEASE_NAME));

        checkState(StringUtils.isNotBlank(projectName));

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
        final RestAPI restAPI = feignService.createClient(taskContext, false);

        try {
            /*
                Find the project and channel ids that we need to create a release
             */
            final Project project = populateProjectAndChannelID(taskContext, release, projectName, channelName);

            /*
                See if a release already exists
             */
            final Optional<Release> existingRelease = lookupService.getRelease(taskContext, releaseVersion, project);

            if (existingRelease.isPresent()) {
                if (BooleanUtils.isTrue(ignoreExisting)) {
                    return commonTaskService.buildResult(taskContext, true);
                } else {
                    commonTaskService.logError(
                            taskContext,
                            "OCTOPUS-BAMBOO-INPUT-ERROR-0007: The release version already exists, and the ignore existing releases option was not selected.");
                    return commonTaskService.buildResult(taskContext, false);
                }
            }

            /*
                Set the package versions for the steps associated with the project
             */
            lookupService.populateSelectedPackages(taskContext, release, project);

            /*
                Create the release
             */
            restAPI.createRelease(false, release);


            /*
                All went well, so return a successful result
             */
            return commonTaskService.buildResult(taskContext, true);
        } catch (final ConfigurationException | IllegalStateException ex) {
            taskContext.getBuildLogger().addErrorLogEntry(ex.getMessage());
            return commonTaskService.buildResult(taskContext, false);
        } catch (final FeignException ex) {
            commonTaskService.logError(taskContext,
                    "OCTOPUS-BAMBOO-INPUT-ERROR-0004: The release could not be created. "
                            + "Make sure the release version number of \"" + releaseVersion + "\" is a "
                            + "valid semver version string.");
            commonTaskService.logError(taskContext, ex.toString());
            return commonTaskService.buildResult(taskContext, false);
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
    private Project populateProjectAndChannelID(@NotNull final TaskContext taskContext,
                                                @NotNull final Release release,
                                                @NotNull final String projectName,
                                                final String channelName) {
        checkNotNull(taskContext);
        checkNotNull(release);
        checkArgument(StringUtils.isNotBlank(projectName));

        final Optional<Project> project = lookupService.getProject(taskContext, projectName);
        if (!project.isPresent()) {
            throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0001: Project named " + projectName + " was not found");
        }
        release.setProjectId(project.get().getId());

        if (StringUtils.isNotBlank(channelName)) {
            final Optional<String> channelId = lookupService.getChannel(taskContext, project.get(), channelName);
            if (!channelId.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0002: Channel named " + channelName + " was not found");
            }
            release.setChannelId(channelId.get());
        } else {
            final Optional<String> channelId = lookupService.getDefaultChannel(taskContext, project.get());
            if (!channelId.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0003: Default channel ID was not found");
            }
            release.setChannelId(channelId.get());
        }

        return project.get();
    }
}
