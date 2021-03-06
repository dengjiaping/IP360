package com.truthso.ip360.updownload;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.database.Observable;
import android.util.Log;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.utils.GsonUtil;
import com.truthso.ip360.utils.JsonUtil;

public class UpLoadRunnable implements Runnable {

	private String  filePath;
	private String uploadUrl=URLConstant.UploadFile;
	private boolean isCancle, isUpLoading;
	private int status;
	private int WAIT=0,PAUSE=1,RUNNING=2,ERROR=3;	
	private ProgressListener upLoadListener;
	private int upLoadProgress, position, resourceId;
    private UpDownLoadDao  dao= UpDownLoadDao.getDao();
	public UpLoadRunnable(String filePath, int position, int resourceId) {
		super();
		this.filePath = filePath;
		this.position = position;
		this.resourceId = resourceId;
	}

	public void pause() {
		isCancle = true;
		status=PAUSE;
	}

	public int getUpLoadProgress() {
		return upLoadProgress;
	}

	public String getUrl() {
		return filePath;
	}
	public int getResourceId(){
		return resourceId;
	}
    public int  getStatue(){
    	return status;
    }
	@Override
	public void run() {
		status=RUNNING;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		String CHARSET = "UTF-8";
		int TIME_OUT = 60000;
		try {
			File uploadFile = new File(filePath);
			if (!uploadFile.exists()) {
				uploadFile.createNewFile();
			}
			long length = uploadFile.length();
			URL url = new URL(uploadUrl);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setReadTimeout(TIME_OUT);
			connection.setConnectTimeout(TIME_OUT);
			connection.setDoInput(true); // 允许输入流
			connection.setDoOutput(true); // 允许输出流
			connection.setUseCaches(false); // 不允许使用缓存
			connection.setRequestMethod("POST"); // 请求方式
			connection.setRequestProperty("Charset", CHARSET);
			connection.setRequestProperty("Range", "bytes=" + position);
			connection.setRequestProperty("Cache-Control", "no-cache"); 
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);			

			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

			Map<String, String> params = new HashMap<String, String>();
			params.put("position", position + "");
			params.put("resourceId", resourceId + "");
			params.put("token", MyApplication.getInstance().getTokenId().trim());
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
				sb.append(LINE_END);
				sb.append(entry.getValue());
				sb.append(LINE_END);
			}

			sb.append(PREFIX);// 开始拼接文件参数
			sb.append(BOUNDARY);
			sb.append(LINE_END);

			sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + uploadFile.getName() + "\"" + LINE_END);
			sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
			sb.append(LINE_END);
			dos.write(sb.toString().getBytes());
			RandomAccessFile raf = new RandomAccessFile(uploadFile, "r");
			raf.seek(Integer.valueOf(position));
		
			byte[] buffer = new byte[1024];
			int len = -1;
			int progress = Integer.valueOf(position);
			while (!isCancle && (len = raf.read(buffer)) != -1) {				
				dos.write(buffer, 0, len);
				progress += len;
				if (upLoadListener != null) {
					upLoadListener.onProgress((int) (progress));
					//dao.updateUpLoadProgress(resourceId, progress);
				}

//				Log.i("djj", "progress" + progress + "length" + length);
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}


			}
			raf.close();

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			dos.write(endData);
			dos.flush();
			dos.close();	
			Log.i("djj", connection.getResponseCode()+"");
			// 读取返回数据			
			if(connection.getResponseCode()==200){
				StringBuffer strBuf = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					strBuf.append(line).append("\n");
				}
				String res = strBuf.toString();
				reader.close();
				reader = null;
				
				Log.i("djj", "res" + res);
				if (progress == uploadFile.length()) {
					if (upLoadListener != null) {
						upLoadListener.onComplete();
					}
					UpDownLoadDao.getDao().deleteByResourceId(resourceId);
				}
			}else{
				Log.i("djj", "上传失败");
			}

		} catch (IOException e) {
			e.printStackTrace();
			status=ERROR;
		}
		
	}

	public void setOnProgressListener(ProgressListener listen) {
		this.upLoadListener = listen;
	}
}
