package com.octopus.services.impl;

import com.atlassian.bamboo.task.TaskContext;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.services.FeignService;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;

import javax.validation.constraints.NotNull;

/**
 * Implementation of the Feign Service.
 */
public class FeignServiceImpl implements FeignService {
    @Override
    @NotNull
    public RestAPI createClient(@NotNull TaskContext taskContext) {
        final Logger buildLogger = new BambooFeignLogger(taskContext.getBuildLogger());

        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(buildLogger)
                .logLevel(Logger.Level.FULL)
                .target(RestAPI.class, serverUrl + "/api");
    }
}
