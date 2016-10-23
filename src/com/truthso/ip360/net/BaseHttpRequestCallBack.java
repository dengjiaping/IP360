package com.truthso.ip360.net;

import com.loopj.android.http.RequestHandle;

import cz.msebera.android.httpclient.Header;


public interface BaseHttpRequestCallBack {
	public void onRequestResult(RequestHandle requestHandle, int errorCode,
			String message,BaseHttpResponse response);
	
	public void onFaile(RequestHandle requestHandle,int statusCode, Header[] headers,
			byte[] responseBody, Throwable error);
}
