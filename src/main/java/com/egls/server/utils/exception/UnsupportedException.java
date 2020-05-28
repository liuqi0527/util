package com.egls.server.utils.exception;

/**
 * @author mayer - [Created on 2018-08-09 20:46]
 */
public final class UnsupportedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnsupportedException(final String message) {
        super(customMessage(message));
    }

    public UnsupportedException(final String message, final Throwable cause) {
        super(customMessage(message), cause);
    }

    private static String customMessage(final String message) {
        return "Unsupported (" + message + ")";
    }

}
