package com.octopus.bamboo.plugins.task.octopusmetadata;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OctopusMetadataWriter {
    private BuildLogger buildLogger;
    private Boolean verboseLogging;

    public OctopusMetadataWriter(final BuildLogger buildLogger, Boolean verboseLogging) {
        this.buildLogger = buildLogger;
        this.verboseLogging = verboseLogging;
    }

    public void writeToFile(final OctopusPackageMetadata octopusPackageMetadata, final String metaFile) throws IOException {
        try {
            final Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();

            if (verboseLogging) {
                buildLogger.addBuildLogEntry("Serializing Octopus metadata");
            }

            final String jsonData = gson.toJson(octopusPackageMetadata);
            if (verboseLogging) {
                buildLogger.addBuildLogEntry("Serialized Octopus metadata - " + jsonData);
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(metaFile));
            bw.write(jsonData);
            bw.close();

            if (verboseLogging) {
                buildLogger.addBuildLogEntry("Wrote " + metaFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            buildLogger.addErrorLogEntry("Error writing the octopus.metadata file");
            throw e;
        }
    }
}
