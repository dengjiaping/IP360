package com.truthso.ip360.activity;

import com.truthso.ip360.bean.FileRemarkBean;
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
import android.widget.RelativeLayout;
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
			tv_account, tv_text;
	private EditText et_text;
	private String fileName, date, size, mode, format, remarkText;
	private int pkValue,dataType;
	private RelativeLayout rl_pc_remark, rl_app_remark;

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
		dataType = getIntent().getIntExtra("dataType", 0);
		remarkText = getIntent().getStringExtra("remarkText");
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
//		tv_account = (TextView) findViewById(R.id.tv_account);
//		String account = "￥" + count / 10 + "." + count % 10 + "元";
//		tv_account.setText(account);
		et_text = (EditText) findViewById(R.id.et_text);
		rl_pc_remark = (RelativeLayout) findViewById(R.id.rl_pc_remark);
		rl_app_remark = (RelativeLayout) findViewById(R.id.rl_app_remark);
		tv_text = (TextView) findViewById(R.id.tv_text);
		if (mode.equals("手机取证")) {// 手机取证可以更改备注
			getFileRemark(pkValue,type);//调接口获取备注
			// et_text.setText(rText);
		} else {// 其他文件不可以更改备注
			btn_title_right.setVisibility(View.INVISIBLE);
			rl_pc_remark.setVisibility(View.VISIBLE);
			rl_app_remark.setVisibility(View.GONE);
			tv_text.setText(remarkText);
		}
		
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
		String str = remarkText.toString().trim();
		if (!CheckUtil.isEmpty(reText)) {
			if (str.equals(reText)) {
				Toaster.showToast(this, "您没有要更新的备注");
			} else {
				getPort(reText);
			}
		}else{
			Toaster.showToast(this, "请输入备注！");
		}
	
		/*
		 * if (!CheckUtil.isEmpty(reText)) { getPort(reText); } else {
		 * Toaster.showToast(this, "请输入备注"); }
		 */

	}

	/**
	 * 调接口
	 * 
	 * @param reText
	 */
	private void getPort(String reText) {
		showProgress("正在保存...");
		ApiManager.getInstance().setFileRemark(reText, pkValue, type,dataType,
				new ApiCallback() {
					@Override
					public void onApiResult(int errorCode, String message,
							BaseHttpResponse response) {
						hideProgress();
						if (!CheckUtil.isEmpty(response)) {
							if (response.getCode() == 200) {
								Toaster.showToast(FileRemarkActivity.this,
										"保存成功");
								finish();
							} else {
								Toaster.showToast(FileRemarkActivity.this,
										response.getMsg());
							}
						} else {
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
	/**
	 * 调获取备注的接口
	 * @param pkValue2
	 * @param type2
	 */
	private void getFileRemark(int pkValue2, int type2) {
		showProgress("正在加载...");
		ApiManager.getInstance().getFileRemark(pkValue2, type2, new ApiCallback() {
			
			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
			}
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
			FileRemarkBean bean = (FileRemarkBean) response;
			if (!CheckUtil.isEmpty(bean)) {
				if (bean.getCode() == 200) {
				String remark = bean.getDatas().getRemarkText();
				if (CheckUtil.isEmpty(remark)) {
					et_text.setHint("请输入备注");
				} else {
					et_text.setText(remark);
				}
				
				}else{
					Toaster.showToast(FileRemarkActivity.this, bean.getMsg());
				}
			}else{
				
				Toaster.showToast(FileRemarkActivity.this, "请重试");			}
			}
		});
	}
}
