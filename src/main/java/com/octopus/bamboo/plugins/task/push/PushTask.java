package com.octopus.bamboo.plugins.task.push;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.octopus.constants.OctoConstants;
import com.octopus.services.CommonTaskService;
import com.octopus.services.LoggerService;
import com.octopus.services.OctopusClient;
import com.octopus.services.impl.BambooFeignLogger;
import com.octopus.services.impl.LoggerServiceImpl;
import org.apache.commons.lang.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Bamboo Task that is used to deploy artifacts to Octopus Deploy
 */
@Component
@ExportAsService({PushTask.class})
@Named("pushTask")
public class PushTask implements TaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushTask.class);
    private final OctopusClient octopusClient;
    private final CommonTaskService commonTaskService;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param octopusClient The service that is used to interact with the API
     * @param commonTaskService The service used for common task operations
     */
    @Inject
    public PushTask(@NotNull final OctopusClient octopusClient,
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
        final String pattern = taskContext.getConfigurationMap().get(OctoConstants.PUSH_PATTERN);
        final String forceUpload = taskContext.getConfigurationMap().get(OctoConstants.FORCE);
        final Boolean forceUploadBoolean = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(forceUpload));
        final String loggingLevel = taskContext.getConfigurationMap().get(OctoConstants.VERBOSE_LOGGING);
        final Boolean verboseLogging = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(loggingLevel));

        final boolean success = octopusClient.pushPackage(
                loggerService,
                buildLogger,
                serverUrl,
                apiKey,
                pattern,
                taskContext.getWorkingDirectory(),
                forceUploadBoolean,
                verboseLogging
        );

        return commonTaskService.buildResult(taskContext, success);
    }
}
