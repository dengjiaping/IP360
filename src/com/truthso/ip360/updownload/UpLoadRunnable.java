package com.truthso.ip360.updownload;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.database.Observable;
import android.util.Log;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.dao.UpDownLoadDao;

public class UpLoadRunnable implements Runnable {

	private String uploadUrl, filePath;
	private boolean isCancle,isUpLoading;
	private UpLoadListener upLoadListener;
	private int upLoadProgress,position,resourceId;

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

	public String getUrl(){
		return filePath;
	}
	
	@Override
	public void run() {
		    String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
	        String PREFIX = "--", LINE_END = "\r\n";
	        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	        String CHARSET="UTF-8";
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
            
            OutputStream outputSteam=connection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            
            
            Map<String, String> params=new HashMap<String, String>();
            params.put("position", position+"");
            params.put("resourceId", resourceId+"");
            params.put("token", MyApplication.getInstance().getTokenId());
            
            StringBuilder sb = new StringBuilder();    
            for (Map.Entry<String, String> entry : params.entrySet()) {    
                sb.append(PREFIX);    
                sb.append(BOUNDARY);    
                sb.append(LINE_END);    
                sb.append("Content-Disposition: form-data; name=\""    
                        + entry.getKey() + "\"" + LINE_END);         
                sb.append(LINE_END);    
                sb.append(entry.getValue());   
                sb.append(LINE_END); 
                sb.append(LINE_END);    
            }    
            dos.write(sb.toString().getBytes());
                        
            sb.append(PREFIX);//开始拼接文件参数
            sb.append(BOUNDARY); 
            sb.append(LINE_END);
            
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + uploadFile.getName() + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset="
                    + CHARSET + LINE_END);
            sb.append(LINE_END);
       
			RandomAccessFile raf = new RandomAccessFile(uploadFile, "r");
			raf.seek(Integer.valueOf(position));
			
			byte[] buffer = new byte[1024];
			int len = -1;
			int progress = Integer.valueOf(position);
			Log.i("djj",sb.toString());
			while (!isCancle && (len = raf.read(buffer)) != -1) {
				isUpLoading=true;
				dos.write(buffer, 0, len);
				progress += len;		
				if(upLoadListener!=null){
					upLoadListener.onProgress((int)(progress/length*100));
				}
				Log.i("djj", "progress"+progress+"length"+length);
			}
			raf.close();
			
			
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  
			dos.write(endData);  
            dos.flush();  
            dos.close();
          
            
         // 读取返回数据    
            StringBuffer strBuf = new StringBuffer();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                strBuf.append(line).append("\n");  
            }  
           String res = strBuf.toString();  
            reader.close();  
            reader = null;  
            
            Log.i("djj", "res"+res);
            if (progress == uploadFile.length()){
				if(upLoadListener!=null){
					upLoadListener.onUpLoadComplete();
				}		
				UpLoadManager.getInstance().removeRunnable(UpLoadRunnable.this);				
			}
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setOnProgressListener(UpLoadListener listen) {
		this.upLoadListener = listen;
	}
	
	public boolean isUpLoading(){
		return isUpLoading;
	}
}
