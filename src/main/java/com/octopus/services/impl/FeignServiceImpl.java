package com.octopus.services.impl;

import com.atlassian.bamboo.task.TaskContext;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.services.FeignService;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.form.FormEncoder;
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
    public RestAPI createClient(@NotNull final TaskContext taskContext) {
        final Logger buildLogger = new BambooFeignLogger(taskContext.getBuildLogger());

        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);
        final String apiKey = taskContext.getConfigurationMap().get(OctoConstants.API_KEY);

        /*
            See https://howtoprogram.xyz/2016/12/29/file-uploading-open-feign/
            for details on uploading files through feign
         */
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .encoder(new FormEncoder(new GsonEncoder()))
                .decoder(new GsonDecoder())
                .logger(buildLogger)
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(new RequestInterceptor() {
                    @Override
                    public void apply(final RequestTemplate template) {
                        template.header(OctoConstants.X_OCTOPUS_APIKEY, apiKey);
                    }
                })
                .target(RestAPI.class, serverUrl + "/api");
    }
}
