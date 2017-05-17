package com.octopus.bamboo.plugins.task.push;

import com.amazonaws.util.IOUtils;
import com.atlassian.bamboo.task.*;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.services.FeignService;
import com.octopus.services.FileService;
import feign.Response;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * The Bamboo Task that is used to deploy artifacts to Octopus Deploy
 */
@Component
@ExportAsService({TaskType.class})
@Named("pushTask")
public class PushTask implements TaskType {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushTask.class);
    private static final int START_HTTP_OK_RANGE = 200;
    private static final int END_HTTP_OK_RANGE = 299;
    private final FeignService feignService;
    private final FileService fileService;

    /**
     * Constructor. Params are injected by Spring under normal usage.
     *
     * @param feignService The service that is used to create feign clients
     * @param fileService  The service that is used to find files to upload
     */
    @Inject
    public PushTask(@NotNull final FeignService feignService, @NotNull final FileService fileService) {
        this.feignService = feignService;
        this.fileService = fileService;
    }

    /**
     * Log an info message
     * @param taskContext The Bamboo task context
     * @param message The message to be logged
     */
    private void logInfo(@NotNull final TaskContext taskContext, @NotNull final String message) {
        LOGGER.info(message);
        taskContext.getBuildLogger().addBuildLogEntry(message);
    }

    /**
     * Log an error message
     * @param taskContext The Bamboo task context
     * @param message The message to be logged
     */
    private void logError(@NotNull final TaskContext taskContext, @NotNull final String message) {
        LOGGER.error(message);
        taskContext.getBuildLogger().addErrorLogEntry(message);
    }

    private TaskResult buildResult(@NotNull final TaskContext taskContext, final boolean success) {
        final TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext);
        if (success) {
            builder.success();
        } else {
            builder.failed();
        }

        return builder.build();
    }

    @NotNull
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

        /*
            Get the value for forcing the upload
         */
        final Boolean forceUpload = BooleanUtils.toBoolean(
                taskContext.getConfigurationMap().get(OctoConstants.FORCE));

        /*
            Create the API client that we will use to call the Octopus REST API.
            We allow any request that is forced to be retried, as this request
            is essentially idempotent.
         */
        final RestAPI restAPI = feignService.createClient(taskContext, forceUpload);

        /*
            Get the list of matching files that need to be uploaded
         */
        final String pattern = taskContext.getConfigurationMap().get(OctoConstants.PUSH_PATTERN);
        checkState(StringUtils.isNotBlank(pattern),
                "The pushPattern configuration value was not set");
        final List<File> files = fileService.getMatchingFile(taskContext.getWorkingDirectory(), pattern);

        /*
            Fail if no files were matched
         */
        if (files.isEmpty()) {
            logError(taskContext, "OCTOPUS-BAMBOO-ERROR-0003: The pattern " + pattern
                    + " failed to match any files");
            return buildResult(taskContext, false);
        }

        /*
            Contact the API to upload the files
         */
        try {
            for (final File file : files) {
                final Response result = restAPI.packagesRaw(forceUpload, file);

                /*
                    Mocked responses have no body
                 */
                if (result.body() != null) {
                    final String body = IOUtils.toString(result.body().asInputStream());
                    taskContext.getBuildLogger().addBuildLogEntry(body);
                }

                /*
                    Make sure the response code indicates success
                 */
                if (result.status() < START_HTTP_OK_RANGE || result.status() > END_HTTP_OK_RANGE) {
                    if (result.status() == HttpStatus.SC_UNAUTHORIZED) {
                        logError(taskContext, "OCTOPUS-BAMBOO-ERROR-0001: Status code "
                                + result.status()
                                + " indicates an authorization error! Make sure the API key is correct.");
                    } else if (result.status() == HttpStatus.SC_CONFLICT) {
                        logError(taskContext, "OCTOPUS-BAMBOO-ERROR-0005: Status code "
                                + result.status()
                                + " most likely means you are trying to push a file that already exists, "
                                + " and you have not enabled the \"Force the package upload\" option!");
                    } else {
                        logError(taskContext, "OCTOPUS-BAMBOO-ERROR-0002: Status code "
                                + result.status()
                                + " indicates an error!");
                    }

                    return buildResult(taskContext, false);
                }
            }
        } catch (final Exception ex) {
            /*
                Any upload errors mean this task has failed.
             */
            logError(taskContext, ex.toString());
            return buildResult(taskContext, false);
        }

        /*
            We're all good, so let bamboo know that the task was a success.
         */
        return buildResult(taskContext, true);
    }
}
