package it.com.octopus.bamboo.plugins.task.pack;

import com.atlassian.bamboo.task.TaskException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.pack.PackTask;
import com.octopus.constants.OctoConstants;
import com.octopus.constants.OctoTestConstants;
import com.octopus.services.impl.MockCommonTaskContext;
import com.octopus.services.impl.RecordingBuildLogger;
import com.octopus.services.impl.StubCapabilityContext;
import it.com.octopus.bamboo.plugins.task.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Integration tests for the pack task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class NoExeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoExeTest.class);
    private final MockCommonTaskContext mockCommonTaskContext = new MockCommonTaskContext();
    private final StubCapabilityContext stubCapabilityContext = new StubCapabilityContext();
    private final PackTask packTask;

    @Inject
    public NoExeTest(@ComponentImport @NotNull final PackTask packTask) {
        this.packTask = packTask;

        stubCapabilityContext.setOctoPath("/this/does/not/exist");
        packTask.setCapabilityContext(stubCapabilityContext);
    }

    /**
     * Ensures that a missing octo executable is detected
     * @throws TaskException this should not be thrown
     */
    @Test
    public void testCommands() throws TaskException {
        Assert.assertNotNull(packTask);

        packTask.execute(mockCommonTaskContext);

        Assert.assertFalse(((RecordingBuildLogger)mockCommonTaskContext.getBuildLogger())
                .findLogs("OCTOPUS-BAMBOO-INPUT-ERROR-0003").isEmpty());
    }
}
