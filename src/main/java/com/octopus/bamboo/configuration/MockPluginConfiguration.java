package com.octopus.bamboo.configuration;

import com.octopus.bamboo.condition.IntegrationTestCondition;
import feign.Client;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Create some Spring beans
 */
@Configuration
@Component
@Conditional(IntegrationTestCondition.class)
public class MockPluginConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockPluginConfiguration.class);

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
        mockClient.noContent(HttpMethod.POST, "/api/packages/raw");
        return mockClient;
    }
}
