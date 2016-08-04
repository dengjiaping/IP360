package com.truthso.ip360.net;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import android.content.Context;

/**
 * 
 * @author wsx_summer
 * @date ����ʱ�䣺2016��5��26������12:01:44
 * @version 1.0
 * @Copyright (c) 2016 ��������Ƽ���������.Co.Ltd. All rights reserved.
 */
public class AsyncHttpClient {
	static private AsyncHttpClient httpClient = new AsyncHttpClient();

	static {

//		httpClient.setTimeout(60000); // 设置链接超时，如果不设置，默认为10s
	}

	public static AsyncHttpClient getClient() {
		return httpClient;
	}

	// 用一个完整url获取一个string对象
	public static RequestHandle get(String urlString, AsyncHttpResponseHandler res) {
		return httpClient.get(urlString, res);
	}

	// url里面带参数
	public static RequestHandle get(String urlString, RequestParams params, AsyncHttpResponseHandler res) {
		return httpClient.get(urlString, params, res);
	}

	//不带参数，获取json对象或者数组
	public static RequestHandle get(String urlString, JsonHttpResponseHandler res) {
		return httpClient.get(urlString, res);
	}

	// 带参数，获取json对象或者数组
	public static RequestHandle get(String urlString, RequestParams params, JsonHttpResponseHandler res) {
		return httpClient.get(urlString, params, res);
	}

	// 下载数据使用，会返回byte数据
	public static RequestHandle get(String uString, BinaryHttpResponseHandler bHandler) {
		return httpClient.get(uString, bHandler);
	}

	// 多个URL？
	public static RequestHandle get(String urlString, RequestParams params, BinaryHttpResponseHandler bHandler) {
		return httpClient.get(urlString, params, bHandler);
	}

	// post
	public static RequestHandle post(String urlString, RequestParams params, ResponseHandlerInterface bHandler) {
		return httpClient.post(urlString, params, bHandler);
	}

	public static void cancelRequest(RequestHandle handle) {
		if (handle != null) {
			if (!handle.isCancelled() && !handle.isFinished()) {
				handle.cancel(true);
			}
		}
	}

//	public static void cancelRequest(String requestTag) {
//		httpClient.cancelRequestsByTAG(requestTag, true);
//
//	}
//
//	public static void cancelRequest(Context context) {
//		httpClient.cancelRequests(context, true);
//
//	}
//
//	public static void cancel() {
//		httpClient.cancelAllRequests(true);
//	}
}
