package com.octopus.domain;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Projects
 */
public class Project {
    private String[] includedLibraryVariableSetIds;

    private String description;

    private String lifecycleId;

    private Boolean isDisabled;

    private String projectGroupId;

    private String tenantedDeploymentMode;

    private String[] autoDeployReleaseOverrides;

    private String[] templates;

    private String slug;

    private String name;

    private String variableSetId;

    private String deploymentProcessId;

    private String defaultToSkipIfAlreadyInstalled;

    private String discreteChannelRelease;

    private String id;

    private Boolean autoCreateRelease;

    public String[] getIncludedLibraryVariableSetIds() {
        return includedLibraryVariableSetIds;
    }

    public void setIncludedLibraryVariableSetIds(final String[] includedLibraryVariableSetIds) {
        this.includedLibraryVariableSetIds = includedLibraryVariableSetIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLifecycleId() {
        return lifecycleId;
    }

    public void setLifecycleId(final String lifecycleId) {
        this.lifecycleId = lifecycleId;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(final Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getProjectGroupId() {
        return projectGroupId;
    }

    public void setProjectGroupId(final String projectGroupId) {
        this.projectGroupId = projectGroupId;
    }

    public String getTenantedDeploymentMode() {
        return tenantedDeploymentMode;
    }

    public void setTenantedDeploymentMode(final String tenantedDeploymentMode) {
        this.tenantedDeploymentMode = tenantedDeploymentMode;
    }

    public String[] getAutoDeployReleaseOverrides() {
        return autoDeployReleaseOverrides;
    }

    public void setAutoDeployReleaseOverrides(final String[] autoDeployReleaseOverrides) {
        this.autoDeployReleaseOverrides = autoDeployReleaseOverrides;
    }

    public String[] getTemplates() {
        return templates;
    }

    public void setTemplates(final String[] templates) {
        this.templates = templates;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(final String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getVariableSetId() {
        return variableSetId;
    }

    public void setVariableSetId(final String variableSetId) {
        this.variableSetId = variableSetId;
    }

    public String getDeploymentProcessId() {
        return deploymentProcessId;
    }

    public void setDeploymentProcessId(final String deploymentProcessId) {
        this.deploymentProcessId = deploymentProcessId;
    }

    public String getDefaultToSkipIfAlreadyInstalled() {
        return defaultToSkipIfAlreadyInstalled;
    }

    public void setDefaultToSkipIfAlreadyInstalled(final String defaultToSkipIfAlreadyInstalled) {
        this.defaultToSkipIfAlreadyInstalled = defaultToSkipIfAlreadyInstalled;
    }

    public String getDiscreteChannelRelease() {
        return discreteChannelRelease;
    }

    public void setDiscreteChannelRelease(final String discreteChannelRelease) {
        this.discreteChannelRelease = discreteChannelRelease;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Boolean getAutoCreateRelease() {
        return autoCreateRelease;
    }

    public void setAutoCreateRelease(final Boolean autoCreateRelease) {
        this.autoCreateRelease = autoCreateRelease;
    }
}
