package com.egls.server.utils.clazz;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import javax.tools.*;

import com.egls.server.utils.file.FileUtil;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *     提供一些编译Java文件的方法.如果需要编译一些Java类,会使用到这个工具类
 *     只有一个输入路径和一个输出路径. 一般输入路径就是"src",输出路径就是"classes"
 *     Note: 在输出之前,会清空输出路径的内容.
 *
 *     Note: 在编译的时候,如果依赖了第三方库,要使用{@link ClassPathUtil}将第三方库加入到class path中.否则无法编译成功
 * </pre>
 *
 * @author mayer - [Created on 2018-08-16 21:27]
 * @see ClassPathUtil
 */
public final class JavaFileCompiler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaFileCompiler.class);

    private final File inputDirectory;

    private final File outputDirectory;

    public JavaFileCompiler(final String inputDirectoryPath, final String outputDirectoryPath) {
        this(new File(inputDirectoryPath), new File(outputDirectoryPath));
    }

    public JavaFileCompiler(final File inputDirectory, final File outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    public boolean compile(final Locale locale, final String charset) throws IOException {
        return compile(locale, charset, null);
    }

    /**
     * 进行编译,可以指定Locale和charset.
     *
     * @param locale                   格式化诊断时要应用的语言环境;null表示默认语言环境。
     * @param charset                  用于解码字节的字符集;如果为空，则使用平台默认值
     * @param thirdPartnerLibDirectory 第三方的依赖库
     * @return true 编译成功
     * @throws IOException IO的异常
     */
    public boolean compile(final Locale locale, final String charset, final File thirdPartnerLibDirectory) throws IOException {
        if (!FileUtil.exists(inputDirectory) || !inputDirectory.isDirectory()) {
            throw new IOException("input directory error. path : " + inputDirectory.getAbsolutePath());
        }

        if (!FileUtil.exists(outputDirectory)) {
            FileUtil.createDirOnNoExists(outputDirectory);
        }

        if (!outputDirectory.isDirectory()) {
            throw new IOException("output directory error. path : " + outputDirectory.getAbsolutePath());
        }

        FileUtils.cleanDirectory(outputDirectory);

        //记录编译之前的class path, 用作编译之后的class path恢复原状
        final String classPath = ClassPathUtil.getClassPath();

        if (FileUtil.exists(thirdPartnerLibDirectory)) {
            if (!thirdPartnerLibDirectory.isDirectory()) {
                throw new IOException("third partner lib directory error. path : " + thirdPartnerLibDirectory.getAbsolutePath());
            }
            ClassPathUtil.addDirectoryToClassPath(thirdPartnerLibDirectory);
        }

        final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        if (javaCompiler == null) {
            LOGGER.error("compile error: can not get system java compiler");
            return false;
        }

        final class JavaFileCompileDiagnosticListener implements DiagnosticListener<JavaFileObject> {

            @Override
            public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                LOGGER.error(String.format("compile error: file [%s] line [%s] column [%s], cause [%s]",
                        diagnostic.getSource().getName(),
                        diagnostic.getLineNumber(),
                        diagnostic.getColumnNumber(),
                        diagnostic.getMessage(locale)));
            }

        }

        final String packageName = "";

        final JavaFileCompileDiagnosticListener diagnosticListener = new JavaFileCompileDiagnosticListener();

        try (StandardJavaFileManager javaFileManager = javaCompiler.getStandardFileManager(diagnosticListener, locale, Charset.forName(charset))) {
            //获取源代码文件
            javaFileManager.setLocation(StandardLocation.SOURCE_PATH, Collections.singletonList(inputDirectory));
            Iterable<JavaFileObject> javaFileObjects = javaFileManager.list(StandardLocation.SOURCE_PATH, packageName, Collections.singleton(JavaFileObject.Kind.SOURCE), true);
            //设置输出路径.
            javaFileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(outputDirectory));

            JavaCompiler.CompilationTask javaCompilerTask = javaCompiler.getTask(
                    null, javaFileManager, diagnosticListener, Arrays.asList("-encoding", charset), null, javaFileObjects
            );

            return javaCompilerTask.call();
        } finally {
            //恢复class path.
            ClassPathUtil.setClassPath(classPath);
        }

    }

}
