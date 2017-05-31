package it.com.octopus.bamboo.plugins.task.push;

import com.atlassian.bamboo.task.TaskException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.push.PushTask;
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
public class PushTaskNoMatchingFilesTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushTaskNoMatchingFilesTest.class);
    private final MockCommonTaskContext mockCommonTaskContext = new MockCommonTaskContext();
    private final PushTask task;

    @Inject
    public PushTaskNoMatchingFilesTest(@ComponentImport @NotNull final PushTask task) {
        this.task = task;
    }

    /**
     * Ensures that a missing octo executable is detected
     * @throws TaskException this should not be thrown
     */
    @Test
    public void testCommands() throws TaskException {
        Assert.assertNotNull(task);

        task.execute(mockCommonTaskContext);

        Assert.assertFalse(((RecordingBuildLogger)mockCommonTaskContext.getBuildLogger())
                .findErrorLogs("OCTOPUS-BAMBOO-INPUT-ERROR-0001").isEmpty());
    }
}
