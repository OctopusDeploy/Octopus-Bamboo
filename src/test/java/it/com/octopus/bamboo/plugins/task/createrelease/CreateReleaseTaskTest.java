package it.com.octopus.bamboo.plugins.task.createrelease;

import com.atlassian.bamboo.task.TaskException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.octopus.bamboo.plugins.task.createrelease.CreateReleaseTask;
import com.octopus.services.MockObjectService;
import com.octopus.services.impl.MockObjectServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;

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
    public void testInjection() throws TaskException {
        Assert.assertNotNull(createReleaseTask);
    }
}
