package it.com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.build.logger.NullBuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.atlassian.bamboo.serialization.WhitelistedSerializable;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskState;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.CommonContext;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.google.common.io.Files;
import com.octopus.bamboo.plugins.task.deploy.OctopusDeployTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Map;

/**
 * Tests for the OctopusDeployTask class
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class OctopusDeployTaskTest {

    private OctopusDeployTask octopusDeployTask;

    public OctopusDeployTaskTest(final OctopusDeployTask octopusDeployTask) {
        this.octopusDeployTask = octopusDeployTask;
    }

    @Test
    public void initialTest() throws TaskException {
        final TaskResult result = octopusDeployTask.execute(buildTaskContext());
        Assert.assertTrue(result.getTaskState() == TaskState.SUCCESS);
    }

    private TaskContext buildTaskContext() {
        return new TaskContext() {

            public long getId() {
                return 0;
            }

            @NotNull
            public String getPluginKey() {
                return "pluginKey";
            }

            @Nullable
            public String getUserDescription() {
                return null;
            }

            public boolean isEnabled() {
                return false;
            }

            public boolean isFinalising() {
                return false;
            }

            @NotNull
            public BuildContext getBuildContext() {
                return null;
            }

            @NotNull
            public CommonContext getCommonContext() {
                return null;
            }

            @NotNull
            public BuildLogger getBuildLogger() {
                return new NullBuildLogger();
            }

            @NotNull
            public File getRootDirectory() {
                return Files.createTempDir();
            }

            @NotNull
            public File getWorkingDirectory() {
                return Files.createTempDir();
            }

            @NotNull
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
