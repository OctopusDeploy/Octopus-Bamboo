package it.com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.build.LogEntry;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.task.*;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.constants.OctoTestConstants;
import com.octopus.services.MockObjectService;
import com.octopus.services.impl.MockObjectServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
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
import java.util.ArrayList;
import java.util.List;

import static com.octopus.constants.OctoConstants.TEST_PROFILE;
import static com.octopus.constants.OctoTestConstants.SPRING_PROFILE_SYSTEM_PROP;

/**
 * Integration tests for the deployment task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class PushTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushTaskTest.class);
    private static final MockObjectService MOCK_OBJECT_SERVICE = new MockObjectServiceImpl();
    private final TaskType octopusDeployTask;
    private final boolean usingTestProfile;
    private Path workingDir;

    @Inject
    public PushTaskTest(@ComponentImport("pushTask") @NotNull final TaskType octopusDeployTask) {
        this.octopusDeployTask = octopusDeployTask;
        usingTestProfile = TEST_PROFILE.equals(System.getProperty(SPRING_PROFILE_SYSTEM_PROP));
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
        FileUtils.copyURLToFile(getClass().getResource("/first-bamboo-int.0.0.1.zip"),
                new File(workingDir.toFile().getAbsolutePath() + File.separator + "first-bamboo-int.0.0.1.zip"));
        FileUtils.copyURLToFile(getClass().getResource("/first-bamboo-int.0.0.1.zip"),
                new File(workingDir.toFile().getAbsolutePath() + File.separator + "second-bamboo-int.0.0.1.zip"));
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

        final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext(
                workingDir.toFile(),
                true,
                "**/first-bamboo-int.0.0.1.zip");
        final TaskResult taskResult = octopusDeployTask.execute(taskContext);

        /*
            If we are not using the mock rest server and no api key
            has been supplied, this will fail
        */
        final boolean shouldFail = !usingTestProfile
                && OctoTestConstants.DUMMY_API_KEY.equals(MOCK_OBJECT_SERVICE.getApiKey());

        Assert.assertEquals(shouldFail ? TaskState.FAILED : TaskState.SUCCESS, taskResult.getTaskState());
    }

    @Test
    public void testPushMultiple() throws TaskException {
        Assert.assertNotNull(octopusDeployTask);

        final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext(
                workingDir.toFile(),
                true,
                "**/*-bamboo-int.0.0.1.zip");
        final TaskResult taskResult = octopusDeployTask.execute(taskContext);

        /*
            If we are not using the mock rest server and no api key
            has been supplied, this will fail
        */
        final boolean shouldFail = !usingTestProfile
                && OctoTestConstants.DUMMY_API_KEY.equals(MOCK_OBJECT_SERVICE.getApiKey());

        Assert.assertEquals(shouldFail ? TaskState.FAILED : TaskState.SUCCESS, taskResult.getTaskState());
    }

    @Test
    public void testFailedPush() throws TaskException {
        Assert.assertNotNull(octopusDeployTask);

        /*
            This test is only valid if we are not using the mock server and
            if a valid api key has been supplied
        */
        final boolean shouldFail = !usingTestProfile
                && !OctoTestConstants.DUMMY_API_KEY.equals(MOCK_OBJECT_SERVICE.getApiKey());

        if (shouldFail) {
            final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext(
                    workingDir.toFile(),
                    true,
                    "**/this.does.not.match.anything.zip");
            final TaskResult taskResult = octopusDeployTask.execute(taskContext);

            Assert.assertEquals(TaskState.FAILED, taskResult.getTaskState());

            Assert.assertFalse(taskContext.getBuildLogger().getErrorLog().isEmpty());

            /*
                Make sure the appropriate error was displayed
            */
            final int errorCount = findErrorLogs(taskContext.getBuildLogger(),
                    "OCTOPUS-BAMBOO-ERROR-0003").size();

            Assert.assertEquals(1, errorCount);
        }
    }

    @Test
    public void testUnforcedPush() throws TaskException {
        Assert.assertNotNull(octopusDeployTask);

        /*
            This test is only valid if we are not using the mock server and
            if a valid api key has been supplied
        */
        final boolean shouldFail = !usingTestProfile
                && !OctoTestConstants.DUMMY_API_KEY.equals(MOCK_OBJECT_SERVICE.getApiKey());

        if (shouldFail) {
            /*
                Do the initial push, forced because we need to ensure that this
                file exists
            */
            final TaskContext taskContextForce = MOCK_OBJECT_SERVICE.getTaskContext(
                    workingDir.toFile(),
                    true,
                    "**/first-bamboo-int.0.0.1.zip");
            octopusDeployTask.execute(taskContextForce);

            /*
                Try again with an unforced push. This must fail
            */
            final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext(
                    workingDir.toFile(),
                    false,
                    "**/first-bamboo-int.0.0.1.zip");
            final TaskResult taskResult = octopusDeployTask.execute(taskContext);

            Assert.assertEquals(TaskState.FAILED, taskResult.getTaskState());

            Assert.assertFalse(taskContext.getBuildLogger().getErrorLog().isEmpty());

            /*
                Make sure the appropriate error was displayed
             */
            final int errorCount = findErrorLogs(taskContext.getBuildLogger(),
                    "OCTOPUS-BAMBOO-ERROR-0005").size();

            Assert.assertEquals(1, errorCount);
        }
    }

    @Test
    public void testBadAPIKey() throws TaskException {
        Assert.assertNotNull(octopusDeployTask);

        /*
            This test is only valid if we are not using the mock server and
            if a valid api key has been supplied
        */
        final boolean shouldFail = !usingTestProfile
                && !OctoTestConstants.DUMMY_API_KEY.equals(MOCK_OBJECT_SERVICE.getApiKey());

        if (shouldFail) {
            /*
                Everything was supplied that we expect to find for a successful
                API call. So now we send a dummy api key to test the auth failure.
             */
            final TaskContext taskContext = MOCK_OBJECT_SERVICE.getTaskContext(
                    workingDir.toFile(),
                    true,
                    "**/first-bamboo-int.0.0.1.zip",
                    OctoTestConstants.DUMMY_API_KEY);
            final TaskResult taskResult = octopusDeployTask.execute(taskContext);

            Assert.assertEquals(TaskState.FAILED, taskResult.getTaskState());

            Assert.assertFalse(taskContext.getBuildLogger().getErrorLog().isEmpty());

            /*
                Make sure the appropriate error was displayed
            */
            final int errorCount = findErrorLogs(taskContext.getBuildLogger(),
                    "OCTOPUS-BAMBOO-ERROR-0001").size();

            Assert.assertEquals(1, errorCount);
        }
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
