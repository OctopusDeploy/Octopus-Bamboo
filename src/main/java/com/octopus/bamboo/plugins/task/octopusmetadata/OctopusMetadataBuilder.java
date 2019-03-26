package com.octopus.bamboo.plugins.task.octopusmetadata;

import com.atlassian.bamboo.build.logger.BuildLogger;
import java.util.List;

public class OctopusMetadataBuilder {

    private BuildLogger buildLogger;

    public OctopusMetadataBuilder(final BuildLogger buildLogger) {
        this.buildLogger = buildLogger;
    }

    public OctopusPackageMetadata build(
            final String vcsRoot,
            final String vcsCommitNumber,
            final List<Commit> commits,
            final String commentParser,
            final String serverUrl,
            final String buildId,
            final String buildNumber) {

        final OctopusPackageMetadata metadata = new OctopusPackageMetadata();

        metadata.Commits = commits;
        metadata.CommentParser = commentParser;
        metadata.BuildNumber = buildId;
        metadata.BuildLink = serverUrl + "/browse/" + buildNumber;
        metadata.VcsRoot = vcsRoot;
        metadata.VcsCommitNumber = vcsCommitNumber;

        return metadata;
    }
}
