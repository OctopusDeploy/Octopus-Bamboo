package com.octopus.bamboo.plugins.task.push;

import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.utils.process.ExternalProcess;
import com.octopus.constants.OctoConstants;
import com.octopus.services.CommonTaskService;
import com.octopus.services.LoggerService;
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
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Bamboo Task that is used to deploy artifacts to Octopus Deploy
 */
@Component
@ExportAsService({PushTask.class})
@Named("pushTask")
public class PushTask implements TaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushTask.class);
    @ComponentImport
    private final ProcessService processService;
    @ComponentImport
    private final CapabilityContext capabilityContext;
    private final CommonTaskService commonTaskService;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param octopusClient The service that is used to interact with the API
     * @param commonTaskService The service used for common task operations
     */
    @Inject
    public PushTask(@NotNull final ProcessService processService,
                    @NotNull final CapabilityContext capabilityContext,
                    @NotNull final CommonTaskService commonTaskService) {
        checkNotNull(processService, "processService cannot be null");
        checkNotNull(capabilityContext, "capabilityContext cannot be null");

        this.processService = processService;
        this.capabilityContext = capabilityContext;
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

        final String octoCli = capabilityContext.getCapabilityValue(OctoConstants.OCTOPUS_CLI_CAPABILITY);

        if (StringUtils.isBlank(octoCli)) {
            commonTaskService.logError(taskContext, "Failed to find the Octopus CLI capability. "
                    + "Make sure you have defined this in as part of the Bamboo configuration.");
            return TaskResultBuilder.create(taskContext).failed().build();
        }

        final ExternalProcess process = processService.createProcess(taskContext,
                new ExternalProcessBuilder()
                        .command(Arrays.asList(octoCli))
                        .workingDirectory(taskContext.getWorkingDirectory()));

        return TaskResultBuilder.create(taskContext)
                .checkReturnCode(process, 0)
                .build();
    }
}
