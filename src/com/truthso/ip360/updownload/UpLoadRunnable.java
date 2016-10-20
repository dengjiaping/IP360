package com.truthso.ip360.updownload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.dao.UpDownLoadDao;

import android.os.Message;

public class UpLoadRunnable implements Runnable {

	private String uploadUrl, filePath;
	private boolean isCancle;
	private UpLoadListener upLoadListener;
	private int upLoadProgress;
	private UpDownLoadDao dao;

	public UpLoadRunnable(String uploadUrl, String filePath) {
		super();
		this.uploadUrl = uploadUrl;
		this.filePath = filePath;
		dao = new UpDownLoadDao(MyApplication.getInstance());
	}

	public void cancle() {
		isCancle = true;
	}

	public int getUpLoadProgress() {
		return upLoadProgress;
	}

	@Override
	public void run() {

		try {
			File uploadFile = new File(filePath);
			if (!uploadFile.exists()) {
				uploadFile.createNewFile();
			}
            long length=uploadFile.length();
			String souceid = dao.getBindId(uploadFile);
		/*	String head = "Content-Length=" + uploadFile.length() + ";filename=" + uploadFile.getName() + ";sourceid=" + (souceid == null ? "" : souceid) + "\r\n";
			Socket socket = new Socket("192.168.1.78", 7878);
			OutputStream outStream = socket.getOutputStream();
			outStream.write(head.getBytes());*/

			URL url=new URL(uploadUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            // 设置允许输出
            connection.setDoOutput(true);
            connection.setDoInput(true);
           
			
			
			
			
			PushbackInputStream inStream = new PushbackInputStream(connection.getInputStream());
			String response = StreamTool.readLine(inStream);
			String[] items = response.split(";");
			String responseid = items[0].substring(items[0].indexOf("=") + 1);
			String position = items[1].substring(items[1].indexOf("=") + 1);
			
			 // 设置断点开始位置
            connection.setRequestProperty("Range", "bytes=" + position);
			
			if (souceid == null) {// 代表原来没有上传过此文件，往数据库添加一条绑定记录
				dao.save(responseid, uploadFile);
			}
			RandomAccessFile raf = new RandomAccessFile(uploadFile, "r");
			raf.seek(Integer.valueOf(position));
			byte[] buffer = new byte[1024];
			int len = -1;
			int progress = Integer.valueOf(position);
			while (!isCancle && (len = raf.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
				progress += len;
		
				if(upLoadListener!=null){
					upLoadListener.onProgress((int)(progress/length*100));
				}
			}
			raf.close();
			outStream.close();
			inStream.close();
			socket.close();
			if (length == uploadFile.length()){
				dao.delete(uploadFile);
				if(upLoadListener!=null){
					upLoadListener.onUpLoadComplete();
				}
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setOnProgressListener(UpLoadListener listen) {
		this.upLoadListener = listen;
	}
}
