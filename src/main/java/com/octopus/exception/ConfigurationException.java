package com.octopus.exception;

/**
 * Represents an exception throw while dealing with configuration
 */
public class ConfigurationException extends RuntimeException {
    public ConfigurationException() {
        super();
    }

    public ConfigurationException(final String message) {
        super(message);
    }

    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(final Throwable cause) {
        super(cause);
    }

    protected ConfigurationException(final String message, final Throwable cause,
                                     final boolean enableSuppression,
                                     final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
