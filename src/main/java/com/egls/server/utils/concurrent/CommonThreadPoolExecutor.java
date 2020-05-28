package com.egls.server.utils.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mayer - [Created on 2019-02-14 18:23]
 */
public class CommonThreadPoolExecutor extends ThreadPoolExecutor {

    public CommonThreadPoolExecutor(String poolName) {
        this(ThreadUtil.AVAILABLE_PROCESSORS, poolName);
    }

    public CommonThreadPoolExecutor(int poolSize, String poolName) {
        super(
                poolSize <= 0 ? 1 : poolSize,
                poolSize <= 0 ? 1 : poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                ThreadUtil.newThreadFactory("CTPool-" + poolName + "-%d")
        );
    }

}
