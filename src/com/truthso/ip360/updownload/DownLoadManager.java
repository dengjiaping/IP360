package com.truthso.ip360.updownload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import android.net.Uri;
import com.truthso.ip360.application.MyApplication;
import android.app.ProgressDialog;
import android.os.Environment;

import com.truthso.ip360.bean.DownLoadInfo;
import com.truthso.ip360.bean.FilePositionBean;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

public class DownLoadManager {

	private ExecutorService es;
	private static DownLoadManager instance=new DownLoadManager();
	private LinkedHashMap<Future<String>, DownLoadRunnable> map;
	LinkedHashMap<Integer, ProgressListener> listenerMap;
	private  DownLoadManager(){
		int size=Runtime.getRuntime().availableProcessors()*2+1;
		es = Executors.newFixedThreadPool(3);
		map=new LinkedHashMap<Future<String>, DownLoadRunnable>();
		listenerMap=new LinkedHashMap<Integer, ProgressListener>();
	}
	
	public LinkedHashMap<Future<String>, DownLoadRunnable> getMap(){
		return map;
	}
	
	public static DownLoadManager getInstance(){
		return instance;
	}
	
	public void  startDownload(FileInfo info){
		DownLoadRunnable runnable = new DownLoadRunnable(info.getFilePath(),info.getFileName(),info.getFileSize(),info.getPosition(),info.getResourceId());
	    Future<String> future = (Future<String>)es.submit(runnable);
		map.put(future, runnable);
		UpDownLoadDao.getDao().saveDownLoadInfo(info.getFilePath(),info.getFileName(),info.getFileSize(),info.getPosition(),info.getResourceId());
	}
	
	
	public void removeRunnable(DownLoadRunnable runnable){

		for (Map.Entry<Future<String>, DownLoadRunnable>  info: map.entrySet()) {
			if(info.getValue().equals(runnable)){
				map.remove(info.getKey());
			}
		}	

		/*Iterator<Entry<Future<String>, UpLoadRunnable>> iterator = map.entrySet().iterator();
	       	while (iterator.hasNext()) {
	       		if(iterator.next().getValue().equals(runnable)){
	       			iterator.remove();
	       		}				
			}*/
	}
	
	public void pauseOrStratDownLoad(int resourceId){
		DownLoadRunnable downLoadRunnable = findDownLoadRunnableByResourceId(resourceId);
		if(downLoadRunnable!=null){
			Future<String> findFuture = findFuture(resourceId);
			if(downLoadRunnable.getStatue()==0||downLoadRunnable.getStatue()==2){
				downLoadRunnable.pause();			
				findFuture.cancel(true);
				map.remove(findFuture);
			}else{
				map.remove(findFuture);
				Future<String> future = (Future<String>)es.submit(downLoadRunnable);
				map.put(future, downLoadRunnable);
			}			
		}else{
			//重新下载
			DownLoadRunnable runnable = createDownLoadRunnableByResourceId(resourceId);
		    Future<String> future = (Future<String>)es.submit(runnable);
			map.put(future, runnable);
			runnable.setOnProgressListener(listenerMap.get(resourceId));
			MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/updownloadlog/down"), null);
		}	
	}		
	
	private Future<String> findFuture(int resourceId){
		for (Map.Entry<Future<String>, DownLoadRunnable>  info: map.entrySet()) {
			if(info.getValue().getResourceId()==resourceId){
				return info.getKey();
			}
		}
		return null;
	}
	
	public void setOnDownLoadProgressListener(int  resourceId,ProgressListener listener){
		DownLoadRunnable runnable = findDownLoadRunnableByResourceId(resourceId);
		if(runnable==null){
			listenerMap.put(resourceId, listener);

		}else{
			runnable.setOnProgressListener(listener);
		}	
	}
	
	private DownLoadRunnable createDownLoadRunnableByResourceId(int resourceId){
		FileInfo info = UpDownLoadDao.getDao().queryDownLoadInfoByResourceId(resourceId);	
		return  new DownLoadRunnable(info.getFilePath(),info.getFileName(),info.getFileSize(),info.getPosition(),info.getResourceId());

	}
	
	private DownLoadRunnable findDownLoadRunnableByResourceId(int resourceId){
		for (Map.Entry<Future<String>, DownLoadRunnable>  info: map.entrySet()) {
			if(info.getValue().getResourceId()==resourceId){
				return info.getValue();
			}
		}
		return null;
	}

	
	public int  getCurrentStatus(int resourceId){
		DownLoadRunnable downLoadRunnable = findDownLoadRunnableByResourceId(resourceId);
		if(downLoadRunnable==null){
			return 1;
		}else{
		   return downLoadRunnable.getStatue();
		}
		
	}
	
	public void deleteByResourceId(int resourceId) {
		Future<String> future = findFuture(resourceId);
		map.remove(future);
		UpDownLoadDao.getDao().deleteDownInfoByResourceId(resourceId);
	}
	
	public void deleteAll(List<Integer> list){
		if(list!=null&&list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Integer integer = list.get(i);
				DownLoadRunnable downLoadRunnable = findDownLoadRunnableByResourceId(integer);
				if (downLoadRunnable != null) {
					Future<String> findFuture = findFuture(integer);				
					    downLoadRunnable.pause();
						findFuture.cancel(true);
						map.remove(findFuture);
					}
				UpDownLoadDao.getDao().deleteDownInfoByResourceId(integer);
			}		
		}		
	}
	
	
	public void pauseAll(List<Integer> list){
		for (int i = 0; i < list.size(); i++) {
			Integer integer = list.get(i);
			DownLoadRunnable downLoadRunnable = findDownLoadRunnableByResourceId(integer);
			if (downLoadRunnable != null) {
				Future<String> findFuture = findFuture(integer);
				if (downLoadRunnable.getStatue() == 0 || downLoadRunnable.getStatue() == 2) {
					downLoadRunnable.pause();
					findFuture.cancel(true);
					map.remove(findFuture);
				 }
			}
		}
	}
	
	public void startAll(List<Integer> list){
		for (int i = 0; i < list.size(); i++) {
			final Integer integer = list.get(i);
			DownLoadRunnable downLoadRunnable = findDownLoadRunnableByResourceId(integer);
			if(downLoadRunnable!=null){
				if(downLoadRunnable.getStatue()==1||downLoadRunnable.getStatue()==3){
					Future<String> findFuture = findFuture(integer);
					map.remove(findFuture);
					Future<String> future = (Future<String>) es.submit(downLoadRunnable);
					map.put(future, downLoadRunnable);
				}		
			}else{
				// 重新上传
				DownLoadRunnable runnable = createDownLoadRunnableByResourceId(integer);
			    Future<String> future = (Future<String>)es.submit(runnable);
				map.put(future, runnable);
				runnable.setOnProgressListener(listenerMap.get(integer));
				MyApplication.getApplication().getContentResolver().notifyChange(Uri.parse("content://com.truthso.ip360/updownloadlog/down"), null);
			}
					
		}
	}
	
	
}
