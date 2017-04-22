package com.truthso.ip360.activity;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

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
 * @despriction :找回密码
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-25下午5:28:46
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class findPwdActivity extends BaseActivity implements OnClickListener {
	private Button btn_next, btn_send_code;
	private EditText et_account, et_cercode;
	private String phoneNum, cerCode;
	private boolean accountIsEmpty,cercodeIsEmpty;
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
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_send_code.setOnClickListener(this);


		et_account = (EditText) findViewById(R.id.et_account);
		et_cercode = (EditText) findViewById(R.id.et_cercode);
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

				if (!CheckUtil.isEmpty(s.toString().trim())){
					accountIsEmpty = true;
				}else{
					accountIsEmpty = false;
				}
				checkButtonStatus();
			}
		});
		et_cercode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
					if (!CheckUtil.isEmpty(s.toString().trim())){
						cercodeIsEmpty = true;
					}else{
						cercodeIsEmpty = false;
					}
				checkButtonStatus();
			}
		});

	}
	/**
	 * 按钮是否是彩色可点击
	 */
	private void checkButtonStatus() {
		if (accountIsEmpty && cercodeIsEmpty) {
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
		return R.layout.activity_findpwd;
	}

	@Override
	public String setTitle() {
		return "找回密码";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:// 下一步
			phoneNum = et_account.getText().toString().trim();
			cerCode = et_cercode.getText().toString().trim();
			if (CheckUtil.isEmpty(phoneNum)||CheckUtil.isEmpty(cerCode)) {
				Toaster.showToast(this, "手机号或验证码不能为空");
			}else{
				String phoneReg="^1\\d{10}";
				if(!phoneNum.matches(phoneReg)){
					Toaster.showToast(this, "请输入正确的手机号");
				}else{
					Intent intent = new Intent(this, FindpwdSetNewPwd.class);
					intent.putExtra("phoneNum", phoneNum);
					intent.putExtra("cerCode", cerCode);
					startActivity(intent);
					finish();
				}				
			}
			break;
		case R.id.btn_send_code:// 发送验证码
			
			phoneNum = et_account.getText().toString().trim();
			if (CheckUtil.isEmpty(phoneNum)) {
				Toaster.showToast(this, "手机号不能为空");
			}else{
				String phoneReg="^1\\d{10}";
				if(!phoneNum.matches(phoneReg)){
					Toaster.showToast(this, "请输入正确的手机号");
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
	 * 发送验证码
	 */
	private void sendVerCode() {
		btn_send_code.setEnabled(false);
		timer.start();
		ApiManager.getInstance().getRegVerCode(MyConstants.FIND_PWD, phoneNum, null, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {

				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode()==200) {

					}else{
						btn_send_code.setEnabled(true);
						timer.cancel();
						btn_send_code.setText("发送验证码");
						Toaster.showToast(findPwdActivity.this,response.getMsg());
					}

				}else{
					Toaster.showToast(findPwdActivity.this,"获取失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
		
	
		
	}

}
