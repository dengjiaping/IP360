package com.truthso.ip360.ossupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Message;
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
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.SharePreferenceUtil;

public class DownloadTask {

	private OSS oss;
	private String objectKey;
	private long position;
	private File downloadFile;
	private OSSAsyncTask task;
	private int progress = 0;
    private ProgressListener listener ;
    private FileInfo info;
	private final int DOWNLOAD_CODE=101,SUCCESS=102,FAILE=103;

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
//		String fileName=objectKey.substring(objectKey.lastIndexOf("/"));
		String fileUrlFormatName=info.getFileUrlFormatName();
	   final File file=new File(downloadFile, fileUrlFormatName);
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
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
						Log.i("progress", "contentLength" + contentLength);
						byte[] buffer = new byte[2048];
						int len;
						try {
							FileOutputStream fos = new FileOutputStream(file);
							while ((len = inputStream.read(buffer)) != -1) {
								// 处理下载的数据
								progress += len;
								fos.write(buffer, 0, len);
								Log.i("progress", "progress" + progress);
								if (listener != null) {
									listener.onProgress(progress);
								}
							}
							if(contentLength==progress){
								Log.i("progress", "complete");
								//下载完成，下载的文件信息存数据库
								DbBean dbBean = new DbBean();
								dbBean.setTitle(info.getFileName());
								dbBean.setCreateTime(info.getFileCreatetime());
								dbBean.setResourceUrl(info.getFilePath());
								dbBean.setRecordTime(info.getFileTime());
								dbBean.setFileFormat(info.getFileFormat());
								int type = info.getType();//类型 1确权文件 2现场取证 3 线上取证
								int mobileType = info.getMobiletype();//现场取证的类型 5001拍照 5002录音 5003录像
//								文件类型 0照片，1视频，2录音 3云端拍照 4云端视频 5云端录音
							
								if (type == 2) {//现场取证
									if (mobileType == 50001) {
										dbBean.setType(MyConstants.CLOUD_PHOTO);
									}else if (mobileType == 50002) {
										dbBean.setType(MyConstants.CLOUD_RECORD);
									}else if (mobileType == 50003){
										dbBean.setType(MyConstants.CLOUD_VIDEO);
									}
								}else if (type == 1) {//确权文件
									dbBean.setType(MyConstants.QUEQUAN);
								}else if(type == 3){//现场取证
									dbBean.setType(MyConstants.XSQZ);
								}

								dbBean.setFileSize(info.getFileSize());
								dbBean.setLocation(info.getFileLoc());
								dbBean.setPkValue(info.getResourceId()+"");
								dbBean.setStatus("1");
							    int userId=  (Integer) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY,"userId",SharePreferenceUtil.VALUE_IS_INT);
								dbBean.setUserId(userId);
								dbBean.setDataType(info.getDataType());
								Message msg=new Message();
								msg.what=DOWNLOAD_CODE;
								msg.arg1=SUCCESS;
								msg.obj=dbBean;
								UpDownloadHandler.getInstance().sendMessage(msg);
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
						Message msg=new Message();
						msg.what=DOWNLOAD_CODE;
						msg.arg1=FAILE;
						msg.obj=info.getResourceId()+"";
						UpDownloadHandler.getInstance().sendMessage(msg);
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
	public void cancel() {
		if(!task.isCompleted()){
			task.cancel();
		}
	}
}
