package com.octopus.services.impl;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.build.logger.NullBuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.atlassian.bamboo.serialization.WhitelistedSerializable;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.CommonContext;
import com.octopus.services.MockObjectService;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

/**
 * Implementation of the MockObjectService
 */
public class MockObjectServiceImpl implements MockObjectService {
    public TaskContext getTaskContext() {
        return new TaskContext() {
            public long getId() {
                return 0;
            }

            @org.jetbrains.annotations.NotNull
            public String getPluginKey() {
                return "PluginKey";
            }

            @Nullable
            public String getUserDescription() {
                return null;
            }

            public boolean isEnabled() {
                return true;
            }

            public boolean isFinalising() {
                return false;
            }

            @org.jetbrains.annotations.NotNull
            public BuildContext getBuildContext() {
                return null;
            }

            @org.jetbrains.annotations.NotNull
            public CommonContext getCommonContext() {
                return null;
            }

            @org.jetbrains.annotations.NotNull
            public BuildLogger getBuildLogger() {
                return new NullBuildLogger();
            }

            @org.jetbrains.annotations.NotNull
            public File getRootDirectory() {
                return new File(System.getProperty("java.io.tmpdir"));
            }

            @org.jetbrains.annotations.NotNull
            public File getWorkingDirectory() {
                return new File(System.getProperty("java.io.tmpdir"));
            }

            @org.jetbrains.annotations.NotNull
            public ConfigurationMap getConfigurationMap() {
                return new ConfigurationMapImpl();
            }

            @Nullable
            public Map<String, String> getRuntimeTaskContext() {
                return null;
            }

            @Nullable
            public Map<String, WhitelistedSerializable> getRuntimeTaskData() {
                return null;
            }

            public boolean doesTaskProduceTestResults() {
                return false;
            }
        };
    }
}
