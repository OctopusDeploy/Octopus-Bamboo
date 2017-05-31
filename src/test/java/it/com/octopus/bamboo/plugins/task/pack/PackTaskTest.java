package it.com.octopus.bamboo.plugins.task.pack;

import com.atlassian.bamboo.task.TaskException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.pack.PackTask;
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
 * Integration tests for the pack task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class PackTaskTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PackTaskTest.class);
    private final PackTask packTask;

    @Inject
    public PackTaskTest(@ComponentImport @NotNull final PackTask packTask) {
        this.packTask = packTask;

        /*
            Replace some of the injected services with mocks/stubs
         */
        packTask.setProcessService(processService);
        packTask.setCapabilityContext(stubCapabilityContext);
    }

    /**
     * Ensures that the expected commands are passed to the mock
     * process service.
     * @throws TaskException this should not be thrown
     */
    @Test
    public void testCommands() throws TaskException {
        Assert.assertNotNull(packTask);

        packTask.execute(mockCommonTaskContext);

        Assert.assertEquals(stubCapabilityContext.getOctoPath(),
                processService.getCommands().get(0));

        Assert.assertEquals(OctoConstants.PACK_COMMAND,
                processService.getCommands().get(1));

        Assert.assertTrue(processService.getCommands().contains("--id"));
        Assert.assertEquals(OctoTestConstants.PACK_ID,
                processService.getCommands().get(processService.getCommands().indexOf("--id") + 1));

        Assert.assertTrue(processService.getCommands().contains("--outFolder"));
        Assert.assertEquals(OctoTestConstants.PACK_OUTPATH,
                processService.getCommands().get(processService.getCommands().indexOf("--outFolder") + 1));

        Assert.assertTrue(processService.getCommands().contains("--basePath"));
        Assert.assertEquals(OctoTestConstants.PACK_BASEPATH,
                processService.getCommands().get(processService.getCommands().indexOf("--basePath") + 1));

        Assert.assertTrue(processService.getCommands().contains("--format"));
        Assert.assertEquals(OctoTestConstants.PACK_FORMAT,
                processService.getCommands().get(processService.getCommands().indexOf("--format") + 1));

        Assert.assertTrue(processService.getCommands().contains("--version"));
        Assert.assertEquals(OctoTestConstants.PACK_VERSION,
                processService.getCommands().get(processService.getCommands().indexOf("--version") + 1));

        Assert.assertTrue(processService.getCommands().contains("--overwrite"));
        Assert.assertTrue(processService.getCommands().contains("--verbose"));

        int foundCount = 0;
        for (int i = 0; i < processService.getCommands().size(); ++i) {
            if (processService.getCommands().get(i).equals("--include")) {
                ++foundCount;
                final String packageName = processService.getCommands().get(i+1);
                LOGGER.info(packageName);
                Assert.assertTrue(
                        packageName.endsWith(OctoTestConstants.PACK_INCLUDES1)
                        || packageName.endsWith(OctoTestConstants.PACK_INCLUDES2));
            }
        }

        Assert.assertEquals(2, foundCount);
    }
}
