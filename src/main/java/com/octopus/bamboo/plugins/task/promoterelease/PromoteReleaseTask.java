package com.octopus.bamboo.plugins.task.promoterelease;

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
 * Task used to deploy a release
 */
@Component
@ExportAsService({PromoteReleaseTask.class})
@Named("promoteReleaseTask")
public class PromoteReleaseTask implements CommonTaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(PromoteReleaseTask.class);
    @ComponentImport
    private final ProcessService processService;
    @ComponentImport
    private final CapabilityContext capabilityContext;
    private final CommonTaskService commonTaskService;
    private final LogMutator logMutator;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param feignService      The service that is used to interact with the API
     * @param commonTaskService The service used for common task operations
     */
    @Inject
    public PromoteReleaseTask(@NotNull final ProcessService processService,
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
    @Override
    public TaskResult execute(@NotNull final CommonTaskContext taskContext) throws TaskException {

        final String octopusCli = taskContext.getConfigurationMap().get(OctoConstants.OCTOPUS_CLI);
        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);
        final String apiKey = taskContext.getConfigurationMap().get(OctoConstants.API_KEY);
        final String projectName = taskContext.getConfigurationMap().get(OctoConstants.PROJECT_NAME);
        final String promoteFrom = taskContext.getConfigurationMap().get(OctoConstants.PROMOTE_FROM_NAME);
        final String promoteTo = taskContext.getConfigurationMap().get(OctoConstants.PROMOTE_TO_NAME);
        final String loggingLevel = taskContext.getConfigurationMap().get(OctoConstants.VERBOSE_LOGGING);
        final Boolean verboseLogging = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(loggingLevel));
        final String deploymentProgress = taskContext.getConfigurationMap().get(OctoConstants.SHOW_DEPLOYMENT_PROGRESS);
        final Boolean deploymentProgressEnabled = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(deploymentProgress));
        final String additionalArgs = taskContext.getConfigurationMap().get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME);
        final String tenants = taskContext.getConfigurationMap().get(OctoConstants.TENANTS_NAME);
        final String tenantTags = taskContext.getConfigurationMap().get(OctoConstants.TENANT_TAGS_NAME);

        checkState(StringUtils.isNotBlank(octopusCli), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus CLI can not be blank");
        checkState(StringUtils.isNotBlank(serverUrl), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus URL can not be blank");
        checkState(StringUtils.isNotBlank(apiKey), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: API key can not be blank");
        checkState(StringUtils.isNotBlank(projectName), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Project name can not be blank");
        checkState(StringUtils.isNotBlank(promoteFrom), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Promote from can not be blank");
        checkState(StringUtils.isNotBlank(promoteTo), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Promote to can not be blank");

        taskContext.getBuildLogger().getMutatorStack().add(logMutator);

        /*
            Build up the commands to be passed to the octopus cli
         */
        final List<String> commands = new ArrayList<String>();

        commands.add("promote-release");

        commands.add("--server");
        commands.add(serverUrl);

        commands.add("--apiKey");
        commands.add(apiKey);

        commands.add("--project");
        commands.add(projectName);

        commands.add("--from");
        commands.add(promoteFrom);

        final Iterable<String> promoteToSplit = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split(promoteTo);
        for (final String promoteToEnv : promoteToSplit) {
            commands.add("--to");
            commands.add(promoteToEnv);
        }

        if (verboseLogging) {
            commands.add("--debug");
        }

        if (deploymentProgressEnabled) {
            commands.add("--progress");
        }

        if (StringUtils.isNotBlank(tenants)) {
            Iterable<String> tenantsSplit = Splitter.on(',')
                    .trimResults()
                    .omitEmptyStrings()
                    .split(tenants);
            for (final String tenant : tenantsSplit) {
                commands.add("--tenant");
                commands.add(tenant);
            }
        }

        if (StringUtils.isNotBlank(tenantTags)) {
            Iterable<String> tenantTagsSplit = Splitter.on(',')
                    .trimResults()
                    .omitEmptyStrings()
                    .split(tenantTags);
            for (final String tenantTag : tenantTagsSplit) {
                commands.add("--tenanttag");
                commands.add(tenantTag);
            }
        }

        if (StringUtils.isNotBlank(additionalArgs)) {
            final String[] myArgs = Commandline.translateCommandline(additionalArgs);
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
