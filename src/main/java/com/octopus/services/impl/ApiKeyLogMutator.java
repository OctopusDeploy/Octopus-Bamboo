package com.octopus.services.impl;

import com.atlassian.bamboo.build.LogEntry;
import com.atlassian.bamboo.build.logger.LogMutator;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Strips out the API keys from the logs
 */
@Component
public class ApiKeyLogMutator implements LogMutator {
    private static final Pattern API_KEY = Pattern.compile("API-\\w{20}");
    private static final String REPLACEMENT = "API-....................";

    @Override
    public LogEntry mutate(@NotNull final LogEntry logEntry) {
        return removeApiKeys(logEntry);
    }

    @Override
    public LogEntry mutateError(@NotNull final LogEntry logEntry) {
        return removeApiKeys(logEntry);
    }

    private LogEntry removeApiKeys(@NotNull final LogEntry logEntry) {
        final String fixedValue = API_KEY.matcher(logEntry.getUnstyledLog()).replaceAll(REPLACEMENT);
        return logEntry.cloneAndMutate(fixedValue);
    }
}
