package com.egls.server.utils.net.http.client;

import com.egls.server.utils.concurrent.CommonThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mayer - [Created on 2018-09-13 15:42]
 */
class HttpJobExecutorService extends CommonThreadPoolExecutor {

    static final Logger LOGGER = LoggerFactory.getLogger(HttpJobExecutorService.class);

    HttpJobExecutorService() {
        super(HttpJobExecutorService.class.getSimpleName());
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        //may be used to re-initialize ThreadLocals, or to perform logging.
        LOGGER.info("beforeExecute BaseHttpJob, {}, {}", t, r);
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        //may be used to re-initialize ThreadLocals, or to perform logging.
        LOGGER.info("afterExecute BaseHttpJob, {}, {}", t, r);
        super.afterExecute(r, t);
    }

}
