package com.egls.server.utils.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间 - 字符串, 互相转换.
 *
 * @author mayer - [Created on 2018-08-20 16:38]
 */
public enum DateTimeString {

    /**
     * 格式化时间,使用':'作为间隔. 例如13点02分35秒表示为'13:02:35'
     */
    TIME(true, "HH:mm:ss"),

    /**
     * 格式化日期,使用'-'作为间隔. 例如2018年3月13日表示为'2018-03-13'
     */
    DATE(true, "yyyy-MM-dd"),

    /**
     * 格式化日期部分,但是年月日之间没有任何分隔符号. 例如2018年3月13日表示为'20180313'
     */
    DATE_NONE_SEPARATOR(true, "yyyyMMdd"),

    /**
     * 格式化日期和时间,日期部分使用'-'作为间隔,时间部分使用':'作为间隔.日期和时间部分使用' '连接
     * 例如2018年3月13日13点02分35秒表示为'2018-03-13 13:02:35'
     */
    DATE_TIME(true, "yyyy-MM-dd HH:mm:ss"),

    /**
     * 格式化日期和时间和毫秒,日期部分使用'-'作为间隔,时间部分使用':'作为间隔.日期和时间部分使用' '连接,毫秒部分使用','连接
     * 例如2018年3月13日13点02分35秒213毫秒表示为'2018-03-13 13:02:35,213'
     */
    DATE_TIME_MILLIS(true, "yyyy-MM-dd HH:mm:ss,SSS"),

    /**
     * 格式化日期和时间和毫秒和时区,日期部分使用'-'作为间隔,时间部分使用':'作为间隔.日期和时间部分使用' '连接,毫秒部分使用','连接,时区部分使用' '连接
     * 例如北京时间2018年3月13日13点02分35秒213毫秒表示为'2018-03-13 13:02:35,213 +0800'
     */
    DATE_TIME_MILLIS_ZONE(false, "yyyy-MM-dd HH:mm:ss,SSS Z");

    private final boolean local;

    private final DateTimeFormatter formatter;

    DateTimeString(final boolean local, final String formatterString) {
        this.local = local;
        this.formatter = DateTimeFormatter.ofPattern(formatterString);
    }

    public final String toString(final LocalDateTime localDateTime) {
        if (local) {
            return localDateTime.format(formatter);
        } else {
            return localDateTime.atZone(ZoneId.systemDefault()).format(formatter);
        }
    }

    public final String toString(final Date date) {
        return toString(DateTimeUtil.toLocalDateTime(date));
    }

    public final LocalDateTime toDateTime(final CharSequence dateTimeCharSequence) {
        if (local) {
            return LocalDateTime.parse(dateTimeCharSequence, formatter);
        } else {
            return ZonedDateTime.parse(dateTimeCharSequence, formatter).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        }
    }

    public final Date toUtilDate(final CharSequence dateTimeCharSequence) {
        return DateTimeUtil.toUtilDate(toDateTime(dateTimeCharSequence));
    }

}
