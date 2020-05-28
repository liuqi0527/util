package com.egls.server.utils.date;

import java.time.LocalDateTime;

import com.egls.server.utils.date.expression.DateTimeExpression;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 15:30]
 */
public class TestDateTimeExpression {

    private void invalid(String expression) {
        try {
            DateTimeExpression.of(expression);
            Assert.fail();
        } catch (Exception e) {
            //do nothing
        }
    }

    @Test
    public void test0() {
        invalid("60 * * * * * *");
        invalid("* 60 * * * * *");
        invalid("* * 60 * * * *");
        invalid("* * * 60 * * *");
        invalid("* * * * 60 * *");
        invalid("* * * * * 60 *");
        invalid("* * * * * * 60");
        invalid("* * * a * * *");
        invalid("* * * 1 2,3 4,5,6 *");
        invalid("* * * 1 2,3 4~6,9~11 *");
        invalid("* * * * * * * * * *");
    }

    @Test
    public void test1() {
        invalid("* * * * * * 2015-2014");
        invalid("* * * * * 2-1 *");
        invalid("* * * * 2-1 * *");
        invalid("* * * 2-1 * * *");
        invalid("* * 2-1 * * * *");
        invalid("* 2-1 * * * * *");
        invalid("2-1 * * * * * *");
        invalid("2-1 2-1 2-1 2-1 2-1 * 2015-2014");
        invalid("2-1 2-1 1-2 2-1 2-1 * 2015-2014");
        invalid("2-1 2-1 1-2 2-1 1-2 * 2015-2014");
    }

