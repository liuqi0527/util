package com.egls.server.utils.clazz;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-03 16:45]
 */
public class TestClassPathUtil {

    @Test
    public void testAdd() {
        final String classPath = ClassPathUtil.getClassPath();
        for (String filePath : StringUtils.split(classPath, File.pathSeparatorChar)) {
            ClassPathUtil.addClassPath(new File(filePath));
        }

        Assert.assertNotEquals(classPath, ClassPathUtil.getClassPath());
        Assert.assertEquals(classPath, ClassPathUtil.getInitialClassPath());

        ClassPathUtil.revertToInitialClassPath();
        Assert.assertEquals(classPath, ClassPathUtil.getClassPath());

    }

    @Test
    public void testRemove() {
        final String classPath = ClassPathUtil.getClassPath();
        for (String filePath : StringUtils.split(classPath, File.pathSeparatorChar)) {
            ClassPathUtil.removeClassPath(new File(filePath));
        }

        Assert.assertNotEquals(classPath, ClassPathUtil.getClassPath());
        Assert.assertEquals(classPath, ClassPathUtil.getInitialClassPath());

        ClassPathUtil.revertToInitialClassPath();
        Assert.assertEquals(classPath, ClassPathUtil.getClassPath());

    }

}
