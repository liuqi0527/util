package com.egls.server.utils.net.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.*;

import com.egls.server.utils.CharsetUtil;
import com.egls.server.utils.CollectionUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 提供一些Http的工具方法
 *
 * @author mayer - [Created on 2018-08-21 23:11]
 */
public final class HttpUtil {

    private static final char URL_SEPARATOR_CHAR = '?';

    private static final String URL_SEPARATOR = "" + URL_SEPARATOR_CHAR;

    private static final char URL_PARAMETER_SEPARATOR_CHAR = '&';

    private static final String URL_PARAMETER_SEPARATOR = "" + URL_PARAMETER_SEPARATOR_CHAR;

    private static final char URL_PARAMETER_JOINT_CHAR = '=';

    private static final String URL_PARAMETER_JOINT = "" + URL_PARAMETER_JOINT_CHAR;

    private static final SSLSocketFactory TRUST_ANY_SSL_SOCKET_FACTORY;

    private static final HostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER = (s, sslSession) -> true;

    static {
        TrustManager[] trustAny = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAny, null);
            TRUST_ANY_SSL_SOCKET_FACTORY = sslContext.getSocketFactory();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String getBaseUrl(final String url) {
        final String[] requestUrlParts = StringUtils.splitByWholeSeparator(url, URL_SEPARATOR, 2);
        return ArrayUtils.isEmpty(requestUrlParts) ? StringUtils.EMPTY : requestUrlParts[0];
    }

    /**
     * <pre>
     *     e.g.:
     *     List<Pair<String, String>> urlParams = new ArrayList<>();
     *     urlParams.add(Pair.of("key", "value"));
     *     String url = buildUrl("http://www.baidu.com", urlParams);
     * </pre>
     */
    public static <T extends Map.Entry<String, String>> String buildUrl(final String baseUrl, final Collection<T> urlParams) {
        if (CollectionUtil.isEmpty(urlParams)) {
            return baseUrl;
        }

        final String url;
        if (StringUtils.lastIndexOf(baseUrl, URL_SEPARATOR_CHAR) < 0) {
            //url中不包含'?',末尾增加'?'
            url = baseUrl + URL_SEPARATOR_CHAR;
        } else {
            //url中包含'?',判断结尾
            if (StringUtils.endsWith(baseUrl, URL_SEPARATOR)) {
                url = baseUrl;
            } else if (StringUtils.endsWith(baseUrl, URL_PARAMETER_SEPARATOR)) {
                url = baseUrl;
            } else {
                url = baseUrl + URL_PARAMETER_SEPARATOR_CHAR;
            }
        }

        final List<String> kvStrings = urlParams.stream()
                .map(param -> param.getKey() + URL_PARAMETER_JOINT + param.getValue())
                .collect(Collectors.toList());

        return url + StringUtils.join(kvStrings, URL_PARAMETER_SEPARATOR_CHAR);
    }

    public static String buildUrl(final String baseUrl, final Map<String, String> urlParams) {
        return buildUrl(baseUrl, urlParams.entrySet());
    }

    public static String buildUrl(final String baseUrl, final String[][] urlParams) {
        return buildUrl(baseUrl, Arrays.stream(urlParams).map(array -> Pair.of(array[0], array[1])).collect(Collectors.toList()));
    }

    // common method ------

    private static void disconnect(final HttpURLConnection httpURLConnection) {
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    private static void adjustCommonSetting(final HttpURLConnection httpURLConnection, final Map<String, String> headers) {
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setReadTimeout(HttpProperties.getHttpConnectTimeout());
        httpURLConnection.setConnectTimeout(HttpProperties.getHttpReadTimeout());

        //默认设置,如果headers里面指定,会进行覆盖掉默认
        httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");

        if (CollectionUtil.isNotEmpty(headers)) {
            headers.forEach(httpURLConnection::setRequestProperty);
        }
    }

    // http method ------------------------

    public static SimpleHttpResponse doHttpGet(final String url, final Map<String, String> headers) throws IOException {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            adjustCommonSetting(httpURLConnection, headers);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            return new SimpleHttpResponse(httpURLConnection);
        } finally {
            disconnect(httpURLConnection);
        }
    }

    public static byte[] doHttpGetBytes(final String url, final Map<String, String> headers) throws IOException {
        return doHttpGet(url, headers).getBytes();
    }

    public static byte[] doHttpGetBytes(final String url) throws IOException {
        return doHttpGetBytes(url, null);
    }

    /**
     * @param responseDefaultCharset 当服务器没有返回charset时使用的charset
     */
    public static String doHttpGetText(final String url, final String responseDefaultCharset, final Map<String, String> headers) throws IOException {
        return doHttpGet(url, headers).getString(responseDefaultCharset);
    }

    public static String doHttpGetText(final String url, final String responseDefaultCharset) throws IOException {
        return doHttpGetText(url, responseDefaultCharset, null);
    }

