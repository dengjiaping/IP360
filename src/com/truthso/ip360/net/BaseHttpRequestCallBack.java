package com.truthso.ip360.net;

import com.loopj.android.http.RequestHandle;

/**
 * 
 * @author wsx_summer
 * @date ����ʱ�䣺2016��5��26������2:32:45
 * @version 1.0  
 * @Copyright (c) 2016 ��������Ƽ���������.Co.Ltd. All rights reserved.
 */
public interface BaseHttpRequestCallBack {
	public void onRequestResult(RequestHandle requestHandle, int errorCode,
			String message,BaseHttpResponse response);
}
