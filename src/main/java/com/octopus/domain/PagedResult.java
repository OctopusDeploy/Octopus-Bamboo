package com.octopus.domain;

import java.util.List;

/**
 * The base class for paged results
 */
public class PagedResult<T> {
    private List<T> items;

    private Integer itemsPerPage;

    private String itemType;

    private Integer totalResults;

    private Boolean isStale;

    public List<T> getItems() {
        return items;
    }

    public void setItems(final List<T> items) {
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

    public Boolean getStale() {
        return isStale;
    }

    public void setStale(final Boolean stale) {
        isStale = stale;
    }
}
