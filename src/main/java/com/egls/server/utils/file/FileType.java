package com.egls.server.utils.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * 文件类型
 *
 * @author mayer - [Created on 2018-08-09 20:32]
 */
public enum FileType implements FilenameFilter {

    /**
     * any的时候表示也监控新增文件夹和任何新增文件
     */
    ANY(StringUtils.EMPTY) {
        @Override
        public String getPointAndExtension() {
            return getExtension();
        }

        @Override
        public String getPatternString() {
            return "^.*$";
        }
    },

    UI("ui"),
    SH("sh"),
    JS("js"),

    FTL("ftl"),
    XML("xml"),
    TXT("txt"),
    CSV("csv"),
    DAT("dat"),
    BAT("bat"),
    SQL("sql"),
    HTM("htm"),
    CSS("css"),
    JSP("jsp"),
    JAR("jar"),

    //艾格拉斯地图数据文件扩展名
    SSBC("ssbc"),
    HTML("html"),
    JAVA("java"),

    CLASS("class"),

    PROPERTIES("properties");

    final String extension;

    FileType(final String endString) {
        this.extension = endString;
    }

    @Override
    public boolean accept(final File dir, final String name) {
        return isMatch(name);
    }

    public boolean isMatch(final String fileNameOrFilePath) {
        return Objects.nonNull(fileNameOrFilePath) && fileNameOrFilePath.toLowerCase().endsWith(getPointAndExtension());
    }

    public boolean isMatch(final File file) {
        return file != null && isMatch(file.getName());
    }

    public String getExtension() {
        return extension;
    }

    public String getPointAndExtension() {
        return '.' + getExtension();
    }

    public String getPatternString() {
        return "^.*\\." + getExtension() + "$";
    }

}