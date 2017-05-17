package com.octopus.services;

import com.atlassian.bamboo.task.TaskContext;
import com.octopus.api.RestAPI;

import javax.validation.constraints.NotNull;

/**
 * A service for creating feign clients for use with Bamboo tasks
 */
public interface FeignService {
    /**
     * @param taskContext The Bamboo task context
     * @param enableRetry set to true to allow the client to retry the request
     * @return A fiegn client that can be used to access the Octopus Deploy API
     */
    @NotNull
    RestAPI createClient(@NotNull TaskContext taskContext, boolean enableRetry);
}
