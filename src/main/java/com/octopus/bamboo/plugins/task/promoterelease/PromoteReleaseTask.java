package com.octopus.bamboo.plugins.task.promoterelease;

import com.atlassian.bamboo.build.logger.LogMutator;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.base.Splitter;
import com.octopus.bamboo.plugins.task.OctoTask;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Task used to push a release
 */
@Component
@ExportAsService({PromoteReleaseTask.class})
@Named("promoteReleaseTask")
public class PromoteReleaseTask extends OctoTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(PromoteReleaseTask.class);

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param processService    The service used to run external executables
     * @param capabilityContext The service holding Bamboo's capabilities
     * @param commonTaskService The service used for common task operations
     * @param logMutator The service used to mask api keys
     */
    @Inject
    public PromoteReleaseTask(@NotNull @ComponentImport final ProcessService processService,
                              @NotNull @ComponentImport final CapabilityContext capabilityContext,
                              @NotNull final CommonTaskService commonTaskService,
                              @NotNull final LogMutator logMutator) {
        super(processService, capabilityContext, commonTaskService, logMutator);
    }

    @NotNull
    @Override
    public TaskResult execute(@NotNull final CommonTaskContext taskContext) throws TaskException {

        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);
        final String apiKey = taskContext.getConfigurationMap().get(OctoConstants.API_KEY);
        final String spaceName = taskContext.getConfigurationMap().get(OctoConstants.SPACE_NAME);
        final String projectName = taskContext.getConfigurationMap().get(OctoConstants.PROJECT_NAME);
        final String promoteFrom = taskContext.getConfigurationMap().get(OctoConstants.PROMOTE_FROM_NAME);
        final String promoteTo = taskContext.getConfigurationMap().get(OctoConstants.PROMOTE_TO_NAME);
        final String loggingLevel = taskContext.getConfigurationMap().get(OctoConstants.VERBOSE_LOGGING);
        final Boolean verboseLogging = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(loggingLevel));
        final String deploymentProgress = taskContext.getConfigurationMap().get(OctoConstants.SHOW_DEPLOYMENT_PROGRESS);
        final Boolean deploymentProgressEnabled = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(deploymentProgress));

        final String deploymentTimeout = taskContext.getConfigurationMap().get(OctoConstants.DEPLOYMENT_TIMEOUT);
        final String cancelOnTimeout = taskContext.getConfigurationMap().get(OctoConstants.CANCEL_DEPLOYMENT_ON_TIMEOUT);
        final Boolean cancelOnTimeoutEnabled = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(cancelOnTimeout));

        final String additionalArgs = taskContext.getConfigurationMap().get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME);
        final String tenants = taskContext.getConfigurationMap().get(OctoConstants.TENANTS_NAME);
        final String tenantTags = taskContext.getConfigurationMap().get(OctoConstants.TENANT_TAGS_NAME);

        checkState(StringUtils.isNotBlank(serverUrl), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus URL can not be blank");
        checkState(StringUtils.isNotBlank(apiKey), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: API key can not be blank");
        checkState(StringUtils.isNotBlank(projectName), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Project name can not be blank");
        checkState(StringUtils.isNotBlank(promoteFrom), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Promote from can not be blank");
        checkState(StringUtils.isNotBlank(promoteTo), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Promote to can not be blank");

        taskContext.getBuildLogger().getMutatorStack().add(getLogMutator());

        /*
            Build up the commands to be passed to the octopus cli
         */
        final List<String> commands = new ArrayList<String>();

        commands.add(OctoConstants.PROMOTE_RELEASE_COMMAND);

        commands.add("--server");
        commands.add(serverUrl);

        commands.add("--apiKey");
        commands.add(apiKey);

        if (StringUtils.isNotBlank(spaceName)) {
            commands.add("--space");
            commands.add(spaceName);
        }

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

            if (!deploymentTimeout.isEmpty()) {
                commands.add("--deploymenttimeout");
                commands.add(deploymentTimeout);
            }

            if (cancelOnTimeoutEnabled) {
                commands.add("--cancelontimeout");
            }
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

        return launchOcto(taskContext, commands);
    }
}
