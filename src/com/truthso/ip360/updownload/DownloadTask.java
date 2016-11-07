package com.truthso.ip360.updownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;

public class DownloadTask {

	private OSS oss;
	private String objectKey;
	private long position;
	private File downloadFile;
	private OSSAsyncTask task;
	private int progress = 0;
    private ProgressListener listener ;
    private FileInfo info;
	public DownloadTask(OSS oss, FileInfo info) {
		this.objectKey = info.getObjectKey();
		this.oss = oss;
		this.position = info.getPosition();
		this.info=info;
//		String root = Environment.getExternalStorageDirectory().toString();
		downloadFile = new File(MyConstants.DOWNLOAD_PATH);
		if (!downloadFile.exists()) {
			downloadFile.mkdirs();
		}
	}

	public OSSAsyncTask start(){
	Log.i("djj", "objectKey"+objectKey);
		String fileName=objectKey.substring(objectKey.lastIndexOf("/"));
		 final File file=new File(downloadFile, fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 GetObjectRequest get = new GetObjectRequest("ip360-test",objectKey);

		// 设置范围
		//get.setRange(new Range(100,10000)); // 下载0到99字节共100个字节，文件范围从0开始计算

		task = oss.asyncGetObject(get,
				new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {

					@Override
					public void onSuccess(GetObjectRequest request,
							GetObjectResult result) {
						// 请求成功
						InputStream inputStream = result.getObjectContent();
						long contentLength = result.getContentLength();
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
							if(contentLength==progress){
								//下载完成
								DbBean dbBean = new DbBean();
								dbBean.setTitle(info.getFileName());
								dbBean.setCreateTime(info.getFileCreatetime());
								dbBean.setResourceUrl(info.getFilePath());
								dbBean.setType(MyConstants.PHOTO);
								dbBean.setFileSize(info.getFileSize());
								dbBean.setLocation(info.getFileLoc());
								SqlDao.getSQLiteOpenHelper(MyApplication.getApplication()).save(dbBean, MyConstants.TABLE_MEDIA_DETAIL);
								
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

	public void setProgressListener(ProgressListener listener ) {
		this.listener = listener;
	}

	public void pause() {
		task.cancel();
		UpDownLoadDao.getDao().updateDownLoadProgress(objectKey, progress);
	}
}
