package com.octopus.domain;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Environments
 */
public class Environment {
    private String name;

    private String description;

    private Boolean useGuidedFailure;

    private Integer sortOrder;

    private String id;

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

    public Boolean getUseGuidedFailure() {
        return useGuidedFailure;
    }

    public void setUseGuidedFailure(final Boolean useGuidedFailure) {
        this.useGuidedFailure = useGuidedFailure;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(final Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
