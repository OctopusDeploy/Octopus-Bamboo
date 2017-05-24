package com.octopus.services.impl;

import com.octopus.services.CommonTaskService;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the CommonTaskServiceImpl service
 */
public class CommonTaskServiceImplTest {
    private static final CommonTaskService COMMON_TASK_SERVICE = new CommonTaskServiceImpl();

    @Test
    public void testSanitisation() {
        final String message = "Performed POST with API-ABCDEFGHIJKLMNOPQRSTUVWXY";
        final String sanitised = "Performed POST with API-....................UVWXY";

        Assert.assertEquals(sanitised, COMMON_TASK_SERVICE.sanitiseMessage(message));
    }
}
