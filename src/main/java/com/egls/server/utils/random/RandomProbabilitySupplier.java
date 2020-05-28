package com.egls.server.utils.random;

/**
 * 一个随机概率的提供者
 *
 * @author mayer - [Created on 2018-08-09 16:10]
 */
public interface RandomProbabilitySupplier {

    /**
     * 得到一个随机概率,只要返回自己的概率即可
     *
     * @return 概率
     */
    int getRandomProbability();

}
