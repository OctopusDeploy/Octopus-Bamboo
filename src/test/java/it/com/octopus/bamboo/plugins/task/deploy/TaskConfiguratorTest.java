package it.com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.task.TaskConfigurator;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import org.junit.runner.RunWith;

import javax.validation.constraints.NotNull;

/**
 * Integration tests for the deployment configuration
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class TaskConfiguratorTest {
    private final TaskConfigurator taskConfigurator;

    public TaskConfiguratorTest(@NotNull final TaskConfigurator taskConfigurator) {
        this.taskConfigurator = taskConfigurator;
    }
}
