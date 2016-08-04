package com.truthso.ip360.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * @despriction :没有引用标题布局的基类
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-26下午7:54:36
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public abstract class NotTitleBaseActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(setLayout());

		
		
		initData();
		initView();
	}
	
	public abstract void initData();
	
	public abstract void initView();
	
	public abstract int setLayout();
	
	

}
