package com.egls.server.utils.net.http;

import com.egls.server.utils.CharsetUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 一个简单封装的HTTP返回类.
 *
 * @author mayer - [Created on 2018-08-22 17:46]
 */
public final class SimpleHttpResponse {

    private static final String GZIP_ENCODING = "GZIP";

    private final byte[] bytes;

    private final int responseCode;

    private final Map<String, String> simpleHeaderFields;

    SimpleHttpResponse(final HttpURLConnection httpConn) throws IOException {
        this.responseCode = httpConn.getResponseCode();

        this.simpleHeaderFields = new HashMap<>();
        httpConn.getHeaderFields().entrySet().stream()
                .filter(entry -> entry.getValue().size() == 1)
                .forEach(entry -> simpleHeaderFields.put(entry.getKey(), entry.getValue().get(0)));

        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = httpConn.getInputStream();
            final int available = inputStream.available();
            byteArrayOutputStream = available > 0 ? new ByteArrayOutputStream(available) : new ByteArrayOutputStream();
            if (GZIP_ENCODING.equalsIgnoreCase(httpConn.getContentEncoding())) {
                inputStream = new GZIPInputStream(inputStream);
            }
            IOUtils.copy(inputStream, byteArrayOutputStream);
            this.bytes = byteArrayOutputStream.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public boolean isOk() {
        return HttpURLConnection.HTTP_OK == getResponseCode();
    }

    public String getHeaderField(final String key) {
        return simpleHeaderFields.getOrDefault(StringUtils.lowerCase(key), StringUtils.EMPTY);
    }

    public String getContentEncoding() {
        return getHeaderField("Content-Encoding");
    }

    public String getContentType() {
        return getHeaderField("Content-Type");
    }

    public String getCharset() {
        //Content-Type: text/html; charset=utf-8
        //Content-Type: multipart/form-data; boundary=something
        String[] contentTypeParts = StringUtils.split(getContentType(), ';');
        if (ArrayUtils.isNotEmpty(contentTypeParts)) {
            for (String contentTypePart : contentTypeParts) {
                if (StringUtils.containsIgnoreCase(contentTypePart, "charset=")) {
                    final String[] array = StringUtils.split(contentTypePart, '=');
                    if (array != null && array.length > 1) {
                        return StringUtils.trim(array[1]);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取Response返回的charset,当不存在charset时,使用给定的默认charset
     *
     * @param defaultCharset 给定的默认charset
     * @return charset
     */
    public String defaultCharset(final String defaultCharset) {
        final String charset = getCharset();
        return StringUtils.isBlank(charset) ? defaultCharset : charset;
    }

    /**
     * 将返回的bytes当作字符串读取.给一个当Response的Headers中没有charset时,对bytes使用的charsetName
     *
     * @param defaultCharset 当不存在charset时使用的.
     * @return 字符串内容
     */
    public String getString(final String defaultCharset) {
        return new String(bytes, Charset.forName(defaultCharset(defaultCharset)));
    }

    /**
     * 将返回的数据当作字符串读取.使用默认字符集
     *
     * @return 字符串内容
     */
    public String getString() {
        return getString(CharsetUtil.defaultEncoding());
    }

}
