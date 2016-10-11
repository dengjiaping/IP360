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
 * @despriction :设置密码 注册
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-8下午4:15:02
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class RegisterSetPwd extends BaseActivity implements OnClickListener {
	private Button btn_regist;
	private String phoneNum,cerCode,userPwd;
	private TextView tv_phone;
	private EditText et_pwd;
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
	
		phoneNum = getIntent().getStringExtra("phoneNum");
		cerCode = getIntent().getStringExtra("cerCode");
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_phone.setText(phoneNum);
		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_regist.setOnClickListener(this);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
	}

	@Override
	public int setLayout() {
		return R.layout.activity_register_setpwd;
	}

	@Override
	public String setTitle() {
		return "注册";
	}

	@Override
	public void onClick(View v) {
		userPwd = et_pwd.getText().toString().trim();
		if (CheckUtil.isEmpty(userPwd)) {
			Toaster.showToast(RegisterSetPwd.this, "请设置密码");
		}else{
			regist();
			
		}
		
		
			}

	private void regist() {
		userPwd=MD5Util.encoder(userPwd);
		ApiManager.getInstance().registUser(phoneNum, userPwd, cerCode, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Intent intent = new Intent(RegisterSetPwd.this,LoginActivity.class);
						startActivity(intent);
						finish();
						
					}else{
						Toaster.showToast(RegisterSetPwd.this,response.getMsg());
					}
				}else{
					Toaster.showToast(RegisterSetPwd.this,"注册失败");
				}
			}
		});
		
	}
	}


