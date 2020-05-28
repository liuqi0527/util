package com.egls.server.utils.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;

/**
 * 表示一段时间的单位.
 * 这里单位分为, "年 月 日 时 分 秒 星期"
 *
 * @author mayer - [Created on 2018-08-20 14:24]
 */
public enum DateTimeUnit {

    /**
     * 表示1秒概念的单位,基于ISO-8601标准
     */
    SECOND,

    /**
     * 表示1分钟概念的单位,基于ISO-8601标准
     */
    MINUTE,

    /**
     * 表示1小时概念的单位,基于ISO-8601标准
     */
    HOUR,

    /**
     * 表示1天概念的单位,基于ISO-8601标准
     */
    DAY,

    /**
     * 表示1星期概念的单位,基于ISO-8601标准
     */
    WEEK,

    /**
     * 表示1个月概念的单位,基于ISO-8601标准
     */
    MONTH,

    /**
     * 表示1年概念的单位,基于ISO-8601标准
     */
    YEAR,;

    /**
     * 根据单位, 利用一个基准时间, 前后调整时间. 基于ISO-8601标准.
     *
     * @param baseDateTime 基准时间
     * @param amount       调整量, 正数是向后调整, 负数是向前调整
     * @return 调整后的时间结果
     */
    public final LocalDateTime adjust(final LocalDateTime baseDateTime, final long amount) {
        if (amount == 0) {
            return baseDateTime;
        }

        switch (this) {
            case SECOND:
                return baseDateTime.plusSeconds(amount);
            case MINUTE:
                return baseDateTime.plusMinutes(amount);
            case HOUR:
                return baseDateTime.plusHours(amount);
            case DAY:
                return baseDateTime.plusDays(amount);
            case WEEK:
                return baseDateTime.plusWeeks(amount);
            case MONTH:
                return baseDateTime.plusMonths(amount);
            case YEAR:
                return baseDateTime.plusYears(amount);
            default:
                return null;
        }
    }

    /**
     * 根据单位, 利用一个基准日期, 前后调整日期. 基于ISO-8601标准.
     *
     * @param baseDate 基准日期
     * @param amount   调整量, 正数是向后调整, 负数是向前调整
     * @return 调整后的日期结果
     */
    public final LocalDate adjust(final LocalDate baseDate, final long amount) {
        if (amount == 0) {
            return baseDate;
        }

        switch (this) {
            case DAY:
                return baseDate.plusDays(amount);
            case WEEK:
                return baseDate.plusWeeks(amount);
            case MONTH:
                return baseDate.plusMonths(amount);
            case YEAR:
                return baseDate.plusYears(amount);
            default:
                return null;
        }
    }

