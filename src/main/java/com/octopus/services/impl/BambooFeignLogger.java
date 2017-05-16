package com.octopus.services.impl;

import com.atlassian.bamboo.build.logger.BuildLogger;
import feign.Logger;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An implementation of the Feign logger that passes messages to the Bamboo
 * logger service.
 */
public class BambooFeignLogger extends Logger {
    private final BuildLogger buildLogger;

    public BambooFeignLogger(@NotNull final BuildLogger buildLogger) {
        checkNotNull(buildLogger);

        this.buildLogger = buildLogger;
    }

    @Override
    protected void log(final String configKey, final String format, final Object... args) {
        checkNotNull(configKey);
        checkNotNull(format);
        checkNotNull(args);

        buildLogger.addBuildLogEntry(String.format(methodTag(configKey) + format, args));
    }
}
