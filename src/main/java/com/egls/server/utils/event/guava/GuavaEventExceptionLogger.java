package com.egls.server.utils.event.guava;

import java.lang.reflect.Method;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mayer - [Created on 2018-09-08 16:18]
 * @see com.google.common.eventbus.EventBus.LoggingHandler
 */
public class GuavaEventExceptionLogger implements SubscriberExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaEventExceptionLogger.class);

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        LOGGER.error(message(context), exception);
    }

    /**
     * 拷贝的私有方法
     *
     * @see com.google.common.eventbus.EventBus.LoggingHandler#message(SubscriberExceptionContext)
     */
    private static String message(SubscriberExceptionContext context) {
        Method method = context.getSubscriberMethod();
        return "Exception thrown by subscriber method "
                + method.getName()
                + '('
                + method.getParameterTypes()[0].getName()
                + ')'
                + " on subscriber "
                + context.getSubscriber()
                + " when dispatching event: "
                + context.getEvent();
    }

}
