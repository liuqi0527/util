package com.egls.server.utils.structure;

import java.util.Objects;

import com.egls.server.utils.array.ArraySearchUtil;
import com.egls.server.utils.array.ArrayUtil;
import com.egls.server.utils.math.MathUtil;

/**
 * <pre>
 *     一个字节缓冲类,主要是减少内存拷贝的问题.
 *     自动变长的缓冲类,内部始终会处于一个合适的大小.
 *
 *     当write的内容超过当前可存储的长度,会自动增长长度,以便能够存储.
 *     当空闲时,也会自动缩短,但最短时不会短于给定的初始长度.
 *
 *     包含几个peek方法,方便在于窥视一下之后在进行读取操作.适用于分片缓冲.
 *     当内部数据长度不符合要求读取长度时,会抛出异常{@link IllegalStateException}
 *
 *     本类不是线程安全的.
 * </pre>
 *
 * @author mayer - [Created on 2018-08-21 13:40]
 */
public final class ByteBuffer {

    /**
     * 对齐大小1kB
     */
    private static final int UNIT = 0x400;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


    private byte[] buf;

    private final int initialSize;

    /**
     * inclusive
     */
    private int position = 0;

    /**
     * exclusive
     */
    private int limit = 0;

    public ByteBuffer() {
        this(UNIT);
    }

    public ByteBuffer(final int initialSize) {
        if (initialSize <= 0) {
            throw new IllegalArgumentException("initial size must be positive." + initialSize);
        }
        this.initialSize = formatSize(initialSize);
        this.buf = new byte[this.initialSize];
    }

    public ByteBuffer(final byte[] initialData) {
        Objects.requireNonNull(initialData);
        this.initialSize = formatSize(initialData.length);
        this.buf = new byte[this.initialSize];
        write(initialData);
    }

    private int formatSize(final int size) {
        return UNIT * MathUtil.divideAndCeil(size, UNIT);
    }

    public final byte[] toByteArray() {
        return ArrayUtil.copyOfRange(buf, position, limit);
    }

    public final void reset() {
        buf = new byte[initialSize];
        position = limit = 0;
    }

    public final int size() {
        return limit - position;
    }

    public final int bufSize() {
        return buf.length;
    }

    public final boolean hasRemaining() {
        return size() > 0;
    }

    public final byte peekByte() {
        checkData(1);
        return buf[position];
    }

    public final short peekShort() {
        checkData(2);
        int value = (buf[position] & 0xFF) << 8;
        value |= (buf[position + 1] & 0xFF);
        return (short) value;
    }

    public final int peekInt() {
        checkData(4);
        int value = (buf[position] & 0xFF) << 24;
        value |= (buf[position + 1] & 0xFF) << 16;
        value |= (buf[position + 2] & 0xFF) << 8;
        value |= (buf[position + 3] & 0xFF);
        return value;
    }

    public final long peekLong() {
        checkData(8);
        long value = (buf[position] & 0xFFL) << 56;
        value |= (buf[position + 1] & 0xFFL) << 48;
        value |= (buf[position + 2] & 0xFFL) << 40;
        value |= (buf[position + 3] & 0xFFL) << 32;
        value |= (buf[position + 4] & 0xFFL) << 24;
        value |= (buf[position + 5] & 0xFFL) << 16;
        value |= (buf[position + 6] & 0xFFL) << 8;
        value |= (buf[position + 7] & 0xFFL);
        return value;
    }

    public final byte readByte() {
        byte b = peekByte();
        position += 1;
        return b;
    }

    public final short readShort() {
        short s = peekShort();
        position += 2;
        return s;
    }

    public final int readInt() {
        int i = peekInt();
        position += 4;
        return i;
    }

    public final long readLong() {
        long l = peekLong();
        position += 8;
        return l;
    }

    public final byte[] read(final int length) {
        checkData(length);
        byte[] result = new byte[length];
        System.arraycopy(buf, position, result, 0, length);
        position += length;
        return result;
    }

    public final byte[] readFully() {
        return read(size());
    }

    public final void writeByte(final byte val) {
        ensureCapacity(1);
        buf[limit++] = val;
    }

    public final void writeShort(final short val) {
        ensureCapacity(2);
        buf[limit++] = (byte) (val >>> 8);
        buf[limit++] = (byte) (val);
    }

    public final void writeInt(final int val) {
        ensureCapacity(4);
        buf[limit++] = (byte) (val >>> 24);
        buf[limit++] = (byte) (val >>> 16);
        buf[limit++] = (byte) (val >>> 8);
        buf[limit++] = (byte) (val);
    }

    public final void writeLong(final long val) {
        ensureCapacity(8);
        buf[limit++] = (byte) (val >>> 56);
        buf[limit++] = (byte) (val >>> 48);
        buf[limit++] = (byte) (val >>> 40);
        buf[limit++] = (byte) (val >>> 32);
        buf[limit++] = (byte) (val >>> 24);
        buf[limit++] = (byte) (val >>> 16);
        buf[limit++] = (byte) (val >>> 8);
        buf[limit++] = (byte) (val);
    }

    public final void write(final byte[] b) {
        write(b, 0, b.length);
    }

    public final void write(final byte[] b, final int off, final int len) {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        ensureCapacity(len);
        System.arraycopy(b, off, buf, limit, len);
        limit += len;
    }

    public int indexOf(byte[] key) {
        return ArraySearchUtil.indexOf(buf, position, limit, key);
    }

    private void checkData(final int length) {
        if (size() < length) {
            throw new IllegalStateException("data length is less than the required length!");
        }
    }

    private void ensureCapacity(final int incremental) {
        if (limit + incremental > bufSize()) {
            if (bufSize() - size() >= incremental) {
                int needLength = incremental + size();
                int formatNeedLength = formatSize(needLength);
                recycle(formatNeedLength);
            } else {
                int needLength = incremental - (bufSize() - limit);
                int formatNeedLength = formatSize(needLength);
                int newLength = bufSize() + formatNeedLength;
                if (newLength > MAX_ARRAY_SIZE) {
                    throw new IllegalStateException("newLength too large! newLength : " + newLength);
                }
                byte[] temp = new byte[newLength];
                System.arraycopy(buf, position, temp, 0, size());
                buf = temp;
                limit -= position;
                position = 0;
            }
        }
    }

    private void recycle(final int recycleLengthLimit) {
        int length = Math.max(initialSize, recycleLengthLimit);
        if (bufSize() > length) {
            byte[] temp = new byte[length];
            System.arraycopy(buf, position, temp, 0, size());
            buf = temp;
            limit -= position;
            position = 0;
        } else if (position != 0) {
            System.arraycopy(buf, position, buf, 0, size());
            limit -= position;
            position = 0;
        }
    }

    @Override
    public final String toString() {
        return getClass().getName()
                + "["
                + "initialSize=" + initialSize
                + ",position=" + position
                + ",limit=" + limit
                + ",size=" + size()
                + "]";
    }
}
