package com.egls.server.utils.net.ipv4;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 本类提供一些Ipv4地址的工具方法
 * <p>
 * IP地址范围: 0.0.0.0～255.255.255.255,包括了mask地址.
 *
 * <pre>
 *      IP地址划分:
 *      A类地址:0.0.0.0～127.255.255.255
 *      B类地址:128.0.0.0～191.255.255.255
 *      C类地址:192.0.0.0～223.255.255.255
 *      D类地址:224.0.0.0～239.255.255.255
 *      E类地址:240.0.0.0～255.255.255.255
 * </pre>
 * <pre>
 *      如何判断两个IP地址是否是同一个网段中:
 *      要判断两个IP地址是不是在同一个网段,就将它们的IP地址分别与子网掩码做与运算,得到的结果一网络号,如果网络号相同,就在同一子网,否则,不在同一子网.
 *
 *      假定选择了子网掩码255.255.254.0,现在分别将上述两个IP地址分别与掩码做与运算,如下所示:
 *      211.95.165.24 11010011 01011111 10100101 00011000
 *      255.255.254.0 11111111 11111111 11111110 00000000
 *      &result       11010011 01011111 10100100 00000000
 *
 *      211.95.164.78 11010011 01011111 10100100 01001110
 *      255.255.254.0 11111111 11111111 11111110 00000000
 *      &result       11010011 01011111 10100100 00000000
 *      可以看出,得到的结果(这个结果就是网络地址)都是一样的,因此可以判断这两个IP地址在同一个子网.
 * </pre>
 *
 * <pre>
 *      子网划分:
 *      A类网络的子网掩码为 255.0.0.0(第1个字节是网络号,后面3个字节是机器号)
 *      B类网络的子网掩码为 255.255.0.0(前2个字节是网络号,后面2个字节是机器号)
 *      C类网络的子网掩码为 255.255.255.0(前3个字节是网络号,后面1个字节是机器号)
 *      D类网络的子网掩码为 255.255.255.255(无网络号机器号,所以没有掩码,也就是全1)
 *      E类网络的子网掩码为 255.255.255.255(无网络号机器号,所以没有掩码,也就是全1)
 *
 *      掩码是为了划分子网的,例如在公有网络上你的网络号根据地址类型来确定,那么你自己可以划分私有子网,子网的网络号就是掩码来确定的了.
 *      也就是说用公有网络的机器号的字节部分,选几位作为私有子网的网络号标记的.
 *
 *      A类保留私有地址:10.0.0.0～10.255.255.255
 *      B类保留私有地址:172.16.0.0～172.31.255.255
 *      C类保留私有地址:192.168.0.0～192.168.255.255
 * </pre>
 *
 * @author mayer - [Created on 2018-08-21 18:02]
 */
public final class Ipv4Util {

    private static final int MIN_MASK_BIT_LENGTH = 0;

    private static final int MAX_MASK_BIT_LENGTH = 32;

    private static final char IPV4_STRING_SEPARATOR_CHAR = '.';

    private static final String IPV4_STRING_SEPARATOR = "" + IPV4_STRING_SEPARATOR_CHAR;

    /**
     * Ipv4的正则表达式,用于判断Ipv4地址是否合法
     */
    private static final Pattern IPV4_REGEX_PATTERN
            = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");

    public enum Ipv4Type {

        /**
         * A类的局域网地址
         */
        IPV4_A_LOCAL("10.0.0.0", "10.255.255.255", "255.0.0.0"),
        /**
         * A类的回环地址
         */
        IPV4_A_LOCALHOST("127.0.0.1", "127.0.0.1", "255.255.255.255"),
        /**
         * A类的公网地址
         */
        IPV4_A("0.0.0.0", "127.255.255.255", "255.0.0.0"),

        /**
         * B类的局域网地址
         */
        IPV4_B_LOCAL("172.16.0.0", "172.31.255.255", "255.255.0.0"),
        /**
         * B类的公网地址
         */
        IPV4_B("128.0.0.0", "191.255.255.255", "255.255.0.0"),

        /**
         * C类的局域网地址
         */
        IPV4_C_LOCAL("192.168.0.0", "192.168.255.255", "255.255.255.0"),
        /**
         * C类的公网地址
         */
        IPV4_C("192.0.0.0", "223.255.255.255", "255.255.255.0"),

        /**
         * D类地址
         */
        IPV4_D("224.0.0.0", "239.255.255.255", "255.255.255.255"),

        /**
         * E类地址
         */
        IPV4_E("240.0.0.0", "255.255.255.255", "255.255.255.255");

        public final long mask;

        public final Range<Long> range;

        Ipv4Type(final String headString, final String tailString, final String maskString) {
            mask = toIpv4Value(maskString);
            range = Range.between(toIpv4Value(headString), toIpv4Value(tailString));
        }

        public boolean contains(final long ipValue) {
            return range.contains(ipValue);
        }

    }

    public static boolean validateIpv4(final String ipString) {
        return StringUtils.isNotBlank(ipString) && IPV4_REGEX_PATTERN.matcher(ipString).matches();
    }

