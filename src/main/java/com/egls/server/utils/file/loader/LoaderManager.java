package com.egls.server.utils.file.loader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.egls.server.utils.CollectionUtil;
import com.egls.server.utils.file.FileUtil;
import com.egls.server.utils.file.watch.BaseFileWatchMonitorWithName;
import com.egls.server.utils.file.watch.BaseFileWatchMonitorWithType;
import com.egls.server.utils.file.watch.FileWatchEventKind;
import com.egls.server.utils.file.watch.FileWatchService;
import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mayer - [Created on 2018-08-25 23:01]
 */
public final class LoaderManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoaderManager.class);

    private static final AtomicReference<Set<BaseDirectoryLoader>> DIRECTORY_LOADERS = new AtomicReference<>(new HashSet<>());

    private static final AtomicReference<Map<BaseFileLoader, List<File>>> FILE_LOADERS = new AtomicReference<>(new HashMap<>());

    public static void startup() throws IOException {
        FileWatchService.startup();
    }

    public static void shutdown() {
        FileWatchService.shutdown();
    }

    public static <T extends BaseFileLoader> void loadAndRegisterFile(String filePath, T loader) {
        try {
            File file = new File(filePath);
            loader.loadFile(file);
            LoaderManager.registerFile(file, loader);
        } catch (Exception exception) {
            LOGGER.error(String.format("load file %s error", filePath), exception);
        }
    }

    public static <T extends BaseDirectoryLoader> void loadAndRegisterDirectory(String folderPath, T loader) {
        try {
            loader.loadDirectory();
            LoaderManager.registerDirectory(new File(folderPath), loader);
        } catch (Exception exception) {
            LOGGER.error(String.format("load directory %s error", folderPath), exception);
        }
    }

    public static void registerDirectory(final File directory, final BaseDirectoryLoader loader) throws IOException {
        final BaseFileWatchMonitorWithType fileWatchMonitor = new BaseFileWatchMonitorWithType(loader, loader.getFileType()) {
            @Override
            protected void monitor(FileWatchEventKind fileWatchEventKind, File eventFile) {
                handle(loader, fileWatchEventKind, eventFile);
            }
        };
        FileWatchService.registerDirectory(directory, fileWatchMonitor, FileWatchEventKind.CREATE, FileWatchEventKind.MODIFY, FileWatchEventKind.DELETE);
    }

    public static void registerFile(final File file, final BaseFileLoader loader) throws IOException {
        final BaseFileWatchMonitorWithName fileWatchMonitor = new BaseFileWatchMonitorWithName(loader, file.getName()) {
            @Override
            protected void monitor(FileWatchEventKind fileWatchEventKind, File eventFile) {
                handle(loader, fileWatchEventKind, eventFile);
            }
        };
        FileWatchService.registerFile(file, fileWatchMonitor, FileWatchEventKind.CREATE, FileWatchEventKind.MODIFY, FileWatchEventKind.DELETE);
    }

    private static void handle(final BaseLoader loader, final FileWatchEventKind fileWatchEventKind, final File eventFile) {
        try {
            switch (fileWatchEventKind) {
                case CREATE:
                    loader.onFileCreated(eventFile);
                    break;
                case MODIFY:
                    loader.onFileModified(eventFile);
                    break;
                case DELETE:
                    loader.onFileDeleted(eventFile);
                    break;
                default:
                    break;
            }
        } catch (IOException iOException) {
            LOGGER.error("BaseLoader occur an error", iOException);
        }
    }

    /**
     * 某个文件变化了.
     */
    static void addReloadFileTask(final BaseFileLoader baseFileLoader, final File file) {
        Map<BaseFileLoader, List<File>> map = FILE_LOADERS.updateAndGet(value -> value != null ? value : Maps.newHashMap());
        map.computeIfAbsent(baseFileLoader, key -> new ArrayList<>()).add(file);
    }

    /**
     * 某个文件夹变化了,文件夹需要注意重复的问题.因为文件夹内包含多个文件,多次触发此方法
     */
    static void addReloadDirectoryTask(final BaseDirectoryLoader baseDirectoryLoader) {
        DIRECTORY_LOADERS.updateAndGet(value -> value != null ? value : new HashSet<>()).add(baseDirectoryLoader);
    }

    /**
     * 调用此方法执行载入资源文件的变化.
     */
    public static void reload() {

        //检测到变化的文件.
        final Map<BaseFileLoader, List<File>> map = FILE_LOADERS.getAndUpdate(value -> Maps.newHashMap());

        //检测到变化的文件夹.
        final Set<BaseDirectoryLoader> set = DIRECTORY_LOADERS.getAndUpdate(value -> new HashSet<>());

        //所有变化过的HotLoader
        final List<BaseLoader> loaderList = new ArrayList<>();
        loaderList.addAll(map.keySet());
        loaderList.addAll(set);
        Collections.sort(loaderList);

        //将所有的HotLoader排序
        for (BaseLoader loader : loaderList) {
            //BaseLoader是一个包内可见的类.所以只有两个继承类,这里不会出现问题.
            if (loader instanceof BaseFileLoader) {
                //执行文件的载入
                reloadFile((BaseFileLoader) loader, map.get(loader));
            } else if (loader instanceof BaseDirectoryLoader) {
                //执行文件夹的载入
                reloadDirectory((BaseDirectoryLoader) loader);
            }
        }
    }

    private static void reloadFile(final BaseFileLoader loader, final List<File> files) {
        if (!CollectionUtil.isEmpty(files)) {
            for (File file : files) {
                try {
                    if (FileUtil.exists(file)) {
                        loader.loadFile(file);
                    }
                } catch (Exception exception) {
                    LOGGER.error("BaseFileLoader reload error", exception);
                }
            }
        }
    }

    private static void reloadDirectory(final BaseDirectoryLoader loader) {
        try {
            loader.loadDirectory();
        } catch (Exception exception) {
            LOGGER.error("BaseDirectoryLoader reload error", exception);
        }
    }

    private LoaderManager() {
        //私有构造方法,禁止构造
    }

}
