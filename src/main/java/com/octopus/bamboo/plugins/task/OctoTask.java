package com.octopus.bamboo.plugins.task;

import com.atlassian.bamboo.build.logger.LogMutator;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.utils.process.ExternalProcess;
import com.octopus.constants.OctoConstants;
import com.octopus.services.CommonTaskService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public abstract class OctoTask extends AbstractTaskConfigurator implements CommonTaskType {

    private ProcessService processService;
    private CapabilityContext capabilityContext;
    private final CommonTaskService commonTaskService;
    private final LogMutator logMutator;

    public ProcessService getProcessService() {
        return processService;
    }

    public void setProcessService(final ProcessService processService) {
        this.processService = processService;
    }

    public CapabilityContext getCapabilityContext() {
        return capabilityContext;
    }

    public void setCapabilityContext(final CapabilityContext capabilityContext) {
        this.capabilityContext = capabilityContext;
    }

    protected CommonTaskService getCommonTaskService() {
        return commonTaskService;
    }

    protected LogMutator getLogMutator() {
        return logMutator;
    }

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param processService    The service used to run external executables
     * @param capabilityContext The service holding Bamboo's capabilities
     * @param commonTaskService The service used for common task operations
     * @param logMutator The service used to mask api keys
     */
    protected OctoTask(@NotNull final ProcessService processService,
                    @NotNull final CapabilityContext capabilityContext,
                    @NotNull final CommonTaskService commonTaskService,
                    @NotNull final LogMutator logMutator) {
        checkNotNull(processService, "processService cannot be null");
        checkNotNull(capabilityContext, "capabilityContext cannot be null");
        checkNotNull(commonTaskService, "commonTaskService cannot be null");
        checkNotNull(logMutator, "logMutator cannot be null");

        this.processService = processService;
        this.capabilityContext = capabilityContext;
        this.commonTaskService = commonTaskService;
        this.logMutator = logMutator;
    }

    public TaskResult launchOcto(@NotNull final CommonTaskContext taskContext,
                                 @NotNull final List<String> commands) {

        final String octopusCli = taskContext.getConfigurationMap().get(OctoConstants.OCTOPUS_CLI);

        checkState(StringUtils.isNotBlank(octopusCli), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus CLI can not be blank");

        final String cliPath = capabilityContext.getCapabilityValue(
                OctoConstants.OCTOPUS_CLI_CAPABILITY + "." + octopusCli);

        if (StringUtils.isNotBlank(cliPath) && new File(cliPath).exists()) {
            commands.add(0, cliPath);

            final String extensionVersion = getClass().getPackage().getImplementationVersion();
            final ExternalProcess process = processService.executeExternalProcess(taskContext,
                    new ExternalProcessBuilder()
                            .env("OCTOEXTENSION", extensionVersion)
                            .command(commands)
                            .workingDirectory(taskContext.getWorkingDirectory()));

            return TaskResultBuilder.newBuilder(taskContext)
                    .checkReturnCode(process, 0)
                    .build();
        }

        commonTaskService.logError(
                taskContext,
                "OCTOPUS-BAMBOO-INPUT-ERROR-0003: The path of \"" + cliPath + "\" for the selected Octopus CLI does not exist.");
        return TaskResultBuilder.newBuilder(taskContext).failed().build();
    }
}
