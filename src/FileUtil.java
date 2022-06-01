package com.grgbanking.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件操作类
 *
 * @author czbao
 *
 */
public class FileUtil {

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * 获取不带扩展名的文件名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileNameNoEx(String filename){
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}



	/**
	 * 创建指定的目录。
	 * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
	 * <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * @param file 要创建的目录
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since  1.0
	 */
	public static boolean makeDirectory(File file) {
		File parent = file.getParentFile();
		if (parent != null) {
			if (parent.exists() && parent.isDirectory()){
				return true;
			}
			return parent.mkdirs();
		}
		return false;
	}

	/**
	 * 创建指定的目录。
	 * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
	 * <b>注意：可能会在返回false的时候创建部分父目录。</b>
	 * @param fileName 要创建的目录的目录名
	 * @return 完全创建成功时返回true，否则返回false。
	 * @since  1.0
	 */
	public static boolean makeDirectory(String fileName) {
		File file = new File(fileName);
		if (fileName.endsWith("/") || fileName.endsWith("\\")){
			if (file.exists() && file.isDirectory()){
				return true;
			}
			return file.mkdirs();
		}
		return makeDirectory(file);
	}

	/**
	 * 删除指定目录及其中的所有内容。
	 * @param dirName 要删除的目录的目录名
	 * @return 删除成功时返回true，否则返回false。
	 * @since  1.0
	 */
	public static boolean deleteDirectory(String dirName) throws Exception{
		return deleteDirectory(new File(dirName));
	}

	/**
	 * 删除指定目录及其中的所有内容。
	 * @param dir 要删除的目录
	 * @return 删除成功时返回true，否则返回false。
	 * @since  1.0
	 */
	public static boolean deleteDirectory(File dir) throws Exception{
		File[] entries = dir.listFiles();
		int sz = 0 ;
		if(entries!=null){
			 sz = entries.length;
		}
		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				if (!deleteDirectory(entries[i])) {
					return false;
				}
			}
			else {
				if (!entries[i].delete()) {
					return false;
				}
			}
		}

		if (!dir.delete()) {
			return false;
		}
		return true;
	}

	/**
	 * 检查给定目录的存在性
	 * 保证指定的路径可用，如果指定的路径不存在，那么建立该路径，可以为多级路径
	 * @param path
	 * @return 真假值
	 * @since  1.0
	 */
	public static  boolean pathValidate(String path)
	{
		String[] arraypath = path.split("/");
		StringBuffer tmppath = new StringBuffer("");
		for (int i = 0; i < arraypath.length; i++)
		{
			tmppath.append("/" + arraypath[i]) ;
			File d = new File(tmppath.substring(1));
			if (!d.exists()) { //检查Sub目录是否存在
				if (!d.mkdir()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 拷贝文件夹，包含文件夹里面的所有文件夹和文件
	 * @param dir 源文件路径
	 * @param todir 目标路径
	 * @return
	 * @throws Exception
	 */
	public static  void copyDirectory(String dir, String todir) throws Exception {
		//获取源目标目录
		File dirFile = new File(dir);
		if(!dirFile.exists()) {
			throw new Exception("Source File Directory is not exist!filePath:" + dir);
		}
		//目标文件夹
		File todirFile = new File(todir);
		//获取一级目录和文件
		File[] dirFileLsit = dirFile.listFiles();

		if (!todirFile.exists()) {//目标目录不存在
			boolean isTodir = todirFile.mkdirs();
		}
		if(dirFileLsit != null){
			for (File f : dirFileLsit) {
				if (f.isFile()) {//如果是文件拷贝文件
					copyFile(f.getPath(),todir+"/"+f.getName());
				}else if(f.isDirectory()){//文件是文件夹递归复制文件
					copyDirectory(f.getPath(),todir+"/"+f.getName());
				}
			}
		}
	}

	/**
	 * 拷贝文件
	 * @param in
	 * @param out
	 * @return
	 * @throws Exception
	 */
	public static void copyFile(File in, File out) throws Exception {
		if(!in.exists()) {
			throw new Exception("Source File:" + in.getName() + " is not exist!");
		}
		if(!out.getParentFile().exists()){
			boolean isout  = out.getParentFile().mkdirs();
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try{
			fis = new FileInputStream(in);
			fos = new FileOutputStream(out);
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		}catch (Exception e){
			throw e;
		}finally {
			if(fis !=null) {
				fis.close();
			}
			if(fos !=null) {
				fos.close();
			}
		}
	}


	/**
	 * 拷贝文件
	 * @param infile
	 * @param outfile
	 * @return
	 * @throws Exception
	 */
	public static void copyFile(String infile, String outfile) throws Exception {
		File in = new File(infile);
		File out = new File(outfile);
		copyFile(in, out);
	}


	/**
	 * 拷贝文件
	 * @param dir
	 * @param todir
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static void copyFile(String dir,String todir,String fileName) throws Exception {
		File in = new File(dir+"/"+fileName);
		File out = new File(todir+"/"+fileName);
		copyFile(in, out);
	}

	/**
	 * 移动文件夹，先复制后删除
	 * @param dir
	 * @param todir
	 */
	public static void moveDirectory(String dir,String todir) throws Exception {
		//先拷贝要移动到文件夹到目标目录
		copyDirectory(dir,todir);
		//然后删除源文件夹目录
		deleteDirectory(dir);
	}

	/**
	 * 移动文件夹，先复制后删除
	 * @param dir
	 * @param todir
	 */
	public static void moveFile(String dir,String todir,String fileName) throws Exception {
		File f = new File(dir+"/"+fileName);
		if(f.exists()) {
			//先拷贝要移动到文件夹到目标目录
			copyFile(dir + "/" + fileName, todir + "/" + fileName);
			//然后删除源文件夹目录
			boolean isSuccess  = f.delete();
		} else {
			throw new Exception("File is not find!");
		}
	}

	public static void moveFile(String srcFilePath,String dstFilePath) throws Exception {
		File f = new File(srcFilePath);
		if(f.exists()) {
			//先拷贝要移动到文件夹到目标目录
			copyFile(srcFilePath, dstFilePath);
			//然后删除源文件夹目录
			boolean isSuccess  = f.delete();
		} else {
			throw new Exception("File is not find!");
		}
	}

	/**
	 * 修改文件名
	 * @param oldFilePath 原文件路径
	 * @param newFileName 新文件名称
	 * @param overriding 判断标志(如果存在相同名的文件是否覆盖)
	 * @return
	 */
	public static boolean renameFile(String oldFilePath,String newFileName,boolean overriding) {
		File oldfile = new File(oldFilePath);
		if (!oldfile.exists()) {
			return false;
		}
		String newFilepath = oldfile.getParent() + File.separator + newFileName;
		File newFile = new File(newFilepath);
		if (!newFile.exists()) {
			return oldfile.renameTo(newFile);
		} else {
			if (overriding) {
				newFile.delete();
				return oldfile.renameTo(newFile);
			} else {
				return false;
			}
		}
	}
}
