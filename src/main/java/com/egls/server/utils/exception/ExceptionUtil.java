package com.egls.server.utils.exception;

import com.egls.server.utils.StringUtil;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供一些异常错误的工具方法
 *
 * @author mayer - [Created on 2018-08-09 21:57]
 */
public final class ExceptionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtil.class);

    public static Throwable getSuperCause(final Throwable throwable) {
        Throwable superCause = throwable;
        while (superCause != null && superCause.getCause() != null) {
            superCause = superCause.getCause();
        }
        return superCause;
    }

    public static String getStackTraceByLine() {
        return getStackTraceByLine(new Throwable());
    }

    public static String getStackTraceByTab() {
        return getStackTraceByTab(new Throwable());
    }

    public static String getStackTraceByLine(final Throwable ex) {
        return getStackTraceByLine(ex, StringUtil.getLineSeparator());
    }

    public static String getStackTraceByTab(final Throwable ex) {
        return getStackTraceByLine(ex, "\t");
    }

    public static String getStackTraceByLine(final Throwable ex, final String retStr) {
        StringBuilder stack = new StringBuilder();
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
            stack.append(ex).append(retStr);
            for (StackTraceElement stackElement : stackElements) {
                stack.append("\t");
                stack.append(stackElement.getClassName());
                stack.append(".").append(stackElement.getMethodName());
                stack.append("(").append(stackElement.getFileName());
                stack.append(":").append(stackElement.getLineNumber());
                stack.append(")").append(retStr);
            }
            Throwable cause = ex.getCause();
            if (cause != null) {
                stack.append("Caused by: ").append(getStackTraceByLine(cause));
            }
        }
        return stack.toString();
    }

    public static String getStackTraceWithDepthByLine(final int depth) {
        return getStackTraceWithDepth(depth, StringUtil.getLineSeparator());
    }

    public static String getStackTraceWithDepthByTab(final int depth) {
        return getStackTraceWithDepth(depth, "\t");
    }

    public static String getStackTraceWithDepth(final int depth, final String retStr) {
        StringBuilder stack = new StringBuilder();
        StackTraceElement[] stackElements = new Throwable().getStackTrace();
        for (int i = 0; i <= depth && i < stackElements.length; i++) {
            stack.append(stackElements[i].getClassName());
            stack.append(".").append(stackElements[i].getMethodName());
            stack.append("(").append(stackElements[i].getFileName());
            stack.append(":").append(stackElements[i].getLineNumber());
            stack.append(")").append(retStr);
        }
        return stack.toString();
    }

    public static void printStackTrace() {
        LOGGER.info("-----printStackTrace-----");
        LOGGER.info(getStackTraceByLine());
        LOGGER.info("-----printStackTrace-----");
    }

    public static void printStackTrace(final Throwable throwable) {
        LOGGER.info("-----printStackTrace-----");
        LOGGER.info(getStackTraceByLine(throwable));
        LOGGER.info("-----printStackTrace-----");
    }

    public static String getMessage(final Throwable th) {
        return ExceptionUtils.getMessage(th);
    }

    public static String getRootCauseMessage(final Throwable th) {
        return ExceptionUtils.getRootCauseMessage(th);
    }

    public static Throwable getRootCause(final Throwable throwable) {
        return ExceptionUtils.getRootCause(throwable);
    }

    public static String getDefaultFormatStackTrace(final Throwable throwable) {
        return ExceptionUtils.getStackTrace(throwable);
    }

}
