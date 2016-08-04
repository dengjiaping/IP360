package com.truthso.ip360.net;

import com.loopj.android.http.RequestHandle;

/**
 * 
 * @author wsx_summer
 * @date 创建时间：2016年5月26日下午2:32:45
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public interface BaseHttpRequestCallBack {
	public void onRequestResult(RequestHandle requestHandle, int errorCode,
			String message,BaseHttpResponse response);
}
