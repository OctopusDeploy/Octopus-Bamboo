package com.octopus.services;

import com.atlassian.bamboo.task.TaskContext;

/**
 * A service for creating mock objects to test with
 */
public interface MockObjectService {
    TaskContext getTaskContext();
}
