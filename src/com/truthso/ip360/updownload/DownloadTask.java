package com.truthso.ip360.updownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.Range;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.UpDownLoadDao;

public class DownloadTask {

	private OSS oss;
	private String objectKey;
	private long position;
	private ProgressListener listener;
	private File downloadFile;
	private OSSAsyncTask task;
	private int progress = 0;

	public DownloadTask(OSS oss, String objectKey, long position) {
		this.objectKey = objectKey;
		this.oss = oss;
		this.position = position;
		String root = Environment.getExternalStorageDirectory().toString();
		downloadFile = new File(root+MyConstants.DOWNLOAD_PATH);
		if (!downloadFile.exists()) {
			downloadFile.mkdirs();
		}
	}

	public OSSAsyncTask start() {
		String fileName=objectKey.substring(objectKey.lastIndexOf("/"));
		 final File file=new File(downloadFile, fileName);

		try {
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}

		GetObjectRequest get = new GetObjectRequest("ip360-test", objectKey);
		// 设置范围
		get.setRange(new Range(position, Range.INFINITE)); // 下载0到99字节共100个字节，文件范围从0开始计算

		task = oss.asyncGetObject(get,
				new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {

					@Override
					public void onSuccess(GetObjectRequest request,
							GetObjectResult result) {
						// 请求成功
						InputStream inputStream = result.getObjectContent();
						byte[] buffer = new byte[2048];
						int len;

						try {
							FileOutputStream fos = new FileOutputStream(file);
							while ((len = inputStream.read(buffer)) != -1) {
								// 处理下载的数据
								progress += len;
								fos.write(buffer, 0, len);
								Log.i("djj", "progress" + progress);
								if (listener != null) {
									listener.onProgress(progress);
								}
							}
							fos.close();
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();

						} finally {
							Log.i("djj", "total" + progress);

						}
					}

					@Override
					public void onFailure(GetObjectRequest request,
							ClientException clientExcepion,
							ServiceException serviceException) {
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
							Log.e("RawMessage",
									serviceException.getRawMessage());
							Log.i("djj", "faile1");
						}
					}

				});
		return task;
	}

	public void setProgressListener(ProgressListener listener) {
		this.listener = listener;
	}

	public void pause() {
		task.cancel();
		UpDownLoadDao.getDao().updateDownLoadProgress(objectKey, progress);
	}
}