    public static void ensureIpv4Valid(final String ipString) {
        if (!validateIpv4(ipString)) {
            throw new IllegalArgumentException("Error ipv4 address : " + ipString);
        }
    }

    /**
     * 将ip字符串转换为无符号字节数组.
     */
    public static short[] toIpv4UnsignedBytes(final String ipString) {
        ensureIpv4Valid(ipString);
        final String[] ipv4StringParts = StringUtils.split(ipString, IPV4_STRING_SEPARATOR_CHAR);
        final short[] ipv4UnsignedBytes = new short[ipv4StringParts.length];
        for (int i = 0; i < ipv4UnsignedBytes.length; i++) {
            ipv4UnsignedBytes[i] = Short.valueOf(ipv4StringParts[i]);
        }
        return ipv4UnsignedBytes;
    }

    /**
     * 将ip字符串转换为32bit值
     */
    public static long toIpv4Value(final String ipString) {
        long value = 0;
        short[] ipv4UnsignedBytes = toIpv4UnsignedBytes(ipString);
        value |= (ipv4UnsignedBytes[3] & 0xFFL);
        value |= (ipv4UnsignedBytes[2] & 0xFFL) << 8;
        value |= (ipv4UnsignedBytes[1] & 0xFFL) << 16;
        value |= (ipv4UnsignedBytes[0] & 0xFFL) << 24;
        return value;
    }

    /**
     * 将ip的无符号字节数组值转换为ip字符串
     */
    public static String toIpv4String(final short[] ipv4UnsignedBytes) {
        return (ipv4UnsignedBytes[0] & 0xFF) + IPV4_STRING_SEPARATOR +
                (ipv4UnsignedBytes[1] & 0xFF) + IPV4_STRING_SEPARATOR +
                (ipv4UnsignedBytes[2] & 0xFF) + IPV4_STRING_SEPARATOR +
                (ipv4UnsignedBytes[3] & 0xFF);
    }

    /**
     * 将ip的32bit值转换为ip字符串
     */
    public static String toIpv4String(final long ipValue) {
        return ((ipValue >> 24) & 0xFF) + IPV4_STRING_SEPARATOR +
                ((ipValue >> 16) & 0xFF) + IPV4_STRING_SEPARATOR +
                ((ipValue >> 8) & 0xFF) + IPV4_STRING_SEPARATOR +
                (ipValue & 0xFF);
    }

    /**
     * 获取32bit值ip地址的类型
     */
    public static Ipv4Type getIpv4Type(final long ipValue) {
        for (Ipv4Type ipv4Type : Ipv4Type.values()) {
            if (ipv4Type.contains(ipValue)) {
                return ipv4Type;
            }
        }
        return null;
    }

    /**
     * 获取ip地址的类型
     */
    public static Ipv4Type getIpv4Type(final String ipString) {
        return getIpv4Type(toIpv4Value(ipString));
    }

    public static boolean noneMatch(final String ipString, final Ipv4Type... types) {
        final Ipv4Type ipv4Type = getIpv4Type(ipString);
        return Arrays.stream(types).noneMatch(t -> t == ipv4Type);
    }

    public static boolean anyMatch(final String ipString, final Ipv4Type... types) {
        final Ipv4Type ipv4Type = getIpv4Type(ipString);
        return Arrays.stream(types).anyMatch(t -> t == ipv4Type);
    }

    public static boolean allMatch(final String ipString, final Ipv4Type... types) {
        final Ipv4Type ipv4Type = getIpv4Type(ipString);
        return Arrays.stream(types).allMatch(t -> t == ipv4Type);
    }

    /**
     * 获取一个IP地址的默认mask值
     */
    public static long getDefaultMaskValue(final String ipString) {
        final Ipv4Type ipv4Type = getIpv4Type(ipString);
        return ipv4Type.mask;
    }

    /**
     * 获取一个IP地址的默认mask值
     */
    public static String getDefaultMaskString(final String ipString) {
        return toIpv4String(getDefaultMaskValue(ipString));
    }

    /**
     * 通过掩码位数,获取如这样表示的Ipv4地址"255.255.255.255"的掩码的字符串
     * 8 - 255.0.0.0
     * 16 - 255.255.0.0
     * 24 - 255.255.255.0
     * 32 - 255.255.255.255
     */
    public static String toMaskString(final int maskBitLength) {
        if (maskBitLength < MIN_MASK_BIT_LENGTH || maskBitLength > MAX_MASK_BIT_LENGTH) {
            //经过测试,在Windows 10操作系统中有可能返回-1的值（存在vpn链接时）,但是通过操作系统ipconfig /all命令发现掩码为255.255.255.255
            //所以这里将非法的掩码长度都定为255.255.255.255
            return toIpv4String(0xFFFFFFFFL);
        }
        return toIpv4String(0xFFFFFFFFL << (MAX_MASK_BIT_LENGTH - maskBitLength));
    }

