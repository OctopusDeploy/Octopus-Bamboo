package com.octopus.bamboo.plugins.task.octopusmetadata;

import java.util.List;

public class OctopusPackageMetadata {
    public String BuildEnvironment;
    public String IssueTrackerId;
    public String BuildNumber;
    public String BuildLink;
    public String VcsRoot;
    public String VcsCommitNumber;

    public List<WorkItem> WorkItems;

    public OctopusPackageMetadata() {
        BuildEnvironment = "Bamboo";
    }
}