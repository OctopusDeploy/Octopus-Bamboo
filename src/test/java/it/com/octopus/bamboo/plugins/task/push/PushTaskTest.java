package it.com.octopus.bamboo.plugins.task.push;

import com.atlassian.bamboo.task.TaskException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.push.PushTask;
import com.octopus.constants.OctoConstants;
import com.octopus.constants.OctoTestConstants;
import com.octopus.services.impl.MockCommonTaskContext;
import com.octopus.services.impl.MockProcessService;
import com.octopus.services.impl.StubCapabilityContext;
import it.com.octopus.bamboo.plugins.task.BaseTest;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Integration tests for the deployment task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class PushTaskTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushTaskTest.class);
    private final PushTask octopusDeployTask;

    @Inject
    public PushTaskTest(@ComponentImport @NotNull final PushTask octopusDeployTask) {
        this.octopusDeployTask = octopusDeployTask;

        /*
            Replace some of the injected services with mocks/stubs
         */
        octopusDeployTask.setProcessService(processService);
        octopusDeployTask.setCapabilityContext(stubCapabilityContext);
    }



    /**
     * Ensures that the expected commands are passed to the mock
     * process service.
     * @throws TaskException this should not be thrown
     */
    @Test
    public void testCommands() throws TaskException {
        Assert.assertNotNull(octopusDeployTask);

        octopusDeployTask.execute(mockCommonTaskContext);

        Assert.assertEquals(stubCapabilityContext.getOctoPath(),
                processService.getCommands().get(0));

        Assert.assertEquals(OctoConstants.PUSH_COMMAND,
                processService.getCommands().get(1));

        Assert.assertTrue(processService.getCommands().contains("--server"));
        Assert.assertEquals(OctoConstants.LOCAL_OCTOPUS_INSTANCE,
                processService.getCommands().get(processService.getCommands().indexOf("--server") + 1));

        Assert.assertTrue(processService.getCommands().contains("--apiKey"));
        Assert.assertEquals(OctoTestConstants.DUMMY_API_KEY,
                processService.getCommands().get(processService.getCommands().indexOf("--apiKey") + 1));

        Assert.assertTrue(processService.getCommands().contains("--replace-existing"));
        Assert.assertTrue(processService.getCommands().contains("--debug"));

        int foundCount = 0;
        for (int i = 0; i < processService.getCommands().size(); ++i) {
            if (processService.getCommands().get(i).equals("--package")) {
                ++foundCount;
                final String packageName = processService.getCommands().get(i+1);
                LOGGER.info(packageName);
                Assert.assertTrue(
                        packageName.endsWith("first-bamboo-int.0.0.1.zip")
                        || packageName.endsWith("second-bamboo-int.0.0.1.zip"));
            }
        }

        Assert.assertEquals(2, foundCount);
    }
}
