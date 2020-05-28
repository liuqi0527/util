package com.egls.server.utils.file.watch;

import java.io.File;
import java.util.Objects;

/**
 * 文件变化之后的处理器,检测是否匹配,匹配之后会被调用处理方法.
 *
 * @author mayer - [Created on 2018-08-24 14:18]
 */
public abstract class BaseFileWatchMonitor {

    /**
     * 这是一个引用对象,用来进行比较两个Monitor是否相同的.
     * 如果使用者持续的调用注册监听器,而监听器对象始终是相同的时候.
     * 防止能够重复被添加相同的操作对象给相同的路径
     *
     * @see #hashCode()
     * @see #equals(Object)
     */
    protected final Object reference;

    protected BaseFileWatchMonitor(final Object reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseFileWatchMonitor that = (BaseFileWatchMonitor) o;
        return Objects.equals(reference, that.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }

    /**
     * 当一个文件传送进来之后,返回是否与我匹配
     *
     * @param file Directory entry
     * @return true 表示是本监听器匹配的. false 表示不匹配.
     */
    protected abstract boolean match(final File file);

    /**
     * 具体的执行逻辑.当匹配方法返回true的时候,紧接着就会调用此方法.
     *
     * @param fileWatchEventKind 事件类型
     * @param file               Directory entry
     */
    protected abstract void monitor(final FileWatchEventKind fileWatchEventKind, final File file);

}
