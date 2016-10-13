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
	
	private Login datas;

	public Login getDatas() {
		return datas;
	}

	public void setDatas(Login datas) {
		this.datas = datas;
	}
	
	public class Login{
		private int userType;//1-付费用户（C）；2-合同用户（B）
		private String token;
		private int accountType;//1 个人 2 企业
		
		public int getAccountType() {
			return accountType;
		}
		public void setAccountType(int accountType) {
			this.accountType = accountType;
		}
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
}