    /**
     * <pre>
     *      比较两个ip地址,222.222.222.222 大于 222.222.222.223
     *
     *      if (ip1 > ip2) return 1;
     *      if (ip1 = ip2) return 0;
     *      if (ip1 < ip2) return -1;
     * </pre>
     *
     * @return the value {@code 0} if {@code x == y}; a value less than {@code 0} if {@code x < y}; and a value greater than {@code 0} if {@code x > y}
     */
    public static int compareIpv4String(final String ipString1, final String ipString2) {
        // 利用32bit值进行比较
        final long ipValue1 = toIpv4Value(ipString1);
        final long ipValue2 = toIpv4Value(ipString2);
        return Long.compare(ipValue2, ipValue1);
    }

    /**
     * 获取ip值与mask值'与'的结果.
     */
    public static long toNetSegmentValue(final String ipString, final String maskString) {
        return toNetSegmentValue(ipString, toIpv4Value(maskString));
    }

    /**
     * 获取ip值与mask值'与'的结果.
     */
    public static long toNetSegmentValue(final String ipString, final long mask) {
        return (toIpv4Value(ipString) & mask);
    }

    /**
     * 比较两个ip地址是否在一个子网中,使用各自的默认掩码
     */
    public static boolean isSameNetSegment(final String ipString1, final String ipString2) {
        final long value1 = getDefaultMaskValue(ipString1) & toIpv4Value(ipString1);
        final long value2 = getDefaultMaskValue(ipString2) & toIpv4Value(ipString2);
        return value1 == value2;
    }

    /**
     * 根据指定的掩码,比较两个ip地址是否在一个子网中
     */
    public static boolean isSameNetSegment(final String ipString1, final String ipString2, final long mask) {
        return toNetSegmentValue(ipString1, mask) == toNetSegmentValue(ipString2, mask);
    }

    /**
     * 根据指定的掩码,比较两个ip地址是否在一个子网中
     */
    public static boolean isSameNetSegment(final String ipString1, final String ipString2, final String maskString) {
        return isSameNetSegment(ipString1, ipString2, toIpv4Value(maskString));
    }

    /**
     * 获取本机的所有Ipv4地址和掩码
     *
     * @param containsLoopback if true then return result contains loopback address
     * @return ipv4 address and mask.
     * @throws SocketException maybe throws. if throw exception please resolve it with JDK help doc.
     */
    public static List<Pair<String, String>> getLocalhostIpv4AddressAndMask(final boolean containsLoopback) throws SocketException {
        final List<Pair<String, String>> ipv4 = new ArrayList<>();
        final Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = netInterfaces.nextElement();
            if (networkInterface.isVirtual() || !networkInterface.isUp()) {
                continue;
            }
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                if (!containsLoopback && interfaceAddress.getAddress().isLoopbackAddress()) {
                    continue;
                }
                final String hostAddress = interfaceAddress.getAddress().getHostAddress();
                if (!validateIpv4(hostAddress)) {
                    continue;
                }
                final String mask = toMaskString(interfaceAddress.getNetworkPrefixLength());
                ipv4.add(Pair.of(hostAddress, mask));
            }
        }
        return ipv4;
    }

    /**
     * 检测给定的ip与当前运行程序的机器是否在一个子网内.掩码运算使用本机器掩码.包含回环地址
     *
     * @param ip ipv4 address to check
     * @return if true mean in local area network. else not in local area network.
     * @throws SocketException maybe throws. if throw exception please resolve it with JDK help doc.
     */
    public static boolean equalsLocalhostSubnet(final String ip) throws SocketException {
        final List<Pair<String, String>> ipv4Address = getLocalhostIpv4AddressAndMask(true);
        return ipv4Address.stream().anyMatch(pair -> isSameNetSegment(pair.getLeft(), ip, pair.getRight()));
    }

    /**
     * 获取本机的所有Ipv4地址
     *
     * @param containsLoopback if true then return result contains loopback address
     * @return ipv4 address
     * @throws SocketException maybe throws. if throw exception please resolve it with JDK help doc.
     */
    public static List<String> getLocalhostIpv4Address(final boolean containsLoopback) throws SocketException {
        final List<Pair<String, String>> ipv4AddressAndMask = getLocalhostIpv4AddressAndMask(containsLoopback);
        return ipv4AddressAndMask.stream().map(Pair::getLeft).collect(Collectors.toList());
    }

    /**
     * 判断一个ip地址是否是本机地址
     */
    public static boolean isLocalhostIpv4Address(final String ip) throws SocketException {
        return getLocalhostIpv4Address(true).contains(ip);
    }

    /**
     * 是否是一个子网地址(内网地址)
     */
    public static boolean isLocalNetworkIpv4Address(final String ipv4) {
        return noneMatch(ipv4, Ipv4Type.IPV4_A, Ipv4Type.IPV4_B, Ipv4Type.IPV4_C, Ipv4Type.IPV4_D, Ipv4Type.IPV4_E);
    }

    /**
     * 是否是一个公网地址(外网地址)
     */
    public static boolean isWideNetworkIpv4Address(final String ipv4) {
        return noneMatch(ipv4, Ipv4Type.IPV4_A_LOCAL, Ipv4Type.IPV4_A_LOCALHOST, Ipv4Type.IPV4_B_LOCAL, Ipv4Type.IPV4_C_LOCAL);
    }

}
