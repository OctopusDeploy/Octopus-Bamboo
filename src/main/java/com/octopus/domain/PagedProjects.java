package com.octopus.domain;

import java.util.List;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/Projects
 */
public class PagedProjects {
    private List<Project> items;

    private Integer itemsPerPage;

    private String itemType;

    private Integer totalResults;

    private Boolean isStale;

    public List<Project> getItems() {
        return items;
    }

    public void setItems(final List<Project> items) {
        this.items = items;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(final Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(final String itemType) {
        this.itemType = itemType;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(final Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Boolean getIsStale() {
        return isStale;
    }

    public void setIsStale(final Boolean isStale) {
        this.isStale = isStale;
    }
}