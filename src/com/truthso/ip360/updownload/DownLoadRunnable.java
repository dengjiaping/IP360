package com.truthso.ip360.updownload;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.util.Log;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.JsonUtil;

public class DownLoadRunnable implements Runnable {

	
	private String downloadUrl, fileName;
	private String locaFilePath=MyConstants.DOWNLOAD_PATH;
	private boolean isCancle;
	private int status,fileSize;
	private int WAIT=0,PAUSE=1,RUNNING=2,ERROR=3;	
	private DownLoadListener downLoadListener;
	private int upLoadProgress, position, resourceId;
	private UpDownLoadDao dao=UpDownLoadDao.getDao();

	public DownLoadRunnable(String downloadUrl, String fileName, int position, int resourceId) {
		super();
		this.downloadUrl = downloadUrl;
		this.fileName = fileName;
		this.position = position;
		this.resourceId = resourceId;
		this.fileSize=fileSize;
		Log.i("djj", "uploadUrl" + downloadUrl);
		Log.i("djj", "fileName" + fileName);
		Log.i("djj", "position" + position);
		Log.i("djj", "resourceId" + resourceId);
	}

	public void pause() {
		isCancle = true;
		status=PAUSE;
	}


	public String getUrl() {
		return downloadUrl;
	}
	public int getResourceId(){
		return resourceId;
	}
    public int  getStatue(){
    	return status;
    }
	@Override
	public void run() {
		status=RUNNING;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		String CHARSET = "UTF-8";
		int TIME_OUT = 60000;
		RandomAccessFile raf=null ;
		InputStream inputStream=null;
		int progress=0;
		try {
			File downloadFileDir = new File(locaFilePath);
			if (!downloadFileDir.exists()) {
				downloadFileDir.mkdirs();
			}
			File file=new File(downloadFileDir,fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			
			//long length = downloadFile.length();
			
			URL url = new URL(downloadUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(6000);
			connection.setRequestProperty("Range", "bytes="+position);
			int responseCode = connection.getResponseCode();
			if(responseCode==206){			
				raf = new RandomAccessFile(file, "rwd");
			//	raf.setLength(fileSize);
				raf.seek(position);	
			    inputStream = connection.getInputStream();
				byte[] b=new byte[1024];
				int length=0;
				
				while (isCancle&&(length=(inputStream.read(b)))!=-1) {
					raf.write(b,0,length);	
					progress+=length;
					if(downLoadListener!=null){
						downLoadListener.onProgress(progress);						
					}					
				}
               if(progress==fileSize){
            	   if(downLoadListener!=null){
						downLoadListener.oncomplete();	
						dao.deleteByResourceId(resourceId);
					}	
				}			
			}		
		} catch (IOException e) {
			e.printStackTrace();
			status=ERROR;
		}finally{
			try {
				if(!CheckUtil.isEmpty(raf)&&!CheckUtil.isEmpty(inputStream)){
					raf.close();
					inputStream.close();
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public void setOnProgressListener(DownLoadListener listen) {
		this.downLoadListener = listen;
	}

	public int getFileSize() {
		// TODO Auto-generated method stub
		return fileSize;
	}

	public CharSequence getFileName() {
		// TODO Auto-generated method stub
		return fileName;
	}

}
