package com.truthso.ip360.activity;

import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.MD5Util;

import android.content.Intent;
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
	private String phoneNum,cerCode,userPwd;
	private TextView tv_phone;
	private EditText et_pwd;
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		phoneNum = getIntent().getStringExtra("phoneNum");
		cerCode = getIntent().getStringExtra("cerCode");
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_phone.setText(phoneNum);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
	}

	@Override
	public int setLayout() {
		return R.layout.activity_findpwd_setnewpwd;
	}

	@Override
	public String setTitle() {
		return "重置密码";
	}

	@Override
	public void onClick(View v) {
		userPwd = et_pwd.getText().toString().trim();
		if (CheckUtil.isEmpty(userPwd)) {
			Toaster.showToast(FindpwdSetNewPwd.this, "请设置密码");
		}else{
			ResetPwd();
			
		}
		
		
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void ResetPwd() {

		userPwd=MD5Util.encoder(userPwd);
		ApiManager.getInstance().registUser(phoneNum, userPwd, cerCode, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Intent intent = new Intent(FindpwdSetNewPwd.this,MainActivity.class);
						startActivity(intent);
						finish();
						
					}else{
						Toaster.showToast(FindpwdSetNewPwd.this,response.getMsg());
					}
				}else{
					Toaster.showToast(FindpwdSetNewPwd.this,"重置失败");
				}
			}
		});
		
	
		
	}

}
