package com.egls.server.utils.random;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.egls.server.utils.CollectionUtil;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 提供一些随机的工具方法.
 *
 * @author mayer - [Created on 2018-08-09 15:10]
 */
public final class RandomUtil {

    private static final Random RANDOM = new Random();

    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    public static int randomInt(final int bound) {
        return RANDOM.nextInt(bound);
    }

    public static float randomFloat(final float bound) {
        return randomFloat() * bound;
    }

    public static float randomFloat() {
        return RANDOM.nextFloat();
    }

    public static double randomDouble(final double bound) {
        return randomDouble() * bound;
    }

    public static double randomDouble() {
        return RANDOM.nextDouble();
    }

    /**
     * <pre>
     *     假如给定的probability = 0, bound = 10;
     *     如果随机int数的时候不加1,那么随机出来的数范围在[0,9].
     *     但是,probability = 0的时候,就算随出来是0.也不应该认为是对的.因为概率是0.但是却有随机成功的概率.这是不严谨的.
     *     所以应当给随机数结果加1,让随机数的范围在[1,10].
     *     这样当给定的probability = 0或者probability = bound.都是没有问题的.
     *     probability = 0,没有问题.随机数不会出现0.所以不会有任何满足的情况.
     *     probability = 10,没有问题.随机数会出现10.所以有可能产生满足的情况.
     * </pre>
     *
     * @param probability 概率,例如,70%.这个参数应该是70
     * @param bound       边界,例如,70%.这个参数应该是100
     * @return 是否在概率内
     */
    public static boolean random(final int probability, final int bound) {
        //这里要+1,防止0概率的还能被命中
        int randomValue = randomInt(bound) + 1;
        return randomValue <= probability;
    }

    public static boolean random(final float probability, final float bound) {
        return random(Math.abs((int) probability), Math.abs((int) bound));
    }

    public static boolean random(final double probability, final double bound) {
        return random(Math.abs((int) probability), Math.abs((int) bound));
    }

    /**
     * 在概率中进行随机
     *
     * @param probabilities 概率
     * @return 随机结果, 如果没有随机结果, 返回-1.
     */
    public static int randomIndex(final int[] probabilities) {
        return randomIndexWithExcludedIndex(probabilities, null);
    }

