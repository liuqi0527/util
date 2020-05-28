package com.egls.server.utils.net.ipv4;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mayer - [Created on 2018-09-04 11:54]
 */
public class TestIpv4Util {

    @Test
    public void test1() {
        assertTrue(Ipv4Util.validateIpv4("192.168.9.1"));
        assertTrue(Ipv4Util.validateIpv4("192.168.9.121"));
        assertFalse(Ipv4Util.validateIpv4(" 192.168.9.1"));
        assertFalse(Ipv4Util.validateIpv4("192.168.9.1 "));
        assertFalse(Ipv4Util.validateIpv4("1 92.168.9.1"));
        assertFalse(Ipv4Util.validateIpv4("255.256.255.255"));
        assertTrue(Ipv4Util.validateIpv4("255.255.255.255"));
        assertTrue(Ipv4Util.validateIpv4("192.168.9.1"));
        assertTrue(Ipv4Util.validateIpv4("192.168.9.121"));
    }

    @Test
    public void test2() {
        assertEquals(Ipv4Util.toMaskString(22), "255.255.252.0");

        assertEquals(Arrays.toString(Ipv4Util.toIpv4UnsignedBytes("255.255.255.255")), "[255, 255, 255, 255]");
        assertEquals(Long.toBinaryString(Ipv4Util.toIpv4Value("255.255.255.255")), "11111111111111111111111111111111");
        assertEquals(Ipv4Util.getDefaultMaskString("255.255.255.255"), "255.255.255.255");
        assertEquals(Ipv4Util.getDefaultMaskValue("255.255.255.255"), Ipv4Util.toIpv4Value(Ipv4Util.getDefaultMaskString("255.255.255.255")));

        assertEquals(Arrays.toString(Ipv4Util.toIpv4UnsignedBytes("0.0.0.0")), "[0, 0, 0, 0]");
        assertEquals(Long.toBinaryString(Ipv4Util.toIpv4Value("0.0.0.0")), "0");
        assertEquals(Ipv4Util.getDefaultMaskString("0.0.0.0"), "255.0.0.0");
        assertEquals(Ipv4Util.getDefaultMaskValue("0.0.0.0"), Ipv4Util.toIpv4Value(Ipv4Util.getDefaultMaskString("0.0.0.0")));

        assertEquals(Ipv4Util.toIpv4String(Ipv4Util.toIpv4Value("0.0.0.0")), "0.0.0.0");
        assertEquals(Ipv4Util.toIpv4String(Ipv4Util.toIpv4UnsignedBytes("0.0.0.0")), "0.0.0.0");

        assertEquals(Ipv4Util.getIpv4Type("0.0.0.0"), Ipv4Util.Ipv4Type.IPV4_A);
        assertEquals(Ipv4Util.getIpv4Type("128.0.0.0"), Ipv4Util.Ipv4Type.IPV4_B);
        assertEquals(Ipv4Util.getIpv4Type("192.0.0.0"), Ipv4Util.Ipv4Type.IPV4_C);
        assertEquals(Ipv4Util.getIpv4Type("224.0.0.0"), Ipv4Util.Ipv4Type.IPV4_D);
        assertEquals(Ipv4Util.getIpv4Type("240.0.0.0"), Ipv4Util.Ipv4Type.IPV4_E);

        assertEquals(Ipv4Util.getIpv4Type("127.255.255.255"), Ipv4Util.Ipv4Type.IPV4_A);
        assertEquals(Ipv4Util.getIpv4Type("191.255.255.255"), Ipv4Util.Ipv4Type.IPV4_B);
        assertEquals(Ipv4Util.getIpv4Type("223.255.255.255"), Ipv4Util.Ipv4Type.IPV4_C);
        assertEquals(Ipv4Util.getIpv4Type("239.255.255.255"), Ipv4Util.Ipv4Type.IPV4_D);
        assertEquals(Ipv4Util.getIpv4Type("255.255.255.255"), Ipv4Util.Ipv4Type.IPV4_E);

        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("0.0.0.0")), Ipv4Util.Ipv4Type.IPV4_A);
        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("128.0.0.0")), Ipv4Util.Ipv4Type.IPV4_B);
        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("192.0.0.0")), Ipv4Util.Ipv4Type.IPV4_C);
        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("224.0.0.0")), Ipv4Util.Ipv4Type.IPV4_D);
        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("240.0.0.0")), Ipv4Util.Ipv4Type.IPV4_E);

        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("127.255.255.255")), Ipv4Util.Ipv4Type.IPV4_A);
        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("191.255.255.255")), Ipv4Util.Ipv4Type.IPV4_B);
        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("223.255.255.255")), Ipv4Util.Ipv4Type.IPV4_C);
        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("239.255.255.255")), Ipv4Util.Ipv4Type.IPV4_D);
        assertEquals(Ipv4Util.getIpv4Type(Ipv4Util.toIpv4Value("255.255.255.255")), Ipv4Util.Ipv4Type.IPV4_E);
    }

    @Test
    public void test3() {
        assertEquals(1, Ipv4Util.compareIpv4String("0.0.0.0", "0.0.0.1"));
        assertEquals(0, Ipv4Util.compareIpv4String("0.0.0.0", "0.0.0.0"));
        assertEquals(-1, Ipv4Util.compareIpv4String("0.0.0.1", "0.0.0.0"));

        assertEquals(1, Ipv4Util.compareIpv4String("255.255.255.254", "255.255.255.255"));
        assertEquals(0, Ipv4Util.compareIpv4String("255.255.255.255", "255.255.255.255"));
        assertEquals(-1, Ipv4Util.compareIpv4String("255.255.255.255", "255.255.255.254"));
    }

    @Test
    public void test4() {
        assertTrue(Ipv4Util.isSameNetSegment("192.168.0.12", "192.168.0.32"));
        assertTrue(Ipv4Util.isSameNetSegment("192.168.0.12", "192.168.0.32", "255.255.255.0"));
        assertFalse(Ipv4Util.isSameNetSegment("192.168.0.12", "192.168.0.32", "255.255.255.255"));
        assertEquals("11000000101010000000000000000000", Long.toBinaryString(Ipv4Util.toNetSegmentValue("192.168.0.12", "255.255.255.0")));
    }

}
