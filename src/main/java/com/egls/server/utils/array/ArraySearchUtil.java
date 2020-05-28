package com.egls.server.utils.array;

import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author mayer - [Created on 2018-08-27 20:59]
 */
@SuppressWarnings("Duplicates")
public final class ArraySearchUtil {

    public static boolean equals(final byte[] array1, final int start1,
                                 final byte[] array2, final int start2,
                                 final int length) {
        if (array1.length - start1 >= length && array2.length - start2 >= length) {
            for (int i = 0; i < length; i++) {
                if (array1[start1 + i] != array2[start2 + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(final boolean[] array1, final int start1,
                                 final boolean[] array2, final int start2,
                                 final int length) {
        if (array1.length - start1 >= length && array2.length - start2 >= length) {
            for (int i = 0; i < length; i++) {
                if (array1[start1 + i] != array2[start2 + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(final char[] array1, final int start1,
                                 final char[] array2, final int start2,
                                 final int length) {
        if (array1.length - start1 >= length && array2.length - start2 >= length) {
            for (int i = 0; i < length; i++) {
                if (array1[start1 + i] != array2[start2 + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(final short[] array1, final int start1,
                                 final short[] array2, final int start2,
                                 final int length) {
        if (array1.length - start1 >= length && array2.length - start2 >= length) {
            for (int i = 0; i < length; i++) {
                if (array1[start1 + i] != array2[start2 + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(final int[] array1, final int start1,
                                 final int[] array2, final int start2,
                                 final int length) {
        if (array1.length - start1 >= length && array2.length - start2 >= length) {
            for (int i = 0; i < length; i++) {
                if (array1[start1 + i] != array2[start2 + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(final long[] array1, final int start1,
                                 final long[] array2, final int start2,
                                 final int length) {
        if (array1.length - start1 >= length && array2.length - start2 >= length) {
            for (int i = 0; i < length; i++) {
                if (array1[start1 + i] != array2[start2 + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(final float[] array1, final int start1,
                                 final float[] array2, final int start2,
                                 final int length) {
        if (array1.length - start1 >= length && array2.length - start2 >= length) {
            for (int i = 0; i < length; i++) {
                if (array1[start1 + i] != array2[start2 + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(final double[] array1, final int start1,
                                 final double[] array2, final int start2,
                                 final int length) {
        if (array1.length - start1 >= length && array2.length - start2 >= length) {
            for (int i = 0; i < length; i++) {
                if (array1[start1 + i] != array2[start2 + i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean equals(final Object[] array1, final int start1,
                                 final Object[] array2, final int start2,
                                 final int length) {
        if (array1.length - start1 >= length && array2.length - start2 >= length) {
            for (int i = 0; i < length; i++) {
                if (!Objects.equals(array1[start1 + i], array2[start2 + i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param array1 to be search data
     * @param begin1 the initial index of the range to be search, inclusive
     * @param end1   the final index of the range to be search, exclusive
     * @param array2 key data
     * @param begin2 the initial index of the range of the key, inclusive
     * @param end2   the final index of the range of the key, exclusive
     * @return the initial index of match part
     */
    public static int indexOf(final byte[] array1, final int begin1, final int end1,
                              final byte[] array2, final int begin2, final int end2) {
        if (begin1 >= end1 || begin2 >= end2) {
            return -1;
        }
        int head1 = begin1 < 0 ? 0 : begin1;
        int head2 = begin2 < 0 ? 0 : begin2;
        int tail1 = end1 > array1.length ? array1.length : end1;
        int tail2 = end2 > array2.length ? array2.length : end2;
        return indexOf(array1, head1, tail1, ArrayUtil.copyOfRange(array2, head2, tail2));
    }

    /**
     * @param src   to be search data
     * @param begin the initial index of the range to be search, inclusive
     * @param end   the final index of the range to be search, exclusive
     * @param key   key
     * @return the initial index of match part
     */
    public static int indexOf(final byte[] src, final int begin, final int end, final byte[] key) {
        if (begin >= end) {
            return -1;
        }
        if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(src)) {
            return -1;
        }
        int stop = end > src.length ? src.length : end;
        for (int srcPos = begin; srcPos < stop; srcPos++) {
            //起始匹配
            if (src[srcPos] == key[0]) {
                int matchCount = 1;
                for (int keyPos = 1, tmpSrcPos = srcPos + keyPos; keyPos < key.length && tmpSrcPos < src.length; keyPos++, tmpSrcPos = srcPos + keyPos) {
                    if (src[tmpSrcPos] != key[keyPos]) {
                        break;
                    }
                    matchCount++;
                }
                if (matchCount == key.length) {
                    return srcPos;
                }
            }
        }
        return -1;
    }

    public static int indexOf(final boolean[] array1, final int begin1, final int end1,
                              final boolean[] array2, final int begin2, final int end2) {
        if (begin1 >= end1 || begin2 >= end2) {
            return -1;
        }
        int head1 = begin1 < 0 ? 0 : begin1;
        int head2 = begin2 < 0 ? 0 : begin2;
        int tail1 = end1 > array1.length ? array1.length : end1;
        int tail2 = end2 > array2.length ? array2.length : end2;
        return indexOf(array1, head1, tail1, ArrayUtil.copyOfRange(array2, head2, tail2));
    }

    public static int indexOf(final boolean[] src, final int begin, final int end, final boolean[] key) {
        if (begin >= end) {
            return -1;
        }
        if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(src)) {
            return -1;
        }
        int stop = end > src.length ? src.length : end;
        for (int srcPos = begin; srcPos < stop; srcPos++) {
            //起始匹配
            if (src[srcPos] == key[0]) {
                int matchCount = 1;
                for (int keyPos = 1, tmpSrcPos = srcPos + keyPos; keyPos < key.length && tmpSrcPos < src.length; keyPos++, tmpSrcPos = srcPos + keyPos) {
                    if (src[tmpSrcPos] != key[keyPos]) {
                        break;
                    }
                    matchCount++;
                }
                if (matchCount == key.length) {
                    return srcPos;
                }
            }
        }
        return -1;
    }

    public static int indexOf(final char[] array1, final int begin1, final int end1,
                              final char[] array2, final int begin2, final int end2) {
        if (begin1 >= end1 || begin2 >= end2) {
            return -1;
        }
        int head1 = begin1 < 0 ? 0 : begin1;
        int head2 = begin2 < 0 ? 0 : begin2;
        int tail1 = end1 > array1.length ? array1.length : end1;
        int tail2 = end2 > array2.length ? array2.length : end2;
        return indexOf(array1, head1, tail1, ArrayUtil.copyOfRange(array2, head2, tail2));
    }

    public static int indexOf(final char[] src, final int begin, final int end, final char[] key) {
        if (begin >= end) {
            return -1;
        }
        if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(src)) {
            return -1;
        }
        int stop = end > src.length ? src.length : end;
        for (int srcPos = begin; srcPos < stop; srcPos++) {
            //起始匹配
            if (src[srcPos] == key[0]) {
                int matchCount = 1;
                for (int keyPos = 1, tmpSrcPos = srcPos + keyPos; keyPos < key.length && tmpSrcPos < src.length; keyPos++, tmpSrcPos = srcPos + keyPos) {
                    if (src[tmpSrcPos] != key[keyPos]) {
                        break;
                    }
                    matchCount++;
                }
                if (matchCount == key.length) {
                    return srcPos;
                }
            }
        }
        return -1;
    }

    public static int indexOf(final short[] array1, final int begin1, final int end1,
                              final short[] array2, final int begin2, final int end2) {
        if (begin1 >= end1 || begin2 >= end2) {
            return -1;
        }
        int head1 = begin1 < 0 ? 0 : begin1;
        int head2 = begin2 < 0 ? 0 : begin2;
        int tail1 = end1 > array1.length ? array1.length : end1;
        int tail2 = end2 > array2.length ? array2.length : end2;
        return indexOf(array1, head1, tail1, ArrayUtil.copyOfRange(array2, head2, tail2));
    }

    public static int indexOf(final short[] src, final int begin, final int end, final short[] key) {
        if (begin >= end) {
            return -1;
        }
        if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(src)) {
            return -1;
        }
        int stop = end > src.length ? src.length : end;
        for (int srcPos = begin; srcPos < stop; srcPos++) {
            //起始匹配
            if (src[srcPos] == key[0]) {
                int matchCount = 1;
                for (int keyPos = 1, tmpSrcPos = srcPos + keyPos; keyPos < key.length && tmpSrcPos < src.length; keyPos++, tmpSrcPos = srcPos + keyPos) {
                    if (src[tmpSrcPos] != key[keyPos]) {
                        break;
                    }
                    matchCount++;
                }
                if (matchCount == key.length) {
                    return srcPos;
                }
            }
        }
        return -1;
    }

    public static int indexOf(final int[] array1, final int begin1, final int end1,
                              final int[] array2, final int begin2, final int end2) {
        if (begin1 >= end1 || begin2 >= end2) {
            return -1;
        }
        int head1 = begin1 < 0 ? 0 : begin1;
        int head2 = begin2 < 0 ? 0 : begin2;
        int tail1 = end1 > array1.length ? array1.length : end1;
        int tail2 = end2 > array2.length ? array2.length : end2;
        return indexOf(array1, head1, tail1, ArrayUtil.copyOfRange(array2, head2, tail2));
    }

    public static int indexOf(final int[] src, final int begin, final int end, final int[] key) {
        if (begin >= end) {
            return -1;
        }
        if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(src)) {
            return -1;
        }
        int stop = end > src.length ? src.length : end;
        for (int srcPos = begin; srcPos < stop; srcPos++) {
            //起始匹配
            if (src[srcPos] == key[0]) {
                int matchCount = 1;
                for (int keyPos = 1, tmpSrcPos = srcPos + keyPos; keyPos < key.length && tmpSrcPos < src.length; keyPos++, tmpSrcPos = srcPos + keyPos) {
                    if (src[tmpSrcPos] != key[keyPos]) {
                        break;
                    }
                    matchCount++;
                }
                if (matchCount == key.length) {
                    return srcPos;
                }
            }
        }
        return -1;
    }

    public static int indexOf(final long[] array1, final int begin1, final int end1,
                              final long[] array2, final int begin2, final int end2) {
        if (begin1 >= end1 || begin2 >= end2) {
            return -1;
        }
        int head1 = begin1 < 0 ? 0 : begin1;
        int head2 = begin2 < 0 ? 0 : begin2;
        int tail1 = end1 > array1.length ? array1.length : end1;
        int tail2 = end2 > array2.length ? array2.length : end2;
        return indexOf(array1, head1, tail1, ArrayUtil.copyOfRange(array2, head2, tail2));
    }

    public static int indexOf(final long[] src, final int begin, final int end, final long[] key) {
        if (begin >= end) {
            return -1;
        }
        if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(src)) {
            return -1;
        }
        int stop = end > src.length ? src.length : end;
        for (int srcPos = begin; srcPos < stop; srcPos++) {
            //起始匹配
            if (src[srcPos] == key[0]) {
                int matchCount = 1;
                for (int keyPos = 1, tmpSrcPos = srcPos + keyPos; keyPos < key.length && tmpSrcPos < src.length; keyPos++, tmpSrcPos = srcPos + keyPos) {
                    if (src[tmpSrcPos] != key[keyPos]) {
                        break;
                    }
                    matchCount++;
                }
                if (matchCount == key.length) {
                    return srcPos;
                }
            }
        }
        return -1;
    }

    public static int indexOf(final float[] array1, final int begin1, final int end1,
                              final float[] array2, final int begin2, final int end2) {
        if (begin1 >= end1 || begin2 >= end2) {
            return -1;
        }
        int head1 = begin1 < 0 ? 0 : begin1;
        int head2 = begin2 < 0 ? 0 : begin2;
        int tail1 = end1 > array1.length ? array1.length : end1;
        int tail2 = end2 > array2.length ? array2.length : end2;
        return indexOf(array1, head1, tail1, ArrayUtil.copyOfRange(array2, head2, tail2));
    }

    public static int indexOf(final float[] src, final int begin, final int end, final float[] key) {
        if (begin >= end) {
            return -1;
        }
        if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(src)) {
            return -1;
        }
        int stop = end > src.length ? src.length : end;
        for (int srcPos = begin; srcPos < stop; srcPos++) {
            //起始匹配
            if (src[srcPos] == key[0]) {
                int matchCount = 1;
                for (int keyPos = 1, tmpSrcPos = srcPos + keyPos; keyPos < key.length && tmpSrcPos < src.length; keyPos++, tmpSrcPos = srcPos + keyPos) {
                    if (src[tmpSrcPos] != key[keyPos]) {
                        break;
                    }
                    matchCount++;
                }
                if (matchCount == key.length) {
                    return srcPos;
                }
            }
        }
        return -1;
    }

    public static int indexOf(final double[] array1, final int begin1, final int end1,
                              final double[] array2, final int begin2, final int end2) {
        if (begin1 >= end1 || begin2 >= end2) {
            return -1;
        }
        int head1 = begin1 < 0 ? 0 : begin1;
        int head2 = begin2 < 0 ? 0 : begin2;
        int tail1 = end1 > array1.length ? array1.length : end1;
        int tail2 = end2 > array2.length ? array2.length : end2;
        return indexOf(array1, head1, tail1, ArrayUtil.copyOfRange(array2, head2, tail2));
    }

    public static int indexOf(final double[] src, final int begin, final int end, final double[] key) {
        if (begin >= end) {
            return -1;
        }
        if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(src)) {
            return -1;
        }
        int stop = end > src.length ? src.length : end;
        for (int srcPos = begin; srcPos < stop; srcPos++) {
            //起始匹配
            if (src[srcPos] == key[0]) {
                int matchCount = 1;
                for (int keyPos = 1, tmpSrcPos = srcPos + keyPos; keyPos < key.length && tmpSrcPos < src.length; keyPos++, tmpSrcPos = srcPos + keyPos) {
                    if (src[tmpSrcPos] != key[keyPos]) {
                        break;
                    }
                    matchCount++;
                }
                if (matchCount == key.length) {
                    return srcPos;
                }
            }
        }
        return -1;
    }

    public static int indexOf(final Object[] array1, final int begin1, final int end1,
                              final Object[] array2, final int begin2, final int end2) {
        if (begin1 >= end1 || begin2 >= end2) {
            return -1;
        }
        int head1 = begin1 < 0 ? 0 : begin1;
        int head2 = begin2 < 0 ? 0 : begin2;
        int tail1 = end1 > array1.length ? array1.length : end1;
        int tail2 = end2 > array2.length ? array2.length : end2;
        return indexOf(array1, head1, tail1, ArrayUtil.copyOfRange(array2, head2, tail2));
    }

    public static int indexOf(final Object[] src, final int begin, final int end, final Object[] key) {
        if (begin >= end) {
            return -1;
        }
        if (ArrayUtils.isEmpty(key) || ArrayUtils.isEmpty(src)) {
            return -1;
        }
        int stop = end > src.length ? src.length : end;
        for (int srcPos = begin; srcPos < stop; srcPos++) {
            //起始匹配
            if (src[srcPos] == key[0]) {
                int matchCount = 1;
                for (int keyPos = 1, tmpSrcPos = srcPos + keyPos; keyPos < key.length && tmpSrcPos < src.length; keyPos++, tmpSrcPos = srcPos + keyPos) {
                    if (!Objects.equals(src[tmpSrcPos], key[keyPos])) {
                        break;
                    }
                    matchCount++;
                }
                if (matchCount == key.length) {
                    return srcPos;
                }
            }
        }
        return -1;
    }

}
