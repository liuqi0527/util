package com.egls.server.utils.compressor;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

/**
 * @author mayer - [Created on 2019-06-18 15:21]
 */
public final class GzipCompressUtil {

    public static String compressToHexString(String string) throws Exception {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        final Compressor compressor = new GzipCompressor();
        final byte[] compressedBytes = compressor.compress(bytes);
        return DatatypeConverter.printHexBinary(compressedBytes);
    }

    public static String decompressHexString(String string) throws Exception {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        final byte[] compressedBytes = DatatypeConverter.parseHexBinary(string);
        final Compressor compressor = new GzipCompressor();
        final byte[] bytes = compressor.decompress(compressedBytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String compressToBase64String(String string) throws Exception {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        final Compressor compressor = new GzipCompressor();
        final byte[] compressedBytes = compressor.compress(bytes);
        return DatatypeConverter.printBase64Binary(compressedBytes);
    }

    public static String decompressBase64String(String string) throws Exception {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        final byte[] compressedBytes = DatatypeConverter.parseBase64Binary(string);
        final Compressor compressor = new GzipCompressor();
        final byte[] bytes = compressor.decompress(compressedBytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
