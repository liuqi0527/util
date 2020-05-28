package com.egls.server.utils.cipher;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2019-12-06 21:08]
 */
public class TestPasswordCipher {

    @Test
    public void test() throws Exception {
        String password = "12345677";
        String salt = PasswordCipher.getPasswordSalt();
        String c = PasswordCipher.getPasswordHash(password, salt);
        System.out.println(salt);
        System.out.println(c);
        Assert.assertTrue(PasswordCipher.verifyPassword(password, salt, c));
    }

}
