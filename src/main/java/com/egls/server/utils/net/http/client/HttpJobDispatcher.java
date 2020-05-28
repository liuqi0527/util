package com.egls.server.utils.net.http.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.egls.server.utils.structure.ElapsedTimeRecorder;

/**
 * BaseHttpJob的任务分配器.
 * 内部使用线程池进行任务的运行.
 * 当一段时间没有任务的时候,线程池内的线程会逐渐被销毁.
 *
 * @author mayer - [Created on 2018-09-13 12:05]
 */
public final class HttpJobDispatcher {

    private static volatile HttpJobExecutorService HTTP_JOB_EXECUTOR_SERVICE;

    private static final Object HTTP_JOB_EXECUTOR_SERVICE_MUTEX = new Object();

    static final AtomicLong EXECUTE_JOB_COUNT = new AtomicLong(0);

    private static final AtomicLong UPPER_POST_JOB_COUNT = new AtomicLong(0);

    static final AtomicLong EXECUTE_JOB_ELAPSED_TIME_MILLIS = new AtomicLong(0);

    static final Map<String, ElapsedTimeRecorder> JOB_ELAPSED_TIME_RECORDER_MAP = new ConcurrentHashMap<>();

    public static void startup() {
        synchronized (HTTP_JOB_EXECUTOR_SERVICE_MUTEX) {
            HTTP_JOB_EXECUTOR_SERVICE = new HttpJobExecutorService();
        }
    }

    public static void postHttpJob(final BaseHttpJob httpJob) {
        //双检查能减少一部分同步的开销.
        if (HTTP_JOB_EXECUTOR_SERVICE == null) {
            synchronized (HTTP_JOB_EXECUTOR_SERVICE_MUTEX) {
                if (HTTP_JOB_EXECUTOR_SERVICE == null) {
                    throw new RuntimeException("need startup");
                }
            }
        }
        UPPER_POST_JOB_COUNT.getAndIncrement();
        HTTP_JOB_EXECUTOR_SERVICE.execute(httpJob);
    }

    public static long getExecuteJobCount() {
        return EXECUTE_JOB_COUNT.get();
    }

    public static long getUpperPostJobCount() {
        return UPPER_POST_JOB_COUNT.get();
    }

    public static long getExecuteJobElapsedTimeMillis() {
        return EXECUTE_JOB_ELAPSED_TIME_MILLIS.get();
    }

    public static String getElapsedTimeRecord() {
        return ElapsedTimeRecorder.toShowString("HTTP JOB ELAPSED NANO TIME", JOB_ELAPSED_TIME_RECORDER_MAP.values());
    }

    private HttpJobDispatcher() {
        //
    }

}
