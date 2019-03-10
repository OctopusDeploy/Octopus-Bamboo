package com.octopus.bamboo.plugins.task.octopusmetadata;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OctopusMetadataWriter {
    private BuildLogger buildLogger;

    public OctopusMetadataWriter(BuildLogger buildLogger) {

        this.buildLogger = buildLogger;
    }

    public void writeToFile(final OctopusPackageMetadata octopusPackageMetadata, final String metaFile) throws IOException {
        try {
            final Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            buildLogger.addBuildLogEntry("Serializing Octopus metadata");

            final String jsonData = gson.toJson(octopusPackageMetadata);
            buildLogger.addBuildLogEntry("Serialized Octopus metadata - " + jsonData);

            BufferedWriter bw = new BufferedWriter(new FileWriter(metaFile));
            bw.write(jsonData);
            bw.close();
            buildLogger.addBuildLogEntry("Wrote " + metaFile);
        } catch (IOException e) {
            e.printStackTrace();
            buildLogger.addErrorLogEntry("Error writing the octopus.metadata file");
            throw e;
        }
    }
}
