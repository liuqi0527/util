package com.egls.server.utils.date.expression;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.util.Arrays;
import java.util.Date;

import com.egls.server.utils.StringUtil;
import com.egls.server.utils.array.ArrayUtil;
import com.egls.server.utils.date.DateTimeUnit;
import com.egls.server.utils.date.DateTimeUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import static java.time.temporal.ChronoField.*;

/**
 * <pre>
 * 一个表达一段时间的表达式,判断给定的时间是否满足这个表达式.
 * 这里认为,一秒也算一段时间,只是长度比较小.
 * 在本类中没有时区的概念的,都是纳秒级别的本地的时间.
 * </pre>
 * <pre>
 *     表达式位置含义:
 *     秒 分 时 天 月 星期 年
 * </pre>
 * <pre>
 *     [列]指空格区分的时间表达式位置
 *     [列]上允许出现1-2这样的一个表示范围的配置,仅写1表示1-1,是一种简写
 *     符号:
 *     '-' 表示起止范围,单独的列是不允许回卷的.配合其他列允许回卷,5-3是不允许的,5-3 1-2是允许的,所有的列的起止范围结合起来用来确定一个时间段
 *     '*' 表示忽略检测该列或者该列可以是任意值
 *     ',' 用来区分一列内的起止范围,注意:所有的列内的起止范围的个数必须相同或者是1. 例如1,1 2,2,2是不允许的, 1,1 2,2是允许的, 1 2,2是允许的.
 *     '~' 表示省略中间的','符号.例如:17~19表示17,18,19
 * </pre>
 * <pre>
 *     简写解释:
 *     1                      =        1-1
 *     1,2                    =        1-1,2-2
 *     1-2,4                  =        1-2,4-4
 *     1~4                    =        1,2,3,4
 *     * * 1 1,2 * * *        =        * * 1-1,1-1 1-1,2-2 * * *
 *     * * 1 1~4 * * *        =        * * 1-1,1-1,1-1,1-1 1-1,2-2,3-3,4-4 * * *
 * </pre>
 *
 * @author mayer - [Created on 2018-08-20 23:20]
 */
public final class DateTimeExpression {

    //TODO: 根据需求重新制作表达式 DMBJ-3677 ,可以考虑使用quartz的表达式组装需求

    private static final char TO = '-';
    private static final char ANY = '*';
    private static final char SEPARATOR = ',';
    private static final char ABBREVIATED_SEPARATOR = '~';

    private static final boolean BACKWARD = true;
    private static final boolean FORWARD = false;

    public static DateTimeExpression of(final String[] expressionParts) {
        return new DateTimeExpression(StringUtils.join(expressionParts, ' '));
    }

    public static DateTimeExpression of(final String expressionString) {
        return new DateTimeExpression(expressionString);
    }

    private static int correctDayOfMonth(final int year, final int month, final int dayOfMonth) {
        YEAR.checkValidValue(year);
        MONTH_OF_YEAR.checkValidValue(month);
        DAY_OF_MONTH.checkValidValue(dayOfMonth);
        int maxDayOfMonth;
        switch (month) {
            case 2:
                maxDayOfMonth = (IsoChronology.INSTANCE.isLeapYear(year) ? 29 : 28);
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                maxDayOfMonth = 30;
                break;
            default:
                maxDayOfMonth = 31;
                break;
        }
        return Math.min(dayOfMonth, maxDayOfMonth);
    }

