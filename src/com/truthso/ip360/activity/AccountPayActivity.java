package com.truthso.ip360.activity;

import com.alibaba.sdk.android.mns.model.Message;
import com.alipay.sdk.app.PayTask;
/**
 * @despriction :个人中心->账户充值
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午8:22:12
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class AccountPayActivity extends BaseActivity {
private String accountBalance;//账户余额

	@Override
	public void initData() {
		accountBalance = getIntent().getStringExtra("accountBalance");
	}
	@Override
	public void initView() {
	}
	@Override
	public int setLayout() {
		return R.layout.activity_account_pay;
	}

	@Override
	public String setTitle() {
		return "账户充值";
	}

}
