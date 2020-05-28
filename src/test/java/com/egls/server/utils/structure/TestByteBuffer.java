package com.egls.server.utils.structure;

import com.egls.server.utils.CharsetUtil;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author mayer - [Created on 2018-09-03 20:25]
 */
public class TestByteBuffer {

    @Test
    public void test1() {
        final String string = "Hello,World!你好,世界!";
        final byte[] bytes = string.getBytes(CharsetUtil.defaultCharset());
        ByteBuffer byteBuffer = new ByteBuffer(bytes);
        assertEquals(byteBuffer.size(), bytes.length);
        assertEquals(string, new String(byteBuffer.read(bytes.length), CharsetUtil.defaultCharset()));

        for (int i = 0; i < 1000; i++) {
            byteBuffer.write(bytes);
            assertEquals(byteBuffer.size(), (bytes.length * (i + 1)));
        }
        assertEquals(0, byteBuffer.bufSize() % 1024);

        for (int i = 1000; i > 0; i--) {
            byte[] tmp = byteBuffer.read(bytes.length);
            assertEquals(tmp.length, bytes.length);
            assertEquals(byteBuffer.size(), (bytes.length * (i - 1)));
            assertEquals(string, new String(tmp, CharsetUtil.defaultCharset()));
        }

        for (int i = 0; i < 1000; i++) {
            byteBuffer.write(bytes);
            assertEquals(byteBuffer.size(), (bytes.length * (i + 1)));
        }

        assertEquals(byteBuffer.size(), bytes.length * 1000);
        assertEquals(0, byteBuffer.bufSize() % 1024);
        byteBuffer.reset();
        assertEquals(0, byteBuffer.size());
        Assert.assertFalse(byteBuffer.hasRemaining());
        assertEquals(0, byteBuffer.bufSize() % 1024);

        for (int i = 0; i < 1000; i++) {
            byteBuffer.write(bytes);
            assertEquals(byteBuffer.size(), (bytes.length * (i + 1)));
        }
        assertEquals(byteBuffer.size(), bytes.length * 1000);
        assertEquals(0, byteBuffer.bufSize() % 1024);

        for (int i = 1000; i > 0; i--) {
            byte[] tmp = byteBuffer.read(bytes.length);
            assertEquals(string, new String(tmp, CharsetUtil.defaultCharset()));
            assertEquals(byteBuffer.size(), (bytes.length * (i - 1)));
            assertEquals(tmp.length, bytes.length);
        }

        for (int i = 0; i < 1000; i++) {
            byteBuffer.write(bytes);
            byteBuffer.read(bytes.length);
            assertEquals(0, byteBuffer.size());
        }

        byteBuffer.writeByte((byte) 1);
        byte b = byteBuffer.readByte();
        assertEquals(b, 1);
        assertEquals(0, byteBuffer.size());
        assertEquals(0, byteBuffer.bufSize() % 1024);

        byteBuffer.writeShort((short) 11);
        short s = byteBuffer.readShort();
        assertEquals(s, 11);
        assertEquals(0, byteBuffer.size());
        assertEquals(0, byteBuffer.bufSize() % 1024);

        byteBuffer.writeInt(111);
        int i = byteBuffer.readInt();
        assertEquals(i, 111);
        assertEquals(0, byteBuffer.size());
        assertEquals(0, byteBuffer.bufSize() % 1024);

        byteBuffer.writeLong(1111);
        long l = byteBuffer.readLong();
        assertEquals(l, 1111);
        assertEquals(0, byteBuffer.size());
        assertEquals(0, byteBuffer.bufSize() % 1024);
    }

    @Test
    public void test2() {
        ByteBuffer buffer = new ByteBuffer(128);
        for (int i = 0; i < 120; i++) {
            buffer.writeByte((byte) 1);
            assertEquals(buffer.size(), (i + 1));
            assertEquals(1024, buffer.bufSize());
        }
        for (int i = 120; i > 0; i--) {
            buffer.readByte();
            assertEquals(buffer.size(), (i - 1));
            assertEquals(1024, buffer.bufSize());
        }
        for (int i = 0; i < 100; i++) {
            buffer.writeByte((byte) 1);
            assertEquals(buffer.size(), (i + 1));
            assertEquals(1024, buffer.bufSize());
        }
    }

    @Test
    public void test3() {
        ByteBuffer buffer = new ByteBuffer(16);
        byte[] array = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        buffer.write(array);
        buffer.write(array);
        buffer.writeByte((byte) 1);
        Assert.assertEquals(buffer.size(), (array.length * 2 + 1));
        Assert.assertEquals(1024, buffer.bufSize());

        buffer.read(16);
        buffer.write(array);
        Assert.assertEquals(buffer.size(), (array.length + 1));
        Assert.assertEquals(1024, buffer.bufSize());
    }

    @Test
    public void test4() {
        ByteBuffer byteBuffer = new ByteBuffer();
        byte[] bytes = new byte[8192];

        byteBuffer.write(bytes);
        for (int i = 0; i < 6376; i++) {
            byteBuffer.readByte();
        }
        byteBuffer.write(bytes);
        for (int i = 0; i < 2308; i++) {
            byteBuffer.readByte();
        }
        byteBuffer.write(bytes);
    }

}
