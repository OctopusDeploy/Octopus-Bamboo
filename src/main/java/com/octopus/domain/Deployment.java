package com.octopus.domain;

import java.util.List;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Deployments
 */
public class Deployment {
    private String comments;

    private Boolean useGuidedFailure;

    private List<String> specificMachineIds;

    /*
        I don't know what form values is.
        https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Deployments doesn't help either.
     */
    //private String FormValues;

    private String manifestVariableSetId;

    private String lastModifiedBy;

    private String queueTimeExpiry;

    private String environmentId;

    private String taskId;

    private Boolean forcePackageDownload;

    private String name;

    private String channelId;

    private String tenantId;

    private String created;

    private List<String> skipActions;

    private String deploymentProcessId;

    private String lastModifiedOn;

    private List<String> excludedMachineIds;

    private String id;

    private String queueTime;

    private String releaseId;

    private Boolean forcePackageRedeployment;

    private String projectId;

    public String getComments() {
        return comments;
    }

    public void setComments(final String comments) {
        this.comments = comments;
    }

    public Boolean getUseGuidedFailure() {
        return useGuidedFailure;
    }

    public void setUseGuidedFailure(final Boolean useGuidedFailure) {
        this.useGuidedFailure = useGuidedFailure;
    }

    public List<String> getSpecificMachineIds() {
        return specificMachineIds;
    }

    public void setSpecificMachineIds(final List<String> specificMachineIds) {
        this.specificMachineIds = specificMachineIds;
    }

    /*
    public String getFormValues() {
        return FormValues;
    }

    public void setFormValues(final String FormValues) {
        this.FormValues = FormValues;
    }
    */

    public String getManifestVariableSetId() {
        return manifestVariableSetId;
    }

    public void setManifestVariableSetId(final String manifestVariableSetId) {
        this.manifestVariableSetId = manifestVariableSetId;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getQueueTimeExpiry() {
        return queueTimeExpiry;
    }

    public void setQueueTimeExpiry(final String queueTimeExpiry) {
        this.queueTimeExpiry = queueTimeExpiry;
    }

    public String getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(final String environmentId) {
        this.environmentId = environmentId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }

    public Boolean getForcePackageDownload() {
        return forcePackageDownload;
    }

    public void setForcePackageDownload(final Boolean forcePackageDownload) {
        this.forcePackageDownload = forcePackageDownload;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(final String created) {
        this.created = created;
    }

    public List<String> getSkipActions() {
        return skipActions;
    }

    public void setSkipActions(final List<String> skipActions) {
        this.skipActions = skipActions;
    }

    public String getDeploymentProcessId() {
        return deploymentProcessId;
    }

    public void setDeploymentProcessId(final String deploymentProcessId) {
        this.deploymentProcessId = deploymentProcessId;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(final String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public List<String> getExcludedMachineIds() {
        return excludedMachineIds;
    }

    public void setExcludedMachineIds(final List<String> excludedMachineIds) {
        this.excludedMachineIds = excludedMachineIds;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(final String queueTime) {
        this.queueTime = queueTime;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(final String releaseId) {
        this.releaseId = releaseId;
    }

    public Boolean getForcePackageRedeployment() {
        return forcePackageRedeployment;
    }

    public void setForcePackageRedeployment(final Boolean forcePackageRedeployment) {
        this.forcePackageRedeployment = forcePackageRedeployment;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(final String projectId) {
        this.projectId = projectId;
    }
}
