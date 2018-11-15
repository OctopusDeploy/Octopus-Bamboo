package com.octopus.bamboo.plugins.task.pack;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.commit.CommitContext;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommentWorkItemHandler {

    public void processComments(final TaskContext taskContext, final String commentParser, final String basePath) throws Exception {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        final BuildContext buildContext = taskContext.getBuildContext();
        final BuildChanges buildChanges = buildContext.getBuildChanges();
        final List<CommitContext> commits = buildChanges.getChanges();
        final CommentParserFactory parserFactory = new CommentParserFactory();
        final List<WorkItem> workItems = new ArrayList<WorkItem>();

        buildLogger.addBuildLogEntry("Checking comments for linked work items");

        for (final CommitContext commit : commits) {
            final String comment = commit.getComment();

            buildLogger.addBuildLogEntry("Checking comment - " + comment);

            final List<WorkItem> items = parserFactory.getParser(commentParser).parse(comment, buildLogger);

            workItems.addAll(items);
        }

        if (workItems.size() > 0) {
            buildLogger.addBuildLogEntry("Found work items in comments, adding " + workItems.size() + " work items to octopus.meta");

            try {
                final String metaFile = Paths.get(taskContext.getRootDirectory().getPath(), basePath, "octopus.meta").toAbsolutePath().toString();
                buildLogger.addBuildLogEntry("Creating " + metaFile);

                final Gson gson = new GsonBuilder().setPrettyPrinting().create();
                buildLogger.addBuildLogEntry("Serializing work items");
                final OctopusMetadata octopusMetadata = new OctopusMetadata();
                octopusMetadata.WorkItems = workItems;
                final String jsonData = gson.toJson(octopusMetadata);
                buildLogger.addBuildLogEntry("Serialized Octopus metadata - " + jsonData);

                BufferedWriter bw = new BufferedWriter(new FileWriter(metaFile));
                bw.write(jsonData);
                bw.close();
                buildLogger.addBuildLogEntry("Wrote " + metaFile);
            } catch (IOException e) {
                e.printStackTrace();
                buildLogger.addErrorLogEntry("Error writing the octopus.meta file", e);
                throw e;
            }
        } else {
            buildLogger.addBuildLogEntry("No work items found in comments");
        }
    }
}