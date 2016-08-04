package com.truthso.ip360.utils;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

public class GetFileSizeUtil {

	public static long getFileSize(String filePath) {
		File file = new File(filePath);
		long size = 0;
		try {
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				size = fis.available();
			} else {
				file.createNewFile();
				System.out.println("文件不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static String FormatFileSize(String filePath) {// 转换文件大小
		long fileSize = getFileSize(filePath);
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}
}
