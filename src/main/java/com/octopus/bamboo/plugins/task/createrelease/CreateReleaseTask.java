package com.octopus.bamboo.plugins.task.createrelease;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.google.common.base.Optional;
import com.octopus.constants.OctoConstants;
import com.octopus.domain.Release;
import com.octopus.services.CommonTaskService;
import com.octopus.services.LoggerService;
import com.octopus.services.OctopusClient;
import com.octopus.services.impl.BambooFeignLogger;
import com.octopus.services.impl.LoggerServiceImpl;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Bamboo Task that is used to create releases in Octopus Deploy
 */
@Component
@ExportAsService({CreateReleaseTask.class})
@Named("createReleaseTask")
public class CreateReleaseTask implements TaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReleaseTask.class);
    private final OctopusClient octopusClient;
    private final CommonTaskService commonTaskService;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param octopusClient      The service that is used to access the api
     * @param commonTaskService The service used for common task operations
     */
    @Inject
    public CreateReleaseTask(@NotNull final OctopusClient octopusClient,
                             @NotNull final CommonTaskService commonTaskService) {
        checkNotNull(octopusClient, "octopusClient cannot be null");
        checkNotNull(commonTaskService, "commonTaskService cannot be null");

        this.octopusClient = octopusClient;
        this.commonTaskService = commonTaskService;
    }


    @NotNull
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

        final feign.Logger buildLogger = new BambooFeignLogger(taskContext.getBuildLogger());
        final LoggerService loggerService = new LoggerServiceImpl(taskContext);
        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);
        final String apiKey = taskContext.getConfigurationMap().get(OctoConstants.API_KEY);
        final String projectName = taskContext.getConfigurationMap().get(OctoConstants.PROJECT_NAME);
        final String channelName = taskContext.getConfigurationMap().get(OctoConstants.CHANNEL_NAME);
        final String releaseVersion = taskContext.getConfigurationMap().get(OctoConstants.RELEASE_VERSION);
        final String environmentName = taskContext.getConfigurationMap().get(OctoConstants.ENVIRONMENT_NAME);
        final Boolean ignoreExisting = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(
                taskContext.getConfigurationMap().get(OctoConstants.IGNORE_EXISTING_RELEASE_NAME)));
        final String loggingLevel = taskContext.getConfigurationMap().get(OctoConstants.VERBOSE_LOGGING);
        final Boolean verboseLogging = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(loggingLevel));

        final Optional<Release> release = octopusClient.createRelease(
                loggerService,
                buildLogger,
                serverUrl,
                apiKey,
                projectName,
                channelName,
                releaseVersion,
                ignoreExisting,
                verboseLogging
        );

        if (StringUtils.isNotBlank(environmentName) && release.isPresent()) {
            final boolean deploySuccess = octopusClient.deployRelease(
                    loggerService,
                    buildLogger,
                    serverUrl,
                    apiKey,
                    projectName,
                    environmentName,
                    release.get().getVersion(),
                    verboseLogging
            );

            return commonTaskService.buildResult(taskContext, deploySuccess);
        }

        return commonTaskService.buildResult(taskContext, release.isPresent());
    }
}
