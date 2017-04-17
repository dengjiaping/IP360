package com.truthso.ip360.activity;

import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.MD5Util;

import cz.msebera.android.httpclient.Header;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
/**
 * @despriction :个人中心->修改密码
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午8:34:22
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class AemndPsdActivity extends BaseActivity implements OnClickListener {
	private Button btn_sure_amend;
	private EditText et_oldpwd,et_newpwd,et_again_newpwd;
	private String newPwd,oldPwd,newPwd2;
	private  boolean  isOldPwdEmp,isNewPwdEmp,isNewPwd2Emp;
	@Override
	public void initData() {
		
	}
	@Override
	public void initView() {
		et_oldpwd = (EditText) findViewById(R.id.et_oldpwd);
		et_newpwd = (EditText) findViewById(R.id.et_newpwd);
		et_again_newpwd = (EditText) findViewById(R.id.et_again_newpwd);
		btn_sure_amend = (Button) findViewById(R.id.btn_sure_amend);
		btn_sure_amend.setOnClickListener(this);
		et_oldpwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			if (!CheckUtil.isEmpty(s.toString().trim())){
				isOldPwdEmp = true;
			}else{
				isOldPwdEmp = false;
			}
				checkButtonStatus();
			}
		});
		et_newpwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!CheckUtil.isEmpty(s.toString().trim())&& s.length()<19 && s.length()>5){
					isNewPwd2Emp = true;
				}else{
					isNewPwd2Emp = false;
				}
				checkButtonStatus();
			}
		});
		et_again_newpwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!CheckUtil.isEmpty(s.toString().trim()) && s.length()<19 && s.length()>5){
					isNewPwdEmp = true;
				}else{
					isNewPwdEmp = false;
				}
				checkButtonStatus();
			}

		});
	}
	/**
	 * 按钮是否是彩色可点击
	 */
	private void checkButtonStatus() {
		if (isNewPwd2Emp && isNewPwdEmp && isOldPwdEmp) {
			btn_sure_amend.setEnabled(true);
			btn_sure_amend.setTextColor(getResources().getColor(R.color.white));
			btn_sure_amend.setBackgroundResource(R.drawable.round_corner_bg);
		} else {
			btn_sure_amend.setEnabled(false);
			btn_sure_amend.setBackgroundResource(R.drawable.round_corner_white);
			btn_sure_amend.setTextColor(getResources().getColor(R.color.huise));
		}
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
		
		case R.id.btn_sure_amend://确定修改
			oldPwd = et_oldpwd.getText().toString().trim();
			newPwd = et_newpwd.getText().toString().trim();
			newPwd2 = et_again_newpwd.getText().toString().trim();
			if (CheckUtil.isEmpty(oldPwd)||CheckUtil.isEmpty(newPwd)) {
				Toaster.showToast(AemndPsdActivity.this, "旧密码或新密码不能为空");
			}else if(!newPwd.equals(newPwd2)){
				Toaster.showToast(AemndPsdActivity.this, "新密码与再次输入的密码不一致");
			}else if(!CheckUtil.isPassWordValidate(newPwd)){
				Toaster.showToast(this, "请输入6~18位字母与数字组成的密码");
			}else if(newPwd.equals(oldPwd)){
				Toaster.showToast(this, "要修改的密码不能与原密码相同");
				
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
				hideProgress();
				Toaster.showToast(AemndPsdActivity.this,"网络链接超时，请重试！");
				
			}
		});
		
	} 
}
