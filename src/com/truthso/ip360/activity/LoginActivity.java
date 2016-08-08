package com.truthso.ip360.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 登录界面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-21下午2:56:58
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class LoginActivity extends Activity implements OnClickListener {
	private TextView tv_register;
	private TextView tv_forget_pwd;
	private Button btn_loginin;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_login);
	initView();
}

	public void initView() {
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_register.setOnClickListener(this);

		tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
		tv_forget_pwd.setOnClickListener(this);
		
		btn_loginin = (Button) findViewById(R.id.btn_loginin);
		btn_loginin.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_register:// 注册
			Intent intent = new Intent(this,RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_forget_pwd:// 忘记密码
			Intent intent1 = new Intent(this,findPwdActivity.class);
			startActivity(intent1);
			break;
		case R.id.btn_loginin:// 登录
			Intent intent2 = new Intent(this,MainActivity.class);
			startActivity(intent2);
			finish();
			break;

		}
	}

}
