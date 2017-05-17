package com.octopus.bamboo.configuration;

import feign.Client;

import javax.validation.constraints.NotNull;

/**
 * Defines the beans that the configuration files must supply
 */
public interface PluginConfiguration {
    @NotNull
    Client getFeignClient();
}
