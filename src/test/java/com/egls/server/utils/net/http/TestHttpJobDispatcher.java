package com.egls.server.utils.net.http;

import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicReference;

import com.egls.server.utils.CharsetUtil;
import com.egls.server.utils.net.http.client.HttpJobDispatcher;
import com.egls.server.utils.net.http.client.http.BaseHttpGetJob;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-13 18:31]
 */
public class TestHttpJobDispatcher {

    @Test
    public void test0() throws InterruptedException {
        AtomicReference<SimpleHttpResponse> atomicReference = new AtomicReference<>();
        AtomicReference<Object> exceptionReference = new AtomicReference<>();
        HttpJobDispatcher.startup();
        HttpJobDispatcher.postHttpJob(new BaseHttpGetJob() {
            @Override
            protected void handleResponse(SimpleHttpResponse httpResponse) {
                atomicReference.set(httpResponse);
            }

            @Override
            protected String getUrl() {
                return "http://www.baidu.com";
            }

            @Override
            protected void handleException(Exception exception) {
                exceptionReference.set(new Object());
            }

        });
        while (atomicReference.get() == null) {
            Thread.sleep(10L);
        }
        SimpleHttpResponse simpleHttpResponse = atomicReference.get();

        Assert.assertEquals(simpleHttpResponse.getResponseCode(), HttpURLConnection.HTTP_OK);
        Assert.assertTrue(simpleHttpResponse.isOk());
        String content = simpleHttpResponse.getString(CharsetUtil.defaultEncoding());
        Assert.assertFalse(StringUtils.isBlank(content));
        Assert.assertNull(exceptionReference.get());
    }

    @Test
    public void test1() throws InterruptedException {
        AtomicReference<SimpleHttpResponse> atomicReference = new AtomicReference<>();
        AtomicReference<Object> exceptionReference = new AtomicReference<>();
        HttpJobDispatcher.startup();
        HttpJobDispatcher.postHttpJob(new BaseHttpGetJob() {
            @Override
            protected void handleResponse(SimpleHttpResponse httpResponse) {
                atomicReference.set(httpResponse);
            }

            @Override
            protected String getUrl() {
                return "http://wrongurl";
            }

            @Override
            protected void handleException(Exception exception) {
                exceptionReference.set(new Object());
            }
        });

        long waitMillis = 20L, totalWaitMillis = 0;
        while (totalWaitMillis < HttpProperties.getHttpConnectTimeout()) {
            totalWaitMillis += waitMillis;
            Thread.sleep(waitMillis);
        }
        Assert.assertNull(atomicReference.get());
        Assert.assertNotNull(exceptionReference.get());
    }

}
