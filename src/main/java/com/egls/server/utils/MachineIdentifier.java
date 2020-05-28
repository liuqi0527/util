package com.egls.server.utils;

import com.egls.server.utils.net.ipv4.Ipv4Util;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 机器标识符
 *
 * @author mayer - [Created on 2019-05-13 22:04]
 */
public class MachineIdentifier {

    /**
     * 利用mac地址生成机器标识符,对于一台机器,是不变的.
     * 使用这种方式而不使用其他硬件，主要原因是因为MAC地址可以方便的通过命令在各个OS中看到,方便的校验是否正确
     * Linux : ifconfig
     * Windows : ipconfig /all
     * 根据IP地址的来判断使用哪个MAC地址,优先级如下(如果级别一样,则取最先出现的):
     * IPv4公网地址 > IPv4局域网地址 > 不为null的硬件地址
     *
     * @return 标识符
     */
    public static String getIdentifier() throws SocketException {
        final Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        Pair<String, String> pair = null;
        while (netInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = netInterfaces.nextElement();
            final byte[] hardwareAddress = networkInterface.getHardwareAddress();
            if (networkInterface.isVirtual() || !networkInterface.isUp() || ArrayUtils.isEmpty(hardwareAddress)) {
                continue;
            }
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                //IPv4或者IPv6地址
                String address = interfaceAddress.getAddress().getHostAddress();
                //只考虑IPv4地址
                if (Ipv4Util.validateIpv4(address)) {
                    if (pair == null) {
                        pair = Pair.of(address, StringUtil.binaryToHex(hardwareAddress));
                    } else if (Ipv4Util.isWideNetworkIpv4Address(address) && Ipv4Util.isLocalhostIpv4Address(pair.getLeft())) {
                        pair = Pair.of(address, StringUtil.binaryToHex(hardwareAddress));
                    }
                }
            }
        }
        if (pair != null) {
            return pair.getRight();
        }
        throw new RuntimeException("get identifier failed.");
    }

}
