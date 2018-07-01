package com.octopus.services.impl;

import com.atlassian.bamboo.process.BackgroundTaskProcesses;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessContext;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.CommonTaskContext;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.utils.process.ExternalProcess;
import com.atlassian.utils.process.ProcessException;
import com.atlassian.utils.process.ProcessHandler;
import com.atlassian.utils.process.Watchdog;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * A mock process execution service that hold onto the commands that
 * were run.
 */
public class MockProcessService implements ProcessService {
    private List<String> commands;

    @NotNull
    @Override
    public ExternalProcess createExternalProcess(@NotNull ProcessContext processContext, @NotNull ExternalProcessBuilder externalProcessBuilder) {
        return null;
    }

    @NotNull
    @Override
    public ExternalProcess createExternalProcess(@NotNull final CommonTaskContext commonTaskContext,
                                                 @NotNull final ExternalProcessBuilder externalProcessBuilder) {
        return null;
    }

    @NotNull
    @Override
    public ExternalProcess executeExternalProcess(@NotNull final CommonTaskContext commonTaskContext,
                                                  @NotNull final ExternalProcessBuilder externalProcessBuilder) {
        commands = externalProcessBuilder.getCommand();
        return new ExternalProcess() {

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public void resetWatchdog() {

            }

            @Override
            public void execute() {

            }

            @Override
            public void executeWhile(final Runnable runnable) {

            }

            @Override
            public void finish() {

            }

            @Override
            public boolean finish(final int maxWait) {
                return false;
            }

            @Override
            public String getCommandLine() {
                return null;
            }

            @Override
            public ProcessHandler getHandler() {
                return new ProcessHandler() {

                    @Override
                    public void complete(final int exitCode,
                                         final boolean canceled,
                                         final ProcessException exception) {

                    }

                    @Override
                    public ProcessException getException() {
                        return null;
                    }

                    @Override
                    public int getExitCode() {
                        return 0;
                    }

                    @Override
                    public boolean hasInput() {
                        return false;
                    }

                    @Override
                    public boolean isCanceled() {
                        return false;
                    }

                    @Override
                    public boolean isComplete() {
                        return false;
                    }

                    @Override
                    public void processError(final InputStream error) throws ProcessException {

                    }

                    @Override
                    public void processOutput(final InputStream output) throws ProcessException {

                    }

                    @Override
                    public void provideInput(final OutputStream input) {

                    }

                    @Override
                    public void reset() {

                    }

                    @Override
                    public void setWatchdog(final Watchdog watchdog) {

                    }

                    @Override
                    public boolean succeeded() {
                        return false;
                    }
                };
            }

            @Override
            public long getStartTime() {
                return 0;
            }

            @Override
            public boolean isTimedOut() {
                return false;
            }

            @Override
            public boolean isAlive() {
                return false;
            }

            @Override
            public void start() {

            }
        };
    }

    @NotNull
    @Override
    public ExternalProcess executeExternalProcess(@NotNull ProcessContext processContext, @NotNull ExternalProcessBuilder externalProcessBuilder) {
        return null;
    }

    @Override
    public BackgroundTaskProcesses getBackgroundProcesses() {
        return null;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(final List<String> commands) {
        this.commands = commands;
    }
}
