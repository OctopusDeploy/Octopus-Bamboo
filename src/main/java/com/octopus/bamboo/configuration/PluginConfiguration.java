package com.octopus.bamboo.configuration;

import com.octopus.bamboo.condition.NotIntegrationTestCondition;
import feign.Client;
import feign.okhttp.OkHttpClient;
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
@Conditional(NotIntegrationTestCondition.class)
public class PluginConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginConfiguration.class);

    /**
     * @return The client that feign will use to make actual HTTP calls
     */
    @NotNull
    @Bean
    public Client getFeignClient() {
        LOGGER.info("Creating OKHTTP client");
        return new OkHttpClient();
    }
}
