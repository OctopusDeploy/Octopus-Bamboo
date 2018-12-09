package com.octopus.bamboo.plugins.task.pack;

import java.util.Arrays;
import java.util.List;

public class CommentParserFactory {
    private static final String JIRA_PARSER = "Jira";

    private static final List<String> PARSERS = Arrays.asList("", JIRA_PARSER);

    public static List<String> getParsers() {
        return PARSERS;
    }

    public CommentParser getParser(final String parser) throws Exception {
        switch (parser) {
            case JIRA_PARSER:
                return new JiraCommentParser();
            default:
                throw new Exception("Unsupported parser value " + parser);
        }
    }
}
