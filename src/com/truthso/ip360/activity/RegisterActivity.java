package com.truthso.ip360.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @despriction :注册页面
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-25下午4:15:40
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class RegisterActivity extends BaseActivity implements OnClickListener {
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
		return R.layout.activity_register;
	}

	@Override
	public String setTitle() {
		return "注册";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next://下一步
			Intent intent = new Intent(this,RegisterSetPwd.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
