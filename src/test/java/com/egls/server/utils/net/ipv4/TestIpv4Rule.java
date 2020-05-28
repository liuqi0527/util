package com.egls.server.utils.net.ipv4;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 11:59]
 */
public class TestIpv4Rule {

    @Test
    public void testIPv4Rule() {
        Ipv4Rule ipv4Rule = new Ipv4Rule();
        ipv4Rule.addRule("192.168.0.1", 32);
        Assert.assertFalse(ipv4Rule.contains("192.168.0.0"));
        Assert.assertTrue(ipv4Rule.contains("192.168.0.1"));
        Assert.assertFalse(ipv4Rule.contains("192.168.0.2"));

        ipv4Rule.addRule("192.168.0.1", 31);
        Assert.assertTrue(ipv4Rule.contains("192.168.0.0"));
        Assert.assertTrue(ipv4Rule.contains("192.168.0.1"));
        Assert.assertFalse(ipv4Rule.contains("192.168.0.2"));

        ipv4Rule.addRule("192.168.0.1", 30);
        Assert.assertTrue(ipv4Rule.contains("192.168.0.0"));
        Assert.assertTrue(ipv4Rule.contains("192.168.0.1"));
        Assert.assertTrue(ipv4Rule.contains("192.168.0.2"));
        Assert.assertTrue(ipv4Rule.contains("192.168.0.3"));
        Assert.assertFalse(ipv4Rule.contains("192.168.0.4"));
    }

}
