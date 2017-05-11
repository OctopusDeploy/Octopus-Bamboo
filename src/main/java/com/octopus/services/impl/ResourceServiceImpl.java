package com.octopus.services.impl;

import com.octopus.exception.ResourceException;
import com.octopus.services.ResourceService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLogger;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Implementation of the GZ extractor using plexus archiver
 */
public class ResourceServiceImpl implements ResourceService {
    private static final Logger LOGGER = Logger.getLogger(ResourceServiceImpl.class.toString());

    public File extractGZToHomeDir(@NotNull final String resource, @NotNull final String dest) {
        checkArgument(StringUtils.isNotBlank(resource));
        checkArgument(StringUtils.isNotBlank(dest));

        return extractGZToHomeDir(resource, dest, false);
    }

    public File extractGZToHomeDir(@NotNull final String resource, @NotNull final String dest, final boolean skipIfExists) {
        checkArgument(StringUtils.isNotBlank(resource));
        checkArgument(StringUtils.isNotBlank(dest));

        try {
            final File tempArchive = File.createTempFile("OctopusTools", ".portable.tar.gz");
            final File destination = new File(System.getProperty("user.home") + File.separator + dest);

            if (destination.exists() && skipIfExists) {
                LOGGER.log(Level.INFO, "Skipping extraction because destination already exists");
                return destination;
            }

            destination.mkdirs();

            final URL client = getClass().getResource(resource);
            FileUtils.copyURLToFile(client, tempArchive);

            final TarGZipUnArchiver ua = new TarGZipUnArchiver();
            ua.enableLogging(new ConsoleLogger());
            ua.setSourceFile(tempArchive);
            ua.setDestDirectory(destination);
            ua.extract();

            FileUtils.deleteQuietly(tempArchive);

            return destination;
        } catch (final IOException ex) {
            throw new ResourceException(ex);
        }
    }
}
