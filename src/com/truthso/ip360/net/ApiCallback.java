package com.truthso.ip360.net;

public interface ApiCallback {
	 public void onApiResult(int errorCode, String message, BaseHttpResponse response);
}
