package com.truthso.ip360.ossupload;

import java.util.HashMap;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;

public class UpLoadManager {

	 private static UpLoadManager instance;
	 private OSS oss;
	 private HashMap<Integer, ResuambleUpload> upLoadTaskMap=new HashMap<Integer,ResuambleUpload>();
     private int resourceId;
     private HashMap<Integer,ProgressListener> progressListenerMap=new HashMap<Integer,ProgressListener>();
	// 运行sample前需要配置以下字段为有效的值
    private static final String endpoint =  "http://oss-cn-beijing.aliyuncs.com";
    private static final String accessKeyId ="LTAIIHLk9eURcRim";
    private static final String accessKeySecret = "6rXdqbwAShL0P8uR4L1zoLVX4eIUKj";


    private static final String testBucket =  "ip360-test";
	
	private UpLoadManager(){

		 OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
    
	        ClientConfiguration conf = new ClientConfiguration();
	        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
	        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
	        conf.setMaxConcurrentRequest(3); // 最大并发请求书，默认5个
	        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
	        OSSLog.enableLog();
	        oss = new OSSClient(MyApplication.getInstance().getApplicationContext(), endpoint, credentialProvider, conf);
	}
	
	public static UpLoadManager getInstance(){
		if(instance==null){
			instance=new UpLoadManager();
		}
		return instance;
	}
	
	public void resuambleUpload(FileInfo info){
		ResuambleUpload resuambleUpload=new ResuambleUpload(oss, testBucket, info.getObjectKey(), info.getFilePath());
		resuambleUpload.resumableUploadWithRecordPathSetting();
		upLoadTaskMap.put(resourceId, resuambleUpload);
		UpDownLoadDao.getDao().saveUpLoadInfo(info.getFilePath(), info.getFileName(), info.getFileSize(), info.getPosition(), resourceId,info.getObjectKey());
	}
	
	public void pause(int resourceId){
		//upLoadTaskMap.remove(resourceId).pause();
		upLoadTaskMap.get(resourceId).pause();
		
	}
	
	public void restart(int resourceId){
		/*FileInfo info = UpDownLoadDao.getDao().queryUpLoadInfoByResourceId(resourceId);
		ResuambleUpload resuambleUpload=new ResuambleUpload(oss, testBucket, info.getObjectKey(), info.getFilePath());
		ProgressListener progressListener = progressListenerMap.get(resourceId);
		Log.i("djj", CheckUtil.isEmpty(progressListener)+"");
		if(progressListener!=null){
			resuambleUpload.setProgressListener(progressListener);	
		}*/
		ResuambleUpload resuambleUpload = upLoadTaskMap.get(resourceId);
		resuambleUpload.resumableUploadWithRecordPathSetting();
	//	upLoadTaskMap.put(resourceId, resuambleUpload);
	}
	
	public void setOnUpLoadProgressListener(int resourceId,ProgressListener progressListener){
		ResuambleUpload resuambleUpload = upLoadTaskMap.get(resourceId);
		if(resuambleUpload!=null){
			resuambleUpload.setProgressListener(progressListener);
		}else{
			progressListenerMap.put(resourceId, progressListener);
		}		
	}

	public int getCurrentStatus(int resourceId) {
		int status=1;
		ResuambleUpload resuambleUpload = upLoadTaskMap.get(resourceId);
		if(resuambleUpload!=null){
			status=resuambleUpload.getStatus();
		}		
		return status;
	}
	
}
