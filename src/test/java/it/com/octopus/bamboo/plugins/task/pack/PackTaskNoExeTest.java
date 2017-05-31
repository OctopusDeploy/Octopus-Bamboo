package it.com.octopus.bamboo.plugins.task.pack;

import com.atlassian.bamboo.task.TaskException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.pack.PackTask;
import com.octopus.services.impl.MockCommonTaskContext;
import com.octopus.services.impl.RecordingBuildLogger;
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
public class PackTaskNoExeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PackTaskNoExeTest.class);
    private final MockCommonTaskContext mockCommonTaskContext = new MockCommonTaskContext();
    private final PackTask packTask;

    @Inject
    public PackTaskNoExeTest(@ComponentImport @NotNull final PackTask packTask) {
        this.packTask = packTask;
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
                .findErrorLogs("OCTOPUS-BAMBOO-INPUT-ERROR-0003").isEmpty());
    }
}
