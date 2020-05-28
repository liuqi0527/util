package com.egls.server.utils.file.watch;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

/**
 * 标准的监控事件类型在{@link StandardWatchEventKinds}里面.
 * 但是由于{@link WatchEvent.Kind}是个接口,实现类是个私有类.
 * 声明类型的时候很麻烦.所以使用此类来进行转接.
 *
 * @author mayer - [Created on 2018-08-24 15:35]
 * @see WatchEvent
 * @see WatchEvent.Kind
 * @see StandardWatchEventKinds
 */
public enum FileWatchEventKind {

    /**
     * @see StandardWatchEventKinds#OVERFLOW
     */
    OVERFLOW(StandardWatchEventKinds.OVERFLOW),

    /**
     * @see StandardWatchEventKinds#ENTRY_CREATE
     */
    CREATE(StandardWatchEventKinds.ENTRY_CREATE),

    /**
     * @see StandardWatchEventKinds#ENTRY_MODIFY
     */
    MODIFY(StandardWatchEventKinds.ENTRY_MODIFY),

    /**
     * 当触发的文件系统事件是这个的时候,该文件已经被删除.注意不能读写,否则会抛出{@link FileNotFoundException}
     * <p>
     * 注意监控此事件类型时需要进行边界检查,调用这个方法{@link File#exists()}判断文件是否存在
     *
     * @see FileNotFoundException
     */
    DELETE(StandardWatchEventKinds.ENTRY_DELETE);

    public static FileWatchEventKind transform(final WatchEvent.Kind<?> watchEventKind) {
        if (watchEventKind == StandardWatchEventKinds.ENTRY_CREATE) {
            return CREATE;
        } else if (watchEventKind == StandardWatchEventKinds.ENTRY_MODIFY) {
            return MODIFY;
        } else if (watchEventKind == StandardWatchEventKinds.ENTRY_DELETE) {
            return DELETE;
        }
        return OVERFLOW;
    }

    private final WatchEvent.Kind<?> fileWatchEventKind;

    FileWatchEventKind(final WatchEvent.Kind<?> fileWatchEventKind) {
        this.fileWatchEventKind = fileWatchEventKind;
    }

    public WatchEvent.Kind<?> getFileWatchEventKind() {
        return fileWatchEventKind;
    }


}
