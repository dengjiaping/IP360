package com.truthso.ip360.activity;

import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import cz.msebera.android.httpclient.Header;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @despriction :云端证据中listView条目下拉菜单的备注
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-10下午5:15:14
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class FileRemarkActivity extends BaseActivity implements OnClickListener {
	private Button btn_title_right;
	private int count, type;
	private TextView tv_filename, tv_type, tv_date, tv_format, tv_size,
			tv_account;
	private EditText et_text;
	private String fileName, date, size, mode, format;
	int pkValue;

	@Override
	public void initData() {
	}
	@Override
	public void initView() {
		pkValue = getIntent().getIntExtra("pkValue", 0);
		fileName = getIntent().getStringExtra("fileName");
		format = getIntent().getStringExtra("format");
		date = getIntent().getStringExtra("date");
		size = getIntent().getStringExtra("size");
		mode = getIntent().getStringExtra("mode");
		count = getIntent().getIntExtra("count", 0);
		type = getIntent().getIntExtra("type", 0);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.VISIBLE);
		btn_title_right.setText("保存");
		btn_title_right.setOnClickListener(this);

		tv_filename = (TextView) findViewById(R.id.tv_filename);
		tv_filename.setText(fileName);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_type.setText(mode);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_date.setText(date);
		tv_format = (TextView) findViewById(R.id.tv_format);
		tv_format.setText(format);
		tv_size = (TextView) findViewById(R.id.tv_size);
		tv_size.setText(size);
		tv_account = (TextView) findViewById(R.id.tv_account);
		String account = "￥" + count / 10 + "." + count % 10 + "元";
		tv_account.setText(account);
		et_text = (EditText) findViewById(R.id.et_text);
	}

	@Override
	public int setLayout() {
		return R.layout.activity_file_remark;
	}

	@Override
	public String setTitle() {
		return "备注";
	}

	@Override
	public void onClick(View v) {// 保存
		String reText = et_text.getText().toString().trim();
		if (!CheckUtil.isEmpty(reText)) {
		getPort(reText);
		} else {
			Toaster.showToast(this, "请输入备注");
		}

	}
	/**
	 * 调接口
	 * @param reText
	 */
	private void getPort(String reText) {
	showProgress("正在保存...");
		ApiManager.getInstance().setFileRemark(reText, pkValue, type,
				new ApiCallback() {
					@Override
					public void onApiResult(int errorCode, String message,
							BaseHttpResponse response) {
						hideProgress();
						if (!CheckUtil.isEmpty(response)) {
							if (response.getCode() == 200) {
								Toaster.showToast(FileRemarkActivity.this, "保存成功");
								finish();
							}else{
								Toaster.showToast(FileRemarkActivity.this, response.getMsg());
							}
						}else{
							Toaster.showToast(FileRemarkActivity.this, "保存失败");
						}

					}
					@Override
					public void onApiResultFailure(int statusCode,
							Header[] headers, byte[] responseBody,
							Throwable error) {

					}
				});

	}

}
