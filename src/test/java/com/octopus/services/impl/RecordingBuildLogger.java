package com.octopus.services.impl;

import com.atlassian.bamboo.build.LogEntry;
import com.atlassian.bamboo.build.SimpleLogEntry;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.build.logger.LogInterceptorStack;
import com.atlassian.bamboo.build.logger.LogMutatorStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock logging implementation
 */
public class RecordingBuildLogger implements BuildLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordingBuildLogger.class);
    private final List<LogEntry> buildlogs = new ArrayList<>();
    private final List<LogEntry> errorlogs = new ArrayList<>();

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
        LOGGER.info("RecordingBuildLogger.addBuildLogEntry() " + logEntry.getLog());
        buildlogs.add(logEntry);
        return logEntry.getLog();
    }

    @Override
    public String addBuildLogEntry(@NotNull String s) {
        LOGGER.info("RecordingBuildLogger.addBuildLogEntry() " + s);
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
        LOGGER.error("RecordingBuildLogger.addErrorLogEntry() " + logEntry.getLog());
        errorlogs.add(logEntry);
        return logEntry.getLog();
    }

    @Override
    public String addErrorLogEntry(String s) {
        LOGGER.error("RecordingBuildLogger.addErrorLogEntry() " + s);
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
        return null;
    }

    @Override
    public void close() {

    }
}
