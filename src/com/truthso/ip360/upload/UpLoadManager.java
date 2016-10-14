package com.truthso.ip360.upload;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UpLoadManager {

	private  ExecutorService es=Executors.newFixedThreadPool(5);

	private void startUpLoad(Runnable runable){
		 Future<String> future = (Future<String>) es.submit(runable);

	}
	
	
}
