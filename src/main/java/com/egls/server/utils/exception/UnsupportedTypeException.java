package com.egls.server.utils.exception;

/**
 * @author mayer - [Created on 2018-08-09 20:46]
 */
public final class UnsupportedTypeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnsupportedTypeException(final Class clazz) {
        super(customMessage(clazz.getName()));
    }

    public UnsupportedTypeException(final Class clazz, final Throwable cause) {
        super(customMessage(clazz.getName()), cause);
    }

    private static String customMessage(final String message) {
        return "Unsupported Type (" + message + ")";
    }

}
