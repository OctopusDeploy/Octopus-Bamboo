package com.octopus.services.impl;

import com.octopus.exception.ExecutionException;
import com.octopus.services.DotNetExeService;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * An implementation of the DotNetExeService
 */
public class DotNetExeServiceImpl implements DotNetExeService {
    private static final List<String> OCTO_EXECUTE_CMD = Arrays.asList(
            "/usr/bin/dotnet",
            "C:/Program Files/dotnet/dotnet.exe");

    public String getDotnetExe() {
        for (final String dotNetExec : OCTO_EXECUTE_CMD) {
            final File dotnetFile = new File(dotNetExec);
            if (dotnetFile.exists() && dotnetFile.isFile()) {
                return dotNetExec;
            }
        }

        throw new ExecutionException("Failed to find the dotnet executable");
    }
}
