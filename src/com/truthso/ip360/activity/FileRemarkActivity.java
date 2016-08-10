package com.truthso.ip360.activity;

import android.view.View;
import android.widget.Button;

/**
 * @despriction :云端证据中listView条目下拉菜单的备注
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-10下午5:15:14
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class FileRemarkActivity extends BaseActivity{
	private Button btn_title_right;
	@Override
	public void initData() {
		
	}

	@Override
	public void initView() {
		btn_title_right= (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.VISIBLE);
		btn_title_right.setText("保存");
	}

	@Override
	public int setLayout() {
		return R.layout.activity_file_remark;
	}

	@Override
	public String setTitle() {
		return "备注";
	}

}
