package com.octopus.services;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskResult;
import org.jetbrains.annotations.NotNull;

/**
 * Useful util functions used when working with tasks
 */
public interface CommonTaskService {
    /**
     * Log an info message
     *
     * @param taskContext The Bamboo task context
     * @param message     The message to be logged
     */
    void logInfo(@NotNull TaskContext taskContext, @NotNull String message);

    /**
     * Log an error message
     *
     * @param taskContext The Bamboo task context
     * @param message     The message to be logged
     */
    void logError(@NotNull TaskContext taskContext, @NotNull String message);

    /**
     * @param taskContext The bamboo task context
     * @param success     true if this was a successful result
     * @return A success or failure result
     */
    TaskResult buildResult(@NotNull TaskContext taskContext, boolean success);
}
