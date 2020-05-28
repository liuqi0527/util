package com.egls.server.utils.file.watch;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 很多{@link BaseFileWatchMonitor}的集合.对于一个文件变化之后,可以响应多种操作.
 * <pre>
 *     比如:
 *          1, 一个路径下,每个文件进行不同的处理
 *          2, 一个路径下,每种类型文件进行不同处理
 *     所以,一个路径下,允许挂载多个监听处理器,用来处理多种情况.
 * </pre>
 * Note: 对于相同的{@link BaseFileWatchMonitor}进行了去重.
 *
 * @author mayer - [Created on 2018-08-24 14:21]
 * @see BaseFileWatchMonitor#equals(Object)
 * @see BaseFileWatchMonitor#hashCode()
 */
final class FileWatchMonitorSet {

    private final Path interestedPath;

    private final CopyOnWriteArrayList<BaseFileWatchMonitor> fileWatchMonitors = new CopyOnWriteArrayList<>();

    FileWatchMonitorSet(final Path interestedPath) {
        this.interestedPath = interestedPath;
    }

    void addMonitor(final BaseFileWatchMonitor fileWatchMonitor) {
        //使用此方法进行去重.equals
        fileWatchMonitors.addIfAbsent(fileWatchMonitor);
    }

    void handle(final List<WatchEvent<?>> events) {
        for (WatchEvent<?> event : events) {
            WatchEvent.Kind<?> eventKind = event.kind();
            final FileWatchEventKind fileWatchEventKind = FileWatchEventKind.transform(eventKind);
            if (fileWatchEventKind == FileWatchEventKind.OVERFLOW) {
                FileWatchService.LOGGER.error("FileWatchMonitorSet cant handle file watch event kind " + eventKind);
            } else {
                handleFileWatchEventKind(fileWatchEventKind, (Path) event.context());
            }
        }
    }

    private void handleFileWatchEventKind(final FileWatchEventKind fileWatchEventKind, final Path eventPath) {
        final File eventFile = interestedPath.resolve(eventPath).toFile();
        if (eventFile == null) {
            return;
        }

        for (BaseFileWatchMonitor fileWatchMonitor : fileWatchMonitors) {
            try {
                if (fileWatchMonitor.match(eventFile)) {
                    fileWatchMonitor.monitor(fileWatchEventKind, eventFile);
                }
            } catch (Exception exception) {
                FileWatchService.LOGGER.error("FileWatchMonitor occur an error", exception);
            }
        }
    }

}
