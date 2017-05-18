package com.octopus.services.impl;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.octopus.services.CommonTaskService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementation of the task service
 */
@Component
public class CommonTaskServiceImpl implements CommonTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonTaskServiceImpl.class);

    public void logInfo(@NotNull final TaskContext taskContext, @NotNull final String message) {
        LOGGER.info(message);
        taskContext.getBuildLogger().addBuildLogEntry(message);
    }

    public void logError(@NotNull final TaskContext taskContext, @NotNull final String message) {
        LOGGER.error(message);
        taskContext.getBuildLogger().addErrorLogEntry(message);
    }

    public TaskResult buildResult(@NotNull final TaskContext taskContext, final boolean success) {
        final TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext);
        if (success) {
            builder.success();
        } else {
            builder.failed();
        }

        return builder.build();
    }
}
