package com.truthso.ip360.net;

import cz.msebera.android.httpclient.Header;

public interface ApiCallback {
	 public void onApiResult(int errorCode, String message, BaseHttpResponse response);
	 public void onApiResultFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error);
}
