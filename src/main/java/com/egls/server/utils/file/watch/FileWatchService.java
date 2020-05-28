package com.egls.server.utils.file.watch;

import com.egls.server.utils.file.FileNameUtil;
import com.egls.server.utils.units.TimeUnitsConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 提供文件系统的一些动作的监视, 封装了{@link java.nio.file.WatchService}的使用.
 * <p>
 * 当文件系统发生了变化的时候,会查找是否有监听器监听这个文件,如果有,则调用该监听器的方法.
 * <p>
 * 这里的监听器执行模式是异步的模式, 本类的线程调用的{@link BaseFileWatchMonitor#monitor(FileWatchEventKind, File)}
 * <p>
 * 如果要实现线程安全的, 需要设计一个线程安全的队列.然后利用{@link BaseFileWatchMonitor#monitor(FileWatchEventKind, File)}向队列中添加.
 *
 * @author mayer - [Created on 2018-08-24 14:11]
 */
public final class FileWatchService {

    static final Logger LOGGER = LoggerFactory.getLogger(FileWatchService.class);

    private static final class Processor extends Thread {

        private Processor() {
            this.setName(this.getClass().getName());
        }

        @Override
        public void run() {
            while (true) {
                while (!shutdown) {
                    try {
                        process();
                    } catch (Exception exception) {
                        LOGGER.error("FileWatchService Processor Error", exception);
                    }
                }
                synchronized (MUTEX) {
                    if (shutdown) {
                        try {
                            CONCURRENT_KEY_MAP.clear();
                            watchService.close();
                            break;
                        } catch (Exception exception) {
                            LOGGER.error("FileWatchService Processor Error", exception);
                        }
                    }
                }
            }
        }

        private void process() throws InterruptedException {
            //此处必须使用一个的等待结束时间,否则没有任务时,会一直陷入在这里.导致无法关闭.
            WatchKey takeKey = watchService.poll(TimeUnitsConst.BLOCKING_QUEUE_POLL_WAITING_MILLISECONDS, TimeUnit.MILLISECONDS);
            if (takeKey == null) {
                return;
            }

            final FileWatchMonitorSet fileWatchMonitorSet = CONCURRENT_KEY_MAP.get(takeKey);
            if (fileWatchMonitorSet != null) {
                fileWatchMonitorSet.handle(takeKey.pollEvents());
            }

            takeKey.reset();
        }

    }

    private static volatile Processor processor;

    /**
     * 初始是关闭着的.
     */
    private static volatile boolean shutdown = true;

    private static volatile WatchService watchService;

    private static final Object MUTEX = new Object();

    private static final ConcurrentMap<WatchKey, FileWatchMonitorSet> CONCURRENT_KEY_MAP = new ConcurrentHashMap<>();

    public static void startup() throws IOException {
        synchronized (MUTEX) {
            if (shutdown) {
                shutdown = false;
                if (processor == null || !processor.isAlive()) {
                    CONCURRENT_KEY_MAP.clear();
                    watchService = FileSystems.getDefault().newWatchService();
                    processor = new Processor();
                    processor.setDaemon(true);
                    processor.start();
                }
            }
        }
    }

    public static void shutdown() {
        synchronized (MUTEX) {
            shutdown = true;
        }
    }

    /**
     * @see #registerDirectory(File, BaseFileWatchMonitor, FileWatchEventKind...)
     */
    public static void registerDirectory(final String directoryPath,
                                         final BaseFileWatchMonitor fileWatchMonitor,
                                         final FileWatchEventKind... fileWatchEventKinds) throws IOException {
        registerDirectory(new File(directoryPath), fileWatchMonitor, fileWatchEventKinds);
    }

    /**
     * 监控文件夹是递归监控的,覆盖子文件夹.
     *
     * @param file                文件
     * @param fileWatchMonitor    监控器
     * @param fileWatchEventKinds 监控的事件
     * @throws IOException 可能产生的IO系统异常
     */
    public static void registerDirectory(final File file,
                                         final BaseFileWatchMonitor fileWatchMonitor,
                                         final FileWatchEventKind... fileWatchEventKinds) throws IOException {
        if (FileNameUtil.nonSubversionFile(file)) {
            if (file.isDirectory()) {

                //传入的参数是文件夹,注册当前的目录
                register(Paths.get(file.getAbsolutePath()), fileWatchMonitor, fileWatchEventKinds);

                //传入的参数是文件夹,注册子目录
                final File[] subDirectories = file.listFiles(File::isDirectory);
                if (subDirectories != null) {
                    for (File subFile : subDirectories) {
                        registerDirectory(subFile, fileWatchMonitor, fileWatchEventKinds);
                    }
                }
            } else {
                //传入的参数是文件.注册文件当前的目录
                register(Paths.get(file.getParentFile().getAbsolutePath()), fileWatchMonitor, fileWatchEventKinds);
            }
        }

    }

    /**
     * @see #registerFile(File, BaseFileWatchMonitor, FileWatchEventKind...)
     */
    public static void registerFile(final String filePath,
                                    final BaseFileWatchMonitor fileWatchMonitor,
                                    final FileWatchEventKind... fileWatchEventKinds) throws IOException {
        registerFile(new File(filePath), fileWatchMonitor, fileWatchEventKinds);
    }

    /**
     * 监视一个文件,通过监控文件所在的文件夹来实现的
     *
     * @param file                文件
     * @param fileWatchMonitor    监控器
     * @param fileWatchEventKinds 监控的事件
     * @throws IOException 可能产生的IO系统异常
     */
    public static void registerFile(final File file,
                                    final BaseFileWatchMonitor fileWatchMonitor,
                                    final FileWatchEventKind... fileWatchEventKinds) throws IOException {
        if (file.isDirectory()) {
            //是文件夹,监听当前文件夹.
            register(Paths.get(file.getAbsolutePath()), fileWatchMonitor, fileWatchEventKinds);
        } else {
            //是文件,监听文件所在的当前文件夹
            register(Paths.get(file.getParentFile().getAbsolutePath()), fileWatchMonitor, fileWatchEventKinds);
        }
    }

    /**
     * 由于{@link WatchService}只能注册文件夹, 通过Directory entry的变化来实现文件监控.
     *
     * @param path                监控的路径(文件夹)
     * @param fileWatchMonitor    监控器
     * @param fileWatchEventKinds 监控的事件
     * @throws IOException 可能产生的IO系统异常
     */
    private static void register(final Path path,
                                 final BaseFileWatchMonitor fileWatchMonitor,
                                 final FileWatchEventKind... fileWatchEventKinds) throws IOException {
        if (shutdown) {
            throw new IllegalStateException("must call FileWatchService#startup() method");
        }

        final WatchEvent.Kind<?>[] kinds = new WatchEvent.Kind[fileWatchEventKinds.length];
        for (int i = 0; i < kinds.length; i++) {
            kinds[i] = fileWatchEventKinds[i].getFileWatchEventKind();
        }

        final WatchKey key = path.register(watchService, kinds);

        final FileWatchMonitorSet fileWatchMonitorSet = CONCURRENT_KEY_MAP.computeIfAbsent(key, watchKey -> new FileWatchMonitorSet(path));

        fileWatchMonitorSet.addMonitor(fileWatchMonitor);
    }

    private FileWatchService() {
        //私有构造方法,禁止构造
    }

}
