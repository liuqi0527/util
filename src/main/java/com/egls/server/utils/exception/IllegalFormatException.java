package com.egls.server.utils.exception;

/**
 * @author mayer - [Created on 2018-08-09 20:45]
 */
public final class IllegalFormatException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IllegalFormatException(final String message) {
        super(customMessage(message));
    }

    public IllegalFormatException(final String message, final Throwable cause) {
        super(customMessage(message), cause);
    }

    private static String customMessage(final String message) {
        return "Illegal Format (" + message + ")";
    }

}
