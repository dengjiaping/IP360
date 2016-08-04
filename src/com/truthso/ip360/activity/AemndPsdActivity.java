package com.truthso.ip360.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * @despriction :个人中心->修改密码
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午8:34:22
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class AemndPsdActivity extends BaseActivity implements OnClickListener {
	private Button btn_sure;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	
		case R.id.btn_sure://确定修改
			
			//保存修改后的新密码
			finish();
			
			break;

		default:
			break;
		}
	}
	@Override
	public void initData() {
		
	}
	@Override
	public void initView() {
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
	}
	@Override
	public int setLayout() {
		return R.layout.avtivity_amend_psd;
	}
	@Override
	public String setTitle() {
		return "修改密码";
	} 
}
