package com.octopus.bamboo.plugins.task.octopusmetadata;

import com.atlassian.bamboo.build.logger.BuildLogger;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OctopusMetadataBuilder {

    private BuildLogger buildLogger;

    public OctopusMetadataBuilder(BuildLogger buildLogger) {
        this.buildLogger = buildLogger;
    }

    public OctopusPackageMetadata build(
            final String vcsRoot,
            final String vcsCommitNumber,
            final String comments,
            final String commentParser,
            final String serverUrl,
            final String buildId,
            final String buildNumber) throws Exception {

        final OctopusPackageMetadata metadata = new OctopusPackageMetadata();

        if (StringUtils.isNotBlank(commentParser)) {
            final CommentParserFactory parserFactory = new CommentParserFactory();
            final CommentParser parser = parserFactory.getParser(commentParser);

            metadata.IssueTrackerId = "issuetracker-" + parser.getIssueTrackerSuffix();

            final List<WorkItem> workItems = new ArrayList<WorkItem>();

            if (comments != null && !comments.isEmpty()) {
                final List<WorkItem> items = parser.parse(comments, buildLogger);
                workItems.addAll(items);
            }

            if (workItems.size() > 0) {
                buildLogger.addBuildLogEntry("Found work-items in comments, adding " + workItems.size() + " work-items to octopus.metadata");

                metadata.WorkItems = workItems;

            } else {
                buildLogger.addBuildLogEntry("No work-items found in comments");
            }
        }

        metadata.BuildNumber = buildId;
        metadata.BuildLink = serverUrl + "/browse/" + buildNumber;
        metadata.VcsRoot = vcsRoot;
        metadata.VcsCommitNumber = vcsCommitNumber;

        return metadata;
    }
}
