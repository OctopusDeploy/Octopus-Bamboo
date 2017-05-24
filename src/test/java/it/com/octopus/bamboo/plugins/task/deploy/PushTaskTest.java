package it.com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.task.TaskException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.push.PushTask;
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

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.octopus.constants.OctoConstants.TEST_PROFILE;
import static com.octopus.constants.OctoTestConstants.SPRING_PROFILE_SYSTEM_PROP;

/**
 * Integration tests for the deployment task
 */
@RunWith(AtlassianPluginsTestRunner.class)
public class PushTaskTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushTaskTest.class);
    private static final MockObjectService MOCK_OBJECT_SERVICE = new MockObjectServiceImpl();
    private final PushTask octopusDeployTask;
    private final boolean usingTestProfile;
    private Path workingDir;

    @Inject
    public PushTaskTest(@ComponentImport @NotNull final PushTask octopusDeployTask) {
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
    public void testInjection() throws TaskException {
        Assert.assertNotNull(octopusDeployTask);
    }
}
