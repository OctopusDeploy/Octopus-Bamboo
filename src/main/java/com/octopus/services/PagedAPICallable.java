package com.octopus.services;

import com.octopus.domain.PagedResult;

/**
 * A callback to get results from the api
 */
public interface PagedAPICallable<T> {
    /**
     * @param skip How many results to skip
     * @return The paged results from the API call
     */
    PagedResult<T> call(int skip);
}
