package com.octopus.services.impl;

import com.atlassian.bamboo.task.TaskContext;
import com.octopus.services.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Logger that logs to the console and to the bamboo output
 */
public class LoggerServiceImpl implements LoggerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerServiceImpl.class);
    final TaskContext taskContext;

    public LoggerServiceImpl(@NotNull final TaskContext taskContext) {
        this.taskContext = taskContext;
    }

    @Override
    public void logMessage(@NotNull String message) {
        checkNotNull(message);

        LOGGER.info(message);
        taskContext.getBuildLogger().addBuildLogEntry(message);
    }

    @Override
    public void logError(@NotNull String message) {
        checkNotNull(message);

        LOGGER.error(message);
        taskContext.getBuildLogger().addErrorLogEntry(message);
    }
}