    private static void checkFormat(final String expressionString) throws IllegalArgumentException {
        if (StringUtils.split(expressionString).length != DateTimeExpressionPart.values().length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid Expression : %s, cause : please uses whitespace as the separator for each part, the number of parts must be %d."
                    , expressionString
                    , DateTimeExpressionPart.values().length
            ));
        }
        for (char c : expressionString.toCharArray()) {
            if (c == TO) {
                continue;
            }
            if (c == ANY) {
                continue;
            }
            if (c == SEPARATOR) {
                continue;
            }
            if (c == ABBREVIATED_SEPARATOR) {
                continue;
            }
            if (c >= '0' && c <= '9') {
                continue;
            }
            if (Character.isSpaceChar(c)) {
                continue;
            }
            throw new IllegalArgumentException(String.format(
                    "Invalid Expression : %s, cause : can not parse expression, it contains illegal characters. use 01234567899-*,~"
                    , expressionString
            ));
        }
    }

    /**
     * <pre>
     * from [3,8~13,4-5] to [3,8,9,10,11,12,13,4-5]
     * from [3,4-5,8~13] to [3,4-5,8,9,10,11,12,13]
     * from [3,4-5,8~13,5-5,3~3,3~2] to [3,4-5,8,9,10,11,12,13,5-5,3]
     * </pre>
     */
    private static String formatAbbreviatedSeparator(final String part) {
        //转换简写
        if (StringUtils.contains(part, ABBREVIATED_SEPARATOR)) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] array = StringUtils.split(part, SEPARATOR);
            for (String abbreviatedString : array) {
                if (StringUtils.contains(abbreviatedString, ABBREVIATED_SEPARATOR)) {
                    String[] ellipsisArray = StringUtils.split(abbreviatedString, ABBREVIATED_SEPARATOR);
                    int low = Integer.parseInt(ellipsisArray[0]);
                    int high = Integer.parseInt(ellipsisArray[1]);
                    while (low <= high) {
                        stringBuilder.append(low++);
                        stringBuilder.append(SEPARATOR);
                    }
                } else {
                    stringBuilder.append(abbreviatedString).append(SEPARATOR);
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }
        return part;
    }

    /**
     * <pre>
     * separatorCount = 2
     * from [*] to [*,*,*]
     * from [2] to [2-2,2-2,2-2]
     * from [2-3] to [2-3,2-3,2-3]
     * from [2-3,6,8] to [2-3,6-6,8-8]
     * from [*,2,4] to [*,2-2,4-4]
     * from [*,2,4-5] to [*,2-2,4-5]
     * </pre>
     */
    private static String formatRangeAndSeparatorCount(final int separatorCount, final String string) {
        String part = string;
        if (!StringUtils.contains(part, SEPARATOR)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(part);
            for (int i = 0; i < separatorCount; i++) {
                stringBuilder.append(SEPARATOR);
                stringBuilder.append(part);
            }
            part = stringBuilder.toString();
        }
        String[] array = StringUtils.split(part, SEPARATOR);
        for (int i = 0; i < array.length; i++) {
            if (!StringUtils.contains(array[i], ANY)) {
                if (!StringUtils.contains(array[i], TO)) {
                    array[i] = array[i] + TO + array[i];
                }
            }
        }
        StringBuilder result = new StringBuilder();
        result.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            result.append(SEPARATOR);
            result.append(array[i]);
        }
        return result.toString();
    }

    private final DateTimeExpressionRange<Integer>[][] rows;
    private final String expressionString;

    @SuppressWarnings("unchecked")
    private DateTimeExpression(final String expressionString) {
        this.expressionString = expressionString;
        checkFormat(expressionString);
        String[] expressionParts = StringUtils.split(expressionString);
        //分成7组,并且处理简写的问题
        for (int i = 0; i < expressionParts.length; i++) {
            expressionParts[i] = formatAbbreviatedSeparator(expressionParts[i]);
        }
        //求每列的分隔符个数,并检测每列是否个数相同
        int tempSeparatorCount = 0;
        for (String expressionPart : expressionParts) {
            int separatorCount = StringUtils.countMatches(expressionPart, SEPARATOR);
            if (separatorCount > 0) {
                if (tempSeparatorCount > 0 && separatorCount != tempSeparatorCount) {
                    throw new IllegalArgumentException(String.format(
                            "Invalid Expression : %s, cause : not the same as the length of each part."
                            , expressionString
                    ));
                }
                tempSeparatorCount = separatorCount;
            }
        }
        //按照分隔符个数,进行最后的整理格式化
        for (int i = 0; i < expressionParts.length; i++) {
            expressionParts[i] = formatRangeAndSeparatorCount(tempSeparatorCount, expressionParts[i]);
        }

        //构造二维数组
        rows = new DateTimeExpressionRange[tempSeparatorCount + 1][expressionParts.length];
        for (int row = 0; row < rows.length; row++) {
            //每个数组就是一个日期.any的地方pair是null
            for (int col = 0; col < rows[row].length; col++) {
                String[] array = StringUtils.split(expressionParts[col], SEPARATOR);
                if (!StringUtils.contains(array[row], ANY)) {
                    String[] rangeArray = StringUtils.split(array[row], TO);
                    if (rangeArray.length < 2) {
                        throw new IllegalArgumentException(String.format(
                                "Invalid Expression : %s, cause : the symbol must connect two numbers. symbol left and right character must be a number. %s."
                                , expressionString
                                , Arrays.toString(expressionParts)
                        ));
                    }
                    DateTimeExpressionRange<Integer> colRange = DateTimeExpressionRange.between(Integer.decode(rangeArray[0]), Integer.decode(rangeArray[1]));
                    if (DateTimeExpressionPart.values()[col].getDefaultRange().containsRange(colRange)) {
                        rows[row][col] = colRange;
                    } else {
                        throw new IllegalArgumentException(String.format(
                                "Invalid Expression : %s, cause : %s must in %s."
                                , expressionString
                                , DateTimeExpressionPart.values()[col].name()
                                , DateTimeExpressionPart.values()[col].getDefaultRange().toString()
                        ));
                    }
                }
            }
        }
        //最后检测有效性.
        checkValid();
    }

    private void checkValid() {
        checkRangeValid();
        checkConflict();
    }

    private void checkRangeValid() {
        //检测时间条目的回卷是否正确.
        LocalDateTime now = LocalDateTime.now();
        for (DateTimeExpressionRange<Integer>[] row : rows) {
            DateTimeExpressionRange<Integer> week = row[DateTimeExpressionPart.DAY_OF_WEEK.ordinal()] != null
                    ? row[DateTimeExpressionPart.DAY_OF_WEEK.ordinal()]
                    : DateTimeExpressionRange.between(now.getDayOfWeek().getValue(), now.getDayOfWeek().getValue());
            if (week.getMinimum() > week.getMaximum()) {
                throw new IllegalArgumentException(String.format(
                        "Invalid Expression : %s, cause : dayOfWeek error, left must greater than right. %s"
                        , expressionString, week.toString()));
            }

            Pair<LocalDateTime, LocalDateTime> pair = getDateTime(now, row);
            LocalDateTime min = pair.getLeft();
            LocalDateTime max = pair.getRight();
            if (min.isAfter(max)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid Expression : %s, cause : range error, left must greater than right. %s ~ %s"
                        , expressionString
                        , String.format(
                                "%s-%s-%s %s:%s:%s"
                                , row[DateTimeExpressionPart.YEAR.ordinal()] == null ? "*" : row[DateTimeExpressionPart.YEAR.ordinal()].getMinimum()
                                , row[DateTimeExpressionPart.MONTH.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.MONTH.ordinal()].getMinimum().toString(), 2, '0')
                                , row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()].getMinimum().toString(), 2, '0')
                                , row[DateTimeExpressionPart.HOUR.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.HOUR.ordinal()].getMinimum().toString(), 2, '0')
                                , row[DateTimeExpressionPart.MINUTE.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.MINUTE.ordinal()].getMinimum().toString(), 2, '0')
                                , row[DateTimeExpressionPart.SECOND.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.SECOND.ordinal()].getMinimum().toString(), 2, '0')
                        )
                        , String.format(
                                "%s-%s-%s %s:%s:%s"
                                , row[DateTimeExpressionPart.YEAR.ordinal()] == null ? "*" : row[DateTimeExpressionPart.YEAR.ordinal()].getMaximum()
                                , row[DateTimeExpressionPart.MONTH.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.MONTH.ordinal()].getMaximum().toString(), 2, '0')
                                , row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()].getMaximum().toString(), 2, '0')
                                , row[DateTimeExpressionPart.HOUR.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.HOUR.ordinal()].getMaximum().toString(), 2, '0')
                                , row[DateTimeExpressionPart.MINUTE.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.MINUTE.ordinal()].getMaximum().toString(), 2, '0')
                                , row[DateTimeExpressionPart.SECOND.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.SECOND.ordinal()].getMaximum().toString(), 2, '0')
                        )));
            }
        }
    }

    private void checkConflict() {
        //检测当年,月,日,星期几都配置的时候出现的星期几和日的冲突.
        for (DateTimeExpressionRange<Integer>[] row : rows) {
            if (row[DateTimeExpressionPart.YEAR.ordinal()] != null
                    && row[DateTimeExpressionPart.MONTH.ordinal()] != null
                    && row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()] != null
                    && row[DateTimeExpressionPart.DAY_OF_WEEK.ordinal()] != null) {
                LocalDate min = LocalDate.of(
                        row[DateTimeExpressionPart.YEAR.ordinal()].getMinimum(),
                        row[DateTimeExpressionPart.MONTH.ordinal()].getMinimum(),
                        row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()].getMinimum()
                );
                LocalDate max = LocalDate.of(
                        row[DateTimeExpressionPart.YEAR.ordinal()].getMaximum(),
                        row[DateTimeExpressionPart.MONTH.ordinal()].getMaximum(),
                        row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()].getMaximum()
                );
                boolean flag = true;
                DateTimeExpressionRange<Integer> week = row[DateTimeExpressionPart.DAY_OF_WEEK.ordinal()];
                while (min.isBefore(max) || min.isEqual(max)) {
                    int dayOfWeek = min.getDayOfWeek().getValue();
                    if (dayOfWeek >= week.getMinimum() && dayOfWeek <= week.getMaximum()) {
                        flag = false;
                        break;
                    }
                    min = min.plusDays(1);
                }
                if (flag) {
                    throw new IllegalArgumentException(String.format(
                            "Invalid Expression : %s, cause : the dayOfMonth and dayOfWeek is conflict. %s ~ %s dayOfWeek %s"
                            , expressionString
                            , String.format("%s-%s-%s"
                                    , row[DateTimeExpressionPart.YEAR.ordinal()].getMinimum()
                                    , StringUtils.leftPad(row[DateTimeExpressionPart.MONTH.ordinal()].getMinimum().toString(), 2, '0')
                                    , StringUtils.leftPad(row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()].getMinimum().toString(), 2, '0'))
                            , String.format("%s-%s-%s"
                                    , row[DateTimeExpressionPart.YEAR.ordinal()].getMaximum()
                                    , StringUtils.leftPad(row[DateTimeExpressionPart.MONTH.ordinal()].getMaximum().toString(), 2, '0')
                                    , StringUtils.leftPad(row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()].getMaximum().toString(), 2, '0'))
                            , week.toString()));
                }
            }
        }
    }

    public final String getExpressionString() {
        return expressionString;
    }

    /**
     * 检测当前.
     */
    public final boolean checkCurrent() {
        return check(LocalDateTime.now());
    }

    public final boolean check(final Date date) {
        return check(DateTimeUtil.toLocalDateTime(date));
    }

    public final boolean check(final long millis) {
        return check(DateTimeUtil.toLocalDateTime(millis));
    }

    public final boolean check(final LocalDateTime localDateTime) {
        for (DateTimeExpressionRange<Integer>[] row : rows) {
            if (checkRow(row, localDateTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see #nextDateTime(LocalDateTime)
     */
    public final LocalDateTime nextDateTime() {
        return nextDateTime(LocalDateTime.now());
    }

    /**
     * <pre>
     * 返回下一个时间.
     * 如果当前时间是满足表达式的,返回的时间是下一个不满足条件的时间.
     * 如果当前时间是不满足表达式的,返回的时间是下一个满足条件的时间.
     * 有可能返回的是null.
     * 例如* * * * * * *这样的表达式,求下一个时间.都是null的.
     * 因为其检测任何时间都是满足的,然后生成下一个不满足的时间.是不可能生成的.
     * </pre>
     */
    public final LocalDateTime nextDateTime(LocalDateTime base) {
        if (check(base)) {
            //生成下一个不满足的时间
            return nextInvalidDateTime(base);
        } else {
            //生成下一个满足的时间
            return nextValidDateTime(base);
        }
    }

    /**
     * 返回下一个满足时间表达式的时间点
     * 如果现在满足时间表达式.则返回现在的时间点
     * 否则返回最近的一个满足时间表达式的时间点
     */
    public final LocalDateTime nextValidDateTime() {
        return nextValidDateTime(LocalDateTime.now());
    }

    /**
     * 返回下一个不满足时间表达式的时间点
     * 如果现在满足时间表达式.则返回现在的时间点
     * 否则返回最近的一个不满足时间表达式的时间点
     * 有可能返回的是null.
     * 例如* * * * * * *这样的表达式,求下一个时间.都是null的.
     * 因为其检测任何时间都是满足的,然后生成下一个不满足的时间.是不可能生成的.
     */
    public final LocalDateTime nextInvalidDateTime() {
        return nextInvalidDateTime(LocalDateTime.now());
    }

    /**
     * 返回一个满足时间表达式的时间点.
     * 如果给定的时间满足时间表达式的,则返回给定的时间点.
     * 如果给定的时间不满足时间表达式的,则返回最近的一个满足时间表达式的时间点
     */
    public final LocalDateTime nextValidDateTime(LocalDateTime base) {
        if (check(base)) {
            //在有效期内,返回给定的时间点
            return base;
        } else {
            //不在有效期内,返回最近的一个有效时间点.
            return nextValidDateTimeImpl(base);
        }
    }

    /**
     * 返回一个不满足时间表达式的时间点.
     * 如果给定的时间不满足时间表达式,则返回给定的时间，否则返回一个最近的不满足时间表达式的时间点.
     * 有可能返回的是null.
     * 例如* * * * * * *这样的表达式,求下一个时间.都是null的.
     * 因为其检测任何时间都是满足的,然后生成下一个不满足的时间.是不可能生成的.
     */
    public final LocalDateTime nextInvalidDateTime(LocalDateTime base) {
        if (check(base)) {
            //在有效期内,返回最近的无效时间点
            return nextInvalidDateTimeImpl(base);
        } else {
            //不在有效期内,返回给定的时间点
            return base;
        }
    }

    /**
     * 注意:
     * 给定的时间是在不满足表达式的状态下.
     * 使用给定的时间为基准,生成下一个满足表达式的时间
     */
    private LocalDateTime nextValidDateTimeImpl(LocalDateTime base) {
        LocalDateTime[] results = new LocalDateTime[rows.length];
        for (int i = 0; i < rows.length; i++) {
            Pair<LocalDateTime, LocalDateTime> pair = getDateTime(base, rows[i]);
            LocalDateTime left = format(rows[i], pair.getLeft(), BACKWARD);
            LocalDateTime right = format(rows[i], pair.getRight(), FORWARD);
            if (left == null || right == null) {
                continue;
            }
            if (base.isBefore(left)) {
                results[i] = left;
            } else if (base.isBefore(right)) {
                results[i] = base;
            } else {
                //求下一个.规律.以出现值的位置之后的第一个任意符的位置的单位进行加1.(不考虑星期.)
                //如果值的后面没有通配符号,认为没有下一个.
                DateTimeExpressionPart[] checks = new DateTimeExpressionPart[]{
                        DateTimeExpressionPart.SECOND, DateTimeExpressionPart.MINUTE, DateTimeExpressionPart.HOUR,
                        DateTimeExpressionPart.DAY_OF_MONTH, DateTimeExpressionPart.MONTH, DateTimeExpressionPart.YEAR
                };

                boolean haveValue = false;
                DateTimeExpressionPart partType = null;
                for (DateTimeExpressionPart check : checks) {
                    if (rows[i][check.ordinal()] != null) {
                        haveValue = true;
                    }
                    if (haveValue && rows[i][check.ordinal()] == null) {
                        partType = check;
                        break;
                    }
                }
                if (partType == null) {
                    //全指定情况.
                    results[i] = null;
                } else {
                    results[i] = partType.nextOne(left);
                }
            }
            if (results[i] != null) {
                DateTimeExpressionRange<Integer> week = rows[i][DateTimeExpressionPart.DAY_OF_WEEK.ordinal()];
                if (week != null) {
                    while (true) {
                        int dayOfWeek = results[i].getDayOfWeek().getValue();
                        if (dayOfWeek >= week.getMinimum() && dayOfWeek <= week.getMaximum()) {
                            if (!check(results[i])) {
                                results[i] = null;
                            }
                            break;
                        } else {
                            results[i] = rows[i][DateTimeExpressionPart.HOUR.ordinal()] == null ? results[i].withHour(0) : results[i];
                            results[i] = rows[i][DateTimeExpressionPart.MINUTE.ordinal()] == null ? results[i].withMinute(0) : results[i];
                            results[i] = rows[i][DateTimeExpressionPart.SECOND.ordinal()] == null ? results[i].withSecond(0) : results[i];
                            results[i] = results[i].plusDays(1);
                        }
                    }
                }
            }
        }
        LocalDateTime result = null;
        for (LocalDateTime localDateTime : results) {
            if (localDateTime != null && localDateTime.isAfter(base)) {
                if (result == null || localDateTime.isBefore(result)) {
                    result = localDateTime;
                }
            }
        }
        return result;
    }

    /**
     * 注意:
     * 给定的时间是在满足表达式的状态下.
     * 使用给定的时间为基准,生成下一个不满足表达式的时间
     */
    private LocalDateTime nextInvalidDateTimeImpl(LocalDateTime base) {
        LocalDateTime[] results = new LocalDateTime[rows.length];
        for (int i = 0; i < rows.length; i++) {
            Pair<LocalDateTime, LocalDateTime> pair = getDateTime(base, rows[i]);
            LocalDateTime right = format(rows[i], pair.getRight(), FORWARD);
            if (right == null) {
                continue;
            }
            DateTimeExpressionRange<Integer> week = rows[i][DateTimeExpressionPart.DAY_OF_WEEK.ordinal()];
            if (week == null || DateTimeUnit.DAY.equals(base, right)) {
                results[i] = right.plusSeconds(1);
            } else {
                LocalDateTime temp = base;
                while (true) {
                    if (temp.isBefore(right)) {
                        int dayOfWeek = temp.getDayOfWeek().getValue();
                        if (dayOfWeek >= week.getMinimum() && dayOfWeek <= week.getMaximum()) {
                            temp = temp.withHour(0).withMinute(0).withSecond(0).plusDays(1);
                        } else {
                            results[i] = check(temp) ? null : temp;
                            break;
                        }
                    } else {
                        results[i] = right.plusSeconds(1);
                        break;
                    }
                }
            }
        }
        LocalDateTime result = null;
        for (LocalDateTime localDateTime : results) {
            if (localDateTime != null && localDateTime.isAfter(base)) {
                if (result == null || localDateTime.isBefore(result)) {
                    result = localDateTime;
                }
            }
        }
        return result;
    }

    private LocalDateTime format(DateTimeExpressionRange<Integer>[] row, LocalDateTime localDateTime, boolean direction) {
        //所有都是*,不能生成任何时间.
        if (ArrayUtil.isAllNull(row)) {
            return null;
        }
        LocalDateTime result = localDateTime;
        if (row[DateTimeExpressionPart.SECOND.ordinal()] == null) {
            result = direction == BACKWARD
                    ? result.withSecond(DateTimeExpressionPart.SECOND.defaultRange.getMinimum())
                    : result.withSecond(DateTimeExpressionPart.SECOND.defaultRange.getMaximum());
        } else {
            return result;
        }
        if (row[DateTimeExpressionPart.MINUTE.ordinal()] == null) {
            result = direction == BACKWARD
                    ? result.withMinute(DateTimeExpressionPart.MINUTE.defaultRange.getMinimum())
                    : result.withMinute(DateTimeExpressionPart.MINUTE.defaultRange.getMaximum());
        } else {
            return result;
        }
        if (row[DateTimeExpressionPart.HOUR.ordinal()] == null) {
            result = direction == BACKWARD
                    ? result.withHour(DateTimeExpressionPart.HOUR.defaultRange.getMinimum())
                    : result.withHour(DateTimeExpressionPart.HOUR.defaultRange.getMaximum());
        } else {
            return result;
        }
        if (row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()] == null) {
            result = direction == BACKWARD
                    ? result.withDayOfMonth(correctDayOfMonth(result.getYear(), result.getMonthValue(), DateTimeExpressionPart.DAY_OF_MONTH.defaultRange.getMinimum()))
                    : result.withDayOfMonth(correctDayOfMonth(result.getYear(), result.getMonthValue(), DateTimeExpressionPart.DAY_OF_MONTH.defaultRange.getMaximum()));
        } else {
            return result;
        }
        if (row[DateTimeExpressionPart.MONTH.ordinal()] == null) {
            //先设置一下日期,防止闰年闰月大小月的问题.
            result.withDayOfMonth(DateTimeExpressionPart.DAY_OF_MONTH.getDefaultRange().getMinimum());
            result = direction == BACKWARD
                    ? result.withMonth(DateTimeExpressionPart.MONTH.defaultRange.getMinimum())
                    : result.withMonth(DateTimeExpressionPart.MONTH.defaultRange.getMaximum());
            result = direction == BACKWARD
                    ? result.withDayOfMonth(correctDayOfMonth(result.getYear(), result.getMonthValue(), DateTimeExpressionPart.DAY_OF_MONTH.defaultRange.getMinimum()))
                    : result.withDayOfMonth(correctDayOfMonth(result.getYear(), result.getMonthValue(), DateTimeExpressionPart.DAY_OF_MONTH.defaultRange.getMaximum()));
        } else {
            return result;
        }
        if (row[DateTimeExpressionPart.YEAR.ordinal()] == null) {
            //异常.不可能出现全是空的情况
            return result;
        } else {
            return result;
        }
    }

    private boolean checkRow(final DateTimeExpressionRange<Integer>[] row, final LocalDateTime localDateTime) {
        DateTimeExpressionRange<Integer> week = row[DateTimeExpressionPart.DAY_OF_WEEK.ordinal()] != null
                ? row[DateTimeExpressionPart.DAY_OF_WEEK.ordinal()]
                : DateTimeExpressionRange.between(localDateTime.getDayOfWeek().getValue(), localDateTime.getDayOfWeek().getValue());
        int dayOfWeek = localDateTime.getDayOfWeek().getValue();
        if (dayOfWeek >= week.getMinimum() && dayOfWeek <= week.getMaximum()) {
            Pair<LocalDateTime, LocalDateTime> pair = getDateTime(localDateTime, row);
            boolean minFlag = localDateTime.isAfter(pair.getLeft()) || localDateTime.isEqual(pair.getLeft());
            boolean maxFlag = localDateTime.isBefore(pair.getRight()) || localDateTime.isEqual(pair.getRight());
            return minFlag && maxFlag;
        }
        return false;
    }

    private Pair<LocalDateTime, LocalDateTime> getDateTime(final LocalDateTime base, final DateTimeExpressionRange<Integer>[] row) {
        DateTimeExpressionRange<Integer> year = row[DateTimeExpressionPart.YEAR.ordinal()] != null
                ? row[DateTimeExpressionPart.YEAR.ordinal()]
                : DateTimeExpressionRange.between(base.getYear(), base.getYear());
        DateTimeExpressionRange<Integer> month = row[DateTimeExpressionPart.MONTH.ordinal()] != null
                ? row[DateTimeExpressionPart.MONTH.ordinal()]
                : DateTimeExpressionRange.between(base.getMonthValue(), base.getMonthValue());
        DateTimeExpressionRange<Integer> day = row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()] != null
                ? row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()]
                : DateTimeExpressionRange.between(base.getDayOfMonth(), base.getDayOfMonth());
        DateTimeExpressionRange<Integer> hour = row[DateTimeExpressionPart.HOUR.ordinal()] != null
                ? row[DateTimeExpressionPart.HOUR.ordinal()]
                : DateTimeExpressionRange.between(base.getHour(), base.getHour());
        DateTimeExpressionRange<Integer> minute = row[DateTimeExpressionPart.MINUTE.ordinal()] != null
                ? row[DateTimeExpressionPart.MINUTE.ordinal()]
                : DateTimeExpressionRange.between(base.getMinute(), base.getMinute());
        DateTimeExpressionRange<Integer> second = row[DateTimeExpressionPart.SECOND.ordinal()] != null
                ? row[DateTimeExpressionPart.SECOND.ordinal()]
                : DateTimeExpressionRange.between(base.getSecond(), base.getSecond());
        DateTimeExpressionRange<Integer> nano = DateTimeExpressionRange.between(0, 999_999_999);
        LocalDateTime min = LocalDateTime.of(
                year.getMinimum(), month.getMinimum(), correctDayOfMonth(year.getMinimum(), month.getMinimum(), day.getMinimum()),
                hour.getMinimum(), minute.getMinimum(), second.getMinimum(), nano.getMinimum()
        );
        LocalDateTime max = LocalDateTime.of(
                year.getMaximum(), month.getMaximum(), correctDayOfMonth(year.getMaximum(), month.getMaximum(), day.getMaximum()),
                hour.getMaximum(), minute.getMaximum(), second.getMaximum(), nano.getMaximum()
        );
        return Pair.of(min, max);
    }

    @Override
    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(expressionString);
        stringBuilder.append(StringUtil.getLineSeparator());
        for (DateTimeExpressionRange<Integer>[] row : rows) {
            String begin = String.format(
                    "%s-%s-%s %s:%s:%s"
                    , row[DateTimeExpressionPart.YEAR.ordinal()] == null ? "*" : row[DateTimeExpressionPart.YEAR.ordinal()].getMinimum()
                    , row[DateTimeExpressionPart.MONTH.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.MONTH.ordinal()].getMinimum().toString(), 2, '0')
                    , row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()].getMinimum().toString(), 2, '0')
                    , row[DateTimeExpressionPart.HOUR.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.HOUR.ordinal()].getMinimum().toString(), 2, '0')
                    , row[DateTimeExpressionPart.MINUTE.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.MINUTE.ordinal()].getMinimum().toString(), 2, '0')
                    , row[DateTimeExpressionPart.SECOND.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.SECOND.ordinal()].getMinimum().toString(), 2, '0')
            );
            String end = String.format(
                    "%s-%s-%s %s:%s:%s"
                    , row[DateTimeExpressionPart.YEAR.ordinal()] == null ? "*" : row[DateTimeExpressionPart.YEAR.ordinal()].getMaximum()
                    , row[DateTimeExpressionPart.MONTH.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.MONTH.ordinal()].getMaximum().toString(), 2, '0')
                    , row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.DAY_OF_MONTH.ordinal()].getMaximum().toString(), 2, '0')
                    , row[DateTimeExpressionPart.HOUR.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.HOUR.ordinal()].getMaximum().toString(), 2, '0')
                    , row[DateTimeExpressionPart.MINUTE.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.MINUTE.ordinal()].getMaximum().toString(), 2, '0')
                    , row[DateTimeExpressionPart.SECOND.ordinal()] == null ? "*" : StringUtils.leftPad(row[DateTimeExpressionPart.SECOND.ordinal()].getMaximum().toString(), 2, '0')
            );
            stringBuilder.append(begin).append(" ~ ").append(end);
            DateTimeExpressionRange<Integer> week = row[DateTimeExpressionPart.DAY_OF_WEEK.ordinal()] != null
                    ? row[DateTimeExpressionPart.DAY_OF_WEEK.ordinal()]
                    : DateTimeExpressionPart.DAY_OF_WEEK.getDefaultRange();
            stringBuilder.append(" dayOfWeek ").append(week.toString());
            stringBuilder.append(StringUtil.getLineSeparator());
        }
        return stringBuilder.toString();
    }

}
