package com.egls.server.utils.date.expression;

import java.time.LocalDateTime;

/**
 * @author mayer - [Created on 2018-09-04 16:29]
 */
public enum DateTimeExpressionPart {

    /**
     * 表达式的部分类型,顺序有意义,不能修改.
     */
    SECOND(0, 59) {
        @Override
        public LocalDateTime nextOne(LocalDateTime localDateTime) {
            return localDateTime.plusSeconds(1);
        }
    },
    MINUTE(0, 59) {
        @Override
        public LocalDateTime nextOne(LocalDateTime localDateTime) {
            return localDateTime.plusMinutes(1);
        }
    },
    HOUR(0, 23) {
        @Override
        public LocalDateTime nextOne(LocalDateTime localDateTime) {
            return localDateTime.plusHours(1);
        }
    },
    DAY_OF_MONTH(1, 31) {
        @Override
        public LocalDateTime nextOne(LocalDateTime localDateTime) {
            return localDateTime.plusDays(1);
        }
    },
    MONTH(1, 12) {
        @Override
        public LocalDateTime nextOne(LocalDateTime localDateTime) {
            return localDateTime.plusMonths(1);
        }
    },
    DAY_OF_WEEK(1, 7) {
        @Override
        public LocalDateTime nextOne(LocalDateTime localDateTime) {
            return localDateTime.plusWeeks(1);
        }
    },
    YEAR(2000, 2099) {
        @Override
        public LocalDateTime nextOne(LocalDateTime localDateTime) {
            return localDateTime.plusYears(1);
        }
    };

    final DateTimeExpressionRange<Integer> defaultRange;

    DateTimeExpressionPart(final int low, final int high) {
        this.defaultRange = DateTimeExpressionRange.between(low, high);
    }

    public final DateTimeExpressionRange<Integer> getDefaultRange() {
        return defaultRange;
    }

    /**
     * 根据类型获取下一个时间
     */
    public abstract LocalDateTime nextOne(LocalDateTime localDateTime);

}
