package com.octopus.configuration.impl;

import com.octopus.configuration.PluginConfiguration;
import feign.Client;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Create some Spring beans
 */
@Configuration
@Component
public class PluginConfigurationImpl implements PluginConfiguration {
    /**
     * @return The client that feign will use to make actual HTTP calls
     */
    @Override
    @NotNull
    @Bean
    public Client getFeignClient() {
        return new OkHttpClient();
    }
}
