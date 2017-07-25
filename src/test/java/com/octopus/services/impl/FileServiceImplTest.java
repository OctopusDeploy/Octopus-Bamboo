package com.octopus.services.impl;

import com.octopus.services.FileService;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Tests of the FileServiceImpl class
 */
public class FileServiceImplTest {
    private static final FileService FILE_SERVICE = new FileServiceImpl();

    @Test
    public void testMatching() {
        final List<File> files =
                FILE_SERVICE.getMatchingFile(new File("."), "**/FileServiceImplTest.java");
        Assert.assertFalse(files.isEmpty());
    }

    @Test
    public void testAbsolutePathMatching() {
        final String path = new File(this.getClass().getResource("/first-bamboo-int.0.0.1.zip")
                .getFile()).getAbsolutePath();
        final List<File> files =
                FILE_SERVICE.getMatchingFile(new File("."), path);
        Assert.assertFalse(files.isEmpty());
    }
}
