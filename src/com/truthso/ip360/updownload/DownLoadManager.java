package com.truthso.ip360.updownload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.ProgressDialog;
import android.os.Environment;

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
	
	public void  startDownload(String dwonLoadUrl,String fileName,String filesize,int position,int resourceId){
		DownLoadRunnable runnable = new DownLoadRunnable(dwonLoadUrl,fileName,position,resourceId);
	    Future<String> future = (Future<String>)es.submit(runnable);
		map.put(future, runnable);
		UpDownLoadDao.getDao().saveDownLoadInfo(dwonLoadUrl,fileName,filesize,position,resourceId);
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
	
	public void pauseOrStratDownLoad(String  url){
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
	/**
	 * 版本更新用
	 * @param path
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//获取到文件的大小 
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(), "IP360.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len ;
			int total=0;
			while((len =bis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
				total+= len;
				//获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else{
			return null;
		}
	}
}
