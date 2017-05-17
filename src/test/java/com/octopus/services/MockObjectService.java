package com.octopus.services;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.v2.build.CommonContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A service for creating mock objects to test with
 */
public interface MockObjectService {
    /**
     * @param workingDir The directory to use as the working dir
     * @return A mock TaskContext
     */
    TaskContext getTaskContext(@NotNull File workingDir);

    /**
     * @param workingDir  The directory to use as the working dir
     * @param forceUpload true if package uploads should be forced
     * @return A mock TaskContext
     */
    TaskContext getTaskContext(@NotNull File workingDir, boolean forceUpload);

    /**
     * @param workingDir  The directory to use as the working dir
     * @param forceUpload true if package uploads should be forced
     * @param pattern     The file matching pattern
     * @return A mock TaskContext
     */
    TaskContext getTaskContext(@NotNull File workingDir, boolean forceUpload, @NotNull String pattern);

    /**
     *
     * @return A mock CommonContext
     */
    CommonContext getCommonContext();
}
