package com.egls.server.utils.task.period;

import com.egls.server.utils.date.DateTimeUnit;
import com.egls.server.utils.units.TimeUnitsConst;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 15:01]
 */
public class TestPeriodicTask {

    private static final long WARNING = 20L;

    @Test
    public void test0() throws InterruptedException {
        final PeriodicTaskManager periodicTaskManager = new PeriodicTaskManager(true, WARNING);
        int[] array = new int[4];
        periodicTaskManager.addTask(new PeriodicTask() {
            @Override
            public void doPeriodicTask() {
                array[0] = array[0] + 1;
            }

            @Override
            public DateTimeUnit getDateTimeUnit() {
                return DateTimeUnit.SECOND;
            }
        });
        periodicTaskManager.addTask(new PeriodicTask() {
            @Override
            public void doPeriodicTask() {
                array[1] = array[1] + 1;
            }

            @Override
            public long getInterval() {
                return 2;
            }

            @Override
            public DateTimeUnit getDateTimeUnit() {
                return DateTimeUnit.SECOND;
            }
        });
        periodicTaskManager.addTask(new PeriodicTask() {
            @Override
            public void doPeriodicTask() {
                array[2] = array[2] + 1;
            }

            @Override
            public boolean isDestroyable() {
                return array[2] > 1;
            }

            @Override
            public DateTimeUnit getDateTimeUnit() {
                return DateTimeUnit.SECOND;
            }
        });
        periodicTaskManager.addTask(new PeriodicTask() {
            @Override
            public void doPeriodicTask() {
                array[3] = array[3] + 1;
            }

            @Override
            public boolean isDestroyable() {
                return true;
            }

            @Override
            public DateTimeUnit getDateTimeUnit() {
                return DateTimeUnit.SECOND;
            }
        });

        long millis = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(10L);
            periodicTaskManager.tick();
        }
        int seconds = (int) ((System.currentTimeMillis() - millis) / TimeUnitsConst.MILLIS_OF_SECOND);
        //这个有可能大于10秒,因为误差.
        Assert.assertTrue(seconds <= array[0]);
        Assert.assertTrue(seconds / 2 <= array[1]);
        Assert.assertEquals(2, array[2]);
        Assert.assertEquals(1, array[3]);

    }

    @Test
    public void test1() throws InterruptedException {
        final PeriodicTaskManager periodicTaskManager = new PeriodicTaskManager(true, WARNING);
        int[] array = new int[2];
        periodicTaskManager.addTask(new PeriodicTask() {
            @Override
            public void doPeriodicTask() {
                array[0] = array[0] + 1;
            }

            @Override
            public DateTimeUnit getDateTimeUnit() {
                return DateTimeUnit.SECOND;
            }
        });
        periodicTaskManager.addTask(new PeriodicTask() {
            @Override
            public void doPeriodicTask() {
                array[1] = array[1] + 1;
                throw new RuntimeException();
            }

            @Override
            public DateTimeUnit getDateTimeUnit() {
                return DateTimeUnit.SECOND;
            }
        });

        long millis = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(10L);
            try {
                periodicTaskManager.tick();
            } catch (Exception exception) {
                //do nothing
            }
        }
        int seconds = (int) ((System.currentTimeMillis() - millis) / TimeUnitsConst.MILLIS_OF_SECOND);
        //这个有可能大于10秒,因为误差.
        Assert.assertTrue(seconds <= array[0]);
        Assert.assertEquals(1, array[1]);
    }

    @Test
    public void test2() throws InterruptedException {

        final PeriodicTaskManager periodicTaskManager = new PeriodicTaskManager(false, WARNING);
        int[] array = new int[2];
        periodicTaskManager.addTask(new PeriodicTask() {
            @Override
            public void doPeriodicTask() {
                array[0] = array[0] + 1;
            }

            @Override
            public DateTimeUnit getDateTimeUnit() {
                return DateTimeUnit.SECOND;
            }
        });
        periodicTaskManager.addTask(new PeriodicTask() {
            @Override
            public void doPeriodicTask() {
                array[1] = array[1] + 1;
                throw new RuntimeException();
            }

            @Override
            public DateTimeUnit getDateTimeUnit() {
                return DateTimeUnit.SECOND;
            }
        });

        long millis = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Thread.sleep(10L);
            try {
                periodicTaskManager.tick();
            } catch (Exception exception) {
                //do nothing
            }
        }
        int seconds = (int) ((System.currentTimeMillis() - millis) / TimeUnitsConst.MILLIS_OF_SECOND);
        //这个有可能大于10秒,因为误差.
        Assert.assertTrue(seconds <= array[0]);
        Assert.assertTrue(seconds <= array[1]);

    }

}
