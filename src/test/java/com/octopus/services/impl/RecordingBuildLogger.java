package com.octopus.services.impl;

import com.atlassian.bamboo.build.LogEntry;
import com.atlassian.bamboo.build.SimpleLogEntry;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.build.logger.LogInterceptorStack;
import com.atlassian.bamboo.build.logger.LogMutatorStack;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Mock logging implementation
 */
public class RecordingBuildLogger implements BuildLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordingBuildLogger.class);
    private final List<LogEntry> buildlogs = new ArrayList<>();
    private final List<LogEntry> errorlogs = new ArrayList<>();

    public List<LogEntry> findErrorLogs(@NotNull final String message) {
        checkNotNull(message);

        final List<LogEntry> retValue = new ArrayList<>(this.getErrorLog());

        CollectionUtils.filter(
                retValue,
                new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        return ((LogEntry) o).toString().contains(message);
                    }
                });

        return retValue;
    }

    public List<LogEntry> findLogs(@NotNull final String message) {
        checkNotNull(message);

        final List<LogEntry> retValue = new ArrayList<>(this.getBuildLog());

        CollectionUtils.filter(
                retValue,
                new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        return ((LogEntry) o).toString().contains(message);
                    }
                });

        return retValue;
    }

    @NotNull
    @Override
    public List<LogEntry> getBuildLog() {
        return buildlogs;
    }

    @NotNull
    @Override
    public List<LogEntry> getErrorLog() {
        return errorlogs;
    }

    @NotNull
    @Override
    public List<LogEntry> getLastNLogEntries(int i) {
        return null;
    }

    @Override
    public List<String> getStringErrorLogs() {
        return null;
    }

    @NotNull
    @Override
    public String addBuildLogEntry(@NotNull LogEntry logEntry) {
        buildlogs.add(logEntry);
        return logEntry.getLog();
    }

    @Override
    public String addBuildLogEntry(@NotNull String s) {
        buildlogs.add(new SimpleLogEntry(s));
        return s;
    }

    @Override
    public String addBuildLogHeader(String s, boolean b) {
        return null;
    }

    @NotNull
    @Override
    public String addErrorLogEntry(LogEntry logEntry) {
        errorlogs.add(logEntry);
        return logEntry.getLog();
    }

    @Override
    public String addErrorLogEntry(String s) {
        errorlogs.add(new SimpleLogEntry(s));
        return s;
    }

    @Override
    public void addErrorLogEntry(String s, @Nullable Throwable throwable) {
        errorlogs.add(new SimpleLogEntry(s + " " + throwable));
    }

    @Override
    public void stopStreamingBuildLogs() {

    }

    @Override
    public void clearBuildLog() {

    }

    @Override
    public long getTimeOfLastLog() {
        return 0;
    }

    @NotNull
    @Override
    public LogInterceptorStack getInterceptorStack() {
        return null;
    }

    @NotNull
    @Override
    public LogMutatorStack getMutatorStack() {
        return new LogMutatorStack();
    }

    @Override
    public void close() {

    }
}
