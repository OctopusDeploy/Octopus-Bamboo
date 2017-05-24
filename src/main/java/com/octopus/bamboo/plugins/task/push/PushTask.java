package com.octopus.bamboo.plugins.task.push;

import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.utils.process.ExternalProcess;
import com.octopus.constants.OctoConstants;
import com.octopus.services.CommonTaskService;
import com.octopus.services.FileService;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

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
    private final FileService fileService;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param processService      The service used the execute external applications
     * @param capabilityContext      The service used get details of server capabilities
     * @param commonTaskService The service used for common task operations
     */
    @Inject
    public PushTask(@NotNull final ProcessService processService,
                    @NotNull final CapabilityContext capabilityContext,
                    @NotNull final CommonTaskService commonTaskService,
                    @NotNull final FileService fileService) {
        checkNotNull(processService, "processService cannot be null");
        checkNotNull(capabilityContext, "capabilityContext cannot be null");
        checkNotNull(commonTaskService, "commonTaskService cannot be null");
        checkNotNull(fileService, "fileService cannot be null");

        this.processService = processService;
        this.capabilityContext = capabilityContext;
        this.commonTaskService = commonTaskService;
        this.fileService = fileService;
    }

    @NotNull
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);
        final String apiKey = taskContext.getConfigurationMap().get(OctoConstants.API_KEY);
        final String pattern = taskContext.getConfigurationMap().get(OctoConstants.PUSH_PATTERN);
        final String forceUpload = taskContext.getConfigurationMap().get(OctoConstants.FORCE);
        final Boolean forceUploadBoolean = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(forceUpload));
        final String loggingLevel = taskContext.getConfigurationMap().get(OctoConstants.VERBOSE_LOGGING);
        final Boolean verboseLogging = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(loggingLevel));
        final String additionalArgs = taskContext.getConfigurationMap().get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME);

        checkState(StringUtils.isNotBlank(serverUrl), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus URL can not be blank");
        checkState(StringUtils.isNotBlank(apiKey), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: API key can not be blank");
        checkState(StringUtils.isNotBlank(pattern), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Package paths can not be blank");

         /*
            Get the list of matching files that need to be uploaded
         */
        final List<File> files = fileService.getMatchingFile(taskContext.getWorkingDirectory(), pattern);

        /*
            Fail if no files were matched
         */
        if (files.isEmpty()) {
            commonTaskService.logError(taskContext, "OCTOPUS-BAMBOO-INPUT-ERROR-0001: The pattern " + pattern
                    + " failed to match any files");
            return commonTaskService.buildResult(taskContext, false);
        }

        /*
            Build up the commands to be passed to the octopus cli
         */
        final List<String> commands = new ArrayList<String>();

        commands.add("push");

        commands.add("--server");
        commands.add(serverUrl);

        commands.add("--apiKey");
        commands.add(apiKey);

        if (forceUploadBoolean) {
            commands.add("--replace-existing");
        }

        if (verboseLogging) {
            commands.add("--debug");
        }

        if (StringUtils.isNotBlank(additionalArgs)) {
            final String myArgs[] = Commandline.translateCommandline(additionalArgs);
            commands.addAll(Arrays.asList(myArgs));
        }

        for (final File file : files) {
            try {
                commands.add("--package");
                commands.add(file.getCanonicalPath());
            } catch (final IOException ex) {
                commonTaskService.logError(
                        taskContext,
                        "An exception was thrown while processing the file " + file.getAbsolutePath());
                return TaskResultBuilder.create(taskContext).failed().build();
            }
        }

        for (final Capability cap : capabilityContext.getCapabilitySet().getCapabilities()) {
            if (cap.getKey().startsWith(OctoConstants.OCTOPUS_CLI_CAPABILITY)) {
                commands.add(0, cap.getValue());

                final ExternalProcess process = processService.createProcess(taskContext,
                        new ExternalProcessBuilder()
                                .command(commands)
                                .workingDirectory(taskContext.getWorkingDirectory()));

                process.execute();

                if (process.getHandler().getExitCode() != 0) {
                    commonTaskService.logError(
                            taskContext,
                            "Failed to execute the Octopus Command line client. "
                                    + "If the error message mentions missing library files "
                                    + "(like unwind or icu) make sure you have installed all "
                                    + "the dependencies that are documented at "
                                    + "https://www.microsoft.com/net/core");
                }

                return TaskResultBuilder.create(taskContext)
                        .checkReturnCode(process, 0)
                        .build();
            }
        }

        commonTaskService.logError(
                taskContext,
                "You need to define the Octopus CLI executable server capability in the Bamboo administration page");
        return TaskResultBuilder.create(taskContext).failed().build();
    }
}
