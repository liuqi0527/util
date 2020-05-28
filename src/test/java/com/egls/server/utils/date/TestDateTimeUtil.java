package com.egls.server.utils.date;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 11:40]
 */
public class TestDateTimeUtil {

    private static final DateTimeString formatter = DateTimeString.DATE_TIME;

    @Test
    public void test0() {
        LocalDateTime localDateTime = LocalDateTime.of(2014, 2, 3, 15, 23, 21);
        Assert.assertEquals("2014-02-03 16:00:00", formatter.toString(DateTimeUtil.adjust(DateTimeUnit.HOUR, localDateTime, true, 1)));
        Assert.assertEquals("2014-02-03 16:23:21", formatter.toString(DateTimeUtil.adjust(DateTimeUnit.HOUR, localDateTime, false, 1)));
    }

    @Test
    public void test1() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        LocalDateTime localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        Assert.assertTrue(DateTimeUtil.equals(DateTimeUnit.YEAR, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2014, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        Assert.assertFalse(DateTimeUtil.equals(DateTimeUnit.YEAR, localDateTime1, localDateTime2));

        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        Assert.assertTrue(DateTimeUtil.equals(DateTimeUnit.MONTH, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 3, 3, 15, 23, 21);
        Assert.assertFalse(DateTimeUtil.equals(DateTimeUnit.MONTH, localDateTime1, localDateTime2));

        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        Assert.assertTrue(DateTimeUtil.equals(DateTimeUnit.DAY, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 4, 15, 23, 21);
        Assert.assertFalse(DateTimeUtil.equals(DateTimeUnit.DAY, localDateTime1, localDateTime2));

        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        Assert.assertTrue(DateTimeUtil.equals(DateTimeUnit.HOUR, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 16, 23, 21);
        Assert.assertFalse(DateTimeUtil.equals(DateTimeUnit.HOUR, localDateTime1, localDateTime2));

        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        Assert.assertTrue(DateTimeUtil.equals(DateTimeUnit.MINUTE, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 24, 21);
        Assert.assertFalse(DateTimeUtil.equals(DateTimeUnit.MINUTE, localDateTime1, localDateTime2));

        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        Assert.assertTrue(DateTimeUtil.equals(DateTimeUnit.SECOND, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 23, 22);
        Assert.assertFalse(DateTimeUtil.equals(DateTimeUnit.SECOND, localDateTime1, localDateTime2));

        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        Assert.assertTrue(DateTimeUtil.equals(DateTimeUnit.WEEK, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2015, 2, 3, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2015, 2, 13, 15, 23, 22);
        Assert.assertFalse(DateTimeUtil.equals(DateTimeUnit.WEEK, localDateTime1, localDateTime2));

        localDateTime1 = LocalDateTime.of(2016, 1, 1, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2016, 1, 2, 15, 23, 21);
        Assert.assertTrue(DateTimeUtil.equals(DateTimeUnit.WEEK, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2015, 12, 31, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2016, 1, 1, 15, 23, 22);
        Assert.assertTrue(DateTimeUtil.equals(DateTimeUnit.WEEK, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2015, 12, 21, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2016, 1, 1, 15, 23, 22);
        Assert.assertFalse(DateTimeUtil.equals(DateTimeUnit.WEEK, localDateTime1, localDateTime2));
        localDateTime1 = LocalDateTime.of(2013, 12, 21, 15, 23, 21);
        localDateTime2 = LocalDateTime.of(2016, 1, 1, 15, 23, 22);
        Assert.assertFalse(DateTimeUtil.equals(DateTimeUnit.WEEK, localDateTime1, localDateTime2));
    }
}
