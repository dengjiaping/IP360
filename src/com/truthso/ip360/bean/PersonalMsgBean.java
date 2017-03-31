package com.truthso.ip360.bean;

import java.util.List;

import com.truthso.ip360.net.BaseHttpResponse;

/**
 * @despriction :获取个人概要信息的bean
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-9下午5:36:36
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class PersonalMsgBean extends BaseHttpResponse{
	private PersonalMsg datas;
	
	public PersonalMsg getDatas() {
		return datas;
	}
	public void setDatas(PersonalMsg datas) {
		this.datas = datas;
	}
	public class PersonalMsg{
//		private int userType;//用户类型 1-付费用户（C）；2-合同用户（B）
		private String bindedMobile;//已绑定的手机号码（未绑定时为空字符串）
		private String bindedEmail;//已绑定的邮箱（未绑定时为空字符串）
		private int realNameState;//实名认证的状态 1-未认证，2-已认证
		private int accountBalance;//账户余额
		private List<product> productBalance;//业务余量  B类用户非空。C类用户无
		private int accountType;//账户种类 1 个人 2 企业
		private String realName;//已进行实名认证的真实姓名
		private String cardId;//已进行实名认证的 身份证号
		private List<GiftsProduct> giftsProduct;//赠送量
		public String getRealName() {
			return realName;
		}

		public void setRealName(String realName) {
			this.realName = realName;
		}

		public String getCardId() {
			return cardId;
		}

		public void setCardId(String cardId) {
			this.cardId = cardId;
		}

		public int getAccountType() {
			return accountType;
		}
		public void setAccountType(int accountType) {
			this.accountType = accountType;
		}

		public String getBindedMobile() {
			return bindedMobile;
		}
		public void setBindedMobile(String bindedMobile) {
			this.bindedMobile = bindedMobile;
		}
		public String getBindedEmail() {
			return bindedEmail;
		}
		public void setBindedEmail(String bindedEmail) {
			this.bindedEmail = bindedEmail;
		}
		public int getRealNameState() {
			return realNameState;
		}
		public void setRealNameState(int realNameState) {
			this.realNameState = realNameState;
		}
		public int getAccountBalance() {
			return accountBalance;
		}
		public void setAccountBalance(int accountBalance) {
			this.accountBalance = accountBalance;
		}
		public List<product> getProductBalance() {
			return productBalance;
		}
		public void setProductBalance(List<product> productBalance) {
			this.productBalance = productBalance;
		}
		
	}
	

}
