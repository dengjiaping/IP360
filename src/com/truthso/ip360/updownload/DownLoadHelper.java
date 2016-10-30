package com.truthso.ip360.updownload;

import java.io.IOException;
import java.io.InputStream;

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

public class DownLoadHelper {

	private String endpoint = "http://oss-cn-beijing.aliyuncs.com";
	private OSS oss;
	private OSSAsyncTask task;

	public DownLoadHelper() {
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIIHLk9eURcRim", "6rXdqbwAShL0P8uR4L1zoLVX4eIUKj");
		oss = new OSSClient(MyApplication.getApplication(), endpoint, credentialProvider);
	}

	// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节

	public void downloadFile(String objectKey,long position) {
		GetObjectRequest get = new GetObjectRequest("ip360-test",objectKey);
		// 设置范围
		get.setRange(new Range(position, Range.INFINITE)); // 下载0到99字节共100个字节，文件范围从0开始计算

		task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {	
			
			@Override
			public void onSuccess(GetObjectRequest request, GetObjectResult result) {
				// 请求成功
				InputStream inputStream = result.getObjectContent();
				byte[] buffer = new byte[2048];
				int len;
				try {
					while ((len = inputStream.read(buffer)) != -1) {
						// 处理下载的数据
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
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
	
	public void pauseDownload(){
		
	}
	

}
