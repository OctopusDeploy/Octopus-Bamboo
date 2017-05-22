package com.octopus.bamboo.plugins.task.deployrelease;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.google.common.base.Optional;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.domain.Deployment;
import com.octopus.domain.Environment;
import com.octopus.domain.Project;
import com.octopus.domain.Release;
import com.octopus.exception.ConfigurationException;
import com.octopus.services.CommonTaskService;
import com.octopus.services.FeignService;
import com.octopus.services.LookupService;
import feign.FeignException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Task used to deploy a release
 */
@Component
@ExportAsService({DeployReleaseTask.class})
@Named("createReleaseTask")
public class DeployReleaseTask implements TaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeployReleaseTask.class);
    private final FeignService feignService;
    private final CommonTaskService commonTaskService;
    private final LookupService lookupService;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param feignService      The service that is used to create feign clients
     * @param commonTaskService The service used for common task operations
     * @param lookupService     The service used to perform common lookups
     */
    @Inject
    public DeployReleaseTask(@NotNull final FeignService feignService,
                             @NotNull final CommonTaskService commonTaskService,
                             @NotNull final LookupService lookupService) {
        checkNotNull(feignService, "feignService cannot be null");
        checkNotNull(commonTaskService, "commonTaskService cannot be null");
        checkNotNull(lookupService, "lookupService cannot be null");

        this.feignService = feignService;
        this.commonTaskService = commonTaskService;
        this.lookupService = lookupService;
    }

    @NotNull
    @Override
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException {

        try {
            final String projectName = taskContext.getConfigurationMap().get(OctoConstants.PROJECT_NAME);
            final String environmentName = taskContext.getConfigurationMap().get(OctoConstants.ENVIRONMENT_NAME);
            final String releaseVersion = taskContext.getConfigurationMap().get(OctoConstants.RELEASE_VERSION);

            /*
                Map project to id
             */
            final Optional<Project> project = lookupService.getProject(taskContext, projectName);
            if (!project.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0001: Project named " + projectName + " was not found");
            }

            /*
                Match environment name to id
             */
            final Optional<Environment> environment = lookupService.getEnvironment(taskContext, environmentName);
            if (!environment.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0005: Environment named " + environmentName + " was not found");
            }

            /*
                Match release to id
             */
            final Optional<Release> release = lookupService.getRelease(taskContext, releaseVersion, project.get());
            if (!release.isPresent()) {
                throw new ConfigurationException("OCTOPUS-BAMBOO-INPUT-ERROR-0006: Release with version " + releaseVersion + " was not found");
            }

            /*
                Create the deployment
             */

            final Deployment deployment = new Deployment();
            deployment.setReleaseId(release.get().getId());
            deployment.setEnvironmentId(environment.get().getId());

            final RestAPI restAPI = feignService.createClient(taskContext, false);
            restAPI.createDeployment(deployment);

            return commonTaskService.buildResult(taskContext, true);
        } catch (final ConfigurationException | IllegalStateException ex) {
            taskContext.getBuildLogger().addErrorLogEntry(ex.getMessage());
            return commonTaskService.buildResult(taskContext, false);
        } catch (final FeignException ex) {
            commonTaskService.logError(taskContext, "OCTOPUS-BAMBOO-ERROR-0007: The release could not be deployed.");
            commonTaskService.logError(taskContext, ex.toString());
            return commonTaskService.buildResult(taskContext, false);
        }
    }
}
