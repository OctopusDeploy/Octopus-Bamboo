package com.octopus.services;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.v2.build.CommonContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A service for creating mock objects to test with
 */
public interface MockObjectService {
    TaskContext getTaskContext(@NotNull File workingDir);

    CommonContext getCommonContext();
}
