package com.octopus.configuration.impl;

import com.octopus.configuration.PluginConfiguration;
import feign.Client;
import feign.mock.MockClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.validation.constraints.NotNull;

/**
 * Create some Spring beans
 */
@Configuration
@Profile("test")
public class MockPluginConfigurationImpl implements PluginConfiguration {
    /**
     * @return The client that feign will use to mock HTTP calls
     */
    @Override
    @NotNull
    @Bean
    public Client getFeignClient() {
        return new MockClient();
    }
}
