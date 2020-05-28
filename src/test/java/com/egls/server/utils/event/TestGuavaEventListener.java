package com.egls.server.utils.event;

import com.google.common.eventbus.Subscribe;

/**
 * @author mayer - [Created on 2018-09-08 16:23]
 */
class TestGuavaEventListener {

    private Integer lastInteger;

    private Long lastLong;

    private int lastAmount = 0;

    @Subscribe
    public void listen(TestGuavaEvent event) {
        lastAmount = event.getAmount();
    }

    @Subscribe
    public void listenInteger(Integer event) {
        lastInteger = event;
    }

    @Subscribe
    public void listenLong(Long event) {
        lastLong = event;
    }

    public Integer getLastInteger() {
        return lastInteger;
    }

    public Long getLastLong() {
        return lastLong;
    }

    public int getLastAmount() {
        return lastAmount;
    }

}
