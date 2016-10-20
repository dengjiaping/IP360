package com.truthso.ip360.updownload;

import java.util.concurrent.Future;

public class UpLoadTashInfo {

	private String filePath;
	private UpLoadRunnable runnable;
	private Future<String> future;
	public UpLoadTashInfo(String filePath, Future<String> future, UpLoadRunnable runnable) {
		this.filePath=filePath;
		this.runnable=runnable;
		this.future=future;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public UpLoadRunnable getRunnable() {
		return runnable;
	}
	public void setRunnable(UpLoadRunnable runnable) {
		this.runnable = runnable;
	}
	public Future<String> getFuture() {
		return future;
	}
	public void setFuture(Future<String> future) {
		this.future = future;
	}
	
}
