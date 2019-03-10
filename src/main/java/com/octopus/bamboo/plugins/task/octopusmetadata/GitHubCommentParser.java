package com.octopus.bamboo.plugins.task.octopusmetadata;

import com.atlassian.bamboo.build.logger.BuildLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubCommentParser extends CommentParser {
    private static final String GITHUB_ID_REGEX = "(?:([/A-Z]*)#|GH-|http[/A-Z:.]*/issues/)(\\d+)";

    public String getIssueTrackerSuffix() {
        return "github";
    }

    public List<WorkItem> parse(final String comment, final BuildLogger buildLogger) {
        buildLogger.addBuildLogEntry("Parsing comments for GitHub issues");
        final List<WorkItem> workItems = new ArrayList<WorkItem>();

        final Pattern githubId = Pattern.compile(GITHUB_ID_REGEX);
        final Matcher githubMatcher = githubId.matcher(comment);

        while (githubMatcher.find()) {
            final WorkItem workItem = new WorkItem();
            final String linkData = githubMatcher.group(0);
            final String issueNumber = githubMatcher.group(2);

            workItem.Id = issueNumber;
            workItem.LinkData = linkData;
            workItem.LinkText = issueNumber;

            buildLogger.addBuildLogEntry("Located GitHub issue " + workItem.LinkData);

            workItems.add(workItem);
        }

        return workItems;
    }
}