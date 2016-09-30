package com.truthso.ip360.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.truthso.ip360.bean.LoginBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.MD5Util;
import com.truthso.ip360.utils.SharePreferenceUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

/**
 * 登录界面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-21下午2:56:58
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class LoginActivity extends BaseActivity implements OnClickListener {
	private TextView tv_register;
	private TextView tv_forget_pwd;
	private Button btn_loginin,btn_title_left;
	private String userAccount,userPwd;
	private CheckBox cb_checkbox;
	private EditText et_useraccount,et_userpwd;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	initView();

}

	public void initView() {
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setVisibility(View.INVISIBLE);
		
		
		et_useraccount = (EditText) findViewById(R.id.et_useraccount);
		userAccount = et_useraccount.getText().toString().trim();
		
		
		et_userpwd = (EditText) findViewById(R.id.et_userpwd);
		userPwd = et_userpwd.getText().toString().trim();
		
		
		cb_checkbox = (CheckBox) findViewById(R.id.cb_checkbox);
		
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_register.setOnClickListener(this);

		tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
		tv_forget_pwd.setOnClickListener(this);
		
		btn_loginin = (Button) findViewById(R.id.btn_loginin);
		btn_loginin.setOnClickListener(this);

	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_register:// 注册
			Intent intent = new Intent(this,RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_forget_pwd:// 忘记密码
			Intent intent1 = new Intent(this,findPwdActivity.class);
			startActivity(intent1);
			break;
		case R.id.btn_loginin:// 登录
			if (CheckUtil.isEmpty("userAccount")||CheckUtil.isEmpty("userPwd")) {
				Toaster.showToast(this, "用户名或密码不能为空");
			}else{
				Login();
//				Intent intent2 = new Intent(this,MainActivity.class);
//				startActivity(intent2);
				finish();
			}
			
			
			break;

		}
	}
	public  void Login(){
		userPwd=MD5Util.encoder(userPwd);
		ApiManager.getInstance().doLogin(userAccount, userPwd, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				LogUtils.e(errorCode+"错误码啊啊啊啊啊啊啊啊啊");
				LoginBean bean = (LoginBean) response;
				if (bean.getCode() == 200) {
					String token = bean.getToken();//登录标识
					LogUtils.e("接口痛不痛"+token);
					//保存登录的token
					SharePreferenceUtil.saveOrUpdateAttribute(LoginActivity.this, MyConstants.SP＿TOKEN, "token", token);
					int userType = bean.getUserType();//用户类型1-付费用户（C）；2-合同用户（B）
					LogUtils.e("接口痛不痛"+userType);
					//保存用户类型
					SharePreferenceUtil.saveOrUpdateAttribute(LoginActivity.this, MyConstants.SP_USER_TYPE, "userType", userType);
				}
				
				
			}
		});
		
		
	}

	@Override
	public void initData() {
		
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	@Override
	public String setTitle() {
		return "用户登录";
	}

}
