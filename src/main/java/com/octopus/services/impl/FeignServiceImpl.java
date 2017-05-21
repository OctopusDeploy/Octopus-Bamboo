package com.octopus.services.impl;

import com.atlassian.bamboo.task.TaskContext;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.services.FeignService;
import feign.*;
import feign.form.FormEncoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Implementation of the Feign Service.
 */
@Component
public class FeignServiceImpl implements FeignService {

    private final Client client;

    @Autowired
    public FeignServiceImpl(@NotNull final Client client) {
        this.client = client;
    }

    @Override
    @NotNull
    public RestAPI createClient(@NotNull final TaskContext taskContext, final boolean enableRetry) {
        checkNotNull(taskContext);

        final Logger buildLogger = new BambooFeignLogger(taskContext.getBuildLogger());

        final String serverUrl = taskContext.getConfigurationMap().get(OctoConstants.SERVER_URL);
        final String apiKey = taskContext.getConfigurationMap().get(OctoConstants.API_KEY);
        final String loggingLevel = taskContext.getConfigurationMap().get(OctoConstants.VERBOSE_LOGGING);

        checkState(StringUtils.isNotBlank(serverUrl));
        checkState(StringUtils.isNotBlank(apiKey),
                "OCTOPUS-BAMBOO-ERROR-0004: The API key was blank. "
                        + "Make sure the task has the API key configured correctly.");

        /*
            See https://howtoprogram.xyz/2016/12/29/file-uploading-open-feign/
            for details on uploading files through feign
         */
        return Feign.builder()
                .client(client)
                .retryer(enableRetry ? new Retryer.Default() : Retryer.NEVER_RETRY)
                .encoder(new FormEncoder(new GsonEncoder(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .create())))
                .decoder(new GsonDecoder(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                        .create()))
                .logger(buildLogger)
                .logLevel(BooleanUtils.toBooleanDefaultIfNull(BooleanUtils.toBooleanObject(loggingLevel), false)
                        ? Logger.Level.FULL
                        : Logger.Level.NONE)
                .requestInterceptor(new RequestInterceptor() {
                    @Override
                    public void apply(final RequestTemplate template) {
                        template.header(OctoConstants.X_OCTOPUS_APIKEY, apiKey);
                    }
                })
                .target(RestAPI.class, serverUrl + "/api");
    }
}
