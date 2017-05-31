package it.com.octopus.bamboo.plugins.task;

import com.octopus.services.impl.MockCommonTaskContext;
import com.octopus.services.impl.MockProcessService;
import com.octopus.services.impl.StubCapabilityContext;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configures and cleans up a stub working environment
 */
public class BaseTest {
    protected final MockProcessService processService = new MockProcessService();
    protected final MockCommonTaskContext mockCommonTaskContext = new MockCommonTaskContext();
    protected final StubCapabilityContext stubCapabilityContext = new StubCapabilityContext();
    protected Path workingDir;

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
        FileUtils.copyURLToFile(getClass().getResource("/Octo"),
                new File(workingDir.toFile().getAbsolutePath() + File.separator + "Octo"));

        mockCommonTaskContext.setWorkingDir(workingDir.toFile());
        stubCapabilityContext.setOctoPath(new File(workingDir.toFile(), "Octo").getAbsolutePath());
    }

    /**
     * Clean up the working directory
     */
    @After
    public void cleanupWorkingDir() {
        FileUtils.deleteQuietly(workingDir.toFile());
    }
}