    /**
     * 根据单位, 利用一个基准日期, 前后调整日期. 基于ISO-8601标准.
     * 可以指定调整后, 是否以时间整点开始. 时间整点是根据单位的不同不一样.
     * <pre>
     *     startWithZero 表示生成的时间是否从整点开始.整点的定义是依据每种类型的.
     *     例如: type={@link #MINUTE},那么整点就是每分钟的0秒.
     *     例如: type={@link #HOUR},那么整点就是每小时的0分.
     *     例如: type={@link #DAY},那么整点就是每天的0点0分0秒.
     *     例如: type={@link #WEEK},那么整点就是每周一的0点0分0秒.
     *     例如: type={@link #MONTH},那么整点就是每月1日的0点0分0秒.
     *     例如: type={@link #YEAR},那么整点就是每年1月1日的0点0分0秒.
     * </pre>
     *
     * <pre>
     *     amount 表示调整量,支持 [负数] [0] [正数].
     *     当 amount 是负数的时候,表示下一个时间是向前的.
     *     当 amount 是0的时候,表示下一个时间是不变的.
     *     当 amount 是正数的时候,表示下一个时间是向后的.
     *
     *     例如: 传进去的amount=8,type={@link #MINUTE},那么就是生成给定时间的8分钟后的时间.
     *     例如: 传进去的amount=8,type={@link #HOUR},那么就是生成给定时间的8小时后的时间.
     *     例如: 传进去的amount=8,type={@link #DAY},那么就是生成给定时间的8天后的时间.
     *     例如: 传进去的amount=8,type={@link #WEEK},那么就是生成给定时间的8周后的时间.
     *     例如: 传进去的amount=8,type={@link #MONTH},那么就是生成给定时间的8月后的时间.
     *     例如: 传进去的amount=8,type={@link #YEAR},那么就是生成给定时间的8年后的时间.
     * </pre>
     *
     * <pre>
     *     通常这样没什么大不了的.但是配合startWithZero就变得神奇起来了.
     *     例如: amount = -2, startWithZero = true. 表示按type向前.例如type=WEEK,取8周前的星期一.
     *     例如: amount = +0, startWithZero = true. 表示按type取整.例如type=WEEK,就是取当前的星期一.
     *     例如: amount = +2, startWithZero = true. 表示按type向后.例如type=WEEK,取8周后的星期一.
     * </pre>
     *
     * <pre>
     *     用法表:
     *     =====================================================================================
     *     |type = SECOND
     *     |time = 2016-01-01 13:10:20,999
     *     |amount =  2 | startWithZero ? (2016-01-01 13:10:22,000) : (2016-01-01 13:10:22,999)
     *     |amount =  0 | startWithZero ? (2016-01-01 13:10:20,000) : (2016-01-01 13:10:20,999)
     *     |amount = -2 | startWithZero ? (2016-01-01 13:10:18,000) : (2016-01-01 13:10:18,999)
     *     =====================================================================================
     *     |type = MINUTE
     *     |time = 2016-01-01 13:10:20,999
     *     |amount =  2 | startWithZero ? (2016-01-01 13:12:00,000) : (2016-01-01 13:12:20,999)
     *     |amount =  0 | startWithZero ? (2016-01-01 13:10:00,000) : (2016-01-01 13:10:20,999)
     *     |amount = -2 | startWithZero ? (2016-01-01 13:08:00,000) : (2016-01-01 13:08:20,999)
     *     =====================================================================================
     *     |type = HOUR
     *     |time = 2016-01-01 13:10:20,999
     *     |amount =  2 | startWithZero ? (2016-01-01 15:00:00,000) : (2016-01-01 15:10:20,999)
     *     |amount =  0 | startWithZero ? (2016-01-01 13:00:00,000) : (2016-01-01 13:10:20,999)
     *     |amount = -2 | startWithZero ? (2016-01-01 11:00:00,000) : (2016-01-01 11:10:20,999)
     *     =====================================================================================
     *     |type = DAY
     *     |time = 2016-01-01 13:10:20,999
     *     |amount =  2 | startWithZero ? (2016-01-03 00:00:00,000) : (2016-01-03 13:10:20,999)
     *     |amount =  0 | startWithZero ? (2016-01-01 00:00:00,000) : (2016-01-01 13:10:20,999)
     *     |amount = -2 | startWithZero ? (2015-12-30 00:00:00,000) : (2015-12-30 13:10:20,999)
     *     =====================================================================================
     *     |type = WEEK
     *     |time = 2016-01-01 13:10:20,999
     *     |amount =  2 | startWithZero ? (2016-01-11 00:00:00,000) : (2016-01-15 13:10:20,999)
     *     |amount =  0 | startWithZero ? (2015-12-28 00:00:00,000) : (2016-01-01 13:10:20,999)
     *     |amount = -2 | startWithZero ? (2015-12-14 00:00:00,000) : (2015-12-18 13:10:20,999)
     *     =====================================================================================
     *     |type = MONTH
     *     |time = 2016-01-01 13:10:20,999
     *     |amount =  2 | startWithZero ? (2016-03-01 00:00:00,000) : (2016-03-01 13:10:20,999)
     *     |amount =  0 | startWithZero ? (2016-01-01 00:00:00,000) : (2016-01-01 13:10:20,999)
     *     |amount = -2 | startWithZero ? (2015-11-01 00:00:00,000) : (2015-11-01 13:10:20,999)
     *     =====================================================================================
     *     |type = YEAR
     *     |time = 2016-01-01 13:10:20,999
     *     |amount =  2 | startWithZero ? (2018-01-01 00:00:00,000) : (2018-01-01 13:10:20,999)
     *     |amount =  0 | startWithZero ? (2016-01-01 00:00:00,000) : (2016-01-01 13:10:20,999)
     *     |amount = -2 | startWithZero ? (2014-01-01 00:00:00,000) : (2014-01-01 13:10:20,999)
     *     =====================================================================================
     * </pre>
     *
     * @param baseDateTime  基准日期
     * @param startWithZero 表示生成的时间是否从整点开始.整点的定义是依据每种单位的.
     * @param amount        调整量, 正数是向后调整, 负数是向前调整
     * @return 调整后的时间结果
     */
    public final LocalDateTime adjust(final LocalDateTime baseDateTime, final boolean startWithZero, final int amount) {
        // 这里不做amount为0的判断, 因为amount为0, 也许是为了startWithZero操作.
        LocalDateTime op = startWithZero ? baseDateTime.withNano(0) : baseDateTime;
        switch (this) {
            case SECOND:
                return op.plusSeconds(amount);
            case MINUTE:
                return startWithZero ? op.withSecond(0).plusMinutes(amount) : op.plusMinutes(amount);
            case HOUR:
                return startWithZero ? op.withMinute(0).withSecond(0).plusHours(amount) : op.plusHours(amount);
            case DAY:
                return startWithZero ? op.withHour(0).withMinute(0).withSecond(0).plusDays(amount) : op.plusDays(amount);
            case WEEK:
                return startWithZero ? op.with(WeekFields.ISO.getFirstDayOfWeek()).withHour(0).withMinute(0).withSecond(0).plusWeeks(amount) : op.plusWeeks(amount);
            case MONTH:
                return startWithZero ? LocalDateTime.of(op.getYear(), op.getMonth(), 1, 0, 0, 0).plusMonths(amount) : op.plusMonths(amount);
            case YEAR:
                return startWithZero ? LocalDateTime.of(op.getYear(), 1, 1, 0, 0, 0).plusYears(amount) : op.plusYears(amount);
            default:
                return null;
        }
    }

