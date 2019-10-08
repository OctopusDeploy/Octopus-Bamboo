package com.octopus.bamboo.plugins.task.octopusbuildinformation;

import java.util.List;

public class OctopusBuildInformation {
    public String BuildEnvironment;
    public String BuildNumber;
    public String BuildUrl;
    public String Branch;
    public String VcsType;
    public String VcsRoot;
    public String VcsCommitNumber;

    public List<Commit> Commits;

    public OctopusBuildInformation() {
        BuildEnvironment = "Bamboo";
    }
}