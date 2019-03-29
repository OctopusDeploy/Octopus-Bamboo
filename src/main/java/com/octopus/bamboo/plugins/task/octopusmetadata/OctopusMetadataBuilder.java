package com.octopus.bamboo.plugins.task.octopusmetadata;

import java.util.List;

public class OctopusMetadataBuilder {

    public OctopusPackageMetadata build(
            final String vcsType,
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
        metadata.BuildUrl = serverUrl + "/browse/" + buildNumber;
        metadata.VcsType = vcsType;
        metadata.VcsRoot = vcsRoot;
        metadata.VcsCommitNumber = vcsCommitNumber;

        return metadata;
    }
}
