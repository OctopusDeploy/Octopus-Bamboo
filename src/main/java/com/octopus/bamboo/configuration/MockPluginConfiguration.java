package com.octopus.bamboo.configuration;

import com.octopus.bamboo.condition.IntegrationTestCondition;
import com.octopus.constants.OctoConstants;
import feign.Client;
import feign.Response;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;

/**
 * Create some Spring beans
 */
@Configuration
@Component
@Conditional(IntegrationTestCondition.class)
public class MockPluginConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockPluginConfiguration.class);

    private static final String GET_PROJECTS_RESPONSE =
            "[\n"
                    + "  { \n"
                    + "    \"Id\": \"Projects-1\", \n"
                    + "    \"VariableSetId\": \"variableset-Projects-1\", \n"
                    + "    \"DeploymentProcessId\": \"deploymentprocess-Projects-1\", \n"
                    + "    \"DiscreteChannelRelease\": false, \n"
                    + "    \"IncludedLibraryVariableSetIds\": [], \n"
                    + "    \"DefaultToSkipIfAlreadyInstalled\": false, \n"
                    + "    \"TenantedDeploymentMode\": \"Untenanted\", \n"
                    + "    \"VersioningStrategy\": { \n"
                    + "      \"DonorPackageStepId\": null, \n"
                    + "      \"Template\": \"#{Octopus.Version.LastMajor}.#{Octopus.Version.LastMinor}.#{Octopus.Version.NextPatch}\" \n"
                    + "    }, \n"
                    + "    \"ReleaseCreationStrategy\": { \n"
                    + "      \"ReleaseCreationPackageStepId\": null, \n"
                    + "      \"ChannelId\": null \n"
                    + "    }, \n"
                    + "    \"Templates\": [], \n"
                    + "    \"AutoDeployReleaseOverrides\": [], \n"
                    + "    \"Name\": \"Bamboo Integration Test\", \n"
                    + "    \"Slug\": \"bamboo-integration-test\", \n"
                    + "    \"Description\": \"\", \n"
                    + "    \"IsDisabled\": false, \n"
                    + "    \"ProjectGroupId\": \"ProjectGroups-1\", \n"
                    + "    \"LifecycleId\": \"Lifecycles-1\", \n"
                    + "    \"AutoCreateRelease\": false, \n"
                    + "    \"ProjectConnectivityPolicy\": { \n"
                    + "      \"SkipMachineBehavior\": \"None\", \n"
                    + "      \"TargetRoles\": [], \n"
                    + "      \"AllowDeploymentsToNoTargets\": false \n"
                    + "    }, \n"
                    + "    \"Links\": { \n"
                    + "      \"Self\": \"/api/projects/Projects-1\", \n"
                    + "      \"Releases\": \"/api/projects/Projects-1/releases{/version}{?skip}\", \n"
                    + "      \"Channels\": \"/api/projects/Projects-1/channels\", \n"
                    + "      \"Triggers\": \"/api/projects/Projects-1/triggers\", \n"
                    + "      \"OrderChannels\": \"/api/projects/Projects-1/channels/order\", \n"
                    + "      \"Variables\": \"/api/variables/variableset-Projects-1\", \n"
                    + "      \"Progression\": \"/api/progression/Projects-1{?aggregate}\", \n"
                    + "      \"DeploymentProcess\": \"/api/deploymentprocesses/deploymentprocess-Projects-1\", \n"
                    + "      \"Web\": \"/app#/projects/Projects-1\", \n"
                    + "      \"Logo\": \"/api/projects/Projects-1/logo\" \n"
                    + "    } \n"
                    + "  } \n"
                    + "]";

    private static final String PROJECT_CHANNELS_RESPONSE =
            "{ \n"
                    + "\"ItemType\": \"Channel\", \n"
                    + "\"IsStale\": false, \n"
                    + "\"TotalResults\": 1, \n"
                    + "\"ItemsPerPage\": 2147483647, \n"
                    + "\"Items\": [ \n"
                    + "  { \n"
                    + "    \"Id\": \"Channels-1\", \n"
                    + "    \"Name\": \"Default\", \n"
                    + "    \"Description\": null, \n"
                    + "    \"ProjectId\": \"Projects-1\", \n"
                    + "    \"LifecycleId\": null, \n"
                    + "    \"IsDefault\": true, \n"
                    + "    \"Rules\": [], \n"
                    + "    \"TenantTags\": [], \n"
                    + "    \"Links\": { \n"
                    + "      \"Self\": \"/api/channels/Channels-1\", \n"
                    + "      \"Releases\": \"/api/channels/Channels-1/releases\", \n"
                    + "      \"Project\": \"/api/projects/Projects-1\" \n"
                    + "    } \n"
                    + "  } \n"
                    + "], \n"
                    + "\"Links\": { \n"
                    + "  \"Self\": \"/api/projects/Projects-1/channels\", \n"
                    + "  \"Template\": \"/api/projects/Projects-1/channels\", \n"
                    + "  \"Page.Current\": \"/api/projects/Projects-1/channels\", \n"
                    + "  \"Page.0\": \"/api/projects/Projects-1/channels\" \n"
                    + "} \n"
                    + "}";

    private static final String DEPLOYMENT_PROCESS_RESPONSE =
            "{ \n"
                    + "  \"Id\": \"deploymentprocess-Projects-1\", \n"
                    + "  \"ProjectId\": \"Projects-1\", \n"
                    + "  \"Steps\": [ \n"
                    + "    { \n"
                    + "      \"Id\": \"1c085213-ef0d-4996-81af-24b8e12b73d8\", \n"
                    + "      \"Name\": \"Tomcat Deploy\", \n"
                    + "      \"RequiresPackagesToBeAcquired\": false, \n"
                    + "      \"Properties\": { \n"
                    + "        \"Octopus.Action.TargetRoles\": \"tomcat\" \n"
                    + "      }, \n"
                    + "      \"Condition\": \"Success\", \n"
                    + "      \"StartTrigger\": \"StartAfterPrevious\", \n"
                    + "      \"Actions\": [ \n"
                    + "        { \n"
                    + "          \"Id\": \"b2b6e12f-1fda-4257-be6a-3ebdd1740be2\", \n"
                    + "          \"Name\": \"Tomcat Deploy\", \n"
                    + "          \"ActionType\": \"Octopus.TentaclePackage\", \n"
                    + "          \"IsDisabled\": false, \n"
                    + "          \"Environments\": [], \n"
                    + "          \"ExcludedEnvironments\": [], \n"
                    + "          \"Channels\": [], \n"
                    + "          \"TenantTags\": [], \n"
                    + "          \"Properties\": { \n"
                    + "            \"Octopus.Action.Package.AutomaticallyRunConfigurationTransformationFiles\": \"True\", \n"
                    + "            \"Octopus.Action.Package.AutomaticallyUpdateAppSettingsAndConnectionStrings\": \"True\", \n"
                    + "            \"Octopus.Action.EnabledFeatures\": \"Octopus.Features.CustomDirectory,Octopus.Features.ConfigurationVariables,Octopus.Features.ConfigurationTransforms\", \n"
                    + "            \"Octopus.Action.Package.DownloadOnTentacle\": \"False\", \n"
                    + "            \"Octopus.Action.Package.FeedId\": \"feeds-builtin\", \n"
                    + "            \"Octopus.Action.Package.PackageId\": \"first-bamboo-int\", \n"
                    + "            \"Octopus.Action.Package.CustomInstallationDirectory\": \"C:\\\\Apps\\\\apache-tomcat-8.5.15\\\\webapps\" \n"
                    + "          }, \n"
                    + "          \"Links\": {} \n"
                    + "        } \n"
                    + "      ] \n"
                    + "    } \n"
                    + "  ], \n"
                    + "  \"Version\": 1, \n"
                    + "  \"LastSnapshotId\": null, \n"
                    + "  \"Links\": { \n"
                    + "    \"Self\": \"/api/deploymentprocesses/deploymentprocess-Projects-1\", \n"
                    + "    \"Project\": \"/api/projects/Projects-1\", \n"
                    + "    \"Template\": \"/api/deploymentprocesses/deploymentprocess-Projects-1/template{?channel,releaseId}\" \n"
                    + "  } \n"
                    + "}";

    private static final String PACKAGES_RESPONSE =
            "{ \n"
                    + "  \"ItemType\": \"Package\", \n"
                    + "  \"IsStale\": false, \n"
                    + "  \"TotalResults\": 2, \n"
                    + "  \"ItemsPerPage\": 30, \n"
                    + "  \"Items\": [ \n"
                    + "    { \n"
                    + "      \"Id\": \"packages-first-bamboo-int-0.0.1\", \n"
                    + "      \"PackageSizeBytes\": 150, \n"
                    + "      \"Hash\": \"58a21b594c9549fa1e94da70eb569da212dfd3b3\", \n"
                    + "      \"NuGetPackageId\": \"first-bamboo-int\", \n"
                    + "      \"PackageId\": \"first-bamboo-int\", \n"
                    + "      \"NuGetFeedId\": \"feeds-builtin\", \n"
                    + "      \"FeedId\": \"feeds-builtin\", \n"
                    + "      \"Title\": \"first-bamboo-int\", \n"
                    + "      \"Summary\": null, \n"
                    + "      \"Version\": \"0.0.1\", \n"
                    + "      \"Description\": null, \n"
                    + "      \"Published\": \"Monday, 22 May 2017 6:58 AM\", \n"
                    + "      \"ReleaseNotes\": null, \n"
                    + "      \"FileExtension\": \".zip\", \n"
                    + "      \"Links\": { \n"
                    + "        \"Self\": \"/api/packages/packages-first-bamboo-int-0.0.1\", \n"
                    + "        \"AllVersions\": \"/api/packages?nuGetPackageId=first-bamboo-int\", \n"
                    + "        \"Feed\": \"/api/feeds/feeds-builtin\", \n"
                    + "        \"Raw\": \"/api/packages/packages-first-bamboo-int-0.0.1/raw\" \n"
                    + "      } \n"
                    + "    }, \n"
                    + "    { \n"
                    + "      \"Id\": \"packages-second-bamboo-int-0.0.1\", \n"
                    + "      \"PackageSizeBytes\": 150, \n"
                    + "      \"Hash\": \"58a21b594c9549fa1e94da70eb569da212dfd3b3\", \n"
                    + "      \"NuGetPackageId\": \"second-bamboo-int\", \n"
                    + "      \"PackageId\": \"second-bamboo-int\", \n"
                    + "      \"NuGetFeedId\": \"feeds-builtin\", \n"
                    + "      \"FeedId\": \"feeds-builtin\", \n"
                    + "      \"Title\": \"second-bamboo-int\", \n"
                    + "      \"Summary\": null, \n"
                    + "      \"Version\": \"0.0.1\", \n"
                    + "      \"Description\": null, \n"
                    + "      \"Published\": \"Monday, 22 May 2017 6:58 AM\", \n"
                    + "      \"ReleaseNotes\": null, \n"
                    + "      \"FileExtension\": \".zip\", \n"
                    + "      \"Links\": { \n"
                    + "        \"Self\": \"/api/packages/packages-second-bamboo-int-0.0.1\", \n"
                    + "        \"AllVersions\": \"/api/packages?nuGetPackageId=second-bamboo-int\", \n"
                    + "        \"Feed\": \"/api/feeds/feeds-builtin\", \n"
                    + "        \"Raw\": \"/api/packages/packages-second-bamboo-int-0.0.1/raw\" \n"
                    + "      } \n"
                    + "    } \n"
                    + "  ], \n"
                    + "  \"Links\": { \n"
                    + "    \"Self\": \"/api/packages?skip=0&take=30\", \n"
                    + "    \"Page.Current\": \"/api/packages?latest=False&skip=0&take=30\", \n"
                    + "    \"Page.0\": \"/api/packages?latest=False&skip=0&take=30\" \n"
                    + "  } \n"
                    + "}";

    private static final String CREATE_RELEASE_RESPONSE =
            "{ \n"
                    + "  \"Id\": \"Releases-5\", \n"
                    + "  \"Assembled\": \"2017-05-21T21:23:39.469+00:00\", \n"
                    + "  \"ReleaseNotes\": null, \n"
                    + "  \"ProjectId\": \"Projects-1\", \n"
                    + "  \"ChannelId\": \"Channels-1\", \n"
                    + "  \"ProjectVariableSetSnapshotId\": \"variableset-Projects-1-s-0-WQEBN\", \n"
                    + "  \"LibraryVariableSetSnapshotIds\": [], \n"
                    + "  \"ProjectDeploymentProcessSnapshotId\": \"deploymentprocess-Projects-1-s-1-C2PEZ\", \n"
                    + "  \"SelectedPackages\": [ \n"
                    + "    { \n"
                    + "      \"StepName\": \"Tomcat Deploy\", \n"
                    + "      \"Version\": \"0.0.1\" \n"
                    + "    } \n"
                    + "  ], \n"
                    + "  \"Version\": \"2017.05.22072339\", \n"
                    + "  \"LastModifiedOn\": \"2017-05-21T21:23:39.485+00:00\", \n"
                    + "  \"LastModifiedBy\": \"admin\", \n"
                    + "  \"Links\": { \n"
                    + "    \"Self\": \"/api/releases/Releases-5{?force}\", \n"
                    + "    \"Project\": \"/api/projects/Projects-1\", \n"
                    + "    \"Progression\": \"/api/releases/Releases-5/progression\", \n"
                    + "    \"Deployments\": \"/api/releases/Releases-5/deployments{?skip}\", \n"
                    + "    \"DeploymentTemplate\": \"/api/releases/Releases-5/deployments/template\", \n"
                    + "    \"Artifacts\": \"/api/artifacts?regarding=Releases-5\", \n"
                    + "    \"ProjectVariableSnapshot\": \"/api/variables/variableset-Projects-1-s-0-WQEBN\", \n"
                    + "    \"ProjectDeploymentProcessSnapshot\": \"/api/deploymentprocesses/deploymentprocess-Projects-1-s-1-C2PEZ\", \n"
                    + "    \"Web\": \"/app#/releases/Releases-5\", \n"
                    + "    \"SnapshotVariables\": \"/api/releases/Releases-5/snapshot-variables\", \n"
                    + "    \"Defects\": \"/api/releases/Releases-5/defects\", \n"
                    + "    \"ReportDefect\": \"/api/releases/Releases-5/defects\", \n"
                    + "    \"ResolveDefect\": \"/api/releases/Releases-5/defects/resolve\" \n"
                    + "  } \n"
            + "}";

    private static final String GET_RELEASES_RESPONSE =
            "{ \n"
                    + "\"ItemType\": \"Release\", \n"
                    + "\"IsStale\": false, \n"
                    + "\"TotalResults\": 0, \n"
                    + "\"ItemsPerPage\": 30, \n"
                    + "\"Items\": [] \n"
                    + "}";

    /**
     * @return The client that feign will use to make actual HTTP calls
     */
    @NotNull
    @Bean
    public Client getFeignClient() {
        LOGGER.info("Creating mock feign client");
        final MockClient mockClient = new MockClient();
        /*
            Here we mock out the rest endpoint that this plugin will call
            during the integration test. This means we can test the plugin
            without any actual HTTP server to connect to.
         */
        mockClient.noContent(HttpMethod.POST, OctoConstants.LOCAL_OCTOPUS_INSTANCE + "/api/packages/raw?replace=true");
        mockClient.noContent(HttpMethod.POST, OctoConstants.LOCAL_OCTOPUS_INSTANCE + "/api/packages/raw?replace=false");
        mockClient.noContent(HttpMethod.POST, OctoConstants.LOCAL_OCTOPUS_INSTANCE + "/api/packages/raw");
        mockClient.add(
                HttpMethod.POST,
                OctoConstants.LOCAL_OCTOPUS_INSTANCE + "/api/releases?ignoreChannelRules=false",
                Response.builder()
                        .body(CREATE_RELEASE_RESPONSE, Charset.defaultCharset())
                        .headers(new HashMap<String, Collection<String>>())
                        .status(HttpStatus.SC_CREATED));
        mockClient.add(
                HttpMethod.GET,
                OctoConstants.LOCAL_OCTOPUS_INSTANCE + "/api/projects/all",
                Response.builder()
                        .body(GET_PROJECTS_RESPONSE, Charset.defaultCharset())
                        .headers(new HashMap<String, Collection<String>>())
                        .status(HttpStatus.SC_OK));

        mockClient.add(
                HttpMethod.GET,
                OctoConstants.LOCAL_OCTOPUS_INSTANCE + "/api/projects/Projects-1/channels",
                Response.builder()
                        .body(PROJECT_CHANNELS_RESPONSE, Charset.defaultCharset())
                        .headers(new HashMap<String, Collection<String>>())
                        .status(HttpStatus.SC_OK));

        mockClient.add(
                HttpMethod.GET,
                OctoConstants.LOCAL_OCTOPUS_INSTANCE + "/api/deploymentprocesses/deploymentprocess-Projects-1",
                Response.builder()
                        .body(DEPLOYMENT_PROCESS_RESPONSE, Charset.defaultCharset())
                        .headers(new HashMap<String, Collection<String>>())
                        .status(HttpStatus.SC_OK));

        mockClient.add(
                HttpMethod.GET,
                OctoConstants.LOCAL_OCTOPUS_INSTANCE + "/api/packages",
                Response.builder()
                        .body(PACKAGES_RESPONSE, Charset.defaultCharset())
                        .headers(new HashMap<String, Collection<String>>())
                        .status(HttpStatus.SC_OK));

        mockClient.add(
                HttpMethod.GET,
                OctoConstants.LOCAL_OCTOPUS_INSTANCE + "/api/releases",
                Response.builder()
                        .body(GET_RELEASES_RESPONSE, Charset.defaultCharset())
                        .headers(new HashMap<String, Collection<String>>())
                        .status(HttpStatus.SC_OK));

        return mockClient;
    }
}
