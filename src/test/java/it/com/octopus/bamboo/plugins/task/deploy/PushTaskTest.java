package it.com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.task.*;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.services.MockObjectService;
import com.octopus.services.impl.MockObjectServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Integration tests for the deployment task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class PushTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushTaskTest.class);
    private static final MockObjectService MOCK_OBJECT_SERVICE = new MockObjectServiceImpl();
    private final TaskType octopusDeployTask;
    private Path workingDir;

    public PushTaskTest(@NotNull final TaskType octopusDeployTask) {
        this.octopusDeployTask = octopusDeployTask;
    }

    /**
     * Create a directory that we will use as the working directory. Copy some files
     * into it that we can work with.
     *
     * @throws IOException
     */
    @Before
    public void setupWorkingDir() throws IOException {
        workingDir = Files.createTempDirectory("bambooIntTest");
        FileUtils.copyURLToFile(getClass().getResource("/test.0.0.1.zip"),
                new File(workingDir.toFile().getAbsolutePath() + File.separator + "test.0.0.1.zip"));
    }

    /**
     * Clean up the working directory
     */
    @After
    public void cleanupWorkingDir() {
        FileUtils.deleteQuietly(workingDir.toFile());
    }

    @Test
    public void testPush() throws TaskException {
        Assert.assertNotNull(octopusDeployTask);

        final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext(workingDir.toFile());
        final TaskResult taskResult = octopusDeployTask.execute(taskContext);

        Assert.assertEquals(TaskState.SUCCESS, taskResult.getTaskState());
    }
}
