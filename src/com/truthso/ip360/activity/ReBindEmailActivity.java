package com.truthso.ip360.activity;


import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :更改绑定的邮箱
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-9上午11:06:53
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class ReBindEmailActivity extends BaseActivity implements OnClickListener{
	private Button btn_next, btn_send_code;
	private EditText et_cercode;
	private TextView tv_binded_mobile;
	private String cerCode;
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
	private String bindedEmail;
	@Override
	public void initData() {
		
	}

	@Override
	public void initView() {
		bindedEmail = getIntent().getStringExtra("bindedEmail");
		tv_binded_mobile = (TextView) findViewById(R.id.tv_binded_mobile);
		tv_binded_mobile.setText(bindedEmail);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		et_cercode = (EditText) findViewById(R.id.et_cercode);
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_send_code.setOnClickListener(this);
			
	}

	@Override
	public int setLayout() {
		return R.layout.activity_revbind_email;
	}

	@Override
	public String setTitle() {
		return "更改绑定邮箱";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:// 下一步
			cerCode = et_cercode.getText().toString().trim();
			if (CheckUtil.isEmpty(cerCode)) {
				Toaster.showToast(this, "验证码不能为空");
			}else{
				//验证码校验
				CaptchaValidation();


			}
			break;
		case R.id.btn_send_code:
			sendVerCode();
			
		default:
			break;
		}
	}
	/**
	 * 验证码校验
	 */
	private void CaptchaValidation() {
		showProgress("");
		ApiManager.getInstance().getCapVerCode(MyConstants.OFFBIND_EMAIL, bindedEmail, cerCode, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						
//						Toaster.showToast(ReBindEmailActivity.this, response.getMsg());
						
						Intent intent = new Intent(ReBindEmailActivity.this, ReBindEmailBindNewActivity.class);
						startActivityForResult(intent, MyConstants.OFFBIND_BINDNEWEMAIL);
						setResult(MyConstants.OFFBIND_BINDNEWEMAIL);
						finish();
					}else{
						Toaster.showToast(ReBindEmailActivity.this, response.getMsg());
					}
				}else{
					Toaster.showToast(ReBindEmailActivity.this, "验证错误");
					
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
		ApiManager.getInstance().getVerCode(MyConstants.OFFBIND_EMAIL, null, null, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
			
				if (!CheckUtil.isEmpty(response)) {
				if (response.getCode()==200) {

				}else{
					btn_send_code.setEnabled(true);
					timer.cancel();
					btn_send_code.setText("发送验证码");
					Toaster.showToast(ReBindEmailActivity.this,response.getMsg());
				}
				}else{
					Toaster.showToast(ReBindEmailActivity.this,"获取失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

}
