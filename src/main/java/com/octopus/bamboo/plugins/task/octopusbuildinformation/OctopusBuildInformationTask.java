package com.octopus.bamboo.plugins.task.octopusbuildinformation;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.build.logger.LogMutator;
import com.atlassian.bamboo.commit.CommitContext;
import com.atlassian.bamboo.configuration.AdministrationConfigurationAccessor;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.deployments.execution.DeploymentTaskContext;
import com.atlassian.bamboo.plan.PlanResultKey;
import com.atlassian.bamboo.plan.branch.VcsBranch;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.BuildChanges;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.vcs.configuration.PlanRepositoryDefinition;
import com.atlassian.bamboo.vcs.configuration.VcsBranchDefinition;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.octopus.bamboo.plugins.task.OctoTask;
import com.octopus.bamboo.plugins.task.OverwriteMode;
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
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

@Component
@ExportAsService({OctopusBuildInformationTask.class})
@Named("octopusMetadataTask")
public class OctopusBuildInformationTask extends OctoTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(OctopusBuildInformationTask.class);
    private AdministrationConfigurationAccessor administrationConfigurationAccessor;

    @Inject
    public OctopusBuildInformationTask(@NotNull @ComponentImport final ProcessService processService,
                                       @NotNull @ComponentImport final CapabilityContext capabilityContext,
                                       @NotNull @ComponentImport final AdministrationConfigurationAccessor administrationConfigurationAccessor,
                                       @NotNull final CommonTaskService commonTaskService,
                                       @NotNull final LogMutator logMutator) {
        super(processService, capabilityContext, commonTaskService, logMutator);
        this.administrationConfigurationAccessor = administrationConfigurationAccessor;
    }

    @NotNull
    public TaskResult execute(@NotNull final CommonTaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

        ConfigurationMap configurationMap = taskContext.getConfigurationMap();

        final String serverUrl = configurationMap.get(OctoConstants.SERVER_URL);
        final String apiKey = configurationMap.get(OctoConstants.API_KEY);
        final String spaceName = configurationMap.get(OctoConstants.SPACE_NAME);

        final String overwriteModeString = configurationMap.get(OctoConstants.OVERWRITE_MODE);
        OverwriteMode overwriteMode = OverwriteMode.FailIfExists;

        // if we don't have a overwriteMode defined then the step still has "legacy data" for forceUpload.
        if (StringUtils.isEmpty(overwriteModeString)) {
            final String forceUpload = configurationMap.get(OctoConstants.FORCE);
            final Boolean forceUploadBoolean = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(forceUpload));

            if (forceUploadBoolean) {
                overwriteMode = OverwriteMode.OverwriteExisting;
            }
        }
        else {
            overwriteMode = OverwriteMode.valueOf(overwriteModeString);
        }

        final String loggingLevel = configurationMap.get(OctoConstants.VERBOSE_LOGGING);
        final Boolean verboseLogging = BooleanUtils.isTrue(BooleanUtils.toBooleanObject(loggingLevel));

        final String id = configurationMap.get(OctoConstants.PACK_ID_NAME);
        final String version = configurationMap.get(OctoConstants.PACK_VERSION_NAME);

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

        commands.add(OctoConstants.OCTOPUS_BUILD_INFORMATION_COMMAND);

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

        final String dataFile = Paths.get(taskContext.getRootDirectory().getPath(), "octopus.buildinformation").toAbsolutePath().toString();

        try {
            if (taskContext instanceof DeploymentTaskContext) {
                buildLogger.addErrorLogEntry("The Octopus Package Build Information step is not supported for Deployments, it is only supported in Builds.");
                throw new TaskException("Deployment tasks not supported");
            }

            final BuildContext buildContext = ((TaskContext)taskContext).getBuildContext();
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

            final OctopusBuildInformationBuilder builder = new OctopusBuildInformationBuilder();

            String vcsType = "Unknown";
            String vcsRoot = "";
            String branch = "";
            Set<Long> relevantRepositoryIds = buildContext.getRelevantRepositoryIds();

            if (relevantRepositoryIds.size() > 0) {
                final PlanRepositoryDefinition vcsRepoDef = buildContext.getVcsRepositoryMap().get(relevantRepositoryIds.toArray()[0]);

                final Map<String, String> configuration = vcsRepoDef.getVcsLocation().getConfiguration();

                if (vcsRepoDef.getPluginKey().contains("git")) {
                    vcsType = "Git";
                }

                VcsBranchDefinition vcsBranchDefinition = vcsRepoDef.getBranch();
                if (vcsBranchDefinition != null) {
                    VcsBranch vcsBranch = vcsBranchDefinition.getVcsBranch();
                    if (vcsBranch != null) {
                        branch = vcsBranch.getDisplayName();
                    }
                }
                for (final String key : configuration.keySet()) {
                    if (key.contains("repositoryUrl")) {
                        vcsRoot = configuration.get(key);
                    } else if (key.contains("github.repository")) {
                        vcsRoot = "https://github.com/" + configuration.get(key);
                    }
                }
            }

            final String vcsCommitNumber = commitNumber;
            PlanResultKey planResultKey = buildContext.getPlanResultKey();
            final String buildId = Integer.toString(planResultKey.getBuildNumber());
            final String buildNumber = planResultKey.getKey();
            final String bambooServerUrl = administrationConfigurationAccessor.getAdministrationConfiguration().getBaseUrl();

            final OctopusBuildInformation buildInformation = builder.build(
                    vcsType,
                    vcsRoot,
                    vcsCommitNumber,
                    commitList,
                    branch,
                    bambooServerUrl,
                    buildId,
                    buildNumber);

            if (verboseLogging) {
                buildLogger.addBuildLogEntry("Creating " + dataFile);
            }

            final OctopusBuildInformationWriter writer = new OctopusBuildInformationWriter(buildLogger, verboseLogging);
            writer.writeToFile(buildInformation, dataFile);

        } catch (Exception ex) {
            buildLogger.addErrorLogEntry("Error processing comment messages", ex);
            return null;
        }

        commands.add("--file");
        commands.add(dataFile);

        if (overwriteMode != OverwriteMode.FailIfExists) {
            commands.add("--overwrite-mode");
            commands.add(overwriteMode.name());
        }

        return launchOcto(taskContext, commands);
    }
}
