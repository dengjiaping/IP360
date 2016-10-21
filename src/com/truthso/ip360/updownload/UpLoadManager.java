package com.truthso.ip360.updownload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.SearchManager.OnCancelListener;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Observable;
import android.os.Handler;

public class UpLoadManager {

	private ExecutorService es;
	private static UpLoadManager instance=new UpLoadManager();
	private LinkedHashMap<Future<String>, UpLoadRunnable> map;
	private  UpLoadManager(){
		int size=Runtime.getRuntime().availableProcessors()*2+1;
		es = Executors.newFixedThreadPool(3);
		map=new LinkedHashMap<Future<String>, UpLoadRunnable>();
	}
	
	
	public LinkedHashMap<Future<String>, UpLoadRunnable> getMap(){
		return map;
	}
	
	public static UpLoadManager getInstance(){
		return instance;
	}
	
	public void  startUpload(String upLoadUrl,String filePath,int position,int resourceId){
		UpLoadRunnable runnable = new UpLoadRunnable(upLoadUrl,filePath,position,resourceId);
	    Future<String> future = (Future<String>)es.submit(runnable);
		map.put(future, runnable);
	}
	
	
	public void removeRunnable(UpLoadRunnable runnable){
		for (Map.Entry<Future<String>, UpLoadRunnable>  info: map.entrySet()) {
			if(info.getValue().equals(runnable)){
				map.remove(info.getKey());
			}
		}		
	}
	
	public void pauseUpLoad(Future<String> future){
		UpLoadRunnable upLoadRunnable = map.get(future);
		if(upLoadRunnable.isUpLoading()){
			upLoadRunnable.cancle();
			future.cancel(true);
		}else{
			map.put((Future<String>)es.submit(upLoadRunnable), upLoadRunnable);
		}
	}
	
}
