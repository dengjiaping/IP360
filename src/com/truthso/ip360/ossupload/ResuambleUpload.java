package com.truthso.ip360.ossupload;

import java.io.File;
import java.util.HashMap;

import android.net.Uri;
import android.os.Environment;
import android.os.Message;
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
import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.event.UpLoadFaileEvent;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhouzhuo on 12/3/15.
 */
public class ResuambleUpload {

    private OSS oss;
    private String testBucket;
   
    private boolean isDone;
    private ProgressListener progressListener;
    private OSSAsyncTask resumableTask;
    private long progress;
    private boolean iscancel=true;
    private int status;
    private int RUNNING=0,PAUSE=1,ERROR=2;
    private int resourceId;
    private int UPLOAD_CODE=201,SUCCESS=102,FAILE=103;

    private FileInfo info;
    private String token;
    public ResuambleUpload(OSS client, String testBucket, FileInfo info) {
        this.oss = client;
        this.testBucket = testBucket;
        this.info=info;
        token=(String) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY, "token", SharePreferenceUtil.VALUE_IS_STRING);
    }

    // 异步断点上传，设置记录保存路径，即使任务失败，下次启动仍能继续
    public void resumableUploadWithRecordPathSetting() {

        String recordDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/oss_record/";

        File recordDir = new File(recordDirectory);

        // 要保证目录存在，如果不存在则主动创建
        if (!recordDir.exists()) {
            recordDir.mkdirs();
        }

        // 创建断点上传请求，参数中给出断点记录文件的保存位置，需是一个文件夹的绝对路径
        ResumableUploadRequest request = new ResumableUploadRequest(testBucket, info.getObjectKey(), info.getFilePath(), recordDirectory);
        
        
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");

        request.setMetadata(metadata);
        request.setCallbackParam(new HashMap<String, String>(){
            {
                put("callbackUrl", URLConstant.UploadFileOssStatus);//上传后的回调地址
                put("callbackBody", "resourceId=${x:resourceId}&token=${x:token}");
            }

        });

        request.setCallbackVars(new HashMap<String, String>() {
            {
                put("x:resourceId", info.getResourceId()+"");
                put("x:token", token);
            }
        });
        
        // 设置上传过程回调
        request.setProgressCallback(new OSSProgressCallback<ResumableUploadRequest>() {
            @Override
            public void onProgress(ResumableUploadRequest request, long currentSize, long totalSize) {
            	progress=currentSize;
             Log.i("djj", currentSize+"");
                if(progressListener!=null){
                	progressListener.onProgress(currentSize);
                }
                LogUtils.e(info.getResourceId()+"回调进程1rescordid");
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

                Message msg=new Message();
                msg.what=UPLOAD_CODE;
                msg.arg1=SUCCESS;
                msg.obj=info;
                UpDownloadHandler.getInstance().sendMessage(msg);
                LogUtils.e(info.getResourceId()+"上传成功回调rescordid");
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
                Message msg=new Message();
                msg.what=UPLOAD_CODE;
                msg.arg1=FAILE;
                msg.obj=info;
                UpDownloadHandler.getInstance().sendMessage(msg);
            }
        });

    }
    
    
    //上传
    public void putObject(){
    	PutObjectRequest put = new PutObjectRequest(testBucket, info.getObjectKey(), info.getFilePath());
    	
    	ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");

        put.setMetadata(metadata);
        put.setCallbackParam(new HashMap<String, String>(){
        	{
        	put("callbackUrl", URLConstant.UploadFileOssStatus);
        	put("callbackBody", "resourceId=${x:resourceId}&token=${x:token}"); 
        	}        	
        });
        
        put.setCallbackVars(new HashMap<String, String>() {
            {
                put("x:resourceId", info.getResourceId()+"");
                put("x:token", token);
            }
       });
    	
    	
    	put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
			@Override
			public void onProgress(PutObjectRequest arg0, long currentSize, long arg2) {
				progress=currentSize;
	                if(progressListener!=null){
	                	progressListener.onProgress(currentSize);
	                }
			}
		});
    	
    	oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
			
			@Override
			public void onSuccess(PutObjectRequest arg0, PutObjectResult arg1) {
				// TODO Auto-generated method stub

	                isDone=true;
	                if(progressListener!=null){
	                	progressListener.onComplete();
	                }
                Log.i("djj","uploadsuccess");
                Message msg=new Message();
                msg.what=UPLOAD_CODE;
                msg.arg1=SUCCESS;
                msg.obj=info;
                UpDownloadHandler.getInstance().sendMessage(msg);
			}
			
			@Override
			public void onFailure(PutObjectRequest arg0, ClientException clientExcepion,
					ServiceException serviceException) {
                Log.i("djj","uploadfaile");
                    status=ERROR;
                if(progressListener!=null){
                    progressListener.onFailure();
                }
	                // 请求异常
	                if (clientExcepion != null) {
	                	Log.i("djj", "failure1");
	                    // 本地异常如网络异常等
	                    clientExcepion.printStackTrace();
	                }
	                if (serviceException != null) {
	                	Log.i("djj", "failure2");
	                    // 服务异常
	                    Log.e("ErrorCode", serviceException.getErrorCode());
	                    Log.e("RequestId", serviceException.getRequestId());
	                    Log.e("HostId", serviceException.getHostId());
	                    Log.e("RawMessage", serviceException.getRawMessage());
	                }
                Message msg=new Message();
                msg.what=UPLOAD_CODE;
                msg.arg1=FAILE;
                msg.obj=info;
                UpDownloadHandler.getInstance().sendMessage(msg);
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
    	UpDownLoadDao.getDao().updateUpLoadProgress(info.getFilePath(), progress);
    }

    public void  cancel(){
        if (!CheckUtil.isEmpty(resumableTask)){

            if(!resumableTask.isCompleted()){
                resumableTask.cancel();
            }
        }

    }
     
}
