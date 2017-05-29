package com.octopus.bamboo.plugins.task.pack;

import com.atlassian.bamboo.build.logger.LogMutator;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.utils.process.ExternalProcess;
import com.google.common.base.Splitter;
import com.octopus.constants.OctoConstants;
import com.octopus.services.CommonTaskService;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.types.Commandline;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * The Bamboo Task that is used to deploy artifacts to Octopus Deploy
 */
@Component
@ExportAsService({PackTask.class})
@Named("pushTask")
public class PackTask extends AbstractTaskConfigurator implements CommonTaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(PackTask.class);
    @ComponentImport
    private final ProcessService processService;
    @ComponentImport
    private final CapabilityContext capabilityContext;
    private final CommonTaskService commonTaskService;
    private final LogMutator logMutator;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param processService    The service used the execute external applications
     * @param capabilityContext The service used get details of server capabilities
     * @param commonTaskService The service used for common task operations
     */
    @Inject
    public PackTask(@NotNull final ProcessService processService,
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

    @NotNull
    public TaskResult execute(@NotNull final CommonTaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

        final String octopusCli = taskContext.getConfigurationMap().get(OctoConstants.OCTOPUS_CLI);
        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);
        final String apiKey = taskContext.getConfigurationMap().get(OctoConstants.API_KEY);
        final String basePath = taskContext.getConfigurationMap().get(OctoConstants.PACK_BASE_PATH_NAME);
        final String format = taskContext.getConfigurationMap().get(OctoConstants.PACK_FORMAT_NAME);
        final String id = taskContext.getConfigurationMap().get(OctoConstants.PACK_ID_NAME);
        final String version = taskContext.getConfigurationMap().get(OctoConstants.PACK_VERSION_NAME);
        final String include = taskContext.getConfigurationMap().get(OctoConstants.PACK_INCLUDE_NAME);
        final String outFolder = taskContext.getConfigurationMap().get(OctoConstants.PACK_OUT_FOLDER_NAME);
        final String overwrite = taskContext.getConfigurationMap().get(OctoConstants.PACK_OVERWRITE_NAME);
        final Boolean overwriteBool = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(overwrite));
        final String loggingLevel = taskContext.getConfigurationMap().get(OctoConstants.VERBOSE_LOGGING);
        final Boolean verboseLogging = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(loggingLevel));

        final String additionalArgs = taskContext.getConfigurationMap().get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME);

        checkState(StringUtils.isNotBlank(octopusCli), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus CLI can not be blank");
        checkState(StringUtils.isNotBlank(serverUrl), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus URL can not be blank");
        checkState(StringUtils.isNotBlank(apiKey), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: API key can not be blank");
        checkState(StringUtils.isNotBlank(id), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Package id can not be blank");

        taskContext.getBuildLogger().getMutatorStack().add(logMutator);

        /*
            Build up the commands to be passed to the octopus cli
         */
        final List<String> commands = new ArrayList<String>();

        commands.add("pack");

        commands.add("--server");
        commands.add(serverUrl);

        commands.add("--apiKey");
        commands.add(apiKey);

        commands.add("--id");
        commands.add(id);

        if (StringUtils.isNotBlank(include)) {
            final Iterable<String> includeSplit = Splitter.on("\n")
                    .trimResults()
                    .omitEmptyStrings()
                    .split(include);
            for (final String includeItem : includeSplit) {
                commands.add("--include");
                commands.add(includeItem);
            }
        }

        if (StringUtils.isNotBlank(outFolder)) {
            commands.add("--outFolder");
            commands.add(outFolder);
        }

        if (StringUtils.isNotBlank(basePath)) {
            commands.add("--basePath");
            commands.add(basePath);
        }

        if (StringUtils.isNotBlank(format)) {
            commands.add("--format");
            commands.add(format);
        }

        if (StringUtils.isNotBlank(version)) {
            commands.add("--version");
            commands.add(version);
        }

        if (overwriteBool) {
            commands.add("--overwrite");
        }

        if (verboseLogging) {
            commands.add("--verbose");
        }

        if (StringUtils.isNotBlank(additionalArgs)) {
            final String myArgs[] = Commandline.translateCommandline(additionalArgs);
            commands.addAll(Arrays.asList(myArgs));
        }

        final String cliPath = capabilityContext.getCapabilityValue(
                OctoConstants.OCTOPUS_CLI_CAPABILITY + "." + octopusCli);

        if (new File(cliPath).exists()) {
            commands.add(0, cliPath);

            final ExternalProcess process = processService.executeExternalProcess(taskContext,
                    new ExternalProcessBuilder()
                            .command(commands)
                            .workingDirectory(taskContext.getWorkingDirectory()));

            return TaskResultBuilder.newBuilder(taskContext)
                    .checkReturnCode(process, 0)
                    .build();
        }

        commonTaskService.logError(
                taskContext,
                "OCTOPUS-BAMBOO-INPUT-ERROR-0003:The path of \"" + cliPath + "\" for the selected Octopus CLI does not exist.");
        return TaskResultBuilder.newBuilder(taskContext).failed().build();
    }
}
