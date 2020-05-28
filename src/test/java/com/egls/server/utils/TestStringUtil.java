package com.egls.server.utils;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import static com.egls.server.utils.StringUtil.subStringsBetween;


/**
 * @author mayer - [Created on 2018-09-04 14:52]
 */
public class TestStringUtil {

    private static final String STRING = "{{a}{b}{c}}{1}{2}{3}";

    @Test
    public void test1() {
        String[] str = subStringsBetween(STRING, "{", "}");
        Assert.assertEquals(str[0], "{a}{b}{c}");
        Assert.assertEquals(str[1], "1");
        Assert.assertEquals(str[2], "2");
        Assert.assertEquals(str[3], "3");

        str = subStringsBetween(STRING, '{', '}');
        Assert.assertEquals(str[0], "{a}{b}{c}");
        Assert.assertEquals(str[1], "1");
        Assert.assertEquals(str[2], "2");
        Assert.assertEquals(str[3], "3");
    }

    @Test
    public void test2() {

        String str1 = "{{{}{}{}}{{}{}{}}}{{{}{}{}}{{}{}{}}}{{{}{}{}}{{}{}{}}}{{{}{}{}}{{}{}{}}}";
        String str2 = "{{我我我a我我我}{我我我a我我我}{我我我a我我我}{我我我a我我我}}{{我我我a我我我}{我我我a我我我}{我我我a我我我}}{我我我a我我我}{{我我我a我我我}{我我我a我我我}}{我我我a我我我}{我我我a我我我}{我我我a我我我}";
        String str3 = "{{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}}{{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}{{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}{abd我我和你~!@#$%^&*()_+|:\"<>[]?`1234567890-=\\;',./abd我}";

        Assert.assertEquals(4, subStringsBetween(str1, "{", "}").length);
        Assert.assertEquals(4, subStringsBetween(str1, '{', '}').length);

        Assert.assertEquals(7, subStringsBetween(str2, "{", "}").length);
        Assert.assertEquals(7, subStringsBetween(str2, '{', '}').length);

        Assert.assertEquals(7, subStringsBetween(str3, "{", "}").length);
        Assert.assertEquals(7, subStringsBetween(str3, '{', '}').length);

        Assert.assertEquals(Arrays.toString(subStringsBetween(str1, "{", "}")), Arrays.toString(subStringsBetween(str1, '{', '}')));
        Assert.assertEquals(Arrays.toString(subStringsBetween(str2, "{", "}")), Arrays.toString(subStringsBetween(str2, '{', '}')));
        Assert.assertEquals(Arrays.toString(subStringsBetween(str3, "{", "}")), Arrays.toString(subStringsBetween(str3, '{', '}')));
    }

}
