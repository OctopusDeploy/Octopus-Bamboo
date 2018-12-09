package com.octopus.bamboo.plugins.task.push;

import com.atlassian.bamboo.build.logger.LogMutator;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.google.common.base.Splitter;
import com.octopus.bamboo.plugins.task.OctoTask;
import com.octopus.constants.OctoConstants;
import com.octopus.services.CommonTaskService;
import com.octopus.services.FileService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
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
 * The Bamboo Task that is used to push artifacts to Octopus Deploy
 */
@Component
@ExportAsService({PushTask.class})
@Named("pushTask")
public class PushTask extends OctoTask implements CommonTaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushTask.class);
    private final FileService fileService;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param processService    The service used to run external executables
     * @param capabilityContext The service holding Bamboo's capabilities
     * @param commonTaskService The service used for common task operations
     * @param logMutator The service used to mask api keys
     */
    @Inject
    public PushTask(@NotNull final ProcessService processService,
                    @NotNull final CapabilityContext capabilityContext,
                    @NotNull final CommonTaskService commonTaskService,
                    @NotNull final FileService fileService,
                    @NotNull final LogMutator logMutator) {
        super(processService, capabilityContext, commonTaskService, logMutator);
        checkNotNull(fileService, "fileService cannot be null");

        this.fileService = fileService;
    }

    @NotNull
    public TaskResult execute(@NotNull final CommonTaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);
        final String apiKey = taskContext.getConfigurationMap().get(OctoConstants.API_KEY);
        final String patterns = taskContext.getConfigurationMap().get(OctoConstants.PUSH_PATTERN);
        final String forceUpload = taskContext.getConfigurationMap().get(OctoConstants.FORCE);
        final Boolean forceUploadBoolean = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(forceUpload));
        final String loggingLevel = taskContext.getConfigurationMap().get(OctoConstants.VERBOSE_LOGGING);
        final Boolean verboseLogging = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(loggingLevel));
        final String additionalArgs = taskContext.getConfigurationMap().get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME);

        checkState(StringUtils.isNotBlank(serverUrl), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus URL can not be blank");
        checkState(StringUtils.isNotBlank(apiKey), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: API key can not be blank");
        checkState(StringUtils.isNotBlank(patterns), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Package paths can not be blank");

        taskContext.getBuildLogger().getMutatorStack().add(getLogMutator());

         /*
            Get the list of matching files that need to be uploaded
         */
        final List<File> files = new ArrayList<>();

        final Iterable<String> patternSplit = Splitter.on("\n")
                .trimResults()
                .omitEmptyStrings()
                .split(patterns);
        for (final String pattern : patternSplit) {
            final List<File> matchingFiles = fileService.getMatchingFile(taskContext.getWorkingDirectory(), pattern);
            /*
                Don't add duplicates
             */
            for (final File file : matchingFiles) {
                if (file != null) {
                    final File existing = CollectionUtils.find(files, new Predicate<File>() {
                        @Override
                        public boolean evaluate(final File existingFile) {
                            return existingFile.getAbsolutePath().equals(file.getAbsolutePath());
                        }
                    });

                    if (existing == null) {
                        files.add(file);
                    }
                }
            }
        }

        /*
            Fail if no files were matched
         */
        if (files.isEmpty()) {
            getCommonTaskService().logError(taskContext, "OCTOPUS-BAMBOO-INPUT-ERROR-0001: The pattern \n"
                    + patterns
                    + "\n failed to match any files");
            return getCommonTaskService().buildResult(taskContext, false);
        }

        /*
            Build up the commands to be passed to the octopus cli
         */
        final List<String> commands = new ArrayList<String>();

        commands.add(OctoConstants.PUSH_COMMAND);

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
            final String[] myArgs = Commandline.translateCommandline(additionalArgs);
            commands.addAll(Arrays.asList(myArgs));
        }

        for (final File file : files) {
            try {
                commands.add("--package");
                commands.add(file.getCanonicalPath());
            } catch (final IOException ex) {
                getCommonTaskService().logError(
                        taskContext,
                        "An exception was thrown while processing the file " + file.getAbsolutePath());
                return TaskResultBuilder.newBuilder(taskContext).failed().build();
            }
        }

        return launchOcto(taskContext, commands);
    }
}
