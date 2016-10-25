package com.truthso.ip360.updownload;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.net.Uri;
import android.util.Log;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.FilePositionBean;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

public class UpLoadManager {

	private ExecutorService es;
	private static UpLoadManager instance = new UpLoadManager();
	private LinkedHashMap<Future<String>, UpLoadRunnable> map;
	private LinkedHashMap<Integer, UpLoadListener> listenerMap;

	private UpLoadManager() {
		int size = Runtime.getRuntime().availableProcessors() * 2 + 1;
		es = Executors.newFixedThreadPool(3);
		map = new LinkedHashMap<Future<String>, UpLoadRunnable>();
		listenerMap = new LinkedHashMap<Integer, UpLoadListener>();
	}

	public LinkedHashMap<Future<String>, UpLoadRunnable> getMap() {
		return map;
	}

	public static UpLoadManager getInstance() {
		return instance;
	}

	/**
	 * 开始上传文件
	 * 
	 * @param upLoadUrl
	 *            文件要上传的路径
	 * @param filePath
	 *            文件路径
	 * @param position
	 *            文件传到的位置
	 * @param resourceId
	 *            文件id
	 */
	public void startUpload(UpLoadInfo info) {
		UpLoadRunnable runnable = new UpLoadRunnable(info.getFilePath(), info.getPosition(), info.getResourceId());
		Future<String> future = (Future<String>) es.submit(runnable);
		map.put(future, runnable);
		Log.i("uploadinfo", info.toString() + "mapsize" + map.size());
		UpDownLoadDao.getDao().saveUpLoadInfo(info.getFilePath(), info.getFileName(), info.getFileSize(), info.getPosition(), info.getResourceId());
	}

	public void setOnUpLoadProgressListener(int resourceId, UpLoadListener listener) {
		UpLoadRunnable runnable = findUpLoadRunnableByResourceId(resourceId);
		if (runnable == null) {
			listenerMap.put(resourceId, listener);
			return;
		}
		runnable.setOnProgressListener(listener);
	}

	private UpLoadRunnable findUpLoadRunnableByResourceId(int resourceId) {

		for (Map.Entry<Future<String>, UpLoadRunnable> info : map.entrySet()) {
			if (info.getValue().getResourceId() == resourceId) {
				return info.getValue();
			}
		}
		return null;
	}

	public void pauseOrStratUpLoad(final int resourceId) {
		int result = 0;
		UpLoadRunnable upLoadRunnable = findUpLoadRunnableByResourceId(resourceId);
		if (upLoadRunnable != null) {
			Future<String> findFuture = findFuture(resourceId);
			if (upLoadRunnable.getStatue() == 0 || upLoadRunnable.getStatue() == 2) {
				upLoadRunnable.pause();
				findFuture.cancel(true);
				map.remove(findFuture);
		
			} else {
				map.remove(findFuture);
				Future<String> future = (Future<String>) es.submit(upLoadRunnable);
				map.put(future, upLoadRunnable);
			}

		} else {
			
			UpLoadInfo info = UpDownLoadDao.getDao().queryUpLoadInfoByResourceId(resourceId);
			UpLoadRunnable runnable = new UpLoadRunnable(info.getFilePath(), info.getPosition(), info.getResourceId());
			Future<String> future = (Future<String>) es.submit(runnable);
			map.put(future, runnable);
			runnable.setOnProgressListener(listenerMap.get(resourceId));
			MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/updownloadlog"), null);
			
			/*// 重新上传
			ApiManager.getInstance().getFilePosition(resourceId, new ApiCallback() {

				@Override
				public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

				}

				@Override
				public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
					FilePositionBean bean = (FilePositionBean) response;
					if (!CheckUtil.isEmpty(bean)) {
						if (bean.getCode() == 200) {
							UpLoadInfo info = UpDownLoadDao.getDao().queryUpLoadInfoByResourceId(resourceId);
							UpLoadRunnable runnable = new UpLoadRunnable(info.getFilePath(), info.getPosition(), info.getResourceId());
							Future<String> future = (Future<String>) es.submit(runnable);
							map.put(future, runnable);
							runnable.setOnProgressListener(listenerMap.get(resourceId));
							MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/updownloadlog"), null);
						} else {
							Toaster.showToast(MyApplication.getApplication(), "获取数据失败");
						}
					} else {
						Toaster.showToast(MyApplication.getApplication(), "获取数据失败");
					}
				}
			});*/
		}
	}

