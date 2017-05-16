package com.octopus.bamboo.plugins.task.push;

import com.amazonaws.util.IOUtils;
import com.atlassian.bamboo.task.*;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.services.FeignService;
import com.octopus.services.FileService;
import feign.Response;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private static final String OCTO_CLIENT_RESOURCE = "/octopus/OctopusTools.4.15.2.portable.tar.gz";
    private static final String OCTO_CLIENT_DEST = ".octopus/OctopusTools.4.15.2/core";
    @Autowired
    private FeignService feignService;
    @Autowired
    private FileService fileService;

    @NotNull
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");

        taskContext.getBuildLogger().addBuildLogEntry("Starting PushTask.execute()");

        /*
            Create the API client that we will use to call the Octopus REST API
         */
        final RestAPI restAPI = feignService.createClient(taskContext);

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
            taskContext.getBuildLogger().addErrorLogEntry("The pattern " + pattern
                    + " failed to match any files");
            return TaskResultBuilder.newBuilder(taskContext)
                    .failed()
                    .build();
        }

        /*
            Contact the API to upload the files
         */
        try {
            for (final File file : files) {
                final Response result = restAPI.packagesRaw(false, file);
                final String body = IOUtils.toString(result.body().asInputStream());
                taskContext.getBuildLogger().addBuildLogEntry(body);
            }
        } catch (final Exception ex) {
            /*
                Any upload errors mean this task has failed.
             */
            taskContext.getBuildLogger().addErrorLogEntry(ex.toString());
            return TaskResultBuilder.newBuilder(taskContext)
                    .failed()
                    .build();
        }

        /*
            We're all good, so let bamboo know that the task was a success.
         */
        return TaskResultBuilder.newBuilder(taskContext)
                .success()
                .build();
    }
}
