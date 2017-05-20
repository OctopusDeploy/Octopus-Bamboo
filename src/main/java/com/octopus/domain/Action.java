package com.octopus.domain;

import java.util.Map;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/DeploymentSteps
 */
public class Action {
    private String[] channels;

    private String name;

    private String[] environments;

    private String actionType;

    private Boolean isDisabled;

    private String id;

    private String[] excludedEnvironments;

    private Map<String, String> properties;

    private String[] tenantTags;

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(final String[] channels) {
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String[] getEnvironments() {
        return environments;
    }

    public void setEnvironments(final String[] environments) {
        this.environments = environments;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(final String actionType) {
        this.actionType = actionType;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(final Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String[] getExcludedEnvironments() {
        return excludedEnvironments;
    }

    public void setExcludedEnvironments(final String[] excludedEnvironments) {
        this.excludedEnvironments = excludedEnvironments;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    public String[] getTenantTags() {
        return tenantTags;
    }

    public void setTenantTags(final String[] tenantTags) {
        this.tenantTags = tenantTags;
    }
}
