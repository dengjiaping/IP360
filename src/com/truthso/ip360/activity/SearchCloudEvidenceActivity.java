package com.truthso.ip360.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class SearchCloudEvidenceActivity extends NotTitleBaseActivity implements OnClickListener {

	private EditText et_find_service;
private RelativeLayout rl_search_cloudevidence;
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		et_find_service = (EditText) findViewById(R.id.et_find_service);
		rl_search_cloudevidence=(RelativeLayout) findViewById(R.id.rl_search_cloudevidence);
		rl_search_cloudevidence.setOnClickListener(this);
		setSearchMode();
	}

	private void setSearchMode() {
		// 自动弹出软键盘
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) et_find_service.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et_find_service, 0);
			}
		}, 300);
		et_find_service.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public int setLayout() {

		return R.layout.activity_search_cloudevidence;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_search_cloudevidence:
			finish();
			break;

		default:
			break;
		}
		
	}


}
