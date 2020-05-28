package com.egls.server.utils.file;

import java.io.File;
import java.util.Objects;

/**
 * 提供一些文件类型的工具方法
 *
 * @author mayer - [Created on 2018-08-09 20:24]
 */
public final class FileTypeUtil {

    public static boolean isMatch(final FileType option, final File file) {
        Objects.requireNonNull(file);
        Objects.requireNonNull(option);
        return option.isMatch(file.getName());
    }

    public static boolean isMatch(final FileType option, final String fileNameOrFilePath) {
        Objects.requireNonNull(fileNameOrFilePath);
        Objects.requireNonNull(option);
        return option.isMatch(fileNameOrFilePath);
    }

    public static boolean containsFileTypes(final File directory, final FileType... fileTypes) {
        if (FileUtil.exists(directory) && FileNameUtil.nonSubversionFile(directory)) {
            if (directory.isDirectory()) {
                File[] file = directory.listFiles();
                if (file != null) {
                    for (File f : file) {
                        if (containsFileTypes(f, fileTypes)) {
                            return true;
                        }
                    }
                }
            } else {
                for (FileType fileType : fileTypes) {
                    if (fileType.isMatch(directory)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
