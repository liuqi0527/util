package com.egls.server.utils.file.watch;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.egls.server.utils.file.FileType;
import com.egls.server.utils.file.FileUtil;
import com.egls.server.utils.file.TextFileUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author mayer - [Created on 2018-09-04 14:04]
 */
public class TestFileWatchService {

    private static final String FILE_NAME = "text_file_watch_service.txt";

    private static final String FILE_CONTENT = "text_file_watch_service";

    private static final Collection<FileWatchEventKind> TYPE_FILE_WATCH_EVENT_KINDS = Collections.synchronizedCollection(new ArrayList<>());
    private static final Collection<FileWatchEventKind> NAME_FILE_WATCH_EVENT_KINDS = Collections.synchronizedCollection(new ArrayList<>());

    private static final Collection<File> TYPE_FILES = Collections.synchronizedCollection(new ArrayList<>());
    private static final Collection<File> NAME_FILES = Collections.synchronizedCollection(new ArrayList<>());

    @Test
    public void testCreate() throws Exception {

        TYPE_FILE_WATCH_EVENT_KINDS.clear();
        NAME_FILE_WATCH_EVENT_KINDS.clear();
        TYPE_FILES.clear();
        NAME_FILES.clear();

        final File file = new File("./src/test/java/com/egls/server/utils/file/watch/" + FILE_NAME);
        FileUtil.deleteFile(file);
        //等待监控线程反应
        Thread.sleep(100L);

        final BaseFileWatchMonitorWithType fileWatchMonitorWithType = new BaseFileWatchMonitorWithType(new Object(), FileType.TXT) {
            @Override
            protected void monitor(FileWatchEventKind fileWatchEventKind, File eventFile) {
                TYPE_FILES.add(eventFile);
                TYPE_FILE_WATCH_EVENT_KINDS.add(fileWatchEventKind);
            }
        };

        final BaseFileWatchMonitorWithName fileWatchMonitorWithName = new BaseFileWatchMonitorWithName(new Object(), FILE_NAME) {
            @Override
            protected void monitor(FileWatchEventKind fileWatchEventKind, File eventFile) {
                NAME_FILES.add(eventFile);
                NAME_FILE_WATCH_EVENT_KINDS.add(fileWatchEventKind);
            }
        };

        FileWatchService.startup();
        FileWatchService.registerDirectory(file, fileWatchMonitorWithType, FileWatchEventKind.CREATE, FileWatchEventKind.MODIFY, FileWatchEventKind.DELETE);
        FileWatchService.registerDirectory(file, fileWatchMonitorWithName, FileWatchEventKind.CREATE, FileWatchEventKind.MODIFY, FileWatchEventKind.DELETE);
        TextFileUtil.write(FILE_CONTENT, file, false, false);

        //等待监控线程反应
        Thread.sleep(100L);

        Assert.assertTrue(TYPE_FILE_WATCH_EVENT_KINDS.contains(FileWatchEventKind.CREATE));
        Assert.assertTrue(TYPE_FILE_WATCH_EVENT_KINDS.contains(FileWatchEventKind.MODIFY));
        Assert.assertEquals(TYPE_FILE_WATCH_EVENT_KINDS.size(), TYPE_FILES.size());
        for (File f : TYPE_FILES) {
            try {
                Assert.assertEquals(FILE_CONTENT, TextFileUtil.read(f, false));
            } catch (Exception e) {
                Assert.fail();
            }
        }

        Assert.assertTrue(NAME_FILE_WATCH_EVENT_KINDS.contains(FileWatchEventKind.CREATE));
        Assert.assertTrue(NAME_FILE_WATCH_EVENT_KINDS.contains(FileWatchEventKind.MODIFY));
        Assert.assertEquals(NAME_FILE_WATCH_EVENT_KINDS.size(), NAME_FILES.size());
        for (File f : NAME_FILES) {
            try {
                Assert.assertEquals(FILE_CONTENT, TextFileUtil.read(f, false));
            } catch (Exception e) {
                Assert.fail();
            }
        }

        //删除
        FileUtil.deleteFile(file);
        //等待监控线程反应
        Thread.sleep(100L);

        Assert.assertTrue(TYPE_FILE_WATCH_EVENT_KINDS.contains(FileWatchEventKind.DELETE));
        Assert.assertEquals(TYPE_FILE_WATCH_EVENT_KINDS.size(), TYPE_FILES.size());
        Assert.assertTrue(NAME_FILE_WATCH_EVENT_KINDS.contains(FileWatchEventKind.DELETE));
        Assert.assertEquals(NAME_FILE_WATCH_EVENT_KINDS.size(), NAME_FILES.size());
    }

}