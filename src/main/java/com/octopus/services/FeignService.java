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
     * @return A fiegn client that can be used to access the Octopus Deploy API
     */
    @NotNull
    RestAPI createClient(@NotNull TaskContext taskContext);
}
