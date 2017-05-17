package com.octopus.bamboo.configuration.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.octopus.bamboo.configuration.PluginConfiguration;
import feign.Client;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;
import javax.validation.constraints.NotNull;

/**
 * Create some Spring beans
 */
@Configuration
@ExportAsService({PluginConfiguration.class})
@Named("pluginConfiguration")
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
