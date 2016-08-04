package com.truthso.ip360.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;




public class DownLoaderTask {
    public boolean downloader(String _urlStr,String path){
    	try {
			File file = new File(path);
			// 如果目标文件已经存在，则删除。产生覆盖旧文件的效果

			if (file.exists()) {
				file.delete();
			} else {
				file.createNewFile();
			}

			// 构造URL
			URL url = new URL(_urlStr);
			// 打开连接
			URLConnection con = url.openConnection();
			con.connect();
			// 获得文件的长度
			int contentLength = con.getContentLength();
			System.out.println("���� :" + contentLength);
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 已下载文件进度
			int total;
			// 输出的文件流

			OutputStream os = new FileOutputStream(path);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
			return true;

		} catch (Exception e) {
			Log.i("djj", "Exception");
			e.printStackTrace();
			return false;
		}
	}

    }

