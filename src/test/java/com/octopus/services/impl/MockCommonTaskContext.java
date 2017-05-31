package com.octopus.services.impl;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.atlassian.bamboo.serialization.WhitelistedSerializable;
import com.atlassian.bamboo.task.CommonTaskContext;
import com.atlassian.bamboo.v2.build.CommonContext;
import com.octopus.constants.OctoConstants;
import com.octopus.constants.OctoTestConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

/**
 * A mock common task context
 */
public class MockCommonTaskContext implements CommonTaskContext {
    private final RecordingBuildLogger logger = new RecordingBuildLogger();
    private final StubCommonContext stubCommonContext = new StubCommonContext();
    private File workingDir = new File(".");

    @Override
    public long getId() {
        return 0;
    }

    @NotNull
    @Override
    public String getPluginKey() {
        return "pluginKey";
    }

    @Nullable
    @Override
    public String getUserDescription() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isFinalising() {
        return false;
    }

    @NotNull
    @Override
    public CommonContext getCommonContext() {
        return stubCommonContext;
    }

    @NotNull
    @Override
    public BuildLogger getBuildLogger() {
        return logger;
    }

    @NotNull
    @Override
    public File getRootDirectory() {
        return new File(".");
    }

    @NotNull
    @Override
    public File getWorkingDirectory() {
        return workingDir;
    }

    public void setWorkingDir(final File workingDir) {
        this.workingDir = workingDir;
    }

    @NotNull
    @Override
    public ConfigurationMap getConfigurationMap() {
        final ConfigurationMap retValue = new ConfigurationMapImpl();
        retValue.put(OctoConstants.SERVER_URL, OctoConstants.LOCAL_OCTOPUS_INSTANCE);
        retValue.put(OctoConstants.API_KEY, OctoTestConstants.DUMMY_API_KEY);
        retValue.put(OctoConstants.OCTOPUS_CLI, "OctopusCli");
        retValue.put(OctoConstants.FORCE, "true");
        retValue.put(OctoConstants.VERBOSE_LOGGING, "true");
        retValue.put(OctoConstants.PUSH_PATTERN, "**/*.zip");
        retValue.put(OctoConstants.PROJECT_NAME, OctoTestConstants.TEST_PROJECT);
        retValue.put(OctoConstants.RELEASE_VERSION, "0.0.1");
        retValue.put(OctoConstants.VERBOSE_LOGGING, "true");
        return retValue;
    }

    @Nullable
    @Override
    public Map<String, String> getRuntimeTaskContext() {
        return null;
    }

    @Nullable
    @Override
    public Map<String, WhitelistedSerializable> getRuntimeTaskData() {
        return null;
    }

    @Override
    public boolean doesTaskProduceTestResults() {
        return false;
    }
}
