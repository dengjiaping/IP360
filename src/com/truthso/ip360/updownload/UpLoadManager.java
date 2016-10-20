package com.truthso.ip360.updownload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UpLoadManager {

	private ExecutorService es;
	private static UpLoadManager instance=new UpLoadManager();

	private UpLoadTashInfo info;
	private List<UpLoadTashInfo> list=new ArrayList<UpLoadTashInfo>();
	
	private  UpLoadManager(){
		int size=Runtime.getRuntime().availableProcessors()*2+1;
		es = Executors.newFixedThreadPool(3);
	}
	
	public List<UpLoadTashInfo>  getList(){
		return list;
	}
	
	public static UpLoadManager getInstance(){
		return instance;
	}
	
	public void  startUpload(String upLoadUrl,String filePath){
		UpLoadRunnable runnable = new UpLoadRunnable(upLoadUrl,filePath);
	    Future<String> future = (Future<String>)es.submit(runnable);
		info=new UpLoadTashInfo(filePath,future,runnable);		
		list.add(info);
	}
	
}
