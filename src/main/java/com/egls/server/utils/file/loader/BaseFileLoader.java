package com.egls.server.utils.file.loader;

import java.io.File;

import com.egls.server.utils.file.FileUtil;

/**
 * @author mayer - [Created on 2018-08-25 21:37]
 */
public abstract class BaseFileLoader extends BaseLoader {

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    protected void onFileCreated(final File file) {
        if (FileUtil.exists(file) && !file.isDirectory()) {
            LoaderManager.addReloadFileTask(this, file);
        }
    }

    @Override
    protected void onFileModified(final File file) {
        if (FileUtil.exists(file) && !file.isDirectory()) {
            LoaderManager.addReloadFileTask(this, file);
        }
    }

    @Override
    protected void onFileDeleted(final File file) {
        //do nothing...
        //这里暂时忽略掉,不做任何处理.所以内存是以最后一次为准.
        //未来也许会增加一个方法,来使得文件被删除时,内存也被清理.
    }

    /**
     * 文件被新创建或者有修改的时候,会被调用此方法.
     *
     * @param file 文件
     */
    public abstract void loadFile(final File file);

}
