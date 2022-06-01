package com.grgbanking.core.utils;


import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: pmtao
 * @Date: 2020/9/21 13:40
 * @Version 1.0
 **/
public class GrgFileUtils {
    /**
     * 传入文件夹，获取文件夹下的所有文件
     **/
    public static File[] getListFileFromDir(String dir) throws Exception {
        File file = new File(dir);
        if (!file.exists())
            throw new Exception("文件夹不存在！");
        if (!file.isDirectory())
            throw new Exception("文件类型不是目录！");

        return file.listFiles();
    }

    /**
     * @param files 文件集合
     * @param type  需要归类的文件类型
     *              [files, type]* @return 找出的文件类型文件集合
     * @author pmtao
     * @date 2020/9/23
     */
    public static List<File> getFileByTypeFromList(File[] files, String type) {

        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            String fileName = file.getName();
            if (!fileName.contains("."))
                continue;
            String fileType = fileName.substring(fileName.lastIndexOf("."), fileName.length());

            //if (fileType.equals(type)) {
            if (fileType.toLowerCase().equals(type.toLowerCase())){
                fileList.add(file);
            }
        }
        if (fileList.size() == 0)
            return null;
        return fileList;
    }

    public static List<File> getFileFromDirByType(String dir, String type) {
        try {
            return getFileByTypeFromList(getListFileFromDir(dir), type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void zipFile(String fileName, String zipName) {
        File file = new File(fileName);
        if (!file.exists())
            return;
        File zipFile = new File(zipName);
        if (zipFile.exists())
            zipFile.delete();
        // 判断压缩后的文件存在不，不存在则创建
        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 创建 FileOutputStream 对象
        FileOutputStream fileOutputStream = null;
        // 创建 ZipOutputStream
        ZipOutputStream zipOutputStream = null;
        // 创建 FileInputStream 对象
        FileInputStream fileInputStream = null;

        try {
            // 实例化 FileOutputStream 对象
            fileOutputStream = new FileOutputStream(zipFile);
            // 实例化 ZipOutputStream 对象
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            // 创建 ZipEntry 对象
            ZipEntry zipEntry = null;
            // 遍历源文件数组

            // 将源文件数组中的当前文件读入 FileInputStream 流中
            fileInputStream = new FileInputStream(file);
            // 实例化 ZipEntry 对象，源文件数组中的当前文件
            zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            // 该变量记录每次真正读的字节个数
            int len;
            // 定义每次读取的字节数组
            byte[] buffer = new byte[1024];
            while ((len = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }

            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFiles(List<File> srcFiles, File zipFile) {
        // 判断压缩后的文件存在不，不存在则创建
        if (!zipFile.exists()) {
            try {
                zipFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 创建 FileOutputStream 对象
        FileOutputStream fileOutputStream = null;
        // 创建 ZipOutputStream
        ZipOutputStream zipOutputStream = null;
        // 创建 FileInputStream 对象
        FileInputStream fileInputStream = null;

        try {
            // 实例化 FileOutputStream 对象
            fileOutputStream = new FileOutputStream(zipFile);
            // 实例化 ZipOutputStream 对象
            zipOutputStream = new ZipOutputStream(fileOutputStream);
            // 创建 ZipEntry 对象
            ZipEntry zipEntry = null;
            // 遍历源文件数组
            for (int i = 0; i < srcFiles.size(); i++) {
                // 将源文件数组中的当前文件读入 FileInputStream 流中
                fileInputStream = new FileInputStream(srcFiles.get(i));
                // 实例化 ZipEntry 对象，源文件数组中的当前文件
                zipEntry = new ZipEntry(srcFiles.get(i).getName());
                zipOutputStream.putNextEntry(zipEntry);
                // 该变量记录每次真正读的字节个数
                int len;
                // 定义每次读取的字节数组
                byte[] buffer = new byte[1024];
                while ((len = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
            }
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static byte[] file2Byte(File file) {
        if (!file.exists())
            return null;
        byte[] buffer = null;
        FileInputStream fis;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 合并byte[]数组 （不改变原数组）
     *
     * @param byte_1
     * @param byte_2
     * @return 合并后的数组
     */
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;

    }

    /**
     * 截取byte数组   不改变原数组
     *
     * @param b      原数组
     * @param off    偏差量（索引）
     * @param length 长度
     * @return 截取后的数组
     */
    public static byte[] subByte(byte[] b, int off, int length) {
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }
    public static void zlib(String input, String output) throws Exception {
//    DeflaterOutputStream dos = new DeflaterOutputStream(new FileOutputStream(output));
    DeflateParameters dp = new DeflateParameters();
    dp.setWithZlibHeader(true);
    DeflateCompressorOutputStream dcos = new DeflateCompressorOutputStream(new FileOutputStream(output),dp);
    FileInputStream fis = new FileInputStream(input);
    int length = (int) new File(input).length();
    byte data[] = new byte[length];
    // int length;
    while ((length = fis.read(data)) > 0) {
      dcos.write(data, 0, length);
    }
    fis.close();
    dcos.finish();
    dcos.close();
  }
    public static void gzip(String input, String output) throws Exception {
        byte[] buffer = new byte[1024];
        try {
            GzipParameters gp = new GzipParameters();
            gp.setCompressionLevel(1);
            GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(new FileOutputStream(output), gp);
            FileInputStream fis = new FileInputStream(input);
            int length = 0;
            while ((length = fis.read(buffer)) > 0) {
                gcos.write(buffer, 0, length);
            }
            fis.close();
            gcos.finish();
            gcos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }


    public static  boolean ungzip(String input, String output) throws Exception {

        byte[] buffer = new byte[1024];
        GzipCompressorInputStream gcis = null;
        FileOutputStream fos = null;
        try {

            gcis = new GzipCompressorInputStream(new FileInputStream(input));
            fos = new FileOutputStream(output);

            int length = 0;
            while ((length = gcis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if(gcis != null)
                gcis.close();
            if(fos != null)
                fos.close();
        }

        return true;
    }
}
