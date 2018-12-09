package com.octopus.bamboo.plugins.task.createrelease;

import com.atlassian.bamboo.build.logger.LogMutator;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.CommonTaskContext;
import com.atlassian.bamboo.task.CommonTaskType;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * The Bamboo Task that is used to create releases in Octopus Deploy
 */
@Component
@ExportAsService({CreateReleaseTask.class})
@Named("createReleaseTask")
public class CreateReleaseTask extends OctoTask implements CommonTaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReleaseTask.class);

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param processService    The service used to run external executables
     * @param capabilityContext The service holding Bamboo's capabilities
     * @param commonTaskService The service used for common task operations
     * @param logMutator The service used to mask api keys
     */
    @Inject
    public CreateReleaseTask(@NotNull final ProcessService processService,
                             @NotNull final CapabilityContext capabilityContext,
                             @NotNull final CommonTaskService commonTaskService,
                             @NotNull final LogMutator logMutator) {
        super(processService, capabilityContext, commonTaskService, logMutator);
    }


    @NotNull
    public TaskResult execute(@NotNull final CommonTaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

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
        final String deploymentProgress = taskContext.getConfigurationMap().get(OctoConstants.SHOW_DEPLOYMENT_PROGRESS);
        final Boolean deploymentProgressEnabled = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(deploymentProgress));
        final String additionalArgs = taskContext.getConfigurationMap().get(OctoConstants.ADDITIONAL_COMMAND_LINE_ARGS_NAME);
        final String tenants = taskContext.getConfigurationMap().get(OctoConstants.TENANTS_NAME);
        final String tenantTags = taskContext.getConfigurationMap().get(OctoConstants.TENANT_TAGS_NAME);

        checkState(StringUtils.isNotBlank(serverUrl), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus URL can not be blank");
        checkState(StringUtils.isNotBlank(apiKey), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: API key can not be blank");
        checkState(StringUtils.isNotBlank(projectName), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Project name can not be blank");

        taskContext.getBuildLogger().getMutatorStack().add(getLogMutator());

        /*
            Build up the commands to be passed to the octopus cli
         */
        final List<String> commands = new ArrayList<String>();

        commands.add("create-release");

        commands.add("--server");
        commands.add(serverUrl);

        commands.add("--apiKey");
        commands.add(apiKey);

        commands.add("--project");
        commands.add(projectName);

        if (StringUtils.isNotBlank(releaseVersion)) {
            commands.add("--version");
            commands.add(releaseVersion);
        }

        if (StringUtils.isNotBlank(channelName)) {
            commands.add("--channel");
            commands.add(channelName);
        }

        if (ignoreExisting) {
            commands.add("--ignoreexisting");
        }

        if (StringUtils.isNotBlank(environmentName)) {
            final Iterable<String> environmentNameSplit = Splitter.on(',')
                    .trimResults()
                    .omitEmptyStrings()
                    .split(environmentName);
            for (final String environment : environmentNameSplit) {
                commands.add("--deployto");
                commands.add(environment);
            }
        }

        if (verboseLogging) {
            commands.add("--debug");
        }

        if (deploymentProgressEnabled) {
            commands.add("--progress");
        }

        if (StringUtils.isNotBlank(tenants)) {
            final Iterable<String> tenantsSplit = Splitter.on(',')
                    .trimResults()
                    .omitEmptyStrings()
                    .split(tenants);
            for (final String tenant : tenantsSplit) {
                commands.add("--tenant");
                commands.add(tenant);
            }
        }

        if (StringUtils.isNotBlank(tenantTags)) {
            final Iterable<String> tenantTagsSplit = Splitter.on(',')
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
