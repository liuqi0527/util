package com.egls.server.utils.array;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mayer - [Created on 2018-09-03 14:08]
 */
public class TestArraySearchUtil {

    @Test
    public void testIndexOf() {
        byte[] a1 = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        byte[] a2 = new byte[]{5, 6};
        assertEquals(4, ArraySearchUtil.indexOf(a1, 0, a1.length, a2));
        boolean[] a3 = new boolean[]{true, true, true, true, false, true, true, true, true};
        boolean[] a4 = new boolean[]{false, true};
        assertEquals(4, ArraySearchUtil.indexOf(a3, 0, a3.length, a4));
    }

    @Test
    public void testEquals() {
        char[] a1 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        char[] a2 = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'};

        assertTrue(ArraySearchUtil.equals(a1, 0, a2, 0, a1.length));
        assertTrue(ArraySearchUtil.equals(a1, 1, a2, 1, a1.length - 1));
        assertTrue(ArraySearchUtil.equals(a1, 2, a2, 2, a1.length - 2));
        assertTrue(ArraySearchUtil.equals(a1, 3, a2, 3, a1.length - 3));
        assertTrue(ArraySearchUtil.equals(a1, 4, a2, 4, a1.length - 4));
        assertTrue(ArraySearchUtil.equals(a1, 5, a2, 5, a1.length - 5));
        assertTrue(ArraySearchUtil.equals(a1, 6, a2, 6, a1.length - 6));

        assertFalse(ArraySearchUtil.equals(a1, 0, a2, 1, a1.length));
        assertFalse(ArraySearchUtil.equals(a1, 0, a2, 2, a1.length));
        assertFalse(ArraySearchUtil.equals(a1, 2, a2, 2, a1.length));
        assertFalse(ArraySearchUtil.equals(a1, 4, a2, 2, a1.length));

    }

}
