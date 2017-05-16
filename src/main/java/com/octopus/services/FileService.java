package com.octopus.services;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * Defines a service that is used to work with files and dirs
 */
public interface FileService {
    /**
     * @param workingDir The directory from which to start matching files
     * @param pattern    The ant pattern used to match files
     * @return A list of matching files
     */
    @NotNull
    public List<File> getMatchingFile(@NotNull final File workingDir, @NotNull final String pattern);
}
