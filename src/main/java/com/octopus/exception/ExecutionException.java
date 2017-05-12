package com.octopus.exception;

/**
 * Represents an exception throw while working with resources
 */
public class ExecutionException extends RuntimeException {
    public ExecutionException() {
        super();
    }

    public ExecutionException(final String message) {
        super(message);
    }

    public ExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExecutionException(final Throwable cause) {
        super(cause);
    }

    protected ExecutionException(final String message, final Throwable cause,
                                 final boolean enableSuppression,
                                 final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
