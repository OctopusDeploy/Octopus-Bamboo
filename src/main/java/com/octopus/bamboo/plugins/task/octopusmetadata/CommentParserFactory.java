package com.octopus.bamboo.plugins.task.octopusmetadata;

import java.util.Arrays;
import java.util.List;

public class CommentParserFactory {
    private static final String JIRA_PARSER = "Jira";
    private static final String GITHUB_PARSER = "GitHub";

    private static final List<String> PARSERS = Arrays.asList("", JIRA_PARSER, GITHUB_PARSER);

    public static List<String> getParsers() {
        return PARSERS;
    }
}
