package com.egls.server.utils.file.watch;

import java.io.File;
import java.util.Objects;

/**
 * 按照文件名字来进行的监控器.
 * 如果对于一个文件夹注册此类型的监听器,那么该文件夹的子文件夹内的文件也会被处理到.
 *
 * @author mayer - [Created on 2018-08-24 14:39]
 */
public abstract class BaseFileWatchMonitorWithName extends BaseFileWatchMonitor {

    protected final String fileName;

    protected BaseFileWatchMonitorWithName(final Object reference, final String fileName) {
        super(reference);
        this.fileName = fileName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BaseFileWatchMonitorWithName that = (BaseFileWatchMonitorWithName) o;
        return Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileName);
    }

    @Override
    protected final boolean match(final File file) {
        //允许子文件夹
        return file.isDirectory() || fileName.equals(file.getName());
    }

}
