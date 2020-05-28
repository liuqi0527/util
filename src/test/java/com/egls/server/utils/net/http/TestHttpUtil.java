package com.egls.server.utils.net.http;

import java.io.IOException;

import com.egls.server.utils.CollectionUtil;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 11:49]
 */
public class TestHttpUtil {

    @Test
    public void test0() throws IOException {
        final String url = "http://www.baidu.com";
        final String text = HttpUtil.doHttpGetText(url);
        Assert.assertFalse(StringUtils.isEmpty(text));
    }

    @Test
    public void test1() {
        String url = HttpUtil.buildUrl("http://www.baidu.com", new String[][]{{"aa", "aa"}, {"bb", "bb"}});
        Assert.assertEquals(url, "http://www.baidu.com?aa=aa&bb=bb");
    }

    @Test
    public void test2() {
        String url = HttpUtil.buildUrl("http://www.baidu.com", CollectionUtil.newHashMap("aa", "bb"));
        Assert.assertEquals(url, "http://www.baidu.com?aa=bb");
    }

    @Test
    public void test3() {
        String url = "http://zh2.eglsgame.com/updateall?v=0&g=zheg&t=etc1&c=0&p=MI+4LTE%7C4.4.4&bin=1.00.00";
        Assert.assertEquals("http://zh2.eglsgame.com/updateall", HttpUtil.getBaseUrl(url));

        url = "http://zh2.eglsgame.com/updateall?v=0&g=zheg&?t=etc1&c=0&p=MI+4LTE%7C4.4.4&bin=1.00.00";
        Assert.assertEquals("http://zh2.eglsgame.com/updateall", HttpUtil.getBaseUrl(url));

        url = "zh2.eglsgame.com/updateall?v=0&g=zheg&?t=etc1&c=0&p=MI+4LTE%7C4.4.4&bin=1.00.00";
        Assert.assertEquals("zh2.eglsgame.com/updateall", HttpUtil.getBaseUrl(url));
    }

}
