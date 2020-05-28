package com.egls.server.utils.exception;

/**
 * @author mayer - [Created on 2018-09-12 18:34]
 */
public class MissingResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MissingResourceException(final String message) {
        super(customMessage(message));
    }

    public MissingResourceException(final String message, final Throwable cause) {
        super(customMessage(message), cause);
    }

    private static String customMessage(final String message) {
        return "Missing Resource (" + message + ")";
    }

}
