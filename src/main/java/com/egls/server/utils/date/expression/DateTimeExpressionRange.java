package com.egls.server.utils.date.expression;

import java.util.Objects;

/**
 * 不检测大小端,可能产生错误.
 * 使用{@link org.apache.commons.lang3.Range}
 *
 * @author mayer - [Created on 2018-09-04 16:31]
 * @see org.apache.commons.lang3.Range
 */
class DateTimeExpressionRange<T extends Comparable<T>> implements Comparable<DateTimeExpressionRange<T>> {

    private final T minimum, maximum;

    public static <T extends Comparable<T>> DateTimeExpressionRange<T> between(final T lowValue, final T highValue) {
        return new DateTimeExpressionRange<>(lowValue, highValue);
    }

    private DateTimeExpressionRange(final T lowValue, final T highValue) {
        this.minimum = lowValue;
        this.maximum = highValue;
    }

    public final T getMinimum() {
        return minimum;
    }

    public final T getMaximum() {
        return maximum;
    }

    public final boolean containsRange(final DateTimeExpressionRange<T> otherRange) {
        return contains(otherRange.minimum) && contains(otherRange.maximum);
    }

    public final boolean contains(final T t) {
        return t.compareTo(minimum) >= 0 && t.compareTo(maximum) <= 0;
    }

    @Override
    public int compareTo(DateTimeExpressionRange<T> o) {
        //以最小端为基准
        return minimum.compareTo(o.minimum);
    }

    @Override
    public final String toString() {
        return minimum + " - " + maximum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateTimeExpressionRange that = (DateTimeExpressionRange) o;
        return minimum == that.minimum &&
                maximum == that.maximum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimum, maximum);
    }

}
