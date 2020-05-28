package com.egls.server.utils.net.http;

/**
 * @author mayer - [Created on 2018-09-13 21:08]
 */
public class HttpProperties {

    /**
     * 连接超时时间
     */
    private static volatile int HTTP_CONNECT_TIMEOUT = 5000;

    /**
     * 读取超时时间
     */
    private static volatile int HTTP_READ_TIMEOUT = 5000;

    public static int getHttpConnectTimeout() {
        return HTTP_CONNECT_TIMEOUT;
    }

    public static void setHttpConnectTimeout(int httpConnectTimeout) {
        HTTP_CONNECT_TIMEOUT = httpConnectTimeout;
    }

    public static int getHttpReadTimeout() {
        return HTTP_READ_TIMEOUT;
    }

    public static void setHttpReadTimeout(int httpReadTimeout) {
        HTTP_READ_TIMEOUT = httpReadTimeout;
    }

}
