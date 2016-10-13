package com.truthso.ip360.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.bean.LoginBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.MD5Util;
import com.truthso.ip360.utils.SharePreferenceUtil;


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
		String userAccount = (String) SharePreferenceUtil.getAttributeByKey(this, MyConstants.SP_USER_KEY, "userAccount", SharePreferenceUtil.VALUE_IS_STRING);
		if(!CheckUtil.isEmpty(userAccount)){
			et_useraccount.setText(userAccount);
		}
		
		
		et_userpwd = (EditText) findViewById(R.id.et_userpwd);			
		String pwd = (String) SharePreferenceUtil.getAttributeByKey(this, MyConstants.SP_USER_KEY, "userPwd", SharePreferenceUtil.VALUE_IS_STRING);
		if(!CheckUtil.isEmpty(pwd)){			
			et_userpwd.setText(pwd);
		}
		
		
		cb_checkbox = (CheckBox) findViewById(R.id.cb_checkbox);
		cb_checkbox.setChecked(true);
		
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
//			finish();
			break;
		case R.id.tv_forget_pwd:// 忘记密码
			Intent intent1 = new Intent(this,findPwdActivity.class);
			startActivity(intent1);
			break;
		case R.id.btn_loginin:// 登录

			userAccount = et_useraccount.getText().toString().trim();
			userPwd = et_userpwd.getText().toString().trim();
			
			if (CheckUtil.isEmpty(userAccount)||CheckUtil.isEmpty(userPwd)) {
				Toaster.showToast(this, "用户名或密码不能为空");
			}else{
				String phoneReg="^1\\d{10}";
				if(!userAccount.matches(phoneReg)){
					Toaster.showToast(this, "请输入正确的手机号");
				}else{
					Login();
				}				
			}
			
			break;

		}
	}
	public  void Login(){
		String encordPwd=MD5Util.encoder(userPwd);
		showProgress("正在登录...");
		ApiManager.getInstance().doLogin(userAccount, encordPwd, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
                hideProgress();
				LoginBean bean = (LoginBean) response;
				if(!CheckUtil.isEmpty(bean)){
					if(bean.getCode()==200){
						//登录成功
						String token = bean.getDatas().getToken();//登录标识
						//保存登录的token
						SharePreferenceUtil.saveOrUpdateAttribute(LoginActivity.this, MyConstants.SP_USER_KEY, "token", token);
						
						int userType = bean.getDatas().getUserType();//用户类型1-付费用户（C）；2-合同用户（B）
						//保存用户类型
						SharePreferenceUtil.saveOrUpdateAttribute(LoginActivity.this, MyConstants.SP_USER_KEY, "userType", userType);
						
						int accountType = bean.getDatas().getAccountType();//1 个人 2 企业
						//保存用户是企业用户还是个人用户
						SharePreferenceUtil.saveOrUpdateAttribute(LoginActivity.this, MyConstants.SP_USER_KEY, "accountType", accountType);
						
						//保存帐号
						SharePreferenceUtil.saveOrUpdateAttribute(LoginActivity.this, MyConstants.SP_USER_KEY, "userAccount", userAccount);
						
						//判断是否保存帐号密码
						savePwd();
						Toaster.showToast(LoginActivity.this, bean.getMsg());
						//跳转到主页面
						Intent intent2 = new Intent(LoginActivity.this,MainActivity.class);
						startActivity(intent2);
						finish();
						
					}else{
						Toaster.showToast(LoginActivity.this, bean.getMsg());
					}					
				}else{
					Toaster.showToast(LoginActivity.this, "登录失败");
				}					
			}
		});
		
		
	}

	private void savePwd() {
		if(cb_checkbox.isChecked()){
			SharePreferenceUtil.saveOrUpdateAttribute(LoginActivity.this, MyConstants.SP_USER_KEY, "userPwd", userPwd);
		}else{
			SharePreferenceUtil.saveOrUpdateAttribute(LoginActivity.this, MyConstants.SP_USER_KEY, "userPwd", null);
		}
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
