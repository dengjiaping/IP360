package com.truthso.ip360.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public abstract class BaseActivity extends Activity{
	private Button btn_title_left;
	private TextView tv_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(setLayout());
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(setTitle());
		initData();
		initView();
	}
	
	public abstract void initData();
	
	public abstract void initView();
	
	public abstract int setLayout();
	
	public abstract String setTitle();
	
}
