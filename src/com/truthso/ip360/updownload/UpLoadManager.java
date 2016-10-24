package com.truthso.ip360.updownload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.FilePositionBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

import android.app.SearchManager.OnCancelListener;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Observable;
import android.os.Handler;
import android.util.Log;

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
	/**
	 * 开始上传文件
	 * @param upLoadUrl 文件要上传的路径
	 * @param filePath 文件路径
	 * @param position 文件传到的位置
	 * @param resourceId 文件id
	 */
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

		/*Iterator<Entry<Future<String>, UpLoadRunnable>> iterator = map.entrySet().iterator();
	       	while (iterator.hasNext()) {
	       		if(iterator.next().getValue().equals(runnable)){
	       			iterator.remove();
	       		}				
			}*/
	}
	

	public void pauseOrStratUpLoad(Future<String> future,int resourceId,final String url){
		UpLoadRunnable upLoadRunnable = map.get(future);				
		if(upLoadRunnable!=null){
			upLoadRunnable.pause();
			future.cancel(true);
			map.remove(future);
			UpDownLoadDao.getDao().save(resourceId, upLoadRunnable.getUrl());
		}else{
			//重新上传
			ApiManager.getInstance().getFilePosition(resourceId, new ApiCallback() {
				
				@Override
				public void onApiResultFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					
				}
				
				@Override
				public void onApiResult(int errorCode, String message,
						BaseHttpResponse response) {
					FilePositionBean bean=(FilePositionBean) response;
					if(!CheckUtil.isEmpty(bean)){
						if(bean.getCode()==200){
							startUpload(URLConstant.UploadFile,url,bean.getDatas().getPosition(),bean.getDatas().getResourceId());
						}else{
							Toaster.showToast(MyApplication.getApplication(), "获取数据失败");
						}
						
					}else{
						Toaster.showToast(MyApplication.getApplication(), "获取数据失败");
					}					
				}
			});
		}	
	}		
}
