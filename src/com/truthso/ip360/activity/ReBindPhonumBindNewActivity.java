package com.truthso.ip360.activity;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @despriction :更改绑定的手机号->下一步->绑定新的手机号
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-9上午10:56:56
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class ReBindPhonumBindNewActivity extends BaseActivity implements OnClickListener{
	private String oldCerCode,newCerCode,phoneNum;
	private Button btn_bind, btn_send_code;
	private EditText et_account, et_cercode;
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
		oldCerCode = getIntent().getStringExtra("cerCode");
		btn_bind = (Button) findViewById(R.id.btn_bind);
		btn_bind.setOnClickListener(this);
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_send_code.setOnClickListener(this);
		et_account = (EditText) findViewById(R.id.et_account);
		et_cercode = (EditText) findViewById(R.id.et_cercode);
	}

	@Override
	public int setLayout() {
		return R.layout.activity_rebind_phonum2;
	}

	@Override
	public String setTitle() {
		return "输入新手机号";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bind:// 确认绑定
			phoneNum = et_account.getText().toString().trim();
			newCerCode = et_cercode.getText().toString().trim();
			if (CheckUtil.isEmpty(phoneNum)||CheckUtil.isEmpty(newCerCode)) {
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
		showProgress();
		ApiManager.getInstance().BindNewPhonum(phoneNum, oldCerCode,newCerCode, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();

				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Toaster.showToast(ReBindPhonumBindNewActivity.this,response.getMsg());
						Intent intent = new Intent(ReBindPhonumBindNewActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
						
					}else{
						Toaster.showToast(ReBindPhonumBindNewActivity.this,response.getMsg());
					}
				}else{
					Toaster.showToast(ReBindPhonumBindNewActivity.this,"绑定失败");
				}
			
				
			}
		});
	}

	/**
	 * 发送验证码
	 */
	private void sendVerCode() {
		showProgress();
		ApiManager.getInstance().getVerCode(MyConstants.BINDNEW_PHONUM, phoneNum, null, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						hideProgress();
						//开始倒计时
						btn_send_code.setEnabled(false);
						timer.start();
					}else{
						Toaster.showToast(ReBindPhonumBindNewActivity.this,response.getMsg());
					}
				}else{
					Toaster.showToast(ReBindPhonumBindNewActivity.this,"获取失败");
				}
			}
		});
		
	}
		
	}

