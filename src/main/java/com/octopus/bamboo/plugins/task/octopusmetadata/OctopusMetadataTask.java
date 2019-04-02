package com.octopus.bamboo.plugins.task.octopusmetadata;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.build.logger.LogMutator;
import com.atlassian.bamboo.commit.CommitContext;
import com.atlassian.bamboo.configuration.AdministrationConfiguration;
import com.atlassian.bamboo.plan.PlanResultKey;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.BuildChanges;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.vcs.configuration.PlanRepositoryDefinition;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.spring.container.ContainerManager;
import com.octopus.bamboo.plugins.task.OctoTask;
import com.octopus.constants.OctoConstants;
import com.octopus.services.CommonTaskService;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

@Component
@ExportAsService({OctopusMetadataTask.class})
@Named("octopusMetadataTask")
public class OctopusMetadataTask extends OctoTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(OctopusMetadataTask.class);

    @Inject
    public OctopusMetadataTask(@NotNull @ComponentImport final ProcessService processService,
                    @NotNull @ComponentImport final CapabilityContext capabilityContext,
                    @NotNull final CommonTaskService commonTaskService,
                    @NotNull final LogMutator logMutator) {
        super(processService, capabilityContext, commonTaskService, logMutator);
    }

    public AdministrationConfiguration getAdministrationConfiguration() {
        return (AdministrationConfiguration) ContainerManager.getComponent("administrationConfiguration");
    }

    @NotNull
    public TaskResult execute(@NotNull final CommonTaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);
        final String apiKey = taskContext.getConfigurationMap().get(OctoConstants.API_KEY);
        final String spaceName = taskContext.getConfigurationMap().get(OctoConstants.SPACE_NAME);
        final String forceUpload = taskContext.getConfigurationMap().get(OctoConstants.FORCE);
        final Boolean forceUploadBoolean = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(forceUpload));
        final String loggingLevel = taskContext.getConfigurationMap().get(OctoConstants.VERBOSE_LOGGING);
        final Boolean verboseLogging = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(loggingLevel));

        final String id = taskContext.getConfigurationMap().get(OctoConstants.PACK_ID_NAME);
        final String version = taskContext.getConfigurationMap().get(OctoConstants.PACK_VERSION_NAME);

        final String commentParser = taskContext.getConfigurationMap().get(OctoConstants.COMMENT_PARSER_NAME);

        checkState(StringUtils.isNotBlank(serverUrl), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Octopus URL can not be blank");
        checkState(StringUtils.isNotBlank(apiKey), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: API key can not be blank");
        checkState(StringUtils.isNotBlank(id), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Package id can not be blank");
        checkState(StringUtils.isNotBlank(version), "OCTOPUS-BAMBOO-INPUT-ERROR-0002: Package version can not be blank");

        BuildLogger buildLogger = taskContext.getBuildLogger();
        buildLogger.getMutatorStack().add(getLogMutator());

        /*
            Build up the commands to be passed to the octopus cli
         */
        final List<String> commands = new ArrayList<String>();

        commands.add(OctoConstants.OCTOPUS_METADATA_COMMAND);

        commands.add("--server");
        commands.add(serverUrl);

        commands.add("--apiKey");
        commands.add(apiKey);

        if (StringUtils.isNotBlank(spaceName)) {
            commands.add("--space");
            commands.add(spaceName);
        }

        commands.add("--package-id");
        commands.add(id);

        if (StringUtils.isNotBlank(version)) {
            commands.add("--version");
            commands.add(version);
        }

        final String metaFile = Paths.get(taskContext.getRootDirectory().getPath(), "octopus.metadata").toAbsolutePath().toString();

        try {
            final BuildContext buildContext = ((TaskContext) taskContext).getBuildContext();
            final BuildChanges buildChanges = buildContext.getBuildChanges();
            final List<CommitContext> commits = buildChanges.getChanges();

            List<Commit> commitList = new ArrayList<Commit>();
            String commitNumber = "";
            for (final CommitContext commit : commits) {
                final String comment = commit.getComment();

                final Commit c = new Commit();
                c.Id = commit.getChangeSetId();
                c.Comment = comment;

                commitList.add(c);

                commitNumber = commit.getChangeSetId();
            }

            final OctopusMetadataBuilder builder = new OctopusMetadataBuilder();

            final PlanRepositoryDefinition vcsRepoDef = buildContext.getVcsRepositoryMap().get(buildContext.getRelevantRepositoryIds().toArray()[0]);

            final Map<String, String> configuration = vcsRepoDef.getVcsLocation().getConfiguration();

            String vcsType = "Unknown";
            if (vcsRepoDef.getPluginKey().contains("git")) {
                vcsType = "Git";
            }

            String vcsRoot = "";
            for (final String key : configuration.keySet()){
                if (key.contains("repositoryUrl")) {
                    vcsRoot = configuration.get(key);
                }
            }

            final String vcsCommitNumber = commitNumber;
            PlanResultKey planResultKey = buildContext.getPlanResultKey();
            final String buildId = Integer.toString(planResultKey.getBuildNumber());
            final String buildNumber = planResultKey.getKey();

            final OctopusPackageMetadata metadata = builder.build(
                    vcsType,
                    vcsRoot,
                    vcsCommitNumber,
                    commitList,
                    commentParser,
                    getAdministrationConfiguration().getBaseUrl(),
                    buildId,
                    buildNumber);

            if (verboseLogging) {
                buildLogger.addBuildLogEntry("Creating " + metaFile);
            }

            final OctopusMetadataWriter writer = new OctopusMetadataWriter(buildLogger, verboseLogging);
            writer.writeToFile(metadata, metaFile);

        } catch (Exception ex) {
            buildLogger.addErrorLogEntry("Error processing comment messages", ex);
            return null;
        }

        commands.add("--metadata-file");
        commands.add(metaFile);

        if (forceUploadBoolean) {
            commands.add("--replace-existing");
        }

        return launchOcto(taskContext, commands);
    }
}
