package com.octopus.services;

import com.octopus.api.RestAPI;
import feign.Logger;

import javax.validation.constraints.NotNull;

/**
 * A service for creating feign clients for use with Bamboo tasks
 */
public interface FeignService {
    /**
     * @param buildLogger The logger
     * @param serverUrl The Octopus Deploy URL
     * @param apiKey The Octopus Deploy API key
     * @param verboseLogging true if verbose logging is required
     * @param enableRetry set to true to allow the client to retry the request
     * @return A fiegn client that can be used to access the Octopus Deploy API
     */
    @NotNull
    RestAPI createClient(@NotNull Logger buildLogger,
                         @NotNull String serverUrl,
                         @NotNull String apiKey,
                         boolean verboseLogging,
                         boolean enableRetry);
}
