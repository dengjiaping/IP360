package com.truthso.ip360.updownload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.Range;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.UpDownLoadDao;

public class DownLoadHelper {

	private String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
	private OSS oss;
	private File downloadFile;
    private static DownLoadHelper helper=new DownLoadHelper();
    private Map<String, OSSAsyncTask> taskMap=new ArrayMap<String, OSSAsyncTask>();
    
    
	private  DownLoadHelper() {
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI6yfPjL9ik3xj", "KgVwREkNPxbfTbxSTV6ERDatCzRKsV");
		oss = new OSSClient(MyApplication.getApplication(), endpoint, credentialProvider);
		downloadFile=new File(MyConstants.DOWNLOAD_PATH);
		if(!downloadFile.exists()){
			downloadFile.mkdirs();
		}
	}

	public static DownLoadHelper  getInstance(){
		return helper;
	}
		
	// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节

	public void downloadFile(final String objectKey,long position) {
		final File file=new File(downloadFile, objectKey);
				
		final GetObjectRequest get = new GetObjectRequest("djj-test",objectKey);
		// 设置范围
		get.setRange(new Range(position, Range.INFINITE)); // 下载0到99字节共100个字节，文件范围从0开始计算

		OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {	
			
			@Override
			public void onSuccess(GetObjectRequest request, GetObjectResult result) {
				// 请求成功
				InputStream inputStream = result.getObjectContent();
				byte[] buffer = new byte[2048];
				int len;
				int total = 0;			
				
				try {
					FileOutputStream fos=new FileOutputStream(file);
					while ((len = inputStream.read(buffer)) != -1) {
						// 处理下载的数据
						total+=len;
						fos.write(buffer, 0, len);
						Log.i("djj", "progress"+total);
						
					}
					fos.close();
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					
				}finally{
					Log.i("djj", "total"+total);
					UpDownLoadDao.getDao().updateDownLoadProgress(objectKey, total);
				}
			}

			@Override
			public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
				// 请求异常
				if (clientExcepion != null) {
					// 本地异常如网络异常等
					clientExcepion.printStackTrace();
					Log.i("djj", "faile");
				}
				if (serviceException != null) {
					// 服务异常
					Log.e("ErrorCode", serviceException.getErrorCode());
					Log.e("RequestId", serviceException.getRequestId());
					Log.e("HostId", serviceException.getHostId());
					Log.e("RawMessage", serviceException.getRawMessage());
					Log.i("djj", "faile1");
				}
			}	
	
		});
		
		taskMap.put(objectKey, task);
	}
	
	public void pauseDownload(String objectKey){
		
		OSSAsyncTask ossAsyncTask = taskMap.get(objectKey);
		taskMap.remove(objectKey).cancel();
	}
	

}
