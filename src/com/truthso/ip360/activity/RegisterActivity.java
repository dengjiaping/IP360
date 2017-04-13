package com.truthso.ip360.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.MD5Util;

import cz.msebera.android.httpclient.Header;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.0
 * @despriction :注册页面
 * @date 创建时间：2016-7-25下午4:15:40
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private Button btn_regist, btn_send_code;
	private EditText et_account, et_cercode;
	private String phoneNum, cerCode, userPwd;
	private EditText et_userpwd;
	private CheckBox cb_password, cb_checkbox;
	private TextView tv_yonghuxieyi;
	private boolean isAccountEmpty, isVcodeEmpty, isPasswordEmpty, isAgree;
	// 倒计时
	private CountDownTimer timer = new CountDownTimer(60000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			btn_send_code.setText(millisUntilFinished / 1000 + "秒");
		}

		@Override
		public void onFinish() {
			btn_send_code.setText("获取验证码");
			btn_send_code.setEnabled(true);
		}
	};
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */

	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		cb_checkbox = (CheckBox) findViewById(R.id.cb_checkbox);
		cb_checkbox.setChecked(true);//服务协议默认选中
		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_regist.setOnClickListener(this);
		et_account = (EditText) findViewById(R.id.et_account);
		et_cercode = (EditText) findViewById(R.id.et_cercode);
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_send_code.setOnClickListener(this);
		tv_yonghuxieyi = (TextView) findViewById(R.id.tv_yonghuxieyi);
		tv_yonghuxieyi.setOnClickListener(this);
		/*if (!CheckUtil.isEmpty(et_account.getText().toString().trim())){//输入手机号后，获取验证码的按钮才可点击
			btn_send_code.setClickable(true);
			btn_send_code.setTextColor(getResources().getColor(R.color.white));
			btn_send_code.setBackgroundColor(Color.parseColor(R.color.jiuhong+""));
		}else{
			btn_send_code.setClickable(false);
			btn_send_code.setBackgroundColor(Color.parseColor(R.color.white+""));
			btn_send_code.setTextColor(getResources().getColor(R.color.black));
		}*/

		et_userpwd = (EditText) findViewById(R.id.et_userpwd);

		cb_password = (CheckBox) findViewById(R.id.cb_password);
		cb_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//密码显示与隐藏
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					//如果选中，显示密码
					et_userpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					//否则隐藏密码
					et_userpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		if (btn_regist.getLinksClickable()) {//註冊按鈕是否可点击
//			btn_regist.setClickable(false);
			btn_regist.setBackgroundColor(getResources().getColor(R.color.white));
			btn_regist.setTextColor(getResources().getColor(R.color.gray));
		} else {
//			btn_regist.setClickable(true);
			btn_regist.setTextColor(getResources().getColor(R.color.white));
			btn_regist.setBackgroundColor(Color.parseColor(R.color.jiuhong + ""));
		}

		//监听编辑框
		et_account.addTextChangedListener(new TextWatcher() {


			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length()>10){

					btn_send_code.setClickable(true);
					btn_send_code.setTextColor(getResources().getColor(R.color.white));
					btn_send_code.setBackgroundResource(R.drawable.round_corner_bg);
				}else{
					btn_send_code.setClickable(false);
					btn_send_code.setBackgroundResource(R.drawable.round_corner_huise);
					btn_send_code.setTextColor(getResources().getColor(R.color.white));
				}

				if (!CheckUtil.isEmpty(s.toString().trim())) {
					isAccountEmpty = true;
				} else {
					isAccountEmpty = false;
				}
				checkButtonStatus();
			}
		});
		//监听编辑框
		et_userpwd.addTextChangedListener(new TextWatcher() {


			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!CheckUtil.isEmpty(s.toString().trim())) {
					isPasswordEmpty = true;
				} else {
					isPasswordEmpty = false;
				}
				checkButtonStatus();
			}
		});
		//监听编辑框
		et_cercode.addTextChangedListener(new TextWatcher() {


			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!CheckUtil.isEmpty(s.toString().trim())) {
					isVcodeEmpty = true;
				} else {
					isVcodeEmpty = false;
				}
				checkButtonStatus();
			}
		});

		cb_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checkButtonStatus();
			}
		});

	}

	/**
	 * 按钮是否是彩色可点击
	 */
	private void checkButtonStatus() {
		if (isAccountEmpty && isVcodeEmpty && isPasswordEmpty && cb_checkbox.isChecked()) {
			btn_regist.setEnabled(true);
			btn_regist.setTextColor(Color.WHITE);
			btn_regist.setBackgroundResource(R.drawable.round_corner_bg);
		} else {
			btn_regist.setEnabled(false);
			btn_regist.setBackgroundResource(R.drawable.round_corner_white);
			btn_regist.setTextColor(getResources().getColor(R.color.huise));
		}
	}

	@Override
	public int setLayout() {
		return R.layout.activity_register;
	}

	@Override
	public String setTitle() {
		return "注册";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_regist:// 注册
				phoneNum = et_account.getText().toString().trim();
				cerCode = et_cercode.getText().toString().trim();
				userPwd = et_userpwd.getText().toString().trim();
				if (CheckUtil.isEmpty(phoneNum) || CheckUtil.isEmpty(cerCode)) {
					Toaster.showToast(this, "手机号或验证码不能为空");
				} else if (!CheckUtil.isPassWordValidate(userPwd)) {
					Toaster.showToast(this, "请输入6~18位字母与数字组成的密码");
				} else {
					String phoneReg = "1[3|5|7|8]\\d{9}$";
					if (!phoneNum.matches(phoneReg)) {
						Toaster.showToast(this, "请输入正确的手机号");
					} else {
						regist();
					}
				}

				break;
			case R.id.tv_yonghuxieyi://用户协议
				Intent intent = new Intent(RegisterActivity.this, UserAgreementActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_send_code:

				phoneNum = et_account.getText().toString().trim();
				if (CheckUtil.isEmpty(phoneNum)) {
					Toaster.showToast(this, "手机号不能为空");
				} else {
					String phoneReg = "1[3|5|7|8]\\d{9}$";
					if (!phoneNum.matches(phoneReg)) {
						Toaster.showToast(RegisterActivity.this, "请输入正确的手机号");
					} else {

						sendVerCode();
					}
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 发送验证码
	 */
	private void sendVerCode() {
		//开始倒计时
		btn_send_code.setEnabled(false);
		timer.start();
		ApiManager.getInstance().getRegVerCode(MyConstants.REGISTER, phoneNum, null, new ApiCallback() {

			@Override
			public void onApiResult(int errorCode, String message,
									BaseHttpResponse response) {

				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {

					} else {
						btn_send_code.setEnabled(true);
						timer.cancel();
						btn_send_code.setText("发送验证码");
						Toaster.showToast(RegisterActivity.this, response.getMsg());
					}
				} else {
					Toaster.showToast(RegisterActivity.this, "获取失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});

	}

	private void regist() {
		userPwd = MD5Util.encoder(userPwd);
		ApiManager.getInstance().registUser(phoneNum, userPwd, cerCode, new ApiCallback() {

			@Override
			public void onApiResult(int errorCode, String message,
									BaseHttpResponse response) {
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();

					} else {
						Toaster.showToast(RegisterActivity.this, response.getMsg());
					}
				} else {
					Toaster.showToast(RegisterActivity.this, "注册失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});

	}
}
