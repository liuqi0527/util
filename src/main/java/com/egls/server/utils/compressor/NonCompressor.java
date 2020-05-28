package com.egls.server.utils.compressor;

/**
 * 不压缩的
 *
 * @author mayer - [Created on 2018-08-09 21:02]
 */
public final class NonCompressor implements Compressor {

    NonCompressor() {
    }

    @Override
    public final byte[] compress(final byte[] bytes) {
        return bytes;
    }

    @Override
    public final byte[] decompress(final byte[] bytes) {
        return bytes;
    }

}
