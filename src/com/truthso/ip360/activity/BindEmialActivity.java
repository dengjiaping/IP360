package com.truthso.ip360.activity;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

import android.app.Activity;
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
 * @despriction :个人中心->绑定邮箱
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午8:30:23
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class BindEmialActivity extends BaseActivity implements OnClickListener {
	private Button btn_bind, btn_send_code;
	private EditText et_account, et_cercode;
	private String bindEmial, cerCode;
	private boolean isAccountEmpty,isPasswordEmpty;
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
					isPasswordEmpty = true;
				} else {
					isPasswordEmpty = false;
				}
				checkButtonStatus();
			}
		});
	}
	/**
	 * 按钮是否是彩色可点击
	 */
	private void checkButtonStatus() {
		if (isAccountEmpty && isPasswordEmpty) {
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
		return R.layout.activity_bind_email;
	}

	@Override
	public String setTitle() {
		return "绑定邮箱";
	}

@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.btn_bind:// 确认绑定
		bindEmial = et_account.getText().toString().trim();
		cerCode = et_cercode.getText().toString().trim();
		if (CheckUtil.isEmpty(bindEmial)||CheckUtil.isEmpty(cerCode)) {
			Toaster.showToast(this, "邮箱或验证码不能为空");
		}else{
//			String phoneReg="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			String phoneReg = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
			if(!bindEmial.matches(phoneReg)){
				Toaster.showToast(this, "请输入正确的邮箱");
			}else{
				bind();
			}				
		}
		break;
	case R.id.btn_send_code:
		
		bindEmial = et_account.getText().toString().trim();
		if (CheckUtil.isEmpty(bindEmial)) {
			Toaster.showToast(this, "邮箱不能为空");
		}else{
//			String phoneReg="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			String phoneReg = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
			if(!bindEmial.matches(phoneReg)){
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
	ApiManager.getInstance().BindEmail(bindEmial, cerCode, new ApiCallback() {
		
		@Override
		public void onApiResult(int errorCode, String message,
				BaseHttpResponse response) {
			hideProgress();

			if (!CheckUtil.isEmpty(response)) {
				if (response.getCode() == 200) {
//					Toaster.showToast(BindEmialActivity.this,response.getMsg());
					setResult(MyConstants.BINDNEWEMAIL);
					finish();
					
				}else{
					Toaster.showToast(BindEmialActivity.this,response.getMsg());
				}
			}else{
				Toaster.showToast(BindEmialActivity.this,"绑定失败");
			}
		
			
		}

		@Override
		public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			hideProgress();
			Toaster.showToast(BindEmialActivity.this,"网络链接超时，请重试！");
			
		}
	});
}
/**
 * 发送验证码
 */

private void sendVerCode() {
	btn_send_code.setEnabled(false);
	timer.start();
	ApiManager.getInstance().getVerCode(MyConstants.BINDEMAIL, bindEmial, null, new ApiCallback() {
		
		@Override
		public void onApiResult(int errorCode, String message,
				BaseHttpResponse response) {
			
			if (!CheckUtil.isEmpty(response)) {
				if (response.getCode()==200) {

				}else{
					btn_send_code.setEnabled(true);
					timer.cancel();
					btn_send_code.setText("发送验证码");
					Toaster.showToast(BindEmialActivity.this,response.getMsg());
				}
			
			}else{
				Toaster.showToast(BindEmialActivity.this,"获取失败");
			}
		}

		@Override
		public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

		}
	});
	

	
}
}
