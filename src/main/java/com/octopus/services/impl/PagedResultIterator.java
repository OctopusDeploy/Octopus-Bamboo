package com.octopus.services.impl;

import com.octopus.domain.PagedResult;
import com.octopus.services.PagedAPICallable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An iterator over pages results
 */
public class PagedResultIterator<T> implements Iterable<PagedResult<T>> {
    private final PagedAPICallable<T> apiCall;
    private int count = 0;
    private boolean hasNext = true;

    public PagedResultIterator(@NotNull final PagedAPICallable<T> apiCall) {
        checkNotNull(apiCall);
        this.apiCall = apiCall;
    }

    @NotNull
    @Override
    public Iterator<PagedResult<T>> iterator() {
        try {
            return new Iterator<PagedResult<T>>() {
                @Override
                public boolean hasNext() {
                    return hasNext;
                }

                @Override
                public PagedResult<T> next() {
                    if (hasNext) {
                        final PagedResult<T> result = apiCall.call(count);
                        count += result.getItems().size();
                        hasNext = count < result.getTotalResults();
                        return result;
                    } else {
                        throw new NoSuchElementException();
                    }
                }
            };
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
