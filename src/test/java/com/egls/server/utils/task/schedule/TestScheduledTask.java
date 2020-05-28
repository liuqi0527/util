package com.egls.server.utils.task.schedule;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-29 15:29]
 */
public class TestScheduledTask {

    private static final long WARNING = 20L;

    private static volatile int TEST = 0;

    @Test
    public void test0() throws InterruptedException {
        init();

        final ScheduledTaskManager scheduledTaskManager = new ScheduledTaskManager(WARNING);
        scheduledTaskManager.schedule(() -> TEST = 1);
        for (int i = 0; i < 20; i++) {
            Thread.sleep(10L);
            scheduledTaskManager.tick();
        }
        Assert.assertEquals(TEST, 1);
    }

    @Test
    public void test1() throws InterruptedException {
        init();

        final ScheduledTaskManager scheduledTaskManager = new ScheduledTaskManager(WARNING);
        scheduledTaskManager.schedule(100, () -> TEST = 1);

        for (int i = 0; i < 5; i++) {
            Thread.sleep(10L);
            scheduledTaskManager.tick();
        }
        Assert.assertEquals(TEST, 0);

        for (int i = 0; i < 6; i++) {
            Thread.sleep(10L);
            scheduledTaskManager.tick();
        }

        Assert.assertEquals(TEST, 1);
    }

    private void init() {
        TEST = 0;
    }

}
