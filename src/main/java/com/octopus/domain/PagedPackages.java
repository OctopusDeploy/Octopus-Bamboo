package com.octopus.domain;

import java.util.List;

/**
 * https://github.com/OctopusDeploy/OctopusDeploy-Api/wiki/PackageFromBuiltInFeeds
 */
public class PagedPackages {
    private List<Package> items;

    private String itemsPerPage;

    private String itemType;

    private String totalResults;

    private Boolean isStale;

    public List<Package> getItems() {
        return items;
    }

    public void setItems(final List<Package> items) {
        this.items = items;
    }

    public String getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(final String itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(final String itemType) {
        this.itemType = itemType;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(final String totalResults) {
        this.totalResults = totalResults;
    }

    public Boolean getIsStale() {
        return isStale;
    }

    public void setIsStale(final Boolean isStale) {
        this.isStale = isStale;
    }
}
