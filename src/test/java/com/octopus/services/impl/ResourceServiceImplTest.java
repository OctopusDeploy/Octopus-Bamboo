package com.octopus.services.impl;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Tests for the ResourceServiceImpl class
 */
public class ResourceServiceImplTest {
    private static final ResourceServiceImpl RESOURCE_SERVICE = new ResourceServiceImpl();

    /**
     * Make sure we can extract the files
     */
    @Test
    public void testExtraction1() {
        File outputDir = null;
        try {
            outputDir = RESOURCE_SERVICE.extractGZToHomeDir(
                    "/octopus/OctopusTools.4.15.2.portable.tar.gz",
                    ".octopus/octoclient/core");
            final File[] children = outputDir.listFiles();
            for (final File child : children) {
                if ("Octo".equals(child.getName())) {
                    return;
                }
            }
            Assert.fail();
        } finally {
            FileUtils.deleteQuietly(outputDir);
        }
    }

    /**
     * Make sure we can attempt to extract files twice
     */
    @Test
    public void testExtraction2() {
        File outputDir = null;
        try {
            outputDir = RESOURCE_SERVICE.extractGZToHomeDir(
                    "/octopus/OctopusTools.4.15.2.portable.tar.gz",
                    ".octopus/octoclient/core");
            final File secondAttempt = RESOURCE_SERVICE.extractGZToHomeDir(
                    "/octopus/OctopusTools.4.15.2.portable.tar.gz",
                    ".octopus/octoclient/core",
                    true);

            Assert.assertEquals(outputDir.getAbsolutePath(), secondAttempt.getAbsolutePath());
        } finally {
            FileUtils.deleteQuietly(outputDir);
        }
    }
}
