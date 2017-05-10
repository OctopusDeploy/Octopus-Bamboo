package com.octopus.services;

import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Defines services that are used to work with bundled resources
 */
public interface ResourceService {
    /**
     * Extract a GZ file held in resources into the destination directory under
     * the current users profile (i.e. /home/jsmith/dest or C:\Users\jsmith\dest)
     *
     * @param resource The name of the GZipped file in the resources folder to extract
     * @param dest     The destination directory to extract the file to
     * @return The destination folder
     */
    File extractGZToHomeDir(@NotNull String resource, @NotNull String dest);

    /**
     * Extract a GZ file held in resources into the destination directory under
     * the current users profile (i.e. /home/jsmith/dest or C:\Users\jsmith\dest)
     *
     * @param resource The name of the GZipped file in the resources folder to extract
     * @param dest     The destination directory to extract the file to
     * @param skipIfExists set to true to skip the extraction if the destination folder exists already
     * @return The destination folder
     */
    File extractGZToHomeDir(@NotNull String resource, @NotNull String dest, boolean skipIfExists);
}
