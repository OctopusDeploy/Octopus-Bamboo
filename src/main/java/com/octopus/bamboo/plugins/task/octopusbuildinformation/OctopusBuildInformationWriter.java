package com.octopus.bamboo.plugins.task.octopusbuildinformation;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OctopusBuildInformationWriter {
    private BuildLogger buildLogger;
    private Boolean verboseLogging;

    public OctopusBuildInformationWriter(final BuildLogger buildLogger, final Boolean verboseLogging) {
        this.buildLogger = buildLogger;
        this.verboseLogging = verboseLogging;
    }

    public void writeToFile(final OctopusBuildInformation octopusBuildInformation, final String metaFile) throws IOException {
        try {
            final Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();

            if (verboseLogging) {
                buildLogger.addBuildLogEntry("Serializing Octopus build information");
            }

            final String jsonData = gson.toJson(octopusBuildInformation);
            if (verboseLogging) {
                buildLogger.addBuildLogEntry("Serialized Octopus build information - " + jsonData);
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(metaFile));
            bw.write(jsonData);
            bw.close();

            if (verboseLogging) {
                buildLogger.addBuildLogEntry("Wrote " + metaFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            buildLogger.addErrorLogEntry("Error writing the octopus.buildinformation file");
            throw e;
        }
    }
}
