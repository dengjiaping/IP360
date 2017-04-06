package com.truthso.ip360.activity;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.fragment.PersonalCenter;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @despriction :更改绑定的邮箱->下一步——>绑定新邮箱
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-9上午11:07:06
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class ReBindEmailBindNewActivity extends BaseActivity implements OnClickListener{
	private String newCerCode,email;
	private Button btn_bind, btn_send_code;
	private EditText et_account, et_cercode;
	private boolean isAccountEmpty,iscercodeIsEmpty;
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
	@Override
	public void initData() {
		
	}

	@Override
	public void initView() {
//		oldCerCode = getIntent().getStringExtra("cerCode");
		btn_bind = (Button) findViewById(R.id.btn_bind);
		btn_bind.setOnClickListener(this);
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_send_code.setOnClickListener(this);
		et_account = (EditText) findViewById(R.id.et_account);
		et_cercode = (EditText) findViewById(R.id.et_cercode);

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
				if(CheckUtil.isEmailFormat(s.toString().trim())){
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
		et_cercode.addTextChangedListener(new TextWatcher() {


			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (!CheckUtil.isEmpty(s.toString().trim())&&s.length()>3) {
					iscercodeIsEmpty = true;
				} else {
					iscercodeIsEmpty = false;
				}
				checkButtonStatus();
			}
		});
	}
	/**
	 * 按钮是否是彩色可点击
	 */
	private void checkButtonStatus() {
		if (isAccountEmpty && iscercodeIsEmpty) {
			btn_bind.setEnabled(true);
			btn_bind.setTextColor(Color.WHITE);
			btn_bind.setBackgroundResource(R.drawable.round_corner_bg);
		} else {
			btn_bind.setEnabled(false);
			btn_bind.setBackgroundResource(R.drawable.round_corner_white);
			btn_bind.setTextColor(getResources().getColor(R.color.huise));
		}
	}


	@Override
	public int setLayout() {
		return R.layout.activity_rebind_email2;
	}

	@Override
	public String setTitle() {
		return "输入新邮箱";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bind:// 确认绑定
			email  = et_account.getText().toString().trim();
			newCerCode = et_cercode.getText().toString().trim();
			if (CheckUtil.isEmpty(email)||CheckUtil.isEmpty(newCerCode)) {
				Toaster.showToast(this, "邮箱或验证码不能为空");
			}else{
				String phoneReg="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
				if(!email.matches(phoneReg)){
					Toaster.showToast(this, "请输入正确的邮箱");
				}else{
					bind();
				}				
			}
			break;
		case R.id.btn_send_code:
			
			email = et_account.getText().toString().trim();
			if (CheckUtil.isEmpty(email)) {
				Toaster.showToast(this, "邮箱不能为空");
			}else{
				String phoneReg="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
				if(!email.matches(phoneReg)){
					Toaster.showToast(this, "请输入正确的邮箱");
				}else{
				
				sendVerCode();
				}				
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 确认绑定
	 */
	private void bind() {
		showProgress("正在绑定...");
		ApiManager.getInstance().BindNewEmail(email, newCerCode, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,BaseHttpResponse response) {
				hideProgress();
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Toaster.showToast(ReBindEmailBindNewActivity.this,response.getMsg());
						setResult(1);
						finish();
						
					}else{
						Toaster.showToast(ReBindEmailBindNewActivity.this,response.getMsg());
					}
				}else{
					Toaster.showToast(ReBindEmailBindNewActivity.this,"绑定失败");
				}				
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}

		});
		
	}
	/**
	 * 发送验证码
	 */
	private void sendVerCode() {
		//开始倒计时
		btn_send_code.setEnabled(false);
		timer.start();
		ApiManager.getInstance().getVerCode(MyConstants.BINDNEW_EMAIL, email, null, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode()==200) {

					}else{
						btn_send_code.setEnabled(true);
						timer.cancel();
						btn_send_code.setText("发送验证码");
						Toaster.showToast(ReBindEmailBindNewActivity.this,response.getMsg());
					}
				}else{
					Toaster.showToast(ReBindEmailBindNewActivity.this,"获取失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
		
		
	}

