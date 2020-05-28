package com.egls.server.utils.net.http.client;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import com.egls.server.utils.structure.ElapsedTimeRecorder;

import org.apache.commons.lang3.StringUtils;

/**
 * 作为客户端请求http服务的一个任务.
 * 通常来说http请求任务可能是一个耗时任务
 * 所以来说,放在一个单独的地方来进行请求
 * 所有job均不支持断点续传.
 * <p>
 * Note: 注意在执行完毕后
 *
 * @author mayer - [Created on 2018-09-13 12:02]
 */
public abstract class BaseHttpJob implements Runnable {

    private static final AtomicReferenceFieldUpdater<BaseHttpJob, Object> ATTACHMENT_UPDATER
            = AtomicReferenceFieldUpdater.newUpdater(BaseHttpJob.class, Object.class, "attachment");

    private volatile Object attachment = null;

    // -- Attachments --

    public final Object attach(final Object object) {
        return ATTACHMENT_UPDATER.getAndSet(this, object);
    }

    public final Object attachment() {
        return attachment;
    }

    @Override
    public void run() {
        long millis = System.currentTimeMillis();
        String url = StringUtils.EMPTY;
        try {
            url = getUrl();
            request(url);
        } catch (Exception exception) {
            HttpJobExecutorService.LOGGER.error("Exception in BaseHttpJob " + url, exception);
            try {
                handleException(exception);
            } catch (Exception e) {
                HttpJobExecutorService.LOGGER.error("Exception in BaseHttpJob#handleException() " + url, e);
            }
        }
        long elapsedTimeMillis = System.currentTimeMillis() - millis;
        if (elapsedTimeMillis > 0) {
            HttpJobDispatcher.EXECUTE_JOB_ELAPSED_TIME_MILLIS.getAndAdd(elapsedTimeMillis);
            HttpJobDispatcher.JOB_ELAPSED_TIME_RECORDER_MAP
                    .computeIfAbsent(getClass().getName(), ElapsedTimeRecorder::new)
                    .addElapsedTime(elapsedTimeMillis);
        }
        HttpJobDispatcher.EXECUTE_JOB_COUNT.getAndIncrement();
    }

    /**
     * 获取访问的URL
     *
     * @return URL
     */
    protected abstract String getUrl();

    /**
     * 这个方法是提供给子类实现,用来扩展请求需要的Headers.默认是为null的,无任何Header
     *
     * @return 请求时的一些特殊的Headers
     */
    protected Map<String, String> getRequestHeaders() {
        return null;
    }

    /**
     * 请求URL地址.
     *
     * @param url URL
     * @throws Exception 异常
     */
    protected abstract void request(final String url) throws Exception;

    /**
     * 处理运行过程中发生的异常.不要打印日志.
     * 用于异常出现的时候,控制逻辑继续运行.
     *
     * @param exception 异常
     */
    protected abstract void handleException(Exception exception);

}
