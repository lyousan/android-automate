package cn.chci.hmcs.common.toolkit.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.*;

/**
 * @Author: 有三
 * @DateTime: 2021/5/17 上午11:19
 * @Description: 文件输出工具类
 */
@Slf4j
public class FileUtils {
    /**
     * 检测文件格式是否正确
     *
     * @param file       受检文件
     * @param needCreate 文件不存在时 是否需要创建
     * @param types      文件类型
     * @throws IOException
     */
    public static void checkFileType(File file, boolean needCreate, Set<String> types) throws IOException {
        checkFileType(file, needCreate, types.toArray(new String[types.size()]));
    }

    /**
     * 检测文件格式是否正确
     *
     * @param file       受检文件
     * @param needCreate 文件不存在时 是否需要创建
     * @param type       文件类型
     * @throws IOException
     */
    public static void checkFileType(File file, boolean needCreate, String... type) throws IOException {
        if (!file.exists() || !file.isFile()) {
            if (needCreate) {
                file.createNewFile();
                log.debug("创建文件 ==>: {}", file.getAbsolutePath());
            }
        }
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        for (String s : type) {
            if (s.equals(suffix)) {
                return;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < type.length; i++) {
            builder.append(type[i]);
            if (i < type.length - 1) {
                builder.append(",");
            }
        }
        log.error("文件类型不支持 ==>: {} \t 请使用 {}", suffix, builder.toString());
        throw new IOException("文件类型不支持, 请使用 " + builder.toString());
    }


    /**
     * 注： 输出格式根据 `toString()` 决定
     * JAVA对象
     *
     * @param file   输出的文件（txt）
     * @param append 追加模式/覆盖模式
     * @param string 输出内容
     * @return 输出到txt文件的行数
     * @throws IOException
     */
    public static Long beanToTxt(File file, boolean append, String string) throws IOException {
        checkFileType(file, true, ".txt");
        log.debug("开始写入数据到 [ {} ] ...... ", file.getAbsolutePath());
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), "utf-8"));
        Long count = 0L;
        bw.write(string);
        bw.newLine();
        count++;
        bw.flush();
        bw.close();
        log.debug(" ...... 写入完成 [ 预计写入:{} \t 实际写入:{}]", 1, count);
        return count;
    }


    /**
     * 获取该类及其除Object类外的所有父类的字段
     *
     * @param clazz
     * @return
     */
    public static Field[] getAncestorsFields(Class clazz) {
        Set<Field> fieldSet = new LinkedHashSet<>();
        Field[] fields = clazz.getDeclaredFields();
        fieldSet.addAll(Arrays.asList(fields));
        while (true) {
            Class superclass = clazz.getSuperclass();
            if (superclass == Object.class) {
                break;
            }
            getAncestorsFields(superclass, fieldSet);
            clazz = superclass;
        }
        return fieldSet.toArray(fields);
    }

    /**
     * 获取父类，并合并到集合中
     *
     * @param clazz
     * @param fieldSet
     */
    private static void getAncestorsFields(Class clazz, Set<Field> fieldSet) {
        Field[] superClassFields = clazz.getDeclaredFields();
        fieldSet.addAll(Arrays.asList(superClassFields));
    }

    /**
     * 读取txt文本文件，转String
     *
     * @param file 读取的文件
     * @return
     */
    public static List<String> txtToString(File file) throws IOException {
        log.debug("开始读取文件 [ {} ] ......", file.getName());
        List<String> list = new ArrayList<>();
        checkFileType(file, true, ".txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        String len = null;
        while ((len = br.readLine()) != null) {
            list.add(len);
        }
        br.close();
        log.debug(" ...... 读取完成");
        return list;
    }


    /**
     * 保存properties文件
     *
     * @param properties
     * @param file
     * @throws IOException
     */
    public static void storeProperties(Properties properties, File file) throws IOException {
        properties.store(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8")), "");
        log.debug(" ...... properties 文件 [ {} ] 保存完成", file.getAbsolutePath());
    }

    /**
     * 读取properties文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Properties loadProperties(File file) throws IOException {
        log.debug("文件地址：{}", file.getAbsolutePath());
        Properties properties = new Properties();
        properties.load(new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8")));
        log.debug(" ...... properties 文件 [ {} ] 读取完成", file.getAbsolutePath());
        return properties;
    }

    /**
     * 判断是否是文件
     *
     * @param file
     * @param needCreate 不存在或者不是文件的时候是否需要创建该文件
     * @return 是文件时返回true，不存在或不是文件时返回false
     * @throws IOException
     */
    public static boolean checkFile(File file, boolean needCreate) throws IOException {
        if (!file.exists() || !file.isFile()) {
            if (needCreate) {
                file.createNewFile();
            }
            return false;
        }
        return true;
    }

    /**
     * 判断是否是文件夹
     *
     * @param folder
     * @param needCreate 不存在或者不是文件夹的时候是否需要创建该文件夹
     * @return 是文件夹和创建成功时返回true，不存在或不是文件夹时返回false
     * @throws IOException
     */
    public static boolean checkDirectory(File folder, boolean needCreate) {
        if (!folder.exists() || !folder.isFile()) {
            if (needCreate) {
                folder.mkdirs();
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * 获取文件创建时间
     *
     * @param filePath
     * @return
     */
    public static Long getFileCreateTime(String filePath) {
        File file = new File(filePath);
        try {
            Path path = Paths.get(filePath);
            BasicFileAttributeView basicView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            BasicFileAttributes attr = basicView.readAttributes();
            return attr.creationTime().toMillis();
        } catch (Exception e) {
            e.printStackTrace();
            return file.lastModified();
        }
    }

    /**
     * 删除本地文件
     *
     * @param filePaths
     */
    public static void deleteFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            new File(filePath).delete();
        }
    }
}
