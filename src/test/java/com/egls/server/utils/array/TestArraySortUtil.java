package com.egls.server.utils.array;

import java.util.Random;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author mayer - [Created on 2018-09-03 14:10]
 */
public class TestArraySortUtil {

    @Test
    public void testIsSorted() {
        int[] array1 = {-3, -2, -1, 0, 1, 2, 3, 4, 5};
        int[] array2 = {5, 4, 3, 2, 1, 0, -1, -2, -3};
        int[] array3 = {-3, -3, -1, 0, 1, 2, 3, 4, 5};
        int[] array4 = {-3, -3, -1, 0, 0, 2, 3, 3, 5};
        int[] array5 = {-3, -4, -1, 0, 0, 2, 3, 3, 5};
        int[] array6 = {-3, -3, -1, 0, 3, 2, 3, 3, 5};
        int[] array7 = {5, 4, 3, 2, 1, 0, -1, -2, -3};
        int[] array8 = {5, 4, 3, 1, 1, 0, -1, -2, -3};
        int[] array9 = {5, 4, 3, 5, 1, 0, -1, -2, -3};
        int[] array10 = {5, 4, 3, 2, 1, 0, -1, -2, -1};
        assertTrue(ArraySortUtil.isSorted(array1, true));
        assertFalse(ArraySortUtil.isSorted(array1, false));
        assertFalse(ArraySortUtil.isSorted(array2, true));
        assertTrue(ArraySortUtil.isSorted(array2, false));
        assertTrue(ArraySortUtil.isSorted(array3, true));
        assertFalse(ArraySortUtil.isSorted(array3, false));
        assertTrue(ArraySortUtil.isSorted(array4, true));
        assertFalse(ArraySortUtil.isSorted(array4, false));
        assertFalse(ArraySortUtil.isSorted(array5, true));
        assertFalse(ArraySortUtil.isSorted(array5, false));
        assertFalse(ArraySortUtil.isSorted(array6, true));
        assertFalse(ArraySortUtil.isSorted(array6, false));
        assertFalse(ArraySortUtil.isSorted(array7, true));
        assertTrue(ArraySortUtil.isSorted(array7, false));
        assertFalse(ArraySortUtil.isSorted(array8, true));
        assertTrue(ArraySortUtil.isSorted(array8, false));
        assertFalse(ArraySortUtil.isSorted(array9, true));
        assertFalse(ArraySortUtil.isSorted(array9, false));
        assertFalse(ArraySortUtil.isSorted(array10, true));
        assertFalse(ArraySortUtil.isSorted(array10, false));
    }

    @Test
    public void testShuffle() {
        Integer[] array = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayUtil.shuffle(array);
        assertFalse(ArraySortUtil.isSorted(array, true));
        assertFalse(ArraySortUtil.isSorted(array, false));
    }

    @Test
    public void testInsertSort() {
        checkSorted1(ArraySortUtil::insertSort);
        checkSorted2(ArraySortUtil::insertSort);
    }

    @Test
    public void testShellSort() {
        checkSorted1(ArraySortUtil::shellSort);
        checkSorted2(ArraySortUtil::shellSort);
    }

    @Test
    public void testQuickSort() {
        checkSorted1(ArraySortUtil::quickSort);
        checkSorted2(ArraySortUtil::quickSort);
    }

    @Test
    public void testBubbleSort() {
        checkSorted1(ArraySortUtil::bubbleSort);
        checkSorted2(ArraySortUtil::bubbleSort);
    }

    @Test
    public void testSelectSort() {
        checkSorted1(ArraySortUtil::selectSort);
        checkSorted2(ArraySortUtil::selectSort);
    }

    @Test
    public void testSort() {
        checkSorted1(ArraySortUtil::sort);
        checkSorted2(ArraySortUtil::sort);
    }

    private Integer[] createTestArray(boolean containsNull) {
        int length = 100;
        Random random = new Random();
        Integer[] array = new Integer[length];
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(length);
        }
        if (containsNull) {
            for (int i = 0; i < length; i++, length--) {
                array[i] = null;
            }
        }
        ArrayUtil.shuffle(array);
        return array;
    }

    private static final int TIMES = 9999;

    @FunctionalInterface
    interface TestSortProxy1 {
        <E extends Comparable<? super E>> void sort(E[] original, boolean lowToHigh);
    }

    @FunctionalInterface
    interface TestSortProxy2 {
        <E extends Comparable<? super E>> void sort(E[] original, int left, int right, boolean lowToHigh);
    }

    private void checkSorted1(TestSortProxy1 proxy) {
        Random random = new Random();
        for (int i = 0; i < TIMES; i++) {
            boolean lowToHigh = random.nextBoolean();
            Integer[] array = createTestArray(random.nextBoolean());
            proxy.sort(array, lowToHigh);
            assertTrue(ArraySortUtil.isSorted(array, lowToHigh));
        }
    }

    private void checkSorted2(TestSortProxy2 proxy) {
        Random random = new Random();
        for (int i = 0; i < TIMES; i++) {
            boolean lowToHigh = random.nextBoolean();
            Integer[] array = createTestArray(random.nextBoolean());
            int mid = random.nextInt(array.length);
            proxy.sort(array, 0, mid, lowToHigh);
            assertTrue(ArraySortUtil.isSorted(ArrayUtil.copyOfRange(array, 0, mid), lowToHigh));
        }
    }

}

