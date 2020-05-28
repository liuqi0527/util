package com.egls.server.utils.compressor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 使用GZIPOutputStream和GZIPInputStream来进行.
 * 是一种通用的压缩数据格式,用来与其他第三方程序对接数据格式.
 * 可以将两次压缩后的字节合并在一起解压缩.
 *
 * @author mayer - [Created on 2018-08-09 20:57]
 */
public final class GzipCompressor implements Compressor {

    /**
     * 缓冲区的长度16K bytes
     */
    private static final int BUFFER_LENGTH = 0x4000;

    GzipCompressor() {
    }

    /**
     * 通过设置GZIPOutputStream内部的字节的大小,使得尽量减少数组的拷贝提升效率.
     * 不使用BufferedOutputStream是因为这样设置之后BufferedOutputStream没有缓冲的作用了
     *
     * @param bytes 需要被压缩的字节数组
     * @return 压缩后的字节数组
     * @throws IOException I/O异常会被抛出,如果在调用java.io包的API出现异常的情况下
     */
    @Override
    public final byte[] compress(final byte[] bytes) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(calcBytesLength(bytes.length))) {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream, BUFFER_LENGTH);
            gzipOutputStream.write(bytes);
            gzipOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * 通过设置GZIPInputStream内部的字节的大小,使得尽量减少数组的拷贝提升效率.
     * 不使用BufferedInputStream是因为这样设置之后BufferedInputStream没有缓冲的作用了
     *
     * @param bytes 需要被解压的字节数组
     * @return 解压后的字节数组
     * @throws IOException I/O异常会被抛出,如果在调用java.io包的API出现异常的情况下
     */
    @Override
    public final byte[] decompress(final byte[] bytes) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream, BUFFER_LENGTH);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(calcBytesLength(bytes.length))) {
            int readLength;
            byte[] buffer = new byte[BUFFER_LENGTH];
            while ((readLength = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, readLength);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * 根据给定的字节长度,产生缓冲区的整数倍的字节长度.
     *
     * @param bytesLength 字节长度
     * @return 缓冲区的整数倍的长度
     */
    private int calcBytesLength(final int bytesLength) {
        return GzipCompressor.BUFFER_LENGTH + (bytesLength / GzipCompressor.BUFFER_LENGTH) * GzipCompressor.BUFFER_LENGTH;
    }
}
