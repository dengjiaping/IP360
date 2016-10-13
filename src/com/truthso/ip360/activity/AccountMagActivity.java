package com.truthso.ip360.activity;

import android.widget.TextView;

/**
 * @despriction :个人中心模块，合同用户余额那栏点击进去的账号信息
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-10-12下午2:43:57
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class AccountMagActivity extends BaseActivity {
	private TextView tv_contract_date,tv_account_photo,tv_account_video,tv_account_record;

	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		// 合同开始日期
		String contractStart = getIntent().getStringExtra("contractStart");
		// 合同结束日期
		String contractEnd = getIntent().getStringExtra("contractEnd");
		// 照片累积使用量
		String usedCountPhoto = getIntent().getStringExtra("usedCount_photo");
		// 录像累积使用量
		String usedCountVideo = getIntent().getStringExtra("usedCount_video");
		// 录音累积使用量
		String usedCountRecord = getIntent().getStringExtra("usedCount_record");
		 String str =contractStart.replace("-", ".")+"—"+contractEnd.replace("-", ".");
		tv_contract_date = (TextView) findViewById(R.id.tv_contract_date);
		tv_contract_date.setText(str);
		
		tv_account_photo = (TextView) findViewById(R.id.tv_account_photo);
		tv_account_photo.setText("已取证"+usedCountPhoto+"次");
		tv_account_video = (TextView) findViewById(R.id.tv_account_video);
		tv_account_video.setText("已取证"+usedCountVideo+"分钟");
		tv_account_record = (TextView) findViewById(R.id.tv_account_record);
		tv_account_record.setText("已取证"+usedCountRecord+"分钟");
	}

	@Override
	public int setLayout() {
		return R.layout.activity_accountmsg;
	}

	@Override
	public String setTitle() {
		return "账号信息";
	}

}
