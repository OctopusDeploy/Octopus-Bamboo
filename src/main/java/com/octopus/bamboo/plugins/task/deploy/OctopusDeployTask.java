package com.octopus.bamboo.plugins.task.deploy;

import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.utils.process.ExternalProcess;
import com.octopus.services.impl.ResourceServiceImpl;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Bamboo Task that is used to deploy artifacts to Octopus Deploy
 */
@ExportAsService
public class OctopusDeployTask implements TaskType {

    private static final String OCTO_CLIENT_RESOURCE = "/octopus/OctopusTools.4.15.2.portable.tar.gz";
    private static final String OCTO_CLIENT_DEST = ".octopus/OctopusTools.4.15.2/core";
    private static final String OCTO_EXECUTE_CMD = "dotnet Octo.dll";
    private static final int EXPECTED_RETURN_CODE = 0;
    private static final ResourceServiceImpl RESOURCE_SERVICE = new ResourceServiceImpl();

    @ComponentImport
    private ProcessService processService;

    @Inject
    public OctopusDeployTask(final ProcessService processService) {
        this.processService = processService;
    }

    @NotNull
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException {
        checkNotNull(taskContext);

        final File octoToolsDir = RESOURCE_SERVICE.extractGZToHomeDir(OCTO_CLIENT_RESOURCE, OCTO_CLIENT_DEST, true);

        final ExternalProcess process = processService.createProcess(taskContext,
                new ExternalProcessBuilder()
                        .command(Arrays.asList(OCTO_EXECUTE_CMD))
                        .workingDirectory(octoToolsDir));

        return TaskResultBuilder.newBuilder(taskContext)
                .checkReturnCode(process, EXPECTED_RETURN_CODE)
                .build();
    }


    public ProcessService getProcessService() {
        return processService;
    }

    public void setProcessService(@NotNull final ProcessService processService) {
        checkNotNull(processService);
        this.processService = processService;
    }
}
