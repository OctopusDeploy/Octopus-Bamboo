package com.octopus.services.impl;

import com.octopus.exception.ResourceException;
import com.octopus.services.FileService;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An implementation of the file service using Spring utils
 */
@Component
public class FileServiceImpl implements FileService {
    @NotNull
    @Override
    public List<File> getMatchingFile(@NotNull final File workingDir, @NotNull final String pattern) {
        checkNotNull(workingDir);
        checkArgument(StringUtils.isNotBlank(pattern));

        try {
            final List<File> retValue = new ArrayList<File>();

            final String workingDirUri = workingDir.toURI().toString();
            final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            final Resource[] resources = resolver.getResources(workingDirUri + "/" + pattern);

            for (final Resource resource : resources) {
                final File resourceFile = resource.getFile();
                if (resourceFile.isFile()) {
                    retValue.add(resourceFile);
                }
            }

            return retValue;
        } catch (final IOException ex) {
            throw new ResourceException(ex);
        }
    }
}
