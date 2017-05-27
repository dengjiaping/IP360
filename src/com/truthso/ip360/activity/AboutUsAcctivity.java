package com.truthso.ip360.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.system.Toaster;

/**
 * @despriction :个人中心->关于我们
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午8:36:28
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 * 
 */
public class AboutUsAcctivity extends BaseActivity implements OnClickListener {
//	private Button btn_title_left;
	private RelativeLayout rl_advice,rl_pingfen,rl_useragreement;
	private TextView tv_versioncode;
	private  Button btn_weichat;
	private Dialog alertDialog;
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
//		btn_title_left = (Button) findViewById(R.id.btn_title_left);
//		btn_title_left.setOnClickListener(this);
		rl_useragreement = (RelativeLayout) findViewById(R.id.rl_useragreement);
		rl_useragreement.setOnClickListener(this);
		rl_advice = (RelativeLayout) findViewById(R.id.rl_advice);
		rl_advice.setOnClickListener(this);
		tv_versioncode = (TextView) findViewById(R.id.tv_versioncode);
		tv_versioncode.setText("版本号:"+getVersion());
		rl_pingfen = (RelativeLayout) findViewById(R.id.rl_pingfen);
		rl_pingfen.setOnClickListener(this);
		rl_useragreement = (RelativeLayout) findViewById(R.id.rl_useragreement);
		rl_useragreement.setOnClickListener(this);
		btn_weichat = (Button) findViewById(R.id.btn_weichat);
		btn_weichat.setOnClickListener(this);
	}

	@Override
	public int setLayout() {
		return R.layout.activity_about_us;
	}

	@Override
	public String setTitle() {
		return "关于我们";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.rl_advice://意见反馈
			Intent intent1 = new Intent(AboutUsAcctivity.this,UserAdviceActivity.class);
				startActivity(intent1);

			break;
			case R.id.rl_useragreement://用户协议
				Intent intent = new Intent(AboutUsAcctivity.this,UserAgreementActivity.class);
				startActivity(intent);
				break;
			case R.id.rl_pingfen://去评分
				Intent intent2 =new Intent(AboutUsAcctivity.this,SetScoreActivity.class);
				startActivity(intent2);
				break;
			case R.id.btn_weichat://微信公众号
				showDialog("公众号\"真相网络\"您可以在微信中直接粘贴搜索");
				break;
		}

	}
	private String getVersion() {
		try {
			// 通过PackageManager获取安装包信息
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);

			// 返回版本信息
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return "";
		}

	}
	private void showDialog(String str) {
		alertDialog = new AlertDialog.Builder(AboutUsAcctivity.this).setTitle("温馨提示")
				.setMessage(str).setIcon(R.drawable.ww)
				.setPositiveButton("去微信", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//复制“真相网络”到系统粘贴板
						ClipboardManager cmb = (ClipboardManager) AboutUsAcctivity.this.getSystemService(AboutUsAcctivity.this.CLIPBOARD_SERVICE);
						cmb.setText("真相网络");
						//去微信
						gotoWeChat();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				}).create();
		alertDialog.show();
	}

	/**
	 * 去微信
	 */
	private void gotoWeChat() {
		try {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");

			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setComponent(cmp);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toaster.showToast(AboutUsAcctivity.this,"检查到您手机没有安装微信，请安装后使用该功能");
		}

	}
}
