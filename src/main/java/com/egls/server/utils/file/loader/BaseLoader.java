package com.egls.server.utils.file.loader;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mayer - [Created on 2018-08-25 21:00]
 */
abstract class BaseLoader implements Comparable<BaseLoader> {

    private static final AtomicInteger GLOBAL_LOADER_INDEX_BUILDER = new AtomicInteger();

    private final int index = GLOBAL_LOADER_INDEX_BUILDER.getAndIncrement();

    public final int getIndex() {
        return index;
    }

    @Override
    public final int compareTo(final BaseLoader hotLoader) {
        return Integer.compare(this.index, hotLoader.index);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseLoader that = (BaseLoader) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    /**
     * 在监听的文件路径下,某个文件被创建后,会调用此方法.
     * Note: 此时该file已经被创建
     *
     * @param file 文件
     * @throws IOException IO异常
     */
    protected abstract void onFileCreated(final File file) throws IOException;

    /**
     * 在监听的文件路径下,某个文件被修改后,会调用此方法.
     * Note: 此时该file的内容已经将修改动作保存.
     *
     * @param file 文件
     */
    protected abstract void onFileModified(final File file);

    /**
     * 在监听的文件路径下,某个文件被删除后,会调用此方法.
     * Note: 此时该file已经被删除
     *
     * @param file 文件
     */
    protected abstract void onFileDeleted(final File file);

}