    public static String doHttpGetText(final String url) throws IOException {
        return doHttpGetText(url, CharsetUtil.defaultEncoding());
    }

    public static SimpleHttpResponse doHttpPost(final String url, final byte[] bytes, final Map<String, String> headers) throws IOException {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            adjustCommonSetting(httpURLConnection, headers);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();
            if (ArrayUtils.isNotEmpty(bytes)) {
                try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                    outputStream.write(bytes);
                    outputStream.flush();
                }
            }
            return new SimpleHttpResponse(httpURLConnection);
        } finally {
            disconnect(httpURLConnection);
        }
    }

    public static byte[] doHttpPostBytes(final String url, final byte[] bytes, final Map<String, String> headers) throws IOException {
        return doHttpPost(url, bytes, headers).getBytes();
    }

    public static byte[] doHttpPostBytes(final String url, final byte[] bytes) throws IOException {
        return doHttpPostBytes(url, bytes, null);
    }

    public static String doHttpPostText(final String url, final byte[] bytes, final String responseDefaultCharset, final Map<String, String> headers) throws IOException {
        return doHttpPost(url, bytes, headers).getString(responseDefaultCharset);
    }

    public static String doHttpPostText(final String url, final byte[] bytes, final String responseDefaultCharset) throws IOException {
        return doHttpPostText(url, bytes, responseDefaultCharset, null);
    }

    public static String doHttpPostText(final String url, final byte[] bytes) throws IOException {
        return doHttpPostText(url, bytes, CharsetUtil.defaultEncoding());
    }

    // https method ------------------------

    public static SimpleHttpResponse doHttpsGet(final String url, final Map<String, String> headers) throws IOException {
        HttpsURLConnection httpsURLConnection = null;
        try {
            httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
            httpsURLConnection.setHostnameVerifier(TRUST_ANY_HOSTNAME_VERIFIER);
            httpsURLConnection.setSSLSocketFactory(TRUST_ANY_SSL_SOCKET_FACTORY);
            adjustCommonSetting(httpsURLConnection, headers);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();
            return new SimpleHttpResponse(httpsURLConnection);
        } finally {
            disconnect(httpsURLConnection);
        }
    }

    public static byte[] doHttpsGetBytes(final String url, final Map<String, String> headers) throws IOException {
        return doHttpsGet(url, headers).getBytes();
    }

    public static byte[] doHttpsGetBytes(final String url) throws IOException {
        return doHttpsGetBytes(url, null);
    }

    /**
     * @param responseDefaultCharset 当服务器没有返回charset时使用的charset
     */
    public static String doHttpsGetText(final String url, final String responseDefaultCharset, final Map<String, String> headers) throws IOException {
        return doHttpsGet(url, headers).getString(responseDefaultCharset);
    }

    public static String doHttpsGetText(final String url, final String responseDefaultCharset) throws IOException {
        return doHttpsGetText(url, responseDefaultCharset, null);
    }

    public static String doHttpsGetText(final String url) throws IOException {
        return doHttpsGetText(url, CharsetUtil.defaultEncoding());
    }

    public static SimpleHttpResponse doHttpsPost(final String url, final byte[] bytes, final Map<String, String> headers) throws IOException {
        HttpsURLConnection httpsURLConnection = null;
        try {
            httpsURLConnection = (HttpsURLConnection) new URL(url).openConnection();
            httpsURLConnection.setHostnameVerifier(TRUST_ANY_HOSTNAME_VERIFIER);
            httpsURLConnection.setSSLSocketFactory(TRUST_ANY_SSL_SOCKET_FACTORY);
            adjustCommonSetting(httpsURLConnection, headers);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.connect();
            if (ArrayUtils.isNotEmpty(bytes)) {
                try (OutputStream outputStream = httpsURLConnection.getOutputStream()) {
                    outputStream.write(bytes);
                    outputStream.flush();
                }
            }
            return new SimpleHttpResponse(httpsURLConnection);
        } finally {
            disconnect(httpsURLConnection);
        }
    }

    public static byte[] doHttpsPostBytes(final String url, final byte[] bytes, final Map<String, String> headers) throws IOException {
        return doHttpsPost(url, bytes, headers).getBytes();
    }

    public static byte[] doHttpsPostBytes(final String url, final byte[] bytes) throws IOException {
        return doHttpsPostBytes(url, bytes, null);
    }

    public static String doHttpsPostText(final String url, final byte[] bytes, final String responseDefaultCharset, final Map<String, String> headers) throws IOException {
        return doHttpsPost(url, bytes, headers).getString(responseDefaultCharset);
    }

    public static String doHttpsPostText(final String url, final byte[] bytes, final String responseDefaultCharset) throws IOException {
        return doHttpsPostText(url, bytes, responseDefaultCharset, null);
    }

    public static String doHttpsPostText(final String url, final byte[] bytes) throws IOException {
        return doHttpsPostText(url, bytes, CharsetUtil.defaultEncoding());
    }

}
