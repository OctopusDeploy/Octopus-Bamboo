package com.octopus.bamboo.plugins.task.push;

import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.octopus.api.RestAPI;
import com.octopus.constants.OctoConstants;
import com.octopus.services.FeignService;
import com.octopus.services.FileService;
import com.octopus.services.impl.FeignServiceImpl;
import com.octopus.services.impl.FileServiceImpl;
import org.jetbrains.annotations.NotNull;
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

    private static final String OCTO_CLIENT_RESOURCE = "/octopus/OctopusTools.4.15.2.portable.tar.gz";
    private static final String OCTO_CLIENT_DEST = ".octopus/OctopusTools.4.15.2/core";
    private static final FeignService FEIGN_SERVICE = new FeignServiceImpl();
    private static final FileService FILE_SERVICE = new FileServiceImpl();

    /**
     * The service we use to execute the Octopus Deploy client.
     * <p>
     * See https://bitbucket.org/atlassian/atlassian-spring-scanner for
     * details on the @ComponentImport annotation.
     */
    @ComponentImport
    private ProcessService processService;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    public PushTask(@NotNull final ProcessService processService) {
        this.processService = processService;
    }

    @NotNull
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException {
        checkNotNull(taskContext, "taskContext cannot be null");
        checkState(processService != null, "processService cannot be null");

        /*
            Create the API client that we will use to call the Octopus REST API
         */
        final RestAPI restAPI = FEIGN_SERVICE.createClient(taskContext);

        /*
            Get the list of matching files that need to be uploaded
         */
        final String pattern = taskContext.getConfigurationMap().get(OctoConstants.PUSH_PATTERN);
        final List<File> files = FILE_SERVICE.getMatchingFile(taskContext.getWorkingDirectory(), pattern);

        /*
            Contact the API to upload the files
         */
        try {
            for (final File file : files) {
                restAPI.packagesRaw(false, file);
            }
        } catch (final Exception ex) {
            /*
                Any upload errors mean this task has failed.
             */
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
