package com.truthso.ip360.ossupload;

import java.io.File;
import java.util.Map;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.updownload.FileInfo;


public class DownLoadHelper {

	private String endpoint = "http://oss-cn-beijing.aliyuncs.com";
	private OSS oss;
	private File downloadFile;
    private static DownLoadHelper helper=new DownLoadHelper();
    private Map<String, DownloadTask> taskMap=new ArrayMap<String, DownloadTask>();
    private Map<String ,ProgressListener> listenerMap=new ArrayMap<String, ProgressListener>();
	private  DownLoadHelper() {
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIIHLk9eURcRim", "6rXdqbwAShL0P8uR4L1zoLVX4eIUKj");	
		oss = new OSSClient(MyApplication.getApplication(), endpoint, credentialProvider);	
	}

	public static DownLoadHelper  getInstance(){
		return helper;
	}
		
	// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节

	public void downloadFile(FileInfo fileinfo) {
		DownloadTask task=new DownloadTask(oss,fileinfo);
		task.start();
		taskMap.put(fileinfo.getObjectKey(), task);
		UpDownLoadDao.getDao().saveDownLoadInfo(fileinfo.getFilePath(),fileinfo.getFileName(),fileinfo.getFileSize(),fileinfo.getPosition(),fileinfo.getResourceId(),fileinfo.getObjectKey(),fileinfo.getLlsize());
	}
	
	public void pauseDownload(String objectKey){
		
		DownloadTask downloadTask = taskMap.get(objectKey);
		if(downloadTask!=null){
			downloadTask.pause();
		}
	}
	
	
	public  void setOnprogressListener(String objectkey,ProgressListener listener){
		DownloadTask downloadTask = taskMap.get(objectkey);
		if(downloadTask!=null){
			downloadTask.setProgressListener(listener);
		}
		listenerMap.put(objectkey, listener);
	}
	

}
