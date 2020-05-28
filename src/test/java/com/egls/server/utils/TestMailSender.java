package com.egls.server.utils;

import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 14:02]
 */
public class TestMailSender {

    @Test
    public void test() {
        MailSender mailSender = new MailSender();
        mailSender.enableDebug(false);
        mailSender.enableSSL(true);
        mailSender.setHost("smtp.163.com");
        mailSender.setAccount("egls_server@163.com", "123asd321dsa");
//        mailSender.setCipherSuites("SSL_RSA_WITH_RC4_128_SHA");
        mailSender.send("egls_server@163.com", "通知邮件", String.format("maintenance tool entrance url:\r\n%s", "http://www.baidu.com"));
    }

}
