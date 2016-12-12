package com.truthso.ip360.bean;

import com.truthso.ip360.net.BaseHttpResponse;

/**
 * @despriction :获取业务计费状态
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-20上午11:21:59
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class AccountStatusBean extends BaseHttpResponse {
	//	status	能否使用	Integer	0-不能使用；1-可以使用。	非空
//	accounBalance	账户余额	Integer	C类用户的余额。单位分。B类用户为0	非空	
//	unit	数量单位	String(10)	业务量的单位。如次、分钟、周。	非空	
//	count	当次消费数量	Integer	B/C类用户均使用。当前消费量。根据输入参数的count是否大于0确定。	非空	
//	freeCount	赠送量	Integer	赠送量。大于等于0。	非空	
//	usedCount	累计使用量	Integer	累计使用量。大于等于0。	非空	
//	balanceCount	剩余量	Integer	freeCount-usedCount。负值时为透支。	非空	
//	limitCount	透支阀值	Integer	透支最大量。超过会停服务。	可空	
	private AccountStatus datas;

	public AccountStatus getDatas() {
		return datas;
	}

	public void setDatas(AccountStatus datas) {
		this.datas = datas;
	}

	public class AccountStatus {
		private int status;
		private String showText;
		public String getShowText() {
			return showText;
		}

		public void setShowText(String showText) {
			this.showText = showText;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

	}
}