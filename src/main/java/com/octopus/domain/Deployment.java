package com.octopus.domain;

import java.util.List;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Deployments
 */
public class Deployment {
    private String Comments;

    private Boolean UseGuidedFailure;

    private List<String> SpecificMachineIds;

    /*
        TODO: I don't know what form values is.
        https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Deployments doesn't help either.
        https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Deployments doesn't help either.
     */
    //private String FormValues;

    private String ManifestVariableSetId;

    private String LastModifiedBy;

    private String QueueTimeExpiry;

    private String EnvironmentId;

    private String TaskId;

    private Boolean ForcePackageDownload;

    private String Name;

    private String ChannelId;

    private String TenantId;

    private String Created;

    private List<String> SkipActions;

    private String DeploymentProcessId;

    private String LastModifiedOn;

    private List<String> ExcludedMachineIds;

    private String Id;

    private String QueueTime;

    private String ReleaseId;

    private Boolean ForcePackageRedeployment;

    private String ProjectId;

    public String getComments() {
        return Comments;
    }

    public void setComments(final String Comments) {
        this.Comments = Comments;
    }

    public Boolean getUseGuidedFailure() {
        return UseGuidedFailure;
    }

    public void setUseGuidedFailure(final Boolean UseGuidedFailure) {
        this.UseGuidedFailure = UseGuidedFailure;
    }

    public List<String> getSpecificMachineIds() {
        return SpecificMachineIds;
    }

    public void setSpecificMachineIds(final List<String> SpecificMachineIds) {
        this.SpecificMachineIds = SpecificMachineIds;
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
        return ManifestVariableSetId;
    }

    public void setManifestVariableSetId(final String ManifestVariableSetId) {
        this.ManifestVariableSetId = ManifestVariableSetId;
    }

    public String getLastModifiedBy() {
        return LastModifiedBy;
    }

    public void setLastModifiedBy(final String LastModifiedBy) {
        this.LastModifiedBy = LastModifiedBy;
    }

    public String getQueueTimeExpiry() {
        return QueueTimeExpiry;
    }

    public void setQueueTimeExpiry(final String QueueTimeExpiry) {
        this.QueueTimeExpiry = QueueTimeExpiry;
    }

    public String getEnvironmentId() {
        return EnvironmentId;
    }

    public void setEnvironmentId(final String EnvironmentId) {
        this.EnvironmentId = EnvironmentId;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(final String TaskId) {
        this.TaskId = TaskId;
    }

    public Boolean getForcePackageDownload() {
        return ForcePackageDownload;
    }

    public void setForcePackageDownload(final Boolean ForcePackageDownload) {
        this.ForcePackageDownload = ForcePackageDownload;
    }

    public String getName() {
        return Name;
    }

    public void setName(final String Name) {
        this.Name = Name;
    }

    public String getChannelId() {
        return ChannelId;
    }

    public void setChannelId(final String ChannelId) {
        this.ChannelId = ChannelId;
    }

    public String getTenantId() {
        return TenantId;
    }

    public void setTenantId(final String TenantId) {
        this.TenantId = TenantId;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(final String Created) {
        this.Created = Created;
    }

    public List<String> getSkipActions() {
        return SkipActions;
    }

    public void setSkipActions(final List<String> SkipActions) {
        this.SkipActions = SkipActions;
    }

    public String getDeploymentProcessId() {
        return DeploymentProcessId;
    }

    public void setDeploymentProcessId(final String DeploymentProcessId) {
        this.DeploymentProcessId = DeploymentProcessId;
    }

    public String getLastModifiedOn() {
        return LastModifiedOn;
    }

    public void setLastModifiedOn(final String LastModifiedOn) {
        this.LastModifiedOn = LastModifiedOn;
    }

    public List<String> getExcludedMachineIds() {
        return ExcludedMachineIds;
    }

    public void setExcludedMachineIds(final List<String> ExcludedMachineIds) {
        this.ExcludedMachineIds = ExcludedMachineIds;
    }

    public String getId() {
        return Id;
    }

    public void setId(final String Id) {
        this.Id = Id;
    }

    public String getQueueTime() {
        return QueueTime;
    }

    public void setQueueTime(final String QueueTime) {
        this.QueueTime = QueueTime;
    }

    public String getReleaseId() {
        return ReleaseId;
    }

    public void setReleaseId(final String ReleaseId) {
        this.ReleaseId = ReleaseId;
    }

    public Boolean getForcePackageRedeployment() {
        return ForcePackageRedeployment;
    }

    public void setForcePackageRedeployment(final Boolean ForcePackageRedeployment) {
        this.ForcePackageRedeployment = ForcePackageRedeployment;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(final String ProjectId) {
        this.ProjectId = ProjectId;
    }
}
