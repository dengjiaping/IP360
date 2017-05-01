package com.truthso.ip360.utils;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.os.Environment;


public class FileUtil {
	 private String SDPATH;
	/**
	 * 将字节数组内容写入到指定路径的文件中
	 * 
	 * @param filePath
	 * @param contents
	 * @throws Exception
	 */
	public static void writeBytes2File(String filePath, byte[] contents)
			throws Exception {
		File f = new File(filePath);
		if (f.exists() == false) {
			f.createNewFile(); // 创建新的文件
		}
		FileOutputStream stream = new FileOutputStream(f);
		// 缓冲区大小256字节
		int j = 0;
		while (j < contents.length) {
			int len = 256;
			if (j + 256 > contents.length) {
				len = contents.length - j;
			}
			stream.write(contents, j, len);
			j = j + 256;
		}
		stream.close();
	}
	 public String getSDPATH() {
		  return SDPATH;
		 }
	public FileUtil() {
		  //得到当前外部存储设备的目录
		  SDPATH = Environment.getExternalStorageDirectory() + "/";
	}
	 /**
	  * 将一个InputStream里面的数据写入到SD卡中
	  */ 
	 public File writeToSDFromInput(String path,String fileName,InputStream input){
	  
	  File file =null;
	  OutputStream output =null;
	  try{
	   creatSDDir(path);
	   file = creatSDFile(path + fileName);
	   output = new FileOutputStream(file);
	   byte buffer [] = new byte[1024];
	   int len  = 0;
	   //如果下载成功就开往SD卡里些数据
	   while((len =input.read(buffer))  != -1){
	    output.write(buffer,0,len);
	   }
	   output.flush();
	     }catch(Exception e){
	   e.printStackTrace();
	     }finally{
	      
	   try{
	    input.close();
	    output.close();
	   }catch(Exception e){
	    
	    e.printStackTrace();
	   }
	  }
	  return file;
	 }
	 /**
	  * 在SD卡上创建目录
	  * 
	  * @param dirName
	  */
	 public File creatSDDir(String dirName) {
	  File dir = new File(SDPATH + dirName);
	  dir.mkdir();
	  return dir;
	 }
	 /**
	  * 在SD卡上创建文件
	  * 
	  * @throws IOException
	  */
	 public File creatSDFile(String fileName) throws IOException {
	  
	  File file = new File(SDPATH + fileName);
	  file.createNewFile();
	  return file;
	 }
	 /**
	  * 判断SD卡上的文件夹是否存在
	  */
	 public boolean isFileExist(String fileName){
	  
	  File file = new File(SDPATH + fileName);
	  file.delete();
	  return file.exists();
	  
	 }
	/**
	 * 将字符串写入到指定路径的文件中
	 * 
	 * @param filePath
	 * @param content
	 * @throws Exception
	 */
	public static void writeString2File(String filePath, String content)
			throws Exception {
		File f = new File(filePath);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		if (f.exists() == false) {
			f.createNewFile(); // 创建新的文件
		}
		BufferedWriter buffWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f), "UTF-8"));
		buffWriter.write(StringUtil.trimNull(content));
		buffWriter.flush();
		buffWriter.close();
	}

	/**
	 * 
	 * @param filePath
	 * @param code
	 * @return
	 */
	public static String parseInputStream(String filePath, String code) {
		try {
			File f = new File(filePath);
			if (f.exists() == false) {
				return "";
			}
			Reader ins = new InputStreamReader(new FileInputStream(f), code);
			BufferedReader br = new BufferedReader(ins);

			StringBuffer strBuf = new StringBuffer();

			String line = null;

			while ((line = br.readLine()) != null) {
				strBuf.append(line);
			}

			br.close();
			ins.close();

			return strBuf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String parseInputStream2(String filePath, String code) {
		String string = "";
		File file = new File(filePath);
		InputStreamReader in = null;
		StringBuffer pzFile = new StringBuffer();
		try {
			if (file.isFile() && file.exists()) {// 判断是否有文件
				// 避免汉字编码问题
				in = new InputStreamReader(new FileInputStream(file), code);
				BufferedReader buffer = new BufferedReader(in);
				String lineText = null;
				while ((lineText = buffer.readLine()) != null) {
					pzFile.append(lineText);
				}
				string = pzFile.toString();
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void deleteFile(String filePath) throws Exception {
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
	}

	/**
	 * 计算文件大小
	 *
	 * @param filePath
	 * @throws Exception
	 */
	public static long getDirSize(final File file) {
		if (file.isFile())
			return file.length();
		final File[] children = file.listFiles();
		long total = 0;
		if (children != null)
			for (final File child : children)
				total += getDirSize(child);
		return total;
	}
	/**
	 * 删除文件夹下的文件
	 *
	 * @param filePath
	 * @throws Exception
	 */
	public static void deleteAllFiles(File root) {
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isDirectory()) { // 判断是否为文件夹
					deleteAllFiles(f);
					try {
						f.delete();
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // 判断是否存在
						deleteAllFiles(f);
						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}
	}

	/**
	 * 将输入流转换成字符串
	 * 
	 * @param ins
	 * @return
	 * @throws Exception
	 */
	public static String parseInputStream(InputStream ins) throws Exception {
		StringBuffer strBuf = new StringBuffer();
		byte[] b = new byte[1024];
		int realLen = ins.read(b);
		while (realLen > -1) {
			strBuf.append(new String(b, 0, realLen));
			realLen = ins.read(b);
		}
		return strBuf.toString();
	}

	/**
	 * 将输入流转换成字符串
	 * 
	 * @param ins
	 * @return
	 * @throws Exception
	 */
	public static String parseInputStream(InputStream ins, String code)
			throws Exception {
		StringBuffer strBuf = new StringBuffer();
		byte[] b = new byte[1024];
		int realLen = ins.read(b);
		while (realLen > -1) {
			strBuf.append(new String(b, 0, realLen, code));
			realLen = ins.read(b);
		}
		return strBuf.toString();
	}

	/**
	 * 2个文件的复制
	 * 
	 * @param outS
	 * @param ins
	 * @throws Exception
	 */
	public static void copy(OutputStream outS, InputStream ins)
			throws Exception {
		byte[] b = new byte[1024];
		int realLen = ins.read(b);
		while (realLen > -1) {
			outS.write(b, 0, realLen);
			realLen = ins.read(b);
		}
	}

	/**
	 * 将输入流转换成字节
	 * 
	 * @param ins
	 * @return
	 * @throws Exception
	 */
	public static byte[] getPostData(InputStream ins) throws Exception {
		java.io.DataInputStream servletIn = new java.io.DataInputStream(ins);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] bufferByte = new byte[256];
		int l = -1;
		while ((l = servletIn.read(bufferByte)) > -1) {
			bout.write(bufferByte, 0, l);
			bout.flush();
		}
		byte[] inByte = bout.toByteArray();
		servletIn.close();
		bout.close();
		if (inByte.length == 0) {
			return null;
		}
		return inByte;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param src
	 * @param dst
	 * @return 是否成功
	 */
	public static boolean copy(File src, File dst) {
		boolean success = false;
		try {
			int BUFFER_SIZE = 4096;
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src),
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
				success = true;
			}
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * 将输入流转换成字节
	 * 
	 * @param ins
	 * @return
	 * @throws Exception
	 */
	public static byte[] getByteData(InputStream ins) throws Exception {
		java.io.DataInputStream servletIn = new java.io.DataInputStream(ins);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] bufferByte = new byte[256];
		int l = -1;
		while ((l = servletIn.read(bufferByte)) > -1) {
			bout.write(bufferByte, 0, l);
			bout.flush();
		}
		byte[] inByte = bout.toByteArray();
		servletIn.close();
		bout.close();
		if (inByte.length == 0) {
			return null;
		}
		return inByte;
	}

	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	public static byte[] readFile(String filePath, int sheet, int total,
			int fileSize) throws Exception {
		RandomAccessFile randomFile = null;
		byte[] retBytes = null;
		if (sheet == total) {
			int size = fileSize % PER_SIZE;
			if (size == 0) {
				retBytes = new byte[PER_SIZE];
			} else {
				retBytes = new byte[size];
			}
			// retBytes = new byte[fileSize%(PER_SIZE)];
		} else {
			retBytes = new byte[PER_SIZE];
		}
		try {
			randomFile = new RandomAccessFile(filePath, "r");
			randomFile.seek((sheet - 1) * PER_SIZE);
			randomFile.read(retBytes);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (randomFile != null) {
				randomFile.close();
			}
		}
		return retBytes;
	}

	/**
	 * 以分段形式保存多媒体信息
	 * 
	 * @param filePath
	 *            多媒体文件
	 * @param data
	 *            二进制流
	 * @param exists
	 *            该文件是否已存在
	 * @param sheet
	 *            当前第几段
	 * @return
	 */
	public static void saveMedia(String filePath, byte data[], int sheet)
			throws Exception {
		// exists true 文件存在，false 不存在
		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(filePath, "rw");
			randomFile.seek((sheet - 1) * PER_SIZE);
			randomFile.write(data);
		} catch (Exception e) {
			throw e;
		} finally {
			if (randomFile != null) {
				randomFile.close();
			}
		}
	}

	/**
	 * 删除指定目录下的所有文件
	 * 
	 * @param path
	 *            String 指定目录
	 * @throws Exception
	 */
	public static void RemoveDirectoryFiles(String path) throws Exception {
		File xmlfile = new File(path);

		String[] listFile = xmlfile.list();
		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {
				File tempfile = new File(path
						+ System.getProperty("file.separator") + listFile[i]);
				// 如果是目录
				if (tempfile.isDirectory()) {
					RemoveDirectoryFiles(tempfile.getPath());

				} else { // 如果不是
					tempfile.delete();

				}
			}
		}

	}

	/**
	 * 删除指定目录下的所有目录
	 * 
	 * @param path
	 *            String 指定目录
	 * @throws Exception
	 */
	public static void RemoveDirectory(String path) throws Exception {
		File xmlfile = new File(path);
		String[] listFile = xmlfile.list();
		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {
				File tempfile = new File(path
						+ System.getProperty("file.separator") + listFile[i]);

				if (tempfile.isDirectory()) {

					RemoveDirectory(tempfile.getPath());
					tempfile.delete();

				}
			}
		}

	}

	/**
	 * 删除指定目录下所有文件和目录
	 * 
	 * @param path
	 *            String 指定目录
	 * @throws Exception
	 */
	public static void RemoveAll(String path) throws Exception {
		RemoveDirectoryFiles(path);
		RemoveDirectory(path);
		File f = new File(path);
		if (f.isDirectory()) {
			f.delete();
		}
	}

	/**
	 * 
	 * @param content
	 */
	public static final void append2File(String fileNm, String content) {
		try {
			RandomAccessFile ranFile = new RandomAccessFile(fileNm, "rw");
			ranFile.seek(ranFile.length());
			ranFile.write(content.getBytes("GBK"));
			ranFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int PER_SIZE = 1024 * 50;

	/**
	 * 判断文件是否存在
	 * 
	 * @param file
	 * @return true
	 */
	public static boolean IsFileEmpty(String fileStr) {

		File file = new File(fileStr);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static void writeString3File(String filePath, String content)
			throws Exception {
		File f = new File(filePath);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		if (f.exists() == false) {
			f.createNewFile(); // 创建新的文件
		}
		BufferedWriter buffWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f), "UTF-8"));
		buffWriter.write(StringUtil.trimNull(content));
		buffWriter.flush();
		buffWriter.close();
	}

	public static boolean deletefile(String delpath) throws Exception {

		try {

			File file = new File(delpath);
			// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + "/" + filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
					} else if (delfile.isDirectory()) {
						deletefile(delpath + "/" + filelist[i]);
					}
				}
				file.delete();
			}

		} catch (FileNotFoundException e) {
		}
		return true;
	}

	/**
	 * 解压缩功能. 将zipFile文件解压到folderPath目录下.
	 * 
	 * @throws Exception
	 */
	public static int upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		// public static void upZipFile() throws Exception{
		ZipFile zfile = new ZipFile(zipFile);
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				// Log.d("upZipFile", "ze.getName() = " + ze.getName());
				String dirstr = folderPath + ze.getName();
				// dirstr.trim();
				dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
				// Log.d("upZipFile", "str = " + dirstr);
				File f = new File(dirstr);
				f.mkdir();
				continue;
			}
			// Log.d("upZipFile", "ze.getName() = " + ze.getName());
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(folderPath, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zfile.close();
		// Log.d("upZipFile", "finishssssssssssssssssssss");
		return 0;
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	public static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		File ret = new File(baseDir);
		String substr = null;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				substr = dirs[i];
				try {
					// substr.trim();
					substr = new String(substr.getBytes("8859_1"), "GB2312");

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = new File(ret, substr);

			}
			// Log.d("upZipFile", "1ret = " + ret);
			if (!ret.exists())
				ret.mkdirs();
			substr = dirs[dirs.length - 1];
			try {
				// substr.trim();
				substr = new String(substr.getBytes("8859_1"), "GB2312");
				// Log.d("upZipFile", "substr = " + substr);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ret = new File(ret, substr);
			// Log.d("upZipFile", "2ret = " + ret);
			return ret;
		}
		return ret;
	}

	public static byte[] File2byte(String filePath)  
    {  
        byte[] buffer = null;  
        try  
        {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            byte[] b = new byte[1024];  
            int n;  
            while ((n = fis.read(b)) != -1)  
            {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        }  
        catch (FileNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
        return buffer;  
    }

}
