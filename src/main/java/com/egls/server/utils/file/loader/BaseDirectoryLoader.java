package com.egls.server.utils.file.loader;

import com.egls.server.utils.file.FileNameUtil;
import com.egls.server.utils.file.FileType;
import com.egls.server.utils.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author mayer - [Created on 2018-08-25 21:44]
 */
public abstract class BaseDirectoryLoader<T> extends BaseLoader {

    protected final FileType fileType;

    public BaseDirectoryLoader(final FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    protected void onFileCreated(final File file) throws IOException {
        if (FileNameUtil.nonSubversionFile(file)) {
            //文件夹内有任何一个文件被创建了,都算作文件夹变化
            LoaderManager.addReloadDirectoryTask(this);
            //监控只能监控当级文件夹,这里需要处理新增的子文件夹.
            if (FileUtil.exists(file) && file.isDirectory()) {
                LoaderManager.registerDirectory(file, this);
            }
        }
    }

    @Override
    protected void onFileModified(final File file) {
        if (FileNameUtil.nonSubversionFile(file)) {
            //文件夹内有任何一个文件变化了,都算作文件夹变化
            LoaderManager.addReloadDirectoryTask(this);
        }
    }

    @Override
    protected void onFileDeleted(final File file) {
        if (FileNameUtil.nonSubversionFile(file)) {
            //文件夹内有任何一个文件删除了,都算作文件夹变化
            LoaderManager.addReloadDirectoryTask(this);
        }
    }

    public final FileType getFileType() {
        return fileType;
    }

    protected List<T> loadDirectory(final File file) {
        List<T> list = Collections.emptyList();
        if (FileNameUtil.nonSubversionFile(file)) {
            list = new ArrayList<>();
            if (file.isDirectory()) {
                final File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        list.addAll(loadDirectory(subFile));
                    }
                }
            } else {
                if (fileType.isMatch(file)) {
                    list.add(loadFile(file));
                }
            }
        }
        return list;
    }


    /**
     * 文件夹内的文件的解析方式.这里使用泛型返回的方式,来避免文件夹内多文件载入批次操作的问题.
     *
     * @param file 文件
     * @return 解析对象
     */
    protected abstract T loadFile(final File file);

    /**
     * 这是一个需要实现的载入方法.
     * <pre>
     *     for example:
     *         protected void loadDirectory() {
     *             File file = new File("/path");
     *             List list = loadDirectory(file);
     *         }
     * </pre>
     */
    public abstract void loadDirectory();

}
