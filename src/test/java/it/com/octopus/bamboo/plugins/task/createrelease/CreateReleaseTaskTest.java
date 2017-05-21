package it.com.octopus.bamboo.plugins.task.createrelease;

import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskState;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.createrelease.CreateReleaseTask;
import com.octopus.constants.OctoConstants;
import com.octopus.constants.OctoTestConstants;
import com.octopus.services.MockObjectService;
import com.octopus.services.impl.MockObjectServiceImpl;
import com.octopus.services.impl.RecordingBuildLogger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.octopus.constants.OctoConstants.TEST_PROFILE;
import static com.octopus.constants.OctoTestConstants.SPRING_PROFILE_SYSTEM_PROP;

/**
 * Integration tests for the deployment task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class CreateReleaseTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateReleaseTaskTest.class);
    private static final MockObjectService MOCK_OBJECT_SERVICE = new MockObjectServiceImpl();
    /**
     * Create a semver from a date and time
     */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.ddHHmmss");
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

        final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext(
                new File("."),
                true,
                "**/*.zip",
                MOCK_OBJECT_SERVICE.getApiKey(),
                OctoConstants.LOCAL_OCTOPUS_INSTANCE,
                SIMPLE_DATE_FORMAT.format(new Date())
        );
        final TaskResult taskResult = createReleaseTask.execute(taskContext);

        /*
            If we are not using the mock rest server and no api key
            has been supplied, this will fail
        */
        final boolean shouldFail = !usingTestProfile
                && OctoTestConstants.DUMMY_API_KEY.equals(MOCK_OBJECT_SERVICE.getApiKey());

        Assert.assertEquals(shouldFail ? TaskState.FAILED : TaskState.SUCCESS, taskResult.getTaskState());
    }

    @Test
    public void testBadSemver() throws TaskException {
        Assert.assertNotNull(createReleaseTask);

        /*
            If we are not using the mock rest server and no api key
            has been supplied, this will fail
        */
        final boolean shouldFail = !usingTestProfile
                && OctoTestConstants.DUMMY_API_KEY.equals(MOCK_OBJECT_SERVICE.getApiKey());

        if (shouldFail) {
            final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext(
                    new File("."),
                    true,
                    "**/*.zip",
                    MOCK_OBJECT_SERVICE.getApiKey(),
                    OctoConstants.LOCAL_OCTOPUS_INSTANCE,
                    "notsemver"
            );
            final TaskResult taskResult = createReleaseTask.execute(taskContext);

            Assert.assertEquals(TaskState.FAILED, taskResult.getTaskState());

            /*
                Make sure the appropriate error was displayed
            */
            final int errorCount = ((RecordingBuildLogger) taskContext.getBuildLogger())
                    .findErrorLogs("OCTOPUS-BAMBOO-INPUT-ERROR-0004").size();

            Assert.assertEquals(1, errorCount);
        }
    }
}
