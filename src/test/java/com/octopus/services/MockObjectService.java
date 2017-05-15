package com.octopus.services;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.v2.build.CommonContext;

/**
 * A service for creating mock objects to test with
 */
public interface MockObjectService {
    TaskContext getTaskContext();

    CommonContext getCommonContext();
}
