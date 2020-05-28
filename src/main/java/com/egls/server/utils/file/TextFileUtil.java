package com.egls.server.utils.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.egls.server.utils.CharsetUtil;
import com.egls.server.utils.StringUtil;
import com.egls.server.utils.compressor.Compressor;
import com.egls.server.utils.compressor.CompressorFactory;

/**
 * 提供一些文本文件的工具方法
 * <pre>
 *    使用压缩器的时候,注意不同的压缩算法并不一定可以追加.例如{@code Snappy}就不可以追加.
 *    使用{@code Deflate}和{@code Inflate}来进行的压缩数据,可以追加,但是解压缩时仅会返回第一次压缩的字节的结果.
 *    具体请参看压缩器不同实现{@link Compressor}
 * </pre>
 *
 * @author mayer - [Created on 2018-08-09 20:41]
 */
public final class TextFileUtil {

    public static String read(final String filePath, final boolean useGzip) throws Exception {
        return read(filePath, CharsetUtil.defaultCharset(), useGzip);
    }

    public static String read(final File file, final boolean useGzip) throws Exception {
        return read(file, CharsetUtil.defaultCharset(), useGzip);
    }

    public static String read(final String filePath, final Charset charset, final boolean useGzip) throws Exception {
        Objects.requireNonNull(filePath);
        return read(new File(filePath), charset, useGzip);
    }

    public static String read(final File file, final Charset charset, final boolean useGzip) throws Exception {
        return read(file, charset, useGzip ? CompressorFactory.newGzipCompressor() : null);
    }

    public static List<String> readByLine(final String filePath, final boolean useGzip) throws Exception {
        return readByLine(filePath, CharsetUtil.defaultCharset(), useGzip);
    }

    public static List<String> readByLine(final File file, final boolean useGzip) throws Exception {
        return readByLine(file, CharsetUtil.defaultCharset(), useGzip);
    }

    public static List<String> readByLine(final String filePath, final Charset charset, final boolean useGzip) throws Exception {
        Objects.requireNonNull(filePath);
        return readByLine(new File(filePath), charset, useGzip);
    }

    public static List<String> readByLine(final File file, final Charset charset, final boolean useGzip) throws Exception {
        return readByLine(file, charset, useGzip ? CompressorFactory.newGzipCompressor() : null);
    }

    public static void write(final String content, final String filePath, final boolean append, final boolean useGzip) throws Exception {
        write(content, filePath, CharsetUtil.defaultCharset(), append, useGzip);
    }

    public static void write(final String content, final File file, final boolean append, final boolean useGzip) throws Exception {
        write(content, file, CharsetUtil.defaultCharset(), append, useGzip);
    }

    public static void write(final String content, final String filePath, final Charset charset, final boolean append, final boolean useGzip) throws Exception {
        Objects.requireNonNull(filePath);
        write(content, new File(filePath), charset, append, useGzip);
    }

    public static void write(final String content, final File file, final Charset charset, final boolean append, final boolean useGzip) throws Exception {
        write(content, file, charset, append, useGzip ? CompressorFactory.newGzipCompressor() : null);
    }

    public static void writeByLine(final List<String> content, final String filePath, final boolean append, final boolean useGzip) throws Exception {
        writeByLine(content, filePath, CharsetUtil.defaultCharset(), append, useGzip);
    }

    public static void writeByLine(final List<String> content, final File file, final boolean append, final boolean useGzip) throws Exception {
        writeByLine(content, file, CharsetUtil.defaultCharset(), append, useGzip);
    }

    public static void writeByLine(final List<String> content, final String filePath, final Charset charset, final boolean append, final boolean useGzip) throws Exception {
        Objects.requireNonNull(filePath);
        writeByLine(content, new File(filePath), charset, append, useGzip);
    }

    public static void writeByLine(final List<String> content, final File file, final Charset charset, final boolean append, final boolean useGzip) throws Exception {
        writeByLine(content, file, charset, append, useGzip ? CompressorFactory.newGzipCompressor() : null);
    }

    private static byte[] prepareReadBytes(final File file, final Compressor compressor) throws Exception {
        Objects.requireNonNull(file);
        byte[] bytes = FileUtil.read(file);
        if (compressor != null) {
            bytes = compressor.decompress(bytes);
        }
        return bytes;
    }

    private static byte[] prepareWriteBytes(final String content, final Charset charset, final Compressor compressor) throws Exception {
        Charset useCharset = CharsetUtil.nullToDefaultCharset(charset);
        byte[] bytes = content.getBytes(useCharset);
        if (compressor != null) {
            bytes = compressor.compress(bytes);
        }
        return bytes;
    }

    private static void writeToFile(final File file, final boolean append, final byte[] bytes) throws Exception {
        Objects.requireNonNull(file);
        FileUtil.createFileOnNoExists(file);
        FileUtil.write(file, bytes, append);
    }

    public static String read(final File file, final Charset charset, final Compressor compressor) throws Exception {
        Charset useCharset = CharsetUtil.nullToDefaultCharset(charset);
        return new String(prepareReadBytes(file, compressor), useCharset);
    }

    public static List<String> readByLine(final File file, final Charset charset, final Compressor compressor) throws Exception {
        List<String> result = new ArrayList<>();
        byte[] bytes = prepareReadBytes(file, compressor);
        Charset useCharset = CharsetUtil.nullToDefaultCharset(charset);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), useCharset))) {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                result.add(line);
            }
            return result;
        }
    }

    public static void write(final String content, final File file, final Charset charset, final boolean append, final Compressor compressor) throws Exception {
        Objects.requireNonNull(content);
        byte[] bytes = prepareWriteBytes(content, charset, compressor);
        writeToFile(file, append, bytes);
    }

    public static void writeByLine(final List<String> content, final File file, final Charset charset, final boolean append, final Compressor compressor) throws Exception {
        Objects.requireNonNull(content);
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : content) {
            stringBuilder.append(string).append(StringUtil.getLineSeparator());
        }
        byte[] bytes = prepareWriteBytes(stringBuilder.toString(), charset, compressor);
        writeToFile(file, append, bytes);
    }

}
