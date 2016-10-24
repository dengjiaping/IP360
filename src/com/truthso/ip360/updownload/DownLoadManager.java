package com.truthso.ip360.updownload;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.truthso.ip360.bean.DownLoadInfo;
import com.truthso.ip360.dao.UpDownLoadDao;

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
	
	public void pauseOrStratUpLoad(String  url){
		DownLoadRunnable downLoadRunnable = findDownLoadRunnableByUrl(url);
		if(downLoadRunnable!=null){
			Future<String> findFuture = findFuture(downLoadRunnable);
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
			DownLoadRunnable runnable = createDownLoadRunnableByUrl(url);
		    Future<String> future = (Future<String>)es.submit(runnable);
			map.put(future, runnable);
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
	
	public void setOnDownLoadProgressListener(String downLoadUrl,DownLoadListener listener){
		DownLoadRunnable runnable = findDownLoadRunnableByUrl(downLoadUrl);
		if(runnable==null){
			runnable=createDownLoadRunnableByUrl(downLoadUrl);
		}
		runnable.setOnProgressListener(listener);
	}
	
	private DownLoadRunnable createDownLoadRunnableByUrl(String url){
		DownLoadInfo info = UpDownLoadDao.getDao().queryDownLoadInfoByUrl(url);
		
		return  new DownLoadRunnable(url,info.getFileName(),info.getPosition(),info.getResourceId());

	}
	
	private DownLoadRunnable findDownLoadRunnableByUrl(String url){
		for (Map.Entry<Future<String>, DownLoadRunnable>  info: map.entrySet()) {
			if(info.getValue().getUrl().equals(url)){
				return info.getValue();
			}
		}
		return null;
	}
	
}
