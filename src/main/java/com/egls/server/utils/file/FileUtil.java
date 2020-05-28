package com.egls.server.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.google.common.io.Files;

import org.apache.commons.io.FileUtils;

/**
 * 提供一些文件工具方法
 *
 * @author mayer - [Created on 2018-08-09 20:14]
 */
public final class FileUtil {

    public static void deleteFile(final String filePath) throws IOException {
        if (Objects.nonNull(filePath)) {
            deleteFile(new File(filePath));
        }
    }

    public static void deleteFile(final File file) throws IOException {
        if (file != null && file.exists()) {
            FileUtils.forceDelete(file);
        }
    }

    public static void createFileOnNoExists(final String filePath) throws IOException {
        Objects.requireNonNull(filePath);
        createFileOnNoExists(new File(filePath));
    }

    public static void createFileOnNoExists(final File file) throws IOException {
        Objects.requireNonNull(file);
        if (!file.exists()) {
            Files.createParentDirs(file);
            if (!file.createNewFile()) {
                throw new IOException("create file error . Path : " + file.getAbsolutePath());
            }
        }
    }

    public static void createDirOnNoExists(final String filePath) throws IOException {
        Objects.requireNonNull(filePath);
        createDirOnNoExists(new File(filePath));
    }

    public static void createDirOnNoExists(final File file) throws IOException {
        Objects.requireNonNull(file);
        if (!file.exists()) {
            Files.createParentDirs(file);
            if (!file.mkdirs()) {
                throw new IOException("create dir error . Path : " + file.getAbsolutePath());
            }
        }
    }

    public static byte[] read(final String filePath) throws IOException {
        Objects.requireNonNull(filePath);
        return read(new File(filePath));
    }

    public static byte[] read(final File file) throws IOException {
        Objects.requireNonNull(file);
        return Files.toByteArray(file);
    }

    public static void write(final String filePath, final byte[] bytes) throws IOException {
        write(filePath, bytes, false);
    }

    public static void write(final File file, final byte[] bytes) throws IOException {
        write(file, bytes, false);
    }

    public static void write(final String filePath, final byte[] bytes, final boolean append) throws IOException {
        Objects.requireNonNull(filePath);
        write(new File(filePath), bytes, append);
    }

    public static void write(final File file, final byte[] bytes, final boolean append) throws IOException {
        Objects.requireNonNull(file);
        FileUtils.writeByteArrayToFile(file, bytes, append);
    }

    public static boolean exists(final String filePath) {
        Objects.requireNonNull(filePath);
        return exists(new File(filePath));
    }

    public static boolean exists(final File file) {
        return file != null && file.exists();
    }

}
