package com.truthso.ip360.updownload;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.FilePositionBean;
import com.truthso.ip360.constants.URLConstant;
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
	private  DownLoadManager(){
		int size=Runtime.getRuntime().availableProcessors()*2+1;
		es = Executors.newFixedThreadPool(3);
		map=new LinkedHashMap<Future<String>, DownLoadRunnable>();
	}
	
	public LinkedHashMap<Future<String>, DownLoadRunnable> getMap(){
		return map;
	}
	
	public static DownLoadManager getInstance(){
		return instance;
	}
	
	public void  startDownload(String dwonLoadUrl,String filePath,int position,int resourceId){
		DownLoadRunnable runnable = new DownLoadRunnable(dwonLoadUrl,filePath,position,resourceId);
	    Future<String> future = (Future<String>)es.submit(runnable);
		map.put(future, runnable);
		UpDownLoadDao.getDao().saveDownLoadUrl(dwonLoadUrl);
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
	
	public void pauseOrStratUpLoad(DownLoadRunnable downLoadRunnable){
			
		if(map.containsValue(downLoadRunnable)){
			downLoadRunnable.pause();
			Future<String> findFuture = findFuture(downLoadRunnable);
			findFuture.cancel(true);
			map.remove(findFuture);
		}else{
			//重新下载
			/*DownLoadRunnable runnable = new DownLoadRunnable(dwonLoadUrl,filePath,position,resourceId);
		    Future<String> future = (Future<String>)es.submit(runnable);
			map.put(future, runnable);*/
		}	
	}		
	
	private Future<String> findFuture(DownLoadRunnable downLoadRunnable){
		for (Map.Entry<Future<String>, DownLoadRunnable>  info: map.entrySet()) {
			if(info.getValue().equals(downLoadRunnable)){
				return info.getKey();
			}
		}
		return null;
	}
	
}