    /**
     * <pre>
     *     基于ISO-8601标准,比较两个时间是否在某一个单位上是相等的.
     *     如果判断基于{@link #WEEK}的此方法,则表示判断两个时间是否位于日历的同一个星期内.
     *
     *     For example:判断两个时间是否在日历的同一个星期内.
     *
     *     if(WEEK.equals(LocalDateTime.now(), LocalDateTime.now())) {
     *          //do something...
     *     }
     * </pre>
     *
     * @param one     第一个时间
     * @param another 第二个时间
     * @return true 在同一个单位内.基于ISO-8601标准
     */
    public final boolean equals(final LocalDateTime one, final LocalDateTime another) {
        //星期单独进行处理
        if (this.equals(WEEK)) {
            //可能出现相差几天是同一星期,但不是同一年的情况.比如2014年和2015年两年交界的那个星期,所以采用基于年的星期数,这样正确处理年交界之处的星期
            if (one.getYear() != another.getYear()) {
                //如果不加这个判断可能导致出现在两个年里,又不在同一星期内,
                //但是基于年取星期就会出现星期一样的情况.比如2013年和2015年.差7天肯定不在一个星期了
                if (Math.abs(one.getYear() - another.getYear()) > 1) {
                    return false;
                }
            }
            return one.get(WeekFields.ISO.weekOfWeekBasedYear()) == another.get(WeekFields.ISO.weekOfWeekBasedYear());
        }

        if (one.getYear() != another.getYear()) {
            return false;
        }
        if (this.equals(YEAR)) {
            return true;
        }

        if (one.getMonthValue() != another.getMonthValue()) {
            return false;
        }
        if (this.equals(MONTH)) {
            return true;
        }

        if (one.getDayOfMonth() != another.getDayOfMonth()) {
            return false;
        }
        if (this.equals(DAY)) {
            return true;
        }

        if (one.getHour() != another.getHour()) {
            return false;
        }
        if (this.equals(HOUR)) {
            return true;
        }

        if (one.getMinute() != another.getMinute()) {
            return false;
        }
        if (this.equals(MINUTE)) {
            return true;
        }

        if (one.getSecond() != another.getSecond()) {
            return false;
        }
        if (this.equals(SECOND)) {
            return true;
        }
        return true;
    }


}
