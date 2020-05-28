package com.egls.server.utils.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.management.ManagementFactory;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;

import com.egls.server.utils.exception.ExceptionUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A static utility class which provides ease of use functionality for {@link Thread}s
 *
 * @author mayer - [Created on 2018-10-01 17:26]
 */
public class ThreadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtil.class);

    /**
     * Returns the amount of available processors available to the Java virtual machine.
     */
    public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static final UncaughtExceptionHandler DEFAULT_EXCEPTION_HANDLER =
            (thread, exception) -> LOGGER.error("Exception in thread " + thread.getName(), exception);

    public static ThreadFactory newThreadFactory(String name) {
        return newThreadFactory(name, false, Thread.NORM_PRIORITY, DEFAULT_EXCEPTION_HANDLER);
    }

    public static ThreadFactory newThreadFactory(String name, boolean daemon) {
        return newThreadFactory(name, daemon, Thread.NORM_PRIORITY, DEFAULT_EXCEPTION_HANDLER);
    }

    public static ThreadFactory newThreadFactory(String name, boolean daemon, int priority) {
        return newThreadFactory(name, daemon, priority, DEFAULT_EXCEPTION_HANDLER);
    }

    /**
     * Creates a {@link ThreadFactory} using the specified {@code String} name-format, priority and
     * {@link UncaughtExceptionHandler}.
     *
     * @param name     The name-format used when creating threads. Must not be {@code null}.
     * @param daemon   whether or not new Threads created with this ThreadFactory will be daemon threads
     * @param priority The priority used when creating threads. Must be {@code 1 <= priority <= 10}.
     * @param handler  The {@link UncaughtExceptionHandler} used when creating threads. Must not be {@code null}.
     * @return The {@link ThreadFactory}. Will never be {@code null}.
     */
    public static ThreadFactory newThreadFactory(String name, boolean daemon, int priority, UncaughtExceptionHandler handler) {
        Objects.requireNonNull(name, "ThreadFactory name must not be null.");
        Objects.requireNonNull(handler, "UncaughtExceptionHandler must not be null.");

        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
        builder.setNameFormat(name);
        builder.setDaemon(daemon);
        builder.setPriority(priority);
        builder.setUncaughtExceptionHandler(handler);

        return builder.build();
    }

    public static Thread adjustThreadProperties(Thread thread, String name) {
        return adjustThreadProperties(thread, name, false, Thread.NORM_PRIORITY, DEFAULT_EXCEPTION_HANDLER);
    }

    public static Thread adjustThreadProperties(Thread thread, String name, boolean daemon) {
        return adjustThreadProperties(thread, name, daemon, Thread.NORM_PRIORITY, DEFAULT_EXCEPTION_HANDLER);
    }

    public static Thread adjustThreadProperties(Thread thread, String name, boolean daemon, int priority) {
        return adjustThreadProperties(thread, name, daemon, priority, DEFAULT_EXCEPTION_HANDLER);
    }

    public static Thread adjustThreadProperties(Thread thread, String name, boolean daemon, int priority, UncaughtExceptionHandler handler) {
        thread.setName(name);
        thread.setDaemon(daemon);
        thread.setPriority(priority);
        thread.setUncaughtExceptionHandler(handler);
        return thread;
    }

    public static String getCurrentProgramId() {
        // format: "pid@hostname"
        String info = ManagementFactory.getRuntimeMXBean().getName();
        return info.split("@")[0];
    }

    public static String getStackTrace() {
        return ExceptionUtil.getStackTraceByLine();
    }

    public static void printStackTrace() {
        ExceptionUtil.printStackTrace();
    }

    /**
     * Sole private constructor to prevent instantiation.
     */
    private ThreadUtil() {

    }

}
