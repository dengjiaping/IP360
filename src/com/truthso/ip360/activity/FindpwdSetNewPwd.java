package com.truthso.ip360.activity;

import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.MD5Util;

import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @despriction :找回密码->下一步->重置密码
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-25下午5:42:18
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class FindpwdSetNewPwd extends BaseActivity implements OnClickListener {
	private Button btn_next;
	private String phoneNum,cerCode,userPwd,userPwd1;
//	private TextView tv_phone;
	private EditText et_pwd,et_pwd1;
	private  boolean pwdIsEmpty,pwd1IsEmpty;
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		phoneNum = getIntent().getStringExtra("phoneNum");
		cerCode = getIntent().getStringExtra("cerCode");
//		tv_phone = (TextView) findViewById(R.id.tv_phone);
//		tv_phone.setText(phoneNum);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_pwd1 = (EditText) findViewById(R.id.et_pwd1);
		et_pwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!CheckUtil.isEmpty(s.toString().trim())){
					pwdIsEmpty = true;
				}else{
					pwdIsEmpty = false;
				}
				checkButtonStatus();
			}
		});
		et_pwd1.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!CheckUtil.isEmpty(s.toString().trim())){
					pwd1IsEmpty = true;
				}else{
					pwd1IsEmpty = false;
				}
				checkButtonStatus();
			}
		});
	}
	/**
	 * 按钮是否是彩色可点击
	 */
	private void checkButtonStatus() {
		if (pwdIsEmpty && pwd1IsEmpty) {
			btn_next.setEnabled(true);
			btn_next.setTextColor(Color.WHITE);
			btn_next.setBackgroundResource(R.drawable.round_corner_bg);
		} else {
			btn_next.setEnabled(false);
			btn_next.setBackgroundResource(R.drawable.round_corner_white);
			btn_next.setTextColor(getResources().getColor(R.color.huise));
		}
	}
	@Override
	public int setLayout() {
		return R.layout.activity_findpwd_setnewpwd;
	}

	@Override
	public String setTitle() {
		return "忘记密码";
	}

	@Override
	public void onClick(View v) {
		userPwd = et_pwd.getText().toString().trim();
		userPwd1 = et_pwd1.getText().toString().trim();
		if (CheckUtil.isEmpty(userPwd)||CheckUtil.isEmpty(userPwd1)) {
			Toaster.showToast(FindpwdSetNewPwd.this, "输入不能为空");
		}else if(!CheckUtil.isPassWordValidate(userPwd)||!CheckUtil.isPassWordValidate(userPwd1)){
			Toaster.showToast(this, "请输入6~18位字母与数字组成的密码");
		}else if(!userPwd.equals(userPwd1)){
			Toaster.showToast(this, "两次输入的密码不一致");
		}else{
			ResetPwd();
		}
		
		
//		Intent intent = new Intent(this,MainActivity.class);
//		startActivity(intent);
//		finish();
	}
   //重置密码的接口
	private void ResetPwd() {
		showProgress("正在重置...");
		userPwd=MD5Util.encoder(userPwd);
		ApiManager.getInstance().ResetPwd(phoneNum, userPwd, cerCode, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Toaster.showToast(FindpwdSetNewPwd.this,"重置成功");
						Intent intent = new Intent(FindpwdSetNewPwd.this,LoginActivity.class);
						startActivity(intent);
						finish();
						
					}else{
						Toaster.showToast(FindpwdSetNewPwd.this,response.getMsg());
					}
				}else{
					Toaster.showToast(FindpwdSetNewPwd.this,"重置失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				hideProgress();
				Toaster.showToast(FindpwdSetNewPwd.this,"网络链接超时，请重试！");
			}
		});
		
	
		
	}

}
