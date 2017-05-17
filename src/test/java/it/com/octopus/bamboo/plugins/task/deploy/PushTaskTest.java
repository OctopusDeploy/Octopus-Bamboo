package it.com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.task.*;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.services.MockObjectService;
import com.octopus.services.impl.MockObjectServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.constraints.NotNull;

/**
 * Integration tests for the deployment task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class PushTaskTest {
    private static final MockObjectService MOCK_OBJECT_SERVICE = new MockObjectServiceImpl();
    private final TaskType octopusDeployTask;


    public PushTaskTest(@NotNull final TaskType octopusDeployTask) {
        this.octopusDeployTask = octopusDeployTask;
    }


    @Test
    public void testPush() throws TaskException {
        Assert.assertNotNull(octopusDeployTask);

        final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext();
        final TaskResult taskResult = octopusDeployTask.execute(taskContext);

        Assert.assertNotNull(taskResult.getTaskState() == TaskState.SUCCESS);
    }
}
