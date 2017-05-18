package com.octopus.domain;

import java.util.List;
import java.util.Map;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/DeploymentSteps
 */
public class Action {
    private List<String> channels;

    private String name;

    private String links;

    private List<String> environments;

    private String actionType;

    private Boolean isDisabled;

    private String id;

    private List<String> excludedEnvironments;

    private Map<String, String> properties;

    private List<String> tenantTags;

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(final List<String> channels) {
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(final String links) {
        this.links = links;
    }

    public List<String> getEnvironments() {
        return environments;
    }

    public void setEnvironments(final List<String> environments) {
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

    public List<String> getExcludedEnvironments() {
        return excludedEnvironments;
    }

    public void setExcludedEnvironments(final List<String> excludedEnvironments) {
        this.excludedEnvironments = excludedEnvironments;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    public List<String> getTenantTags() {
        return tenantTags;
    }

    public void setTenantTags(final List<String> tenantTags) {
        this.tenantTags = tenantTags;
    }
}
