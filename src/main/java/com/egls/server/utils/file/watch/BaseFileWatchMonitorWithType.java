package com.egls.server.utils.file.watch;

import java.io.File;
import java.util.Objects;

import com.egls.server.utils.file.FileType;

/**
 * 按照文件类型来进行的监控器.
 * 如果对于一个文件夹注册此类型的监听器,那么该文件夹的子文件夹内的文件也会被处理到.
 *
 * @author mayer - [Created on 2018-08-24 14:39]
 */
public abstract class BaseFileWatchMonitorWithType extends BaseFileWatchMonitor {

    protected final FileType fileType;

    protected BaseFileWatchMonitorWithType(final Object reference, final FileType fileType) {
        super(reference);
        this.fileType = fileType;
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
        BaseFileWatchMonitorWithType that = (BaseFileWatchMonitorWithType) o;
        return fileType == that.fileType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileType);
    }

    @Override
    protected final boolean match(final File file) {
        //允许子文件夹
        return file.isDirectory() || fileType.isMatch(file);
    }

}
