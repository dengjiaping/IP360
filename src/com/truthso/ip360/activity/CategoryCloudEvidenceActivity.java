package com.truthso.ip360.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CategoryCloudEvidenceActivity extends NotTitleBaseActivity {

	private Button btn_category;
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		btn_category=(Button) findViewById(R.id.btn_category);
		btn_category.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_category_cloudcvidence;
	}



}
