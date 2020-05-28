package com.egls.server.utils.clazz;

import com.egls.server.utils.file.FileNameUtil;
import com.egls.server.utils.file.FileType;
import com.egls.server.utils.file.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 *     Java中的class loader的继承顺序
 *         Bootstrap ClassLoader - Extension ClassLoader - Application ClassLoader - UserExtClassLoader
 *
 *         本类的父类加载器是AppClassLoader
 *         本类只能使用一次.当你加载过的类更改了,新创建一个类加载器,重新载入class.旧的类和类加载器会在适当的时候自动销毁.
 *
 *         如果需要载入第三方类库.请调用{@link #addURL(URL)}引入.如果不这样引入第三方库.会调用时候无法找到Class.
 *         当你要载入的类需要引入资源的时候,调用{@link #addURL(URL)}引入.{@code this.getClass().getResource(String)}
 * </pre>
 *
 * <pre>
 *      用户扩展类加载器,能够应付下面的几种场景:
 *          1.在执行非置信代码之前，自动验证数字签名
 *          2.动态地创建符合用户特定需要的定制化构建类
 *          3.从特定的场所取得java class，例如数据库中
 *          4.加载资源
 *      例如：
 *          实现jsp动态加载页面功能.jsp更改需要进行编译和载入.相关依赖的资源也要载入进来.{@code this.getClass().getResource(String)}
 * </pre>
 *
 * <pre>
 *      类加载器和类不能显式的指定销毁或者卸载,但是在官方文档中是这样的说明的.
 *
 *      "class or interface may be unloaded if and only if its class loader is unreachable. Classes loaded by the bootstrap loader may not be unloaded."
 *
 *      所以当你加载过的类更改了,如何销毁原来的类?新创建一个类加载器,重新载入class.旧的类和类加载器会在适当的时候自动销毁.
 * </pre>
 *
 * <pre>
 *     这个类加载器设计针对一个应用项目的.这样可以防止Class Name被重新定义,产生冲突.
 *     如果你需要载入多个应用项目.你可以创建多个类加载器对象.
 *
 *     例如:
 *     下面的情况,这是两个应用项目.应当创建两个类加载器.否则Class Name冲突无法解决.
 *
 *     jsp
 *     |
 *     |___com
 *          |____test
 *                 |____Test.class
 *
 *     gameScript
 *     |
 *     |___com
 *          |____test
 *                 |____Test.class
 *
 * </pre>
 *
 * @author mayer - [Created on 2018-08-16 22:10]
 */
public final class UserClassLoader extends URLClassLoader {

    /**
     * 你的项目的class根目录.从这里开始递归载入文件夹内的所有class文件.
     * 如果你需要将资源加入到class path中.请调用{@link #addURL(URL)}
     */
    private final File classFileRootDirectory;

    /**
     * 存放类名和类对象的Map
     */
    private final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    /**
     * 标记是否已经使用过了.只能使用一次,重复使用会造成冲突
     */
    private volatile boolean dirty = false;

    public UserClassLoader(final String classRootFileDirectoryPath) {
        this(new File(classRootFileDirectoryPath));
    }

    public UserClassLoader(final File classFileRootDirectory) {
        //获取本类的加载器作为父加载器,AppClassLoader
        super(new URL[0], UserClassLoader.class.getClassLoader());
        this.classFileRootDirectory = classFileRootDirectory;
    }

    public Class<?> getClass(final String className) {
        return classMap.get(className);
    }

    public void addURL(final File... files) throws MalformedURLException {
        addURL(Arrays.asList(files));
    }

    public void addURL(final Collection<File> files) throws MalformedURLException {
        for (File file : files) {
            if (FileUtil.exists(file) && !file.isDirectory()) {
                addURL(file.toURI().toURL());
            }
        }
    }

    public void addDirectoryURL(final File directory) throws MalformedURLException {
        if (FileUtil.exists(directory) && FileNameUtil.nonSubversionFile(directory)) {
            if (directory.isDirectory()) {
                File[] subFiles = directory.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        addDirectoryURL(subFile);
                    }
                }
            } else {
                addURL(directory);
            }
        }
    }

    /**
     * 使类加载器进行递归加载所有class文件.只能使用一次.
     *
     * @param thirdPartnerLibDirectory 第三方库的路径.lib/ , 可以为null
     * @param resourceDirectory        资源路径 resource/ , 可以为null
     * @throws IOException IO异常
     */
    public void loadClassFiles(final File thirdPartnerLibDirectory, final File resourceDirectory) throws IOException {
        if (!FileUtil.exists(classFileRootDirectory) || !classFileRootDirectory.isDirectory()) {
            throw new IOException("class file root directory error. path : " + classFileRootDirectory.getAbsolutePath());
        }
        if (dirty) {
            throw new RuntimeException("this class loader was dirty. please construct a new class loader when you have some new class files.");
        }
        dirty = true;

        addDirectoryURL(thirdPartnerLibDirectory);
        addDirectoryURL(resourceDirectory);

        this.loadClassFiles(classFileRootDirectory);
    }

    private void loadClassFiles(final File classFile) throws IOException {
        if (FileUtil.exists(classFile) && FileNameUtil.nonSubversionFile(classFile)) {
            if (classFile.isDirectory()) {
                File[] subFiles = classFile.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        loadClassFiles(subFile);
                    }
                }
            } else {
                loadClassFile(classFile);
            }
        }
    }

    private void loadClassFile(final File classFile) throws IOException {
        String className = StringUtils.removeStart(classFile.getAbsolutePath(), classFileRootDirectory.getAbsolutePath());
        className = StringUtils.strip(className, File.separator);
        className = StringUtils.removeEnd(className, FileType.CLASS.getPointAndExtension());
        className = StringUtils.replaceChars(className, File.separatorChar, '.');
        byte[] classBinaryCode = FileUtil.read(classFile);
        // 使用字节码定义类
        final Class<?> clazz = this.defineClass(className, classBinaryCode, 0, classBinaryCode.length);
        classMap.put(className, clazz);
    }

}
