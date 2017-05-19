package it.com.octopus.bamboo.plugins.task.createrelease;

import com.atlassian.bamboo.build.LogEntry;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskState;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.createrelease.CreateReleaseTask;
import com.octopus.constants.OctoTestConstants;
import com.octopus.services.MockObjectService;
import com.octopus.services.impl.MockObjectServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.octopus.constants.OctoConstants.TEST_PROFILE;
import static com.octopus.constants.OctoTestConstants.SPRING_PROFILE_SYSTEM_PROP;

/**
 * Integration tests for the deployment task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class CreateReleaseTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReleaseTaskTest.class);
    private static final MockObjectService MOCK_OBJECT_SERVICE = new MockObjectServiceImpl();
    private final CreateReleaseTask createReleaseTask;
    private final boolean usingTestProfile;

    @Inject
    public CreateReleaseTaskTest(@ComponentImport @NotNull final CreateReleaseTask createReleaseTask) {
        this.createReleaseTask = createReleaseTask;
        usingTestProfile = TEST_PROFILE.equals(System.getProperty(SPRING_PROFILE_SYSTEM_PROP));
    }

    @Test
    public void testCreateRelease() throws TaskException {
        Assert.assertNotNull(createReleaseTask);

        final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext();
        final TaskResult taskResult = createReleaseTask.execute(taskContext);

        /*
            If we are not using the mock rest server and no api key
            has been supplied, this will fail
        */
        final boolean shouldFail = !usingTestProfile
                && OctoTestConstants.DUMMY_API_KEY.equals(MOCK_OBJECT_SERVICE.getApiKey());

        Assert.assertEquals(shouldFail ? TaskState.FAILED : TaskState.SUCCESS, taskResult.getTaskState());
    }


    /**
     * Filter out the error logs to return those with the specific code
     *
     * @param logger  The logger
     * @param message The message to find
     * @return A list of the matching messages
     */
    private List<LogEntry> findErrorLogs(@NotNull final BuildLogger logger, @NotNull final String message) {
        final List<LogEntry> retValue = new ArrayList<>(logger.getErrorLog());

        CollectionUtils.filter(
                retValue,
                new Predicate() {
                    @Override
                    public boolean evaluate(Object o) {
                        return ((LogEntry) o).toString().contains(message);
                    }
                });

        return retValue;
    }
}
