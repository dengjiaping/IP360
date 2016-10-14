package com.truthso.ip360.activity;

import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.MD5Util;

import cz.msebera.android.httpclient.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
	private EditText et_oldpwd,et_newpwd,et_again_newpwd;
	private String newPwd,oldPwd,newPwd2;

	@Override
	public void initData() {
		
	}
	@Override
	public void initView() {
		et_oldpwd = (EditText) findViewById(R.id.et_oldpwd);
		et_newpwd = (EditText) findViewById(R.id.et_newpwd);
		et_again_newpwd = (EditText) findViewById(R.id.et_again_newpwd);
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_sure://确定修改
			oldPwd = et_oldpwd.getText().toString().trim();
			newPwd = et_newpwd.getText().toString().trim();
			newPwd2 = et_again_newpwd.getText().toString().trim();
			if (CheckUtil.isEmpty(oldPwd)||CheckUtil.isEmpty(newPwd)) {
				Toaster.showToast(AemndPsdActivity.this, "旧密码或新密码不能为空");
			}else{
				ChangePwd();
				
			}
			
			
			break;

		default:
			break;
		}
		
	}
	//确认修改
	private void ChangePwd() {
		oldPwd = MD5Util.encoder(oldPwd);
		newPwd = MD5Util.encoder(newPwd);
		showProgress("正在修改...");
		ApiManager.getInstance().ChangePwd(oldPwd, newPwd, new ApiCallback() {
			
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Toaster.showToast(AemndPsdActivity.this,"修改成功");
						finish();
					}else{
						Toaster.showToast(AemndPsdActivity.this, response.getMsg());
					}
				}else{
					Toaster.showToast(AemndPsdActivity.this, "修改失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
		
	} 
}
