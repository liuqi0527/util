package com.egls.server.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * @author mayer - [Created on 2018-09-03 20:37]
 */
public class TestMethodInvoke {

    public static final int WARM_UP = 500_000; //预热次数
    public static final int TEST_TIME = Integer.MAX_VALUE; //执行次数
    public static final boolean NEED_WARM_UP = true; //是否预热

    public static int good(int num) {
        return num * 2 + 1;
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("WARM_UP =" + NEED_WARM_UP + ",TEST_TIME=" + TEST_TIME);
        for (int i = 0; i < 10; i++) {
            test0(); //直接方法调用
            test1(); //Method反射调用
            test2(); //MethodHandle的invoke()调用
            test3(); //MethodHandle的invokeExact调用
        }
    }

    private static void test0() {
        int sum = 0;
        if (NEED_WARM_UP)
            for (int i = 0; i < WARM_UP; i++) {
                sum += good(i);
            }

        long start = System.nanoTime();
        for (int i = 0; i < TEST_TIME; i++) {
            sum += good(i);
        }

        long end = System.nanoTime();
        System.out.println("Result =" + sum + ",Test0 cost " + (end - start) + "ns.");
        BigDecimal bd = new BigDecimal(end - start);
        bd = bd.divide(new BigDecimal(TEST_TIME), 20, BigDecimal.ROUND_HALF_UP);
        System.out.println("per Time cost " + bd + " ns");
    }

    private static void test1() throws Exception {
        Method m = TestMethodInvoke.class.getDeclaredMethod("good", int.class);

        int sum = 0;
        if (NEED_WARM_UP)
            for (int i = 0; i < WARM_UP; i++) {
                sum += (int) m.invoke(null, i);
            }

        long start = System.nanoTime();
        for (int i = 0; i < TEST_TIME; i++) {
            sum += (int) m.invoke(null, i);
        }
        long end = System.nanoTime();
        System.out.println("Result =" + sum + ",Test1 cost " + (end - start) + "ns.");
        BigDecimal bd = new BigDecimal(end - start);
        bd = bd.divide(new BigDecimal(TEST_TIME), 20, BigDecimal.ROUND_HALF_UP);
        System.out.println("per Time cost " + bd + " ns");
    }

    private static void test2() throws Throwable {
        MethodType mt = MethodType.methodType(int.class, int.class);
        MethodHandle m = MethodHandles.lookup().findStatic(TestMethodInvoke.class, "good", mt);

        int sum = 0;
        if (NEED_WARM_UP)
            for (int i = 0; i < WARM_UP; i++) {
                sum += (int) m.invoke(i);
            }

        long start = System.nanoTime();
        for (int i = 0; i < TEST_TIME; i++) {
            sum += (int) m.invoke(i);
        }
        long end = System.nanoTime();
        System.out.println("Result =" + sum + ",Test2 cost " + (end - start) + "ns.");
        BigDecimal bd = new BigDecimal(end - start);
        bd = bd.divide(new BigDecimal(TEST_TIME), 20, BigDecimal.ROUND_HALF_UP);
        System.out.println("per Time cost " + bd + " ns");
    }

    private static void test3() throws Throwable {
        MethodType mt = MethodType.methodType(int.class, int.class);
        MethodHandle m = MethodHandles.lookup().findStatic(TestMethodInvoke.class, "good", mt);

        int sum = 0;
        if (NEED_WARM_UP)
            for (int i = 0; i < WARM_UP; i++) {
                sum += (int) m.invokeExact(i);
            }

        long start = System.nanoTime();
        for (int i = 0; i < TEST_TIME; i++) {
            sum += (int) m.invokeExact(i);
        }
        long end = System.nanoTime();
        System.out.println("Result =" + sum + ",Test3 cost " + (end - start) + "ns.");
        BigDecimal bd = new BigDecimal(end - start);
        bd = bd.divide(new BigDecimal(TEST_TIME), 20, BigDecimal.ROUND_HALF_UP);
        System.out.println("per Time cost " + bd + " ns");
    }

}
