package com.egls.server.utils.array;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mayer - [Created on 2018-09-03 14:09]
 */
public class TestArrayUtil {

    @Test
    public void testCopy() {
        Integer[] array = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals(array.length, ArrayUtil.copyOf(array, array.length).length);
        assertEquals(1, ArrayUtil.copyOfRange(array, 0, 1).length);
        assertEquals(2, ArrayUtil.copyOfRange(array, 0, 2).length);
        assertEquals(5, ArrayUtil.copyOfRange(array, 0, 5).length);
        assertEquals(1, (int) ArrayUtil.copyOfRange(array, 0, 5)[1]);
        assertEquals(4, (int) ArrayUtil.copyOfRange(array, 0, 5)[4]);
    }

    @Test
    public void testArrangeNullElement() {
        Integer[] array1 = new Integer[]{null, null, 0, null, 1, null, 2, null, null, 3, null, null, 4, null, null};
        Integer[] array2 = new Integer[]{null, null, 0, null, 1, null, 2, null, null, 3, null, null, 4};
        ArrayUtil.arrangeNullElementToTheRight(array1);
        ArrayUtil.arrangeNullElementToTheRight(array2);
        assertTrue(array1[0] != null && array1[1] != null && array1[2] != null && array1[3] != null && array1[4] != null);
        assertTrue(array1[5] == null && array1[6] == null && array1[7] == null && array1[8] == null && array1[9] == null);
        assertTrue(array2[0] != null && array2[1] != null && array2[2] != null && array2[3] != null && array2[4] != null);
        assertTrue(array2[5] == null && array2[6] == null && array2[7] == null && array2[8] == null && array2[9] == null);
    }

    @Test
    public void testPrimitiveArray() {

        byte[] bytes = new byte[]{0, 1, 2, 3, 4, 5};
        assertArrayEquals(bytes, ArrayUtil.toBytePrimitiveArray(StringUtils.split(Arrays.toString(bytes), "[], ")));

        boolean[] booleans = new boolean[]{true, true, false, false};
        assertArrayEquals(booleans, ArrayUtil.toBooleanPrimitiveArray(StringUtils.split(Arrays.toString(booleans), "[], ")));

        short[] shorts = new short[]{0, 1, 2, 3, 4, 5};
        assertArrayEquals(shorts, ArrayUtil.toShortPrimitiveArray(StringUtils.split(Arrays.toString(shorts), "[], ")));

        int[] ints = new int[]{0, 1, 2, 3, 4, 5};
        assertArrayEquals(ints, ArrayUtil.toIntPrimitiveArray(StringUtils.split(Arrays.toString(ints), "[], ")));

        long[] longs = new long[]{0, 1, 2, 3, 4, 5};
        assertArrayEquals(longs, ArrayUtil.toLongPrimitiveArray(StringUtils.split(Arrays.toString(longs), "[], ")));

        char[] chars = new char[]{0, 1, 2, 3, 4, 5};
        assertArrayEquals(chars, ArrayUtil.toCharPrimitiveArray(StringUtils.split(Arrays.toString(chars), "[], ")));

        float[] floats = new float[]{0, 1, 2, 3, 4, 5};
        assertArrayEquals(floats, ArrayUtil.toFloatPrimitiveArray(StringUtils.split(Arrays.toString(floats), "[], ")), 0);

        double[] doubles = new double[]{0, 1, 2, 3, 4, 5};
        assertArrayEquals(doubles, ArrayUtil.toDoublePrimitiveArray(StringUtils.split(Arrays.toString(doubles), "[], ")), 0);

    }

}