    @Test
    public void test2() {
        int maxNano = 999999999;
        DateTimeExpression expression = DateTimeExpression.of("10-20 * * * * * *");//每年的每月的每天的每小时的每分钟的10秒-20秒
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 11, 11, 9)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 11, 11, 9, maxNano)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 11, 11, 10)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 11, 11, 11)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 11, 11, 19)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 11, 11, 20)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 11, 11, 20, maxNano)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 11, 11, 21)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 11, 11, 23)));
        //System.out.println(expression.toString());

        expression = DateTimeExpression.of("* 10-20 * * * * *");//每年的每月的每天的每小时的10分钟-20分钟
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 11, 9, 59)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 11, 9, 59, maxNano)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 11, 10, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 11, 15, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 11, 20, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 11, 20, 59, maxNano)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 11, 21, 0)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 11, 23, 0)));
        //System.out.println(expression.toString());

        expression = DateTimeExpression.of("* * 10-20 * * * *");//每年的每月的每天的10:00 - 20:00
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 9, 59, 59)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 9, 59, 59, maxNano)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 10, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 15, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 20, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 11, 20, 59, 59, maxNano)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 21, 0, 0)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 11, 23, 0, 0)));
        //System.out.println(expression.toString());

        expression = DateTimeExpression.of("* * * 10-20 * * *");//每年的每月的10-20号
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 9, 23, 59, 59)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 9, 23, 59, 59, maxNano)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 10, 0, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 15, 0, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 20, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 20, 23, 59, 59, maxNano)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 21, 0, 0, 0)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 23, 0, 0, 0)));
        //System.out.println(expression.toString());

        expression = DateTimeExpression.of("* * * * 2-3 * *");//每年的2-3月
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 1, 31, 23, 59, 59)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 1, 31, 23, 59, 59, maxNano)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 2, 1, 0, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 2, 27, 0, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 3, 27, 0, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 3, 31, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 3, 31, 23, 59, 59, maxNano)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 4, 1, 0, 0, 0)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 4, 23, 0, 0, 0)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 5, 23, 0, 0, 0)));
        //System.out.println(expression.toString());

        expression = DateTimeExpression.of("* * * * * * 2015-2017");//2015年-2017年
        Assert.assertFalse(expression.check(LocalDateTime.of(2014, 12, 31, 23, 59, 59)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2014, 12, 31, 23, 59, 59, maxNano)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 1, 1, 0, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 12, 31, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2016, 1, 1, 0, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2016, 12, 31, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2017, 1, 1, 0, 0, 0)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2017, 12, 31, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2017, 12, 31, 23, 59, 59, maxNano)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2018, 1, 1, 0, 0, 0)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2018, 1, 30, 0, 0, 0)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2019, 1, 1, 0, 0, 0)));
        //System.out.println(expression.toString());

        expression = DateTimeExpression.of("* * * * * 2-3 *");//每个星期2-3
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 23, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 24, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 25, 23, 59, 59)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 26, 23, 59, 59)));
        Assert.assertFalse(expression.check(LocalDateTime.of(2015, 11, 27, 23, 59, 59)));
        //System.out.println(expression.toString());

        expression = DateTimeExpression.of("* * * * * * *");//任意时间
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 23, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 24, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 25, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 26, 23, 59, 59)));
        Assert.assertTrue(expression.check(LocalDateTime.of(2015, 11, 27, 23, 59, 59)));
        //System.out.println(expression.toString());
    }

    @Test
    public void test3() {
        //不限定星期几
        DateTimeExpression expression = DateTimeExpression.of("* * 15-17 3-5 5~7 * *");
        //System.out.println(expression.toString());
        LocalDateTime nextValidDateTime = expression.nextDateTime(LocalDateTime.of(2017, 5, 1, 1, 1, 1));
        Assert.assertTrue(DateTimeUnit.SECOND.equals(nextValidDateTime, LocalDateTime.of(2017, 5, 3, 15, 0, 0)));
        //System.out.println("nextValidDateTime : " + DateTimeString.DATE_TIME.toString(nextValidDateTime));

        LocalDateTime nextInvalidDateTime = expression.nextDateTime(LocalDateTime.of(2017, 5, 4, 1, 1, 1));
        Assert.assertTrue(DateTimeUnit.SECOND.equals(nextInvalidDateTime, LocalDateTime.of(2017, 5, 5, 18, 0, 0)));
        //System.out.println("nextInvalidDateTime :" + DateTimeString.DATE_TIME.toString(nextInvalidDateTime));
        //System.out.println();

        long millis = System.currentTimeMillis();
        for (int j = 0; j < 100000; j++) {
            expression.nextDateTime(LocalDateTime.of(2017, 5, 1, 1, 1, 1));
        }
        //System.out.println("nextValidDateTime cost : " + ((System.currentTimeMillis() - millis) / 100000.0));
        //System.out.println("nextValidDateTime cost : " + ((System.currentTimeMillis() - millis)));

        millis = System.currentTimeMillis();
        for (int j = 0; j < 100000; j++) {
            expression.nextDateTime(LocalDateTime.of(2017, 5, 4, 1, 1, 1));
        }
        //System.out.println("nextInvalidDateTime cost : " + ((System.currentTimeMillis() - millis) / 100000.0));
        //System.out.println("nextInvalidDateTime cost : " + ((System.currentTimeMillis() - millis)));


        //限定星期几
        expression = DateTimeExpression.of("* * 15-17 3-5 5~7 4 *");
        //System.out.println(expression.toString());
        nextValidDateTime = expression.nextDateTime(LocalDateTime.of(2017, 5, 1, 1, 1, 1));
        Assert.assertTrue(DateTimeUnit.SECOND.equals(nextValidDateTime, LocalDateTime.of(2017, 5, 4, 15, 0, 0)));
        //System.out.println(DateTimeString.DATE_TIME.toString(nextValidDateTime));

        nextInvalidDateTime = expression.nextDateTime(LocalDateTime.of(2017, 5, 4, 1, 1, 1));
        Assert.assertTrue(DateTimeUnit.SECOND.equals(nextInvalidDateTime, LocalDateTime.of(2017, 5, 5, 0, 0, 0)));
        //System.out.println(DateTimeString.DATE_TIME.toString(nextInvalidDateTime));
        //System.out.println();

        millis = System.currentTimeMillis();
        for (int j = 0; j < 100000; j++) {
            expression.nextDateTime(LocalDateTime.of(2017, 5, 1, 1, 1, 1));
        }
        //System.out.println("nextValidDateTime cost : " + ((System.currentTimeMillis() - millis) / 100000.0));
        //System.out.println("nextValidDateTime cost : " + ((System.currentTimeMillis() - millis)));

        millis = System.currentTimeMillis();
        for (int j = 0; j < 100000; j++) {
            expression.nextDateTime(LocalDateTime.of(2017, 5, 4, 1, 1, 1));
        }
        //System.out.println("nextInvalidDateTime cost : " + ((System.currentTimeMillis() - millis) / 100000.0));
        //System.out.println("nextInvalidDateTime cost : " + ((System.currentTimeMillis() - millis)));

    }

}