package com.egls.server.utils.file;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 提供一些文件名的工具方法
 *
 * @author mayer - [Created on 2018-08-09 20:18]
 */
public final class FileNameUtil {

    private static final char[] INVALID_FILENAME_CHAR = new char[]{'\\', '/', ':', '*', '?', '\"', '<', '>', '|', '.'};

    public static boolean isValidFileName(final String fileName) {
        return StringUtils.containsAny(fileName, INVALID_FILENAME_CHAR);
    }

    public static String toValidFileName(final String string) {
        String fileName = string;
        for (char removeChar : INVALID_FILENAME_CHAR) {
            fileName = StringUtils.remove(fileName, removeChar);
        }
        return fileName;
    }

    /**
     * 判断一个文件的文件名与.svn一样,或者整体路径包含.svn.
     *
     * @return 返回true表示是svn路径.
     */
    public static boolean isSubversionFile(final File file) {
        //这里只对文件名做判断
        if (file == null) {
            return false;
        }
        String fullFileName = FilenameUtils.separatorsToSystem(FilenameUtils.normalize(file.getAbsolutePath()));
        return StringUtils.contains(fullFileName, ".svn");
    }

    /**
     * 判断一个文件不是svn的文件夹
     *
     * @return 返回true表示不是svn路径
     */
    public static boolean nonSubversionFile(final File file) {
        return !isSubversionFile(file);
    }

    /**
     * 格式化一个文件夹的路径
     *
     * @param dirPath 必须是文件夹路径
     * @return 格式化后的路径
     */
    public static String formatDirectoryPath(final String dirPath) {
        String result = FilenameUtils.normalize(dirPath);
        result = FilenameUtils.separatorsToSystem(result);
        result = StringUtils.endsWith(result, File.separator) ? result : result + File.separator;
        return result;
    }

}
