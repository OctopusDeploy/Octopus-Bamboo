package com.octopus.services;

import javax.validation.constraints.NotNull;

/**
 * Abstraction for logging messages
 */
public interface LoggerService {
    void logMessage(@NotNull String message);

    void logError(@NotNull String message);
}
