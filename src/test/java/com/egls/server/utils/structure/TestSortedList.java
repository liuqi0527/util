package com.egls.server.utils.structure;

import java.util.*;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-03 20:45]
 */
public class TestSortedList {

    @Test
    public void test0() {
        SortedList<Integer> sortedList = new SortedList<>();
        sortedList.add(0);
        sortedList.add(2);
        sortedList.add(3);
        sortedList.add(1);
        sortedList.add(4);
        Assert.assertEquals(sortedList.peekFirst(), new Integer(0));
        Assert.assertEquals(sortedList.peekLast(), new Integer(4));
        Assert.assertEquals(sortedList.pollFirst(), new Integer(0));
        Assert.assertEquals(sortedList.pollLast(), new Integer(4));

        Assert.assertEquals(3, sortedList.size());
        Assert.assertTrue(sortedList.listSize() > 0);
        Assert.assertFalse(sortedList.isEmpty());

        sortedList.clear();
        Assert.assertEquals(sortedList.size(), 0);
        Assert.assertTrue(sortedList.listSize() > 0);
        Assert.assertTrue(sortedList.isEmpty());
    }

    @Test
    public void test1() {
        SortedList<Integer> sortedList = new SortedList<>();
        sortedList.add(0);
        sortedList.add(2);
        sortedList.add(3);
        sortedList.add(1);
        sortedList.add(4);
        sortedList.add(5);
        Assert.assertEquals(new Integer(0), sortedList.get(0));
        Assert.assertEquals(new Integer(1), sortedList.get(1));
        Assert.assertEquals(new Integer(2), sortedList.get(2));
        Assert.assertEquals(new Integer(3), sortedList.get(3));
        Assert.assertEquals(new Integer(4), sortedList.get(4));
        Assert.assertEquals(new Integer(5), sortedList.get(5));
        int i = 0;
        for (Integer integer : sortedList) {
            Assert.assertEquals(integer, new Integer(i));
            i++;
        }
    }

    @Test
    public void test2() {
        SortedList<Integer> sortedList = new SortedList<>();
        for (int i = 0; i <= 50; i++) {
            sortedList.add(i);
        }
        Iterator<Integer> itr = sortedList.iterator();
        while (itr.hasNext()) {
            Integer integer = itr.next();
            if (integer % 5 == 0) {
                itr.remove();
            }
        }
        Assert.assertEquals(40, sortedList.size());
        for (int i = 0; i <= 50; i++) {
            if (i % 5 == 0) {
                Assert.assertFalse(sortedList.contains(i));
            } else {
                Assert.assertTrue(sortedList.contains(i));
            }
        }
        itr = sortedList.iterator();
        while (itr.hasNext()) {
            Integer integer = itr.next();
            Assert.assertNotEquals(0, integer % 5);
        }
    }

    @Test
    public void test3() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(i);
            list.add(i);
        }
        Collections.shuffle(list);
        SortedList<Integer> sortedList = new SortedList<>(10);
        list.forEach(sortedList::add);

        Assert.assertEquals(sortedList.size(), 100);
        int i1 = sortedList.remove(30);

        Assert.assertEquals(sortedList.size(), 99);
        int i2 = sortedList.remove(30);

        Assert.assertEquals(sortedList.size(), 98);
        Integer[] arr = sortedList.toArray(new Integer[sortedList.size()]);
        Assert.assertTrue(ArrayUtils.isSorted(arr));

        list.clear();
        list.addAll(Arrays.asList(arr));
        Assert.assertFalse(list.contains(i1));
        Assert.assertFalse(list.contains(i2));

        Assert.assertFalse(sortedList.contains(i1));
        Assert.assertFalse(sortedList.contains(i2));
    }

    @Test
    public void test4() {
        SortedList<Integer> sortedList = new SortedList<>();
        sortedList.add(0);
        sortedList.add(2);
        sortedList.add(3);
        sortedList.add(1);
        sortedList.add(4);
        sortedList.add(5);
        sortedList.addAll(new Integer[]{6, 7});
        ArrayList<Integer> list = new ArrayList<>();
        list.add(8);
        list.add(9);
        sortedList.addAll(list);

        Assert.assertTrue(sortedList.hasRemaining());
        Assert.assertFalse(sortedList.isEmpty());
        Assert.assertEquals(0, (int) sortedList.get(0));
        Assert.assertTrue(sortedList.contains(0));
        Assert.assertTrue(Objects.equals(sortedList.remove(0), 0));
        Assert.assertTrue(sortedList.remove(new Integer(1)));
        Assert.assertEquals(8, sortedList.size());
        Assert.assertEquals(1, sortedList.indexOf(3));
        Assert.assertEquals(1, sortedList.lastIndexOf(3));
        Assert.assertEquals(1, sortedList.rankOf(3));
        Assert.assertEquals(6, sortedList.lastRankOf(3));
        Assert.assertEquals(8, sortedList.toArray().length);
        Assert.assertEquals(8, sortedList.toArray(new Integer[]{}).length);
        Assert.assertEquals(8, sortedList.toArray(new Integer[8]).length);
        Assert.assertEquals(256, sortedList.listSize());
    }

    @Test
    public void test5() {
        SortedList<String> list = new SortedList<>(10);
        for (int i = 0; i < 6; i++) {
            list.add(Integer.toString(i));
        }
        for (int i = 0; i < 20; i++) {
            list.pollFirst();
            list.add(Integer.toString(i));
            Assert.assertEquals(6, list.size());
            for (String threatEntity : list) {
                Assert.assertTrue(Objects.nonNull(threatEntity));
            }
        }
    }

    @Test
    public void test6() {
        SortedList<Integer> list = new SortedList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        Assert.assertEquals(new Integer(0), list.remove(0));
        Assert.assertEquals(new Integer(1), list.remove(0));
        Assert.assertEquals(new Integer(2), list.remove(0));
        Assert.assertEquals(new Integer(3), list.remove(0));
        try {
            Assert.assertEquals(new Integer(3), list.remove(0));
            Assert.fail();
        } catch (Exception exception) {
            // do nothing
        }
        Assert.assertEquals(0, list.size());
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void test7() {
        SortedList<Integer> list = new SortedList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        Assert.assertEquals(new Integer(3), list.remove(list.size() - 1));
        Assert.assertEquals(new Integer(2), list.remove(list.size() - 1));
        Assert.assertEquals(new Integer(1), list.remove(list.size() - 1));
        Assert.assertEquals(new Integer(0), list.remove(list.size() - 1));
        try {
            Assert.assertEquals(new Integer(0), list.remove(list.size() - 1));
            Assert.fail();
        } catch (Exception exception) {
            // do nothing
        }
        Assert.assertEquals(0, list.size());
        Assert.assertTrue(list.isEmpty());
    }

}
