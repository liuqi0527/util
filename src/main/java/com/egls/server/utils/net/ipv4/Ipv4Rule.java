package com.egls.server.utils.net.ipv4;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 表示一些Ip规则组合.每一条规则是由ip和mask组成.
 *
 * @author mayer - [Created on 2018-08-21 17:37]
 */
public final class Ipv4Rule {

    private final List<Pair<Long, Long>> rules = new ArrayList<>();

    /**
     * like "10.0.0.0/8"
     */
    public final void addRule(final String string) {
        final String[] array = StringUtils.split(string, '/');
        addRule(array[0], Integer.valueOf(array[1]));
    }

    public final void addRule(final String ipString, final int maskBitLength) {
        addRule(ipString, Ipv4Util.toMaskString(maskBitLength));
    }

    public final void addRule(final String ipString, final String maskString) {
        Ipv4Util.ensureIpv4Valid(ipString);
        Ipv4Util.ensureIpv4Valid(maskString);

        final long netSegmentValue = Ipv4Util.toNetSegmentValue(ipString, maskString);
        final long maskValue = Ipv4Util.toIpv4Value(maskString);
        rules.add(Pair.of(netSegmentValue, maskValue));
    }

    /**
     * 某个IP地址是否在规则内
     *
     * @param ipString ip类型的字符串
     * @return true 表示在规则内, false 表示不在规则内.
     */
    public boolean contains(final String ipString) {
        return rules.stream().anyMatch(ipAndMask -> ipAndMask.getLeft() == Ipv4Util.toNetSegmentValue(ipString, ipAndMask.getRight()));
    }

    /**
     * 清除所有的规则
     */
    public void clear() {
        rules.clear();
    }

}
