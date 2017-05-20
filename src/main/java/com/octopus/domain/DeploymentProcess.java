package com.octopus.domain;

import java.util.List;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/DeploymentProcesses
 */
public class DeploymentProcess {
    private List<Step> steps;

    private String id;

    private Integer version;

    private String lastSnapshotId;

    private String projectId;

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(final List<Step> steps) {
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    public String getLastSnapshotId() {
        return lastSnapshotId;
    }

    public void setLastSnapshotId(final String lastSnapshotId) {
        this.lastSnapshotId = lastSnapshotId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(final String projectId) {
        this.projectId = projectId;
    }

}
