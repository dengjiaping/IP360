package com.truthso.ip360.updownload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.alibaba.sdk.android.common.auth.FederationCredentialProvider;
import com.alibaba.sdk.android.common.auth.FederationToken;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.Range;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.net.DownLoaderTask;

public class DownLoadHelper {

	private String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
	private OSS oss;
	private File downloadFile;
    private static DownLoadHelper helper=new DownLoadHelper();
    private Map<String, DownloadTask> taskMap=new ArrayMap<String, DownloadTask>();
    
    
	private  DownLoadHelper() {
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI6yfPjL9ik3xj", "KgVwREkNPxbfTbxSTV6ERDatCzRKsV");
	
		oss = new OSSClient(MyApplication.getApplication(), endpoint, credentialProvider);
	
	}

	public static DownLoadHelper  getInstance(){
		return helper;
	}
		
	// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节

	public void downloadFile(final String objectKey,long position) {
		DownloadTask task=new DownloadTask(oss,objectKey,position);
		task.start();
		taskMap.put(objectKey, task);
	}
	
	public void pauseDownload(String objectKey){
		
		DownloadTask downloadTask = taskMap.get(objectKey);
		if(downloadTask!=null){
			downloadTask.pause();
		}
	}
	

}
