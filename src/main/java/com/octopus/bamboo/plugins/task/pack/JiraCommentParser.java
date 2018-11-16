package com.octopus.bamboo.plugins.task.pack;

import com.atlassian.bamboo.build.logger.BuildLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JiraCommentParser extends CommentParser {
    private static final String JIRA_ID_REGEX = "[A-Z]*-\\d+";

    public List<WorkItem> parse(final String comment, final BuildLogger buildLogger) {
        buildLogger.addBuildLogEntry("Parsing comments for Jira work items");
        final List<WorkItem> workItems = new ArrayList<WorkItem>();

        final Pattern jiraId = Pattern.compile(JIRA_ID_REGEX);
        final Matcher jiraMatcher = jiraId.matcher(comment);

        while (jiraMatcher.find()) {
            final WorkItem workItem = new WorkItem();
            workItem.Id = jiraMatcher.group(0);
            workItem.LinkText = jiraMatcher.group(0);

            buildLogger.addBuildLogEntry("Located Jira work item " + workItem.Id);

            workItems.add(workItem);
        }

        return workItems;
    }
}