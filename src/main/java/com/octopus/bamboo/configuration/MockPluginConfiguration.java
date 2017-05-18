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

/**
 * Create some Spring beans
 */
@Configuration
@Component
@Conditional(IntegrationTestCondition.class)
public class MockPluginConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockPluginConfiguration.class);
    private static final String CREATE_RELEASE_RESPONSE = "{\n"
            + "  \"Id\": \"Releases-22\",\n"
            + "  \"Assembled\": \"2017-05-18T19:35:39.349+00:00\",\n"
            + "  \"ReleaseNotes\": null,\n"
            + "  \"ProjectId\": \"Projects-22\",\n"
            + "  \"ChannelId\": \"Channels-22\",\n"
            + "  \"ProjectVariableSetSnapshotId\": \"variableset-Projects-22-s-0-UB2Z5\",\n"
            + "  \"LibraryVariableSetSnapshotIds\": [],\n"
            + "  \"ProjectDeploymentProcessSnapshotId\": \"deploymentprocess-Projects-22-s-2-MY6TD\",\n"
            + "  \"SelectedPackages\": [\n"
            + "    {\n"
            + "      \"StepName\": \"Deploy to Tomcat\",\n"
            + "      \"Version\": \"0.0.13\"\n"
            + "    }\n"
            + "  ],\n"
            + "  \"Version\": \"0.0.8\",\n"
            + "  \"LastModifiedOn\": \"2017-05-18T19:35:39.391+00:00\",\n"
            + "  \"LastModifiedBy\": \"admin\",\n"
            + "  \"Links\": {\n"
            + "    \"Self\": \"/api/releases/Releases-22{?force}\",\n"
            + "    \"Project\": \"/api/projects/Projects-22\",\n"
            + "    \"Progression\": \"/api/releases/Releases-22/progression\",\n"
            + "    \"Deployments\": \"/api/releases/Releases-22/deployments{?skip}\",\n"
            + "    \"DeploymentTemplate\": \"/api/releases/Releases-22/deployments/template\",\n"
            + "    \"Artifacts\": \"/api/artifacts?regarding=Releases-22\",\n"
            + "    \"ProjectVariableSnapshot\": \"/api/variables/variableset-Projects-22-s-0-UB2Z5\",\n"
            + "    \"ProjectDeploymentProcessSnapshot\": \"/api/deploymentprocesses/deploymentprocess-Projects-22-s-2-MY6TD\",\n"
            + "    \"Web\": \"/app#/releases/Releases-22\",\n"
            + "    \"SnapshotVariables\": \"/api/releases/Releases-22/snapshot-variables\",\n"
            + "    \"Defects\": \"/api/releases/Releases-22/defects\",\n"
            + "    \"ReportDefect\": \"/api/releases/Releases-22/defects\",\n"
            + "    \"ResolveDefect\": \"/api/releases/Releases-22/defects/resolve\"\n"
            + "  }\n"
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
                        .status(HttpStatus.SC_CREATED));

        return mockClient;
    }
}
