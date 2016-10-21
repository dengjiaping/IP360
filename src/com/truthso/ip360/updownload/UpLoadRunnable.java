package com.truthso.ip360.updownload;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.util.Log;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.dao.UpDownLoadDao;

public class UpLoadRunnable implements Runnable {

	private String uploadUrl, filePath;
	private boolean isCancle;
	private UpLoadListener upLoadListener;
	private int upLoadProgress,position,resourceId;
	private UpDownLoadDao dao;

	public UpLoadRunnable(String uploadUrl, String filePath,int position,int resourceId) {
		super();
		this.uploadUrl = uploadUrl;
		this.filePath = filePath;
		this.position=position;
		this.resourceId=resourceId;
	 Log.i("djj","uploadUrl"+uploadUrl);
	 Log.i("djj", "filePath"+filePath);
	 Log.i("djj", "position"+position);
	 Log.i("djj", "resourceId"+resourceId);
	}

	public void cancle() {
		isCancle = true;
	}

	public int getUpLoadProgress() {
		return upLoadProgress;
	}

	@Override
	public void run() {
		    String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
	        String PREFIX = "--", LINE_END = "\r\n";
	        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	        String CHARSET="utf-8";
	        int TIME_OUT=60000;
		try {
			File uploadFile = new File(filePath);
			if (!uploadFile.exists()) {
				uploadFile.createNewFile();
			}
            long length=uploadFile.length();
			URL url=new URL(uploadUrl);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true); // 允许输入流
            connection.setDoOutput(true); // 允许输出流
            connection.setUseCaches(false); // 不允许使用缓存
            connection.setRequestMethod("POST"); // 请求方式
            connection.setRequestProperty("Charset", CHARSET);
       	 // 设置断点开始位置
            connection.setRequestProperty("Range", "bytes=" + position);
            // 设置编码
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);
	
            Map<String, String> params=new HashMap<String, String>();
            params.put("position", position+"");
            params.put("resourceId", resourceId+"");
            
            StringBuilder sb = new StringBuilder();    
            for (Map.Entry<String, String> entry : params.entrySet()) {    
                sb.append(PREFIX);    
                sb.append(BOUNDARY);    
                sb.append(LINE_END);    
                sb.append("Content-Disposition: form-data; name=\""    
                        + entry.getKey() + "\"" + LINE_END);    
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);    
                sb.append("Content-Transfer-Encoding: 8bit" + LINE_END);    
                sb.append(LINE_END);    
                sb.append(entry.getValue());    
                sb.append(LINE_END);    
            }    
            sb.append(PREFIX);//开始拼接文件参数
            sb.append(BOUNDARY); sb.append(LINE_END);
            
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + uploadFile.getName() + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset="
                    + CHARSET + LINE_END);
            sb.append(LINE_END);
            
            OutputStream outputSteam=connection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            dos.write(sb.toString().getBytes());
            
			RandomAccessFile raf = new RandomAccessFile(uploadFile, "r");
			raf.seek(Integer.valueOf(position));
			
			byte[] buffer = new byte[1024];
			int len = -1;
			int progress = Integer.valueOf(position);
			Log.i("djj", dos.toString());
			while (!isCancle && (len = raf.read(buffer)) != -1) {
				dos.write(buffer, 0, len);
				progress += len;
		
				if(upLoadListener!=null){
					upLoadListener.onProgress((int)(progress/length*100));
				}
			}
			raf.close();
			dos.close();

			
			if (length == uploadFile.length()){
				dao.delete(uploadFile);
				if(upLoadListener!=null){
					upLoadListener.onUpLoadComplete();
				}
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setOnProgressListener(UpLoadListener listen) {
		this.upLoadListener = listen;
	}
}
