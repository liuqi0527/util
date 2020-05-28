package com.egls.server.utils.compressor;

/**
 * 抽象压缩算法的接口
 * <p>
 * 不同的场景使用不同的实现
 * <p>
 * 对于无流量压力的网络通信(内网),建议使用无压缩方式,提升速率.
 * 对于有流量压力的网络通信(外网),使用不同的压缩方式,达到一个平衡.
 * <p>
 * 用于文件压缩,在文件大小和耗费时间上选择一个平衡.
 *
 * @author mayer - [Created on 2018-08-09 20:42]
 */
public interface Compressor {

    /**
     * 压缩方法,实现不同的压缩算法
     *
     * @param bytes 压缩前的字节
     * @return 压缩后的字节
     * @throws Exception 产生一些压缩异常
     */
    byte[] compress(final byte[] bytes) throws Exception;

    /**
     * 解压方法,实现不同的解压方法
     *
     * @param bytes 被压缩过的字节
     * @return 解压后的字节
     * @throws Exception 产生一些解压缩异常
     */
    byte[] decompress(final byte[] bytes) throws Exception;

}