	private Future<String> findFuture(int resourceId) {
		for (Map.Entry<Future<String>, UpLoadRunnable> info : map.entrySet()) {
			if (info.getValue().getResourceId() == resourceId) {
				return info.getKey();
			}
		}
		return null;
	}

	public void deleteByResourceId(int resourceId) {
		Future<String> future = findFuture(resourceId);
		map.remove(future);
		UpDownLoadDao.getDao().deleteByResourceId(resourceId);
	}
	
	
	public void deleteAll(List<Integer> list){
		
		for (int i = 0; i < list.size(); i++) {
			Integer integer = list.get(i);
			UpLoadRunnable upLoadRunnable = findUpLoadRunnableByResourceId(integer);
			if (upLoadRunnable != null) {
				Future<String> findFuture = findFuture(integer);				
					upLoadRunnable.pause();
					findFuture.cancel(true);
					map.remove(findFuture);
				}
			UpDownLoadDao.getDao().deleteByResourceId(integer);
		}		
	}
	
	
	
	public void pauseAll(List<Integer> list){
		for (int i = 0; i < list.size(); i++) {
			Integer integer = list.get(i);
			UpLoadRunnable upLoadRunnable = findUpLoadRunnableByResourceId(integer);
			if (upLoadRunnable != null) {
				Future<String> findFuture = findFuture(integer);
				if (upLoadRunnable.getStatue() == 0 || upLoadRunnable.getStatue() == 2) {
					upLoadRunnable.pause();
					findFuture.cancel(true);
					map.remove(findFuture);
				 }
				}
		}
	}
	
	public void startAll(List<Integer> list){
		for (int i = 0; i < list.size(); i++) {
			final Integer integer = list.get(i);
			UpLoadRunnable upLoadRunnable = findUpLoadRunnableByResourceId(integer);
			if(upLoadRunnable!=null){
				if(upLoadRunnable.getStatue()==1||upLoadRunnable.getStatue()==3){
					Future<String> findFuture = findFuture(integer);
					map.remove(findFuture);
					Future<String> future = (Future<String>) es.submit(upLoadRunnable);
					map.put(future, upLoadRunnable);
				}		
			}else{
				// 重新上传
				ApiManager.getInstance().getFilePosition(integer, new ApiCallback() {

					@Override
					public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

					}

					@Override
					public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
						FilePositionBean bean = (FilePositionBean) response;
						if (!CheckUtil.isEmpty(bean)) {
							if (bean.getCode() == 200) {
								UpLoadInfo info = UpDownLoadDao.getDao().queryUpLoadInfoByResourceId(integer);
								UpLoadRunnable runnable = new UpLoadRunnable(info.getFilePath(), info.getPosition(), info.getResourceId());
								Future<String> future = (Future<String>) es.submit(runnable);
								map.put(future, runnable);
								runnable.setOnProgressListener(listenerMap.get(integer));
								MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/updownloadlog"), null);
							} else {
								Toaster.showToast(MyApplication.getApplication(), "获取数据失败");
							}
						} else {
							Toaster.showToast(MyApplication.getApplication(), "获取数据失败");
						}
					}
				});
			}
					
		}
	}
	
	
	public int  getCurrentStatus(int resourceId){
		UpLoadRunnable upLoadRunnable = findUpLoadRunnableByResourceId(resourceId);
		if(upLoadRunnable==null){
			return 1;
		}else{
		   return upLoadRunnable.getStatue();
		}
		
	}
}
