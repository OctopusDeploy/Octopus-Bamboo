package com.octopus.domain;

import java.util.List;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Channels
 */
public class Channel {
    private String name;

    private String description;

    private String lifecycleId;

    private Boolean isDefault;

    private String id;

    //private List<String> rules;

    private String projectId;

    private List<String> tenantTags;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(final Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    /*public List<String> getRules() {
        return rules;
    }

    public void setRules(final List<String> rules) {
        this.rules = rules;
    }*/

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(final String projectId) {
        this.projectId = projectId;
    }

    public List<String> getTenantTags() {
        return tenantTags;
    }

    public void setTenantTags(final List<String> tenantTags) {
        this.tenantTags = tenantTags;
    }
}
