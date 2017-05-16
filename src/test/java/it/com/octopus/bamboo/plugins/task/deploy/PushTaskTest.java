package it.com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.task.TaskType;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.services.MockObjectService;
import com.octopus.services.impl.MockObjectServiceImpl;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.constraints.NotNull;

/**
 * Integration tests for the deployment task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class PushTaskTest {
    private static final MockObjectService MOCK_OBJECT_SERVICE = new MockObjectServiceImpl();
    private TaskType octopusDeployTask;
    private MockClient client;

    public PushTaskTest(@NotNull final TaskType octopusDeployTask) {
        this.octopusDeployTask = octopusDeployTask;

    }

    @Before
    public void configureMock() {
        client.noContent(HttpMethod.POST, "/api/packages/raw");
    }

    @Test
    public void test1() {
        Assert.assertNotNull(octopusDeployTask);
    }

    /*@Test
    public void test2() throws TaskException  {

        final TaskResult taskResult = octopusDeployTask.execute(MOCK_OBJECT_SERVICE.getTaskContext());

        Assert.assertTrue(taskResult.getTaskState() == SUCCESS);
        client.verifyOne(HttpMethod.POST, "/api/packages/raw");
    }*/
}
