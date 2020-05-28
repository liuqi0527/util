package com.egls.server.utils.compressor;

import com.egls.server.utils.CharsetUtil;
import com.egls.server.utils.NumberUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author mayer - [Created on 2018-09-03 17:11]
 */
public class TestCompressor {

    private static final String string = "Compaction algorithm (compaction algorithm) is an algorithm of exponential data compression, also known as signal coding in the field of electronics and communication, including compression and reduction (or decoding and encoding) two steps.\n" +
            "\n" +
            "Due to the large data volume of multimedia signal, compression is needed. At the same time, due to the existence of redundancy in multimedia data, it can be compressed.";

    private void compress(Compressor compressor) throws Exception {
        long millis = System.currentTimeMillis();
        double beforeLength = 0;
        double afterLength = 0;
        for (int i = 0; i < 100; i++) {
            String str = string + i;
            byte[] before = str.getBytes(CharsetUtil.defaultCharset());
            byte[] after = compressor.compress(before);
            beforeLength += before.length;
            afterLength += after.length;
            assertEquals(str, new String(compressor.decompress(after), CharsetUtil.defaultCharset()));
        }
        double ratio = NumberUtil.convertPrecision(afterLength / beforeLength * 100D, 2);
        System.out.println(compressor.getClass().getSimpleName() + ", Time(ms):" + (System.currentTimeMillis() - millis) + ", ratio:" + ratio + "%");
    }

    @Test
    public void testGzipCompressor() throws Exception {
        compress(CompressorFactory.newGzipCompressor());
    }

    @Test
    public void testNonCompressor() throws Exception {
        compress(CompressorFactory.newNonCompressor());
    }

}
