package com.truthso.ip360.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
		return R.layout.activity_findpwd_setnewpwd;
	}

	@Override
	public String setTitle() {
		return "重置密码";
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}

}