    /**
     * 在概率中进行随机,但是可以指定排除某些索引
     *
     * @param probabilities 概率
     * @param exclusion     被排除的索引
     * @return 随机结果, 如果没有随机结果, 返回-1.
     */
    public static int randomIndexWithExcludedIndex(final int[] probabilities, final Collection<Integer> exclusion) {
        if (ArrayUtils.isEmpty(probabilities)) {
            return -1;
        }
        int probabilitySum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            if (exclusion == null || !exclusion.contains(i)) {
                probabilitySum = probabilitySum + probabilities[i];
            }
        }
        if (probabilitySum > 0) {
            int probability = 0;
            //这里需要+1,防止0概率事件被随机出来
            int randomValue = randomInt(probabilitySum) + 1;
            for (int i = 0; i < probabilities.length; i++) {
                if (exclusion == null || !exclusion.contains(i)) {
                    probability = probability + probabilities[i];
                    if (randomValue <= probability) {
                        //命中
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 在给定的对象中等概率随机一个
     *
     * @param objects 数据
     * @param <E>     泛型
     * @return 随机结果, 如果没有随机结果, 返回null
     */
    public static <E> E naturalRandomOne(final E[] objects) {
        return ArrayUtils.isEmpty(objects) ? null : objects[randomInt(objects.length)];
    }

    /**
     * 在给定的对象中随机一个,概率是对象自身拥有的.
     *
     * @param objects 数据
     * @param <E>     泛型
     * @return 随机结果, 如果没有随机结果, 返回null
     */
    public static <E extends RandomProbabilitySupplier> E randomOne(final E[] objects) {
        return randomOneWithExcludedIndex(objects, null);
    }

    /**
     * 在给定的对象中随机一个,概率是对象自身拥有的.但是可以指定排除某些索引
     *
     * @param objects   数据
     * @param exclusion 排除的索引
     * @param <E>       泛型
     * @return 随机结果, 如果没有随机结果, 返回null
     */
    public static <E extends RandomProbabilitySupplier> E randomOneWithExcludedIndex(final E[] objects, final Collection<Integer> exclusion) {
        if (ArrayUtils.isEmpty(objects)) {
            return null;
        }
        return randomOneWithExcludedIndex(Arrays.asList(objects), exclusion);
    }

    /**
     * 在给定的对象中随机一个,概率是对象自身拥有的.但是可以指定排除某些对象
     *
     * @param objects   数据
     * @param exclusion 排除的对象
     * @param <E>       泛型
     * @return 随机结果, 如果没有随机结果, 返回null
     */
    public static <E extends RandomProbabilitySupplier> E randomOneWithExcludedObject(final E[] objects, final Collection<E> exclusion) {
        if (ArrayUtils.isEmpty(objects)) {
            return null;
        }
        return randomOneWithExcludedObject(Arrays.asList(objects), exclusion);
    }

    /**
     * 在给定的对象中等概率随机一个
     *
     * @param objects 数据
     * @param <E>     泛型
     * @return 随机结果, 如果没有随机结果, 返回null
     */
    public static <E> E naturalRandomOne(final List<E> objects) {
        return CollectionUtil.isEmpty(objects) ? null : objects.get(randomInt(objects.size()));
    }

    /**
     * 在给定的对象中随机一个,概率是对象自身拥有的.
     *
     * @param objects 数据
     * @param <E>     泛型
     * @return 随机结果, 如果没有随机结果, 返回null
     */
    public static <E extends RandomProbabilitySupplier> E randomOne(final Collection<E> objects) {
        return randomOneWithExcludedObject(objects, null);
    }

    /**
     * 在给定的对象中随机一个,概率是对象自身拥有的.但是可以指定排除某些索引
     *
     * @param objects   数据
     * @param exclusion 排除的索引
     * @param <E>       泛型
     * @return 随机结果, 如果没有随机结果, 返回null
     */
    public static <E extends RandomProbabilitySupplier> E randomOneWithExcludedIndex(final List<E> objects, final Collection<Integer> exclusion) {
        if (CollectionUtil.isEmpty(objects)) {
            return null;
        }

        int probabilitySum = 0;
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) == null) {
                continue;
            }
            if (exclusion != null && exclusion.contains(i)) {
                continue;
            }
            probabilitySum = probabilitySum + objects.get(i).getRandomProbability();
        }

        if (probabilitySum > 0) {
            int probability = 0;
            //这里需要+1,防止0概率事件被随机出来
            int randomValue = randomInt(probabilitySum) + 1;
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i) == null) {
                    continue;
                }
                if (exclusion != null && exclusion.contains(i)) {
                    continue;
                }
                probability = probability + objects.get(i).getRandomProbability();
                if (randomValue <= probability) {
                    //命中
                    return objects.get(i);
                }
            }
        }

        return null;
    }

    /**
     * 在给定的对象中随机一个,概率是对象自身拥有的.但是可以指定排除某些对象
     *
     * @param objects   数据
     * @param exclusion 排除的对象
     * @param <E>       泛型
     * @return 随机结果, 如果没有随机结果, 返回null
     */
    public static <E extends RandomProbabilitySupplier> E randomOneWithExcludedObject(final Collection<E> objects, final Collection<E> exclusion) {
        if (CollectionUtil.isEmpty(objects)) {
            return null;
        }

        int probabilitySum = 0;
        for (E object : objects) {
            if (object == null) {
                continue;
            }
            if (exclusion != null && exclusion.contains(object)) {
                continue;
            }
            probabilitySum = probabilitySum + object.getRandomProbability();
        }

        if (probabilitySum > 0) {
            int probability = 0;
            //这里需要+1,防止0概率事件被随机出来
            int randomValue = randomInt(probabilitySum) + 1;
            for (E object : objects) {
                if (object == null) {
                    continue;
                }
                if (exclusion != null && exclusion.contains(object)) {
                    continue;
                }
                probability = probability + object.getRandomProbability();
                if (randomValue <= probability) {
                    //命中
                    return object;
                }
            }
        }

        return null;
    }

    /**
     * 在给定的对象中随机一个,概率是通过function指定的
     *
     * @param objects             数据
     * @param probabilityFunction 自身概率
     * @param <E>                 泛型
     * @return 随机结果, 如果没有随机结果, 返回null
     */
    public static <E> E randomOne(final Collection<E> objects, final Function<E, Integer> probabilityFunction) {
        if (CollectionUtil.isEmpty(objects)) {
            return null;
        }


        int probabilitySum = 0;
        for (E object : objects) {
            if (object == null) {
                continue;
            }
            probabilitySum = probabilitySum + probabilityFunction.apply(object);
        }

        if (probabilitySum > 0) {
            int probability = 0;
            //这里需要+1,防止0概率事件被随机出来
            int randomValue = randomInt(probabilitySum) + 1;
            for (E object : objects) {
                if (object == null) {
                    continue;
                }
                probability = probability + probabilityFunction.apply(object);
                if (randomValue <= probability) {
                    //命中
                    return object;
                }
            }
        }

        return null;
    }

}
