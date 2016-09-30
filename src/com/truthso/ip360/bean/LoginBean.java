package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;
/**
 * @despriction :登录返回的jsonBean
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-9-30上午11:47:34
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class LoginBean extends BaseHttpResponse{
	private int userType;
	private String token;
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
