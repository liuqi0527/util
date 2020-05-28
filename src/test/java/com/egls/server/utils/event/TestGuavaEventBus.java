package com.egls.server.utils.event;

import com.egls.server.utils.event.guava.GuavaEventExceptionLogger;
import com.google.common.eventbus.EventBus;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-08 16:11]
 */
public class TestGuavaEventBus {

    @Test
    public void test() {
        final EventBus eventBus = new EventBus(new GuavaEventExceptionLogger());

        final TestGuavaEventListener eventListener = new TestGuavaEventListener();
        eventBus.register(eventListener);

        eventBus.post(new TestGuavaEvent(1));
        eventBus.post(2);
        eventBus.post(3L);

        Assert.assertEquals(1, eventListener.getLastAmount());
        Assert.assertEquals(2, (int) eventListener.getLastInteger());
        Assert.assertEquals(3L, (long) eventListener.getLastLong());
    }

}
