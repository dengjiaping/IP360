package com.truthso.ip360.ossupload;

import java.io.File;
import java.util.HashMap;

import android.os.Environment;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.SharePreferenceUtil;

/**
 * Created by zhouzhuo on 12/3/15.
 */
public class ResuambleUpload {

    private OSS oss;
    private String testBucket;
    private String testObject;
    private String uploadFilePath;
    private boolean isDone;
    private ProgressListener progressListener;
    private OSSAsyncTask resumableTask;
    private long progress;
    private boolean iscancel=true;
    private int status;
    private int RUNNING=0,PAUSE=1,ERROR=2;
    private int resourceid;
    private FileInfo info;
    private String token;
    public ResuambleUpload(OSS client, String testBucket, FileInfo info) {
        this.oss = client;
        this.testBucket = testBucket;
        this.testObject = info.getObjectKey();
        this.resourceid=info.getResourceId();
        this.uploadFilePath=info.getFilePath();
        token=(String) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "token", SharePreferenceUtil.VALUE_IS_STRING);
    }

    // 异步断点上传，设置记录保存路径，即使任务失败，下次启动仍能继续
    public void resumableUploadWithRecordPathSetting() {
       Log.i("djj","testBucket"+testBucket+":testObject"+testObject);
        String recordDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/oss_record/";

        File recordDir = new File(recordDirectory);

        // 要保证目录存在，如果不存在则主动创建
        if (!recordDir.exists()) {
            recordDir.mkdirs();
        }

        // 创建断点上传请求，参数中给出断点记录文件的保存位置，需是一个文件夹的绝对路径
        ResumableUploadRequest request = new ResumableUploadRequest(testBucket, testObject, uploadFilePath, recordDirectory);
        
        
      /*  ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");

        request.setMetadata(metadata);
        request.setCallbackParam(new HashMap<String, String>(){
        	{
        	put("callbackUrl", "http://101.201.74.230:9091/api/v1/file/uploadFileOssStatus"); 
        	put("callbackBody", "resourceid=${x:resourceid}&token=${x:token}"); 
          
        	}
        	
        });
        
        request.setCallbackVars(new HashMap<String, String>() {
            {
                put("x:resourceid", resourceid+"");
            }
       });*/
        
        // 设置上传过程回调
        request.setProgressCallback(new OSSProgressCallback<ResumableUploadRequest>() {
            @Override
            public void onProgress(ResumableUploadRequest request, long currentSize, long totalSize) {
            	progress=currentSize;
             Log.i("djj", currentSize+"");
                if(progressListener!=null){
                	progressListener.onProgress(currentSize);
                }
            }
            
        });
   
    
        resumableTask = oss.asyncResumableUpload(request, new OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult>() {
                   	
            @Override
            public void onSuccess(ResumableUploadRequest request, ResumableUploadResult result) {
            	  Log.i("djj", "success");
                isDone=true;
                if(progressListener!=null){
                	progressListener.onComplete();               	
                }
                UpDownLoadDao.getDao().deleteUploadInfoByUrl(uploadFilePath);
                SqlDao.getSQLiteOpenHelper(MyApplication.getApplication()).updateStatus(info.getFileName(), "1");
            }

            @Override
            public void onFailure(ResumableUploadRequest request, ClientException clientExcepion, ServiceException serviceException) {
            	 Log.i("djj", "failure");
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
   
            }
        });
    }
    
    
    //上传
    public void putObject(){
    	PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);
    	
    	ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");

        put.setMetadata(metadata);
        put.setCallbackParam(new HashMap<String, String>(){
        	{
        	put("callbackUrl", "http://101.201.74.230:9091/api/v1/file/uploadFileOssStatus"); 
        	put("callbackBody", "resourceid=${x:resourceid}&token=${x:token}"); 
        	}        	
        });
        
        put.setCallbackVars(new HashMap<String, String>() {
            {
                put("x:resourceid", resourceid+"");
                put("x:token", token);
            }
       });
    	
    	
    	put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
			
			@Override
			public void onProgress(PutObjectRequest arg0, long currentSize, long arg2) {
				progress=currentSize;
	             Log.i("djj", currentSize+"");
	                if(progressListener!=null){
	                	progressListener.onProgress(currentSize);
	                }
				
			}
		});
    	
    	oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
			
			@Override
			public void onSuccess(PutObjectRequest arg0, PutObjectResult arg1) {
				// TODO Auto-generated method stub
				 Log.i("djj", "success");
				 Log.i("djj", arg1.getStatusCode()+"");
	                isDone=true;
	                if(progressListener!=null){
	                	progressListener.onComplete();               	
	                }
	                UpDownLoadDao.getDao().deleteUploadInfoByUrl(uploadFilePath);
	                SqlDao.getSQLiteOpenHelper(MyApplication.getApplication()).updateStatus(info.getFileName(), "1");
			}
			
			@Override
			public void onFailure(PutObjectRequest arg0, ClientException clientExcepion,
					ServiceException serviceException) {
				 Log.i("djj", "failure");
	                // 请求异常
	                if (clientExcepion != null) {
	                	Log.i("djj", "failure");
	                    // 本地异常如网络异常等
	                    clientExcepion.printStackTrace();
	                }
	                if (serviceException != null) {
	                	Log.i("djj", "failure1");
	                    // 服务异常
	                    Log.e("ErrorCode", serviceException.getErrorCode());
	                    Log.e("RequestId", serviceException.getRequestId());
	                    Log.e("HostId", serviceException.getHostId());
	                    Log.e("RawMessage", serviceException.getRawMessage());
	                }
			}
		});
    }
    
    
    
    public void setProgressListener(ProgressListener progressListener){
    	this.progressListener=progressListener;
    }
    
    public boolean isDone(){
    	return isDone;
    }
    
    public int getStatus(){
    	return status;
    }
    
    public void pause(){   	
    	resumableTask.cancel();
   
    	status=PAUSE;
    	UpDownLoadDao.getDao().updateUpLoadProgress(uploadFilePath, progress);
    }
     
}
