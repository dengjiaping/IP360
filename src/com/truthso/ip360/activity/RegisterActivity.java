package com.truthso.ip360.activity;

import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @despriction :注册页面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-25下午4:15:40
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private Button btn_next, btn_send_code;
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
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_send_code.setOnClickListener(this);

		et_account = (EditText) findViewById(R.id.et_account);
		et_cercode = (EditText) findViewById(R.id.et_cercode);
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
					Intent intent = new Intent(this, RegisterSetPwd.class);
					intent.putExtra("phoneNum", phoneNum);
					intent.putExtra("cerCode", cerCode);
					startActivity(intent);
					finish();
				}				
			}
//			
//			Intent intent = new Intent(this, RegisterSetPwd.class);
//			startActivity(intent);
			break;
		case R.id.btn_send_code:
			
			phoneNum = et_account.getText().toString().trim();
			if (CheckUtil.isEmpty(phoneNum)) {
				Toaster.showToast(this, "手机号不能为空");
			}else{
				String phoneReg="^1\\d{10}";
				if(!phoneNum.matches(phoneReg)){
					Toaster.showToast(RegisterActivity.this, "请输入正确的手机号");
				}else{
					//开始倒计时
					btn_send_code.setEnabled(false);
					timer.start();
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
		
		ApiManager.getInstance().getRegVerCode(MyConstants.REGISTER, phoneNum, null, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode()!=200) {
						Toaster.showToast(RegisterActivity.this,response.getMsg());
					}else{
						timer.start();
					}
				}else{
					Toaster.showToast(RegisterActivity.this,"获取失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				
			}
		});
		
	}
}
