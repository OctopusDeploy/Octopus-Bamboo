package com.octopus.domain;

import java.util.List;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Releases
 */
public class Release {
    private String projectDeploymentProcessSnapshotId;

    private String assembled;

    private String projectVariableSetSnapshotId;

    private List<String> libraryVariableSetSnapshotIds;

    private String lastModifiedBy;

    private String channelId;

    private List<SelectedPackages> selectedPackages;

    private String lastModifiedOn;

    private String id;

    private String releaseNotes;

    private String version;

    private String projectId;

    public Release() {

    }

    public String getProjectDeploymentProcessSnapshotId() {
        return projectDeploymentProcessSnapshotId;
    }

    public void setProjectDeploymentProcessSnapshotId(final String projectDeploymentProcessSnapshotId) {
        this.projectDeploymentProcessSnapshotId = projectDeploymentProcessSnapshotId;
    }

    public String getAssembled() {
        return assembled;
    }

    public void setAssembled(final String assembled) {
        this.assembled = assembled;
    }

    public String getProjectVariableSetSnapshotId() {
        return projectVariableSetSnapshotId;
    }

    public void setProjectVariableSetSnapshotId(final String projectVariableSetSnapshotId) {
        this.projectVariableSetSnapshotId = projectVariableSetSnapshotId;
    }

    public List<String> getLibraryVariableSetSnapshotIds() {
        return libraryVariableSetSnapshotIds;
    }

    public void setLibraryVariableSetSnapshotIds(final List<String> libraryVariableSetSnapshotIds) {
        this.libraryVariableSetSnapshotIds = libraryVariableSetSnapshotIds;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public List<SelectedPackages> getSelectedPackages() {
        return selectedPackages;
    }

    public void setSelectedPackages(final List<SelectedPackages> selectedPackages) {
        this.selectedPackages = selectedPackages;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(final String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(final String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(final String projectId) {
        this.projectId = projectId;
    }
}

