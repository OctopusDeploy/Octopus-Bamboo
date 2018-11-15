package com.octopus.bamboo.plugins.task.pack;

import java.util.Arrays;
import java.util.List;

public class CommentParserFactory {
    private static final String JIRA_PARSER = "Jira";

    private static final List<String> parsers = Arrays.asList("", JIRA_PARSER);

    public static List<String> getParsers() {
        return parsers;
    }

    public CommentParser getParser(String parser) throws Exception {
        switch (parser) {
            case JIRA_PARSER:
                return new JiraCommentParser();
        }
        throw new Exception("Unsupported parser value " + parser);
    }
}
