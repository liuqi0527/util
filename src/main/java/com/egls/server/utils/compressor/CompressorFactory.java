package com.egls.server.utils.compressor;

/**
 * @author mayer - [Created on 2018-08-09 21:04]
 */
public final class CompressorFactory {

    public static Compressor newGzipCompressor() {
        return new GzipCompressor();
    }

    public static Compressor newNonCompressor() {
        return new NonCompressor();
    }

}
