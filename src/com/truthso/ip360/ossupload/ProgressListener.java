package com.truthso.ip360.ossupload;

public interface ProgressListener {

	public void onProgress(long progress);
	public void onComplete();
}
