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
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
/**
 * @despriction :个人中心->绑定手机号
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午8:29:01
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class BindPhoNumActivity extends BaseActivity implements OnClickListener {
	private Button btn_bind, btn_send_code;
	private EditText et_account, et_cercode;
	private String phoneNum, cerCode;
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
	}

	@Override
	public int setLayout() {
		return R.layout.activity_bind_phonum;
	}

	@Override
	public String setTitle() {
		return "绑定手机";
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_bind:// 确认绑定
			phoneNum = et_account.getText().toString().trim();
			cerCode = et_cercode.getText().toString().trim();
			if (CheckUtil.isEmpty(phoneNum)||CheckUtil.isEmpty(cerCode)) {
				Toaster.showToast(this, "手机号或验证码不能为空");
			}else{
				String phoneReg="^1\\d{10}";
				if(!phoneNum.matches(phoneReg)){
					Toaster.showToast(this, "请输入正确的手机号");
				}else{
					bind();
				}				
			}
			break;
		case R.id.btn_send_code:
			
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
	 * 确认绑定
	 */
	private void bind() {
		showProgress("正在绑定...");
		ApiManager.getInstance().BindPhonum(phoneNum, cerCode, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();

				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Toaster.showToast(BindPhoNumActivity.this,response.getMsg());
						setResult(MyConstants.BINDNEWEMOBILE);
						finish();
						
					}else{
						Toaster.showToast(BindPhoNumActivity.this,response.getMsg());
					}
				}else{
					Toaster.showToast(BindPhoNumActivity.this,"绑定失败");
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
		btn_send_code.setEnabled(false);
		timer.start();
		ApiManager.getInstance().getVerCode(MyConstants.BIND_PHONUM, phoneNum, null, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						
						Toaster.showToast(BindPhoNumActivity.this,response.getMsg());
					}
				}else{
					Toaster.showToast(BindPhoNumActivity.this,"获取失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
