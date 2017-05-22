package com.octopus.domain;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Environments
 */
public class Environment {
    private String Name;

    private String Description;

    private Boolean UseGuidedFailure;

    private Integer SortOrder;

    private String Id;

    public String getName() {
        return Name;
    }

    public void setName(final String Name) {
        this.Name = Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(final String Description) {
        this.Description = Description;
    }

    public Boolean getUseGuidedFailure() {
        return UseGuidedFailure;
    }

    public void setUseGuidedFailure(final Boolean UseGuidedFailure) {
        this.UseGuidedFailure = UseGuidedFailure;
    }

    public Integer getSortOrder() {
        return SortOrder;
    }

    public void setSortOrder(final Integer SortOrder) {
        this.SortOrder = SortOrder;
    }

    public String getId() {
        return Id;
    }

    public void setId(final String Id) {
        this.Id = Id;
    }
}
