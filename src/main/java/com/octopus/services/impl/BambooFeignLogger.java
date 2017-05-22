package com.octopus.services.impl;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.octopus.services.CommonTaskService;
import feign.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An implementation of the Feign logger that passes messages to the Bamboo
 * logger service.
 */
public class BambooFeignLogger extends Logger {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BambooFeignLogger.class);
    private static final CommonTaskService COMMON_TASK_SERVICE = new CommonTaskServiceImpl();
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

        final String sanitisedConfigKey = COMMON_TASK_SERVICE.sanitiseMessage(configKey);
        final String entry = String.format(methodTag(sanitisedConfigKey) + format, args);
        /*
            This is the message that the user sees in the build log
         */
        buildLogger.addBuildLogEntry(entry);
        /*
            This is so integration tests can also see the output
         */
        LOGGER.info(entry);
    }
}
