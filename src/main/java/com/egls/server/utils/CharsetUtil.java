package com.egls.server.utils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.List;

import com.egls.server.utils.file.FileUtil;
import com.egls.server.utils.math.MathUtil;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供字符集或者字符编码的一些工具方法
 *
 * @author mayer - [Created on 2018-08-09 19:57]
 */
public final class CharsetUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharsetUtil.class);

    //@formatter:off

    public static final String ENCODING_UTF_8           = "UTF-8";
    public static final String ENCODING_UTF_16          = "UTF-16";
    public static final String ENCODING_UNICODE         = "Unicode";
    public static final String ENCODING_UTF_16BE        = "UTF-16BE";
    public static final String ENCODING_UTF_16LE        = "UTF-16LE";
    public static final String ENCODING_GBK             = "GBK";
    public static final String ENCODING_GB2312          = "GB2312";
    public static final String ENCODING_ASCII           = "ASCII";
    public static final String ENCODING_ISO_8859_1      = "ISO-8859-1";

    public static final Charset CHARSET_UTF_8           = Charset.forName(ENCODING_UTF_8);
    public static final Charset CHARSET_UTF_16          = Charset.forName(ENCODING_UTF_16);
    public static final Charset CHARSET_UNICODE         = Charset.forName(ENCODING_UNICODE);
    public static final Charset CHARSET_UTF_16BE        = Charset.forName(ENCODING_UTF_16BE);
    public static final Charset CHARSET_UTF_16LE        = Charset.forName(ENCODING_UTF_16LE);
    public static final Charset CHARSET_GBK             = Charset.forName(ENCODING_GBK);
    public static final Charset CHARSET_GB2312          = Charset.forName(ENCODING_GB2312);
    public static final Charset CHARSET_ASCII           = Charset.forName(ENCODING_ASCII);
    public static final Charset CHARSET_ISO_8859_1      = Charset.forName(ENCODING_ISO_8859_1);

    //@formatter:on

    private static final List<String> SUPPORTED_ENCODINGS = Lists.newArrayList(
            StringUtils.lowerCase(ENCODING_UTF_8),
            StringUtils.lowerCase(ENCODING_UTF_16),
            StringUtils.lowerCase(ENCODING_UNICODE),
            StringUtils.lowerCase(ENCODING_UTF_16BE),
            StringUtils.lowerCase(ENCODING_UTF_16LE),
            StringUtils.lowerCase(ENCODING_GBK),
            StringUtils.lowerCase(ENCODING_GB2312),
            StringUtils.lowerCase(ENCODING_ASCII),
            StringUtils.lowerCase(ENCODING_ISO_8859_1)
    );

    private static final List<Charset> SUPPORTED_CHARSETS = Lists.newArrayList(
            CHARSET_UTF_8,
            CHARSET_UTF_16,
            CHARSET_UNICODE,
            CHARSET_UTF_16BE,
            CHARSET_UTF_16LE,
            CHARSET_GBK,
            CHARSET_GB2312,
            CHARSET_ASCII,
            CHARSET_ISO_8859_1
    );

    public static Charset defaultCharset() {
        return CHARSET_UTF_8;
    }

    public static String defaultEncoding() {
        return ENCODING_UTF_8;
    }

    public static Charset nullToDefaultCharset(final Charset charset) {
        return charset == null ? defaultCharset() : charset;
    }

    public static boolean isSupportedEncoding(final String encoding) {
        return SUPPORTED_ENCODINGS.contains(StringUtils.lowerCase(encoding));
    }

    /**
     * Charset底层是有缓存的
     */
    public static boolean isSupportedCharset(final Charset charset) {
        return SUPPORTED_CHARSETS.contains(charset);
    }

    public static boolean canDecode(final File file, final String encoding) throws IOException {
        return canDecode(FileUtil.read(file), encoding);
    }

    public static boolean canDecode(final byte[] fileData, final String encoding) {
        return canDecode(fileData, Charset.forName(encoding));
    }

    /**
     * 是否可以使用某种字符集解码文件内容
     */
    public static boolean canDecode(final byte[] fileData, final Charset charset) {
        CharsetDecoder charsetDecoder = charset.newDecoder();
        CoderResult coderResult = charsetDecoder.decode(
                ByteBuffer.wrap(fileData),
                CharBuffer.allocate(MathUtil.divideAndCeil(fileData.length, 2)),
                true);
        return !coderResult.isError();
    }

    /**
     * 检测一个文本文件的字符集,字符编码
     */
    public static Charset detectCharset(final File file) throws IOException {
        return detectCharset(FileUtil.read(file));
    }

    /**
     * 检测一个文本文件内容的字符集,字符编码
     */
    public static Charset detectCharset(final byte[] fileData) {
        for (Charset charset : SUPPORTED_CHARSETS) {
            try {
                CharsetDecoder charsetDecoder = charset.newDecoder();
                CoderResult coderResult = charsetDecoder.decode(
                        ByteBuffer.wrap(fileData),
                        CharBuffer.allocate(MathUtil.divideAndCeil(fileData.length, 2)),
                        true);
                if (!coderResult.isError()) {
                    return charset;
                }
            } catch (Exception exception) {
                LOGGER.error("detect file charset error", exception);
            }
        }
        return null;
    }


}
