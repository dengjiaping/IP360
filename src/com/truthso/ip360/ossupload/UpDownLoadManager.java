package com.truthso.ip360.ossupload;



import java.util.HashMap;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.truthso.ip360.application.MyApplication;

public class UpDownLoadManager {

	 private static UpDownLoadManager instance;
	 private OSS oss;
	 private HashMap<String, ResuambleUpload> upLoadTaskMap=new HashMap<String,ResuambleUpload>();

	// 运行sample前需要配置以下字段为有效的值
    private static final String endpoint =  "http://oss-cn-beijing.aliyuncs.com";
    private static final String accessKeyId ="LTAIIHLk9eURcRim";
    private static final String accessKeySecret = "6rXdqbwAShL0P8uR4L1zoLVX4eIUKj";
    private static final String uploadFilePath = "<upload_file_path>";

    private static final String testBucket =  "ip360-test";
    private static final String uploadObject = "123456";
    private static final String downloadObject = "sampleObject";
	
	private UpDownLoadManager(){

		 OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
    
	        ClientConfiguration conf = new ClientConfiguration();
	        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
	        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
	        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
	        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
	        OSSLog.enableLog();
	        oss = new OSSClient(MyApplication.getInstance().getApplicationContext(), endpoint, credentialProvider, conf);
	}
	
	public static UpDownLoadManager getInstance(){
		if(instance==null){
			instance=new UpDownLoadManager();
		}
		return instance;
	}
	
	public void resuambleUpload(String uploadFilePath){
		ResuambleUpload resuambleUpload=new ResuambleUpload(oss, testBucket, uploadObject, uploadFilePath);
		resuambleUpload.resumableUploadWithRecordPathSetting();
		upLoadTaskMap.put(uploadFilePath, resuambleUpload);
	}
	
	public HashMap<String, ResuambleUpload> getUpLoadTaskMap(){
		return upLoadTaskMap;
	}
	
	public void putObject(String uploadFilePath ){
		PutObjectSamples putObject=new PutObjectSamples(oss, testBucket, uploadObject, uploadFilePath);
		putObject.asyncPutObjectFromLocalFile();
		
	}
	
	public void  listObjects( ){
		ListObjectsSamples listObjects=new ListObjectsSamples(oss, testBucket);
		listObjects.AyncListObjects();
	}
	
	
}
