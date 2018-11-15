package com.octopus.bamboo.plugins.task.pack;

import com.atlassian.bamboo.build.logger.BuildLogger;

import java.util.List;

public abstract class CommentParser {
    public abstract List<WorkItem> parse(String comment, final BuildLogger buildLogger);
}