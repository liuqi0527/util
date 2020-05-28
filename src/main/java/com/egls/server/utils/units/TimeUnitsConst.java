package com.egls.server.utils.units;

import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 提供一些时间单位
 *
 * @author mayer - [Created on 2018-08-09 15:19]
 */
public final class TimeUnitsConst {

    //@formatter:off

    public static final long DAYS_OF_WEEK                     = ChronoUnit.WEEKS.getDuration().toDays();

    public static final long HOURS_OF_DAY                     = ChronoUnit.DAYS.getDuration().toHours();

    public static final long MINUTES_OF_HOUR                  = ChronoUnit.HOURS.getDuration().toMinutes();

    public static final long MINUTES_OF_DAY                   = ChronoUnit.DAYS.getDuration().toMinutes();

    public static final long SECONDS_OF_MINUTE                = ChronoUnit.MINUTES.getDuration().getSeconds();

    public static final long SECONDS_OF_HOUR                  = ChronoUnit.HOURS.getDuration().getSeconds();

    public static final long SECONDS_OF_DAY                   = ChronoUnit.DAYS.getDuration().getSeconds();

    public static final long MILLIS_OF_SECOND                 = ChronoUnit.SECONDS.getDuration().toMillis();

    public static final long MILLIS_OF_MINUTE                 = ChronoUnit.MINUTES.getDuration().toMillis();

    public static final long MILLIS_OF_HOUR                   = ChronoUnit.HOURS.getDuration().toMillis();

    public static final long MILLIS_OF_DAY                    = ChronoUnit.DAYS.getDuration().toMillis();

    //---

    public static final long FIVE_MILLISECONDS                = 5L;

    public static final long TEN_MILLISECONDS                 = 10L;

    public static final long TWENTY_MILLISECONDS              = 20L;

    public static final long FIFTY_MILLISECONDS               = 50L;

    public static final long ONE_HUNDRED_MILLISECONDS         = 100L;

    public static final long TWO_HUNDRED_MILLISECONDS         = 200L;

    public static final long FIVE_HUNDRED_MILLISECONDS        = 500L;

    public static final long MILLIS_OF_FIVE_SECONDS           = 5 * MILLIS_OF_SECOND;

    public static final long MILLIS_OF_TEN_SECONDS            = 10 * MILLIS_OF_SECOND;

    public static final long MILLIS_OF_TWENTY_SECONDS         = 20 * MILLIS_OF_SECOND;

    public static final long MILLIS_OF_THIRTY_SECONDS         = 30 * MILLIS_OF_SECOND;

    public static final long MILLIS_OF_HALF_MINUTE            = MILLIS_OF_THIRTY_SECONDS;

    //@formatter:on

    /**
     * 防止其他使用的地方,javac 编译 inline
     */
    public static final long BLOCKING_QUEUE_POLL_WAITING_MILLISECONDS = ObjectUtils.CONST(FIVE_MILLISECONDS);

}
