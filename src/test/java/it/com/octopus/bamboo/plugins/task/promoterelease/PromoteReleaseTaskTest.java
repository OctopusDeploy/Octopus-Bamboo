package it.com.octopus.bamboo.plugins.task.promoterelease;

import com.atlassian.bamboo.task.TaskException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.deployrelease.DeployReleaseTask;
import com.octopus.bamboo.plugins.task.promoterelease.PromoteReleaseTask;
import com.octopus.constants.OctoConstants;
import com.octopus.constants.OctoTestConstants;
import it.com.octopus.bamboo.plugins.task.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Integration tests for the pack task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class PromoteReleaseTaskTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PromoteReleaseTaskTest.class);
    private final PromoteReleaseTask promoteReleaseTask;

    @Inject
    public PromoteReleaseTaskTest(@ComponentImport @NotNull final PromoteReleaseTask promoteReleaseTask) {
        this.promoteReleaseTask = promoteReleaseTask;

        /*
            Replace some of the injected services with mocks/stubs
         */
        this.promoteReleaseTask.setProcessService(processService);
        this.promoteReleaseTask.setCapabilityContext(stubCapabilityContext);
    }

    /**
     * Ensures that the expected commands are passed to the mock
     * process service.
     * @throws TaskException this should not be thrown
     */
    @Test
    public void testCommands() throws TaskException {
        Assert.assertNotNull(promoteReleaseTask);

        promoteReleaseTask.execute(mockCommonTaskContext);

        Assert.assertEquals(stubCapabilityContext.getOctoPath(),
                processService.getCommands().get(0));

        Assert.assertEquals(OctoConstants.PROMOTE_RELEASE_COMMAND,
                processService.getCommands().get(1));

        Assert.assertTrue(processService.getCommands().contains("--server"));
        Assert.assertEquals(OctoConstants.LOCAL_OCTOPUS_INSTANCE,
                processService.getCommands().get(processService.getCommands().indexOf("--server") + 1));

        Assert.assertTrue(processService.getCommands().contains("--apiKey"));
        Assert.assertEquals(OctoTestConstants.DUMMY_API_KEY,
                processService.getCommands().get(processService.getCommands().indexOf("--apiKey") + 1));


        Assert.assertTrue(processService.getCommands().contains("--project"));
        Assert.assertEquals(OctoTestConstants.TEST_PROJECT,
                processService.getCommands().get(processService.getCommands().indexOf("--project") + 1));

        Assert.assertTrue(processService.getCommands().contains("--from"));
        Assert.assertEquals(OctoTestConstants.FROM_ENV,
                processService.getCommands().get(processService.getCommands().indexOf("--from") + 1));

        Assert.assertTrue(processService.getCommands().contains("--debug"));
        Assert.assertTrue(processService.getCommands().contains("--progress"));

        int toCount = 0;
        for (int i = 0; i < processService.getCommands().size(); ++i) {
            if (processService.getCommands().get(i).equals("--to")) {
                ++toCount;
                final String packageName = processService.getCommands().get(i+1);
                LOGGER.info(packageName);
                Assert.assertTrue(
                        packageName.endsWith(OctoTestConstants.TO_ENV1)
                                || packageName.endsWith(OctoTestConstants.TO_ENV2));
            }
        }

        Assert.assertEquals(2, toCount);

        int tenantCount = 0;
        for (int i = 0; i < processService.getCommands().size(); ++i) {
            if (processService.getCommands().get(i).equals("--tenant")) {
                ++tenantCount;
                final String packageName = processService.getCommands().get(i+1);
                LOGGER.info(packageName);
                Assert.assertTrue(
                        packageName.endsWith(OctoTestConstants.TEST_TENANT1)
                                || packageName.endsWith(OctoTestConstants.TEST_TENANT2));
            }
        }

        Assert.assertEquals(2, tenantCount);

        int tenantTagCount = 0;
        for (int i = 0; i < processService.getCommands().size(); ++i) {
            if (processService.getCommands().get(i).equals("--tenanttag")) {
                ++tenantTagCount;
                final String packageName = processService.getCommands().get(i+1);
                LOGGER.info(packageName);
                Assert.assertTrue(
                        packageName.endsWith(OctoTestConstants.TEST_TENANT_TAG1)
                                || packageName.endsWith(OctoTestConstants.TEST_TENANT_TAG2));
            }
        }

        Assert.assertEquals(2, tenantTagCount);
    }
}
