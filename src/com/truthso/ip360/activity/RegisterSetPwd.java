package com.truthso.ip360.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterSetPwd extends BaseActivity implements OnClickListener {
	private Button btn_next;
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
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
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			}
	}


