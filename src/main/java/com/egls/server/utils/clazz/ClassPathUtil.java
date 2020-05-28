package com.egls.server.utils.clazz;

import com.egls.server.utils.file.FileNameUtil;
import com.egls.server.utils.file.FileUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * <pre>
 *     提供一些Class Path的工具.java -cp ...
 *
 *     如果在Jvm运行时进行编译一些Class文件,但是这个时候又用到了新的第三方库的时候.
 *     需要增加第三方库到Class path中, 以便于能够进行成功的编译新的Java文件.
 * </pre>
 *
 * @author mayer - [Created on 2018-08-16 21:26]
 * @see JavaFileCompiler
 */
public final class ClassPathUtil {

    private static final String CLASS_PATH_SYSTEM_PROPERTY_NAME = "java.class.path";

    /**
     * 在启动时,获取到Class Path.记录下最初的class path的样子,用来退回
     */
    private static final String INITIAL_CLASS_PATH = getClassPath();

    public static String getClassPath() {
        return System.getProperty(CLASS_PATH_SYSTEM_PROPERTY_NAME);
    }

    public static void setClassPath(final String classPath) {
        System.setProperty(CLASS_PATH_SYSTEM_PROPERTY_NAME, classPath);
    }

    public static String getInitialClassPath() {
        return INITIAL_CLASS_PATH;
    }

    public static void revertToInitialClassPath() {
        setClassPath(INITIAL_CLASS_PATH);
    }

    /**
     * 将一个文件夹内的包都添加到class path
     */
    public static void addDirectoryToClassPath(final File thirdPartnerLibDirectory) {
        if (FileUtil.exists(thirdPartnerLibDirectory) && FileNameUtil.nonSubversionFile(thirdPartnerLibDirectory)) {
            if (thirdPartnerLibDirectory.isDirectory()) {
                File[] subFiles = thirdPartnerLibDirectory.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        addDirectoryToClassPath(subFile);
                    }
                }
            } else {
                addClassPath(thirdPartnerLibDirectory);
            }
        }
    }

    /**
     * 从class path中删除一个文件夹下的所有
     */
    public static void removeDirectoryFromClassPath(final File thirdPartnerLibDirectory) {
        if (FileUtil.exists(thirdPartnerLibDirectory) && FileNameUtil.nonSubversionFile(thirdPartnerLibDirectory)) {
            if (thirdPartnerLibDirectory.isDirectory()) {
                File[] subFiles = thirdPartnerLibDirectory.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        removeDirectoryFromClassPath(subFile);
                    }
                }
            } else {
                removeClassPath(thirdPartnerLibDirectory);
            }
        }
    }

    public static void addClassPath(final File... classPathFiles) {
        if (ArrayUtils.isNotEmpty(classPathFiles)) {
            addClassPath(Arrays.asList(classPathFiles));
        }
    }

    public static void addClassPath(final Collection<File> classPathFiles) {
        final StringBuilder classPathBuilder = new StringBuilder();
        final String[] classPathArray = StringUtils.split(getClassPath(), File.pathSeparatorChar);
        Arrays.stream(classPathArray).forEach(classPath -> classPathBuilder.append(classPath).append(File.pathSeparator));
        classPathFiles.stream().filter(file -> FileUtil.exists(file) && !file.isDirectory())
                .forEach(classPathFile -> classPathBuilder.append(classPathFile.getAbsolutePath()).append(File.pathSeparator));
        System.setProperty(CLASS_PATH_SYSTEM_PROPERTY_NAME, StringUtils.stripEnd(classPathBuilder.toString(), File.pathSeparator));
    }

    public static void removeClassPath(final File... classPathFiles) {
        if (ArrayUtils.isNotEmpty(classPathFiles)) {
            removeClassPath(Arrays.asList(classPathFiles));
        }
    }

    public static void removeClassPath(final Collection<File> classPathFiles) {
        final StringBuilder classPathBuilder = new StringBuilder();
        final String[] classPathArray = StringUtils.split(getClassPath(), File.pathSeparatorChar);
        Arrays.stream(classPathArray).filter(classPath -> {
            for (File file : classPathFiles) {
                if (FileUtil.exists(file) && !file.isDirectory() && StringUtils.equals(file.getAbsolutePath(), classPath)) {
                    return false;
                }
            }
            return true;
        }).forEach(classPath -> classPathBuilder.append(classPath).append(File.pathSeparator));
        System.setProperty(CLASS_PATH_SYSTEM_PROPERTY_NAME, StringUtils.stripEnd(classPathBuilder.toString(), File.pathSeparator));
    }

}
