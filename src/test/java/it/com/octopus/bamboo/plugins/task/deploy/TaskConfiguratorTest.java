package it.com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.task.TaskConfigurator;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Integration tests for the deployment configuration
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class TaskConfiguratorTest {
    private final TaskConfigurator taskConfigurator;

    @Inject
    public TaskConfiguratorTest(@ComponentImport @NotNull @Qualifier("pushTaskConfigurator") final TaskConfigurator taskConfigurator) {
        this.taskConfigurator = taskConfigurator;
    }

    @Test
    public void testServiceExists() {
        Assert.assertNotNull(taskConfigurator);
    }
}
