package com.octopus.bamboo.plugins.task.octopusbuildinformation;

import java.util.List;

public class OctopusBuildInformationBuilder {

    public OctopusBuildInformation build(
            final String vcsType,
            final String vcsRoot,
            final String vcsCommitNumber,
            final List<Commit> commits,
            final String branch,
            final String serverUrl,
            final String buildId,
            final String buildNumber) {

        final OctopusBuildInformation metadata = new OctopusBuildInformation();

        metadata.Commits = commits;
        metadata.Branch = branch;
        metadata.BuildNumber = buildId;
        metadata.BuildUrl = serverUrl + "/browse/" + buildNumber;
        metadata.VcsType = vcsType;
        metadata.VcsRoot = vcsRoot;
        metadata.VcsCommitNumber = vcsCommitNumber;

        return metadata;
    }
}
