package com.truthso.ip360.ossupload;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.NetStatusUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

public class UpLoadManager {

	 private static UpLoadManager instance;
	 private OSS oss;
	 private HashMap<Integer, ResuambleUpload> upLoadTaskMap=new HashMap<Integer,ResuambleUpload>();
     private HashMap<Integer,ProgressListener> progressListenerMap=new HashMap<Integer,ProgressListener>();
	// 运行sample前需要配置以下字段为有效的值
    private static final String endpoint =  "http://oss-cn-beijing.aliyuncs.com";
    private static final String accessKeyId ="LTAIIHLk9eURcRim";
    private static final String accessKeySecret ="6rXdqbwAShL0P8uR4L1zoLVX4eIUKj";
    private static final String testBucket =  "ip360-test";
	
	private UpLoadManager(){

		 OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
    
	        ClientConfiguration conf = new ClientConfiguration();
	        conf.setConnectionTimeout(60 * 1000); // 连接超时，默认15秒
	        conf.setSocketTimeout(60 * 1000); // socket超时，默认15秒
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
	
	public boolean resuambleUpload(FileInfo info){
		ResuambleUpload resuambleUpload=new ResuambleUpload(oss, testBucket, info);

		boolean isWifi= (Boolean) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY,MyConstants.ISWIFI,SharePreferenceUtil.VALUE_IS_BOOLEAN);
         if(isWifi&&!NetStatusUtil.isWifiValid(MyApplication.getApplication())){
			 UpDownLoadDao.getDao().saveUpLoadInfo(info.getFilePath(), info.getFileName(), info.getLlsize(), info.getPosition(), info.getResourceId(),info.getObjectKey(),1);
//			 Toaster.showToast(MyApplication.getApplication(),"您已设置仅在wifi下保全，请连接wifi或更改设置");

            return false;
		 }
		resuambleUpload.putObject();
		upLoadTaskMap.put(info.getResourceId(), resuambleUpload);
		UpDownLoadDao.getDao().saveUpLoadInfo(info.getFilePath(), info.getFileName(), info.getFileSize(), info.getPosition(), info.getResourceId(),info.getObjectKey(),2);
		return true;
	}

	public void resuambleUploadAgain(FileInfo info){

		boolean isWifi= (Boolean) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY,MyConstants.ISWIFI,SharePreferenceUtil.VALUE_IS_BOOLEAN);
		if(isWifi&&!NetStatusUtil.isWifiValid(MyApplication.getApplication())){
			Toaster.showToast(MyApplication.getApplication(),"您已设置仅在wifi下保全，请连接wifi或更改设置");
		}else {
			ResuambleUpload resuambleUpload=new ResuambleUpload(oss, testBucket, info);
			resuambleUpload.putObject();
			upLoadTaskMap.put(info.getResourceId(), resuambleUpload);
			//UpDownLoadDao.getDao().saveUpLoadInfo(info.getFilePath(), info.getFileName(), info.getFileSize(), info.getPosition(), info.getResourceId(),info.getObjectKey(),2);
			UpDownLoadDao.getDao().updateStatusByResourceId("2",info.getResourceId()+"",null);
		}
	}
	
	public void resuambleUploadUnCaseNet(FileInfo info){
		ResuambleUpload resuambleUpload=new ResuambleUpload(oss, testBucket, info);
		resuambleUpload.putObject();
		upLoadTaskMap.put(info.getResourceId(), resuambleUpload);
	}
	
	
	public void pause(int resourceId){
		//upLoadTaskMap.remove(resourceId).pause();
		upLoadTaskMap.get(resourceId).pause();
		
	}

	public void setOnUpLoadProgressListener(int resourceId,ProgressListener progressListener){
		ResuambleUpload resuambleUpload = upLoadTaskMap.get(resourceId);
		if(resuambleUpload!=null){
			resuambleUpload.setProgressListener(progressListener);
		}else{
			progressListenerMap.put(resourceId, progressListener);
		}		
	}

	public void cancelAll(){
		if(upLoadTaskMap!=null&&upLoadTaskMap.size()>0){
			for ( Map.Entry<Integer, ResuambleUpload> entry : upLoadTaskMap.entrySet()) {
				entry.getValue().cancel();
			}
		}
	}
	
}
