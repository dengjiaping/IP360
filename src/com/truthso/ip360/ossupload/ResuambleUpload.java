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
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;
import com.truthso.ip360.dao.UpDownLoadDao;

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
    public ResuambleUpload(OSS client, String testBucket, String testObject, String uploadFilePath,int resourceid) {
        this.oss = client;
        this.testBucket = testBucket;
        this.testObject = testObject;
        this.resourceid=resourceid;
        this.uploadFilePath=uploadFilePath;
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
        
        request.setCallbackParam(new HashMap<String, String>(){
        	{
        	put("callbackUrl", "http://101.201.74.230:9091/api/v1/file/uploadFileOssStatus"); 
        	put("callbackBody", "resourceid=${resourceid}"); 
        	}
        	
        });
        
        request.setCallbackVars(new HashMap<String, String>() {
            {
                put("resourceid", resourceid+"");
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
            }
            
        });
   
 
        resumableTask = oss.asyncResumableUpload(request, new OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult>() {
                   	
            @Override
            public void onSuccess(ResumableUploadRequest request, ResumableUploadResult result) {
            	  Log.i("djj", "success");
                isDone=true;
                if(progressListener!=null){
                	progressListener.onComplete();
                	UpDownLoadDao.getDao().deleteUploadInfoByUrl(uploadFilePath);
                }
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
    	
   
       
   /* 
    new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			 File file=new File(uploadFilePath);
			    File file1=new File(Environment.getExternalStorageDirectory()+"/ip360/test/test1.jpg");
			    try {
					FileOutputStream fos=new FileOutputStream(file1);
					FileInputStream fis=new FileInputStream(file);
					int len;
					int total=0;
					byte[] buffer=new byte[1024];
					try {
						while (iscancel&&(len=fis.read(buffer))!=-1) {
							fos.write(buffer, 0, len);
							progress+=len;
							Log.i("djj", progress+"");
							if(progressListener!=null){
			                	progressListener.onProgress(progress);
			                }
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							};
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}).start();*/
    
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
