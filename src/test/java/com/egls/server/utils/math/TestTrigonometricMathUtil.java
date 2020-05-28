package com.egls.server.utils.math;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 14:55]
 */
public class TestTrigonometricMathUtil {

    @Test
    public void test() {
        for (int i = 0; i < 100000; i++) {
            double x1 = RandomUtils.nextInt(0, 100);
            double y1 = RandomUtils.nextInt(0, 100);
            double x2 = RandomUtils.nextInt(0, 100);
            double y2 = RandomUtils.nextInt(0, 100);
            while (Math.abs(x1 - x2) < 1e-8 && Math.abs(y1 - y2) < 1e-8) {
                x1 = RandomUtils.nextInt(0, 100);
                y1 = RandomUtils.nextInt(0, 100);
                x2 = RandomUtils.nextInt(0, 100);
                y2 = RandomUtils.nextInt(0, 100);
            }
            double t2 = Math.atan2(y2 - y1, x2 - x1);
            Assert.assertEquals(Math.round(((t2 * 180 / Math.PI) + 360) % 360), TrigonometricMathUtil.getAngle(x1, y1, x2, y2));
        }
    }

}
