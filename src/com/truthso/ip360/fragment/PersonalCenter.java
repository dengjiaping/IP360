package com.truthso.ip360.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.activity.AboutUsAcctivity;
import com.truthso.ip360.activity.AccountPayActivity;
import com.truthso.ip360.activity.AemndPsdActivity;
import com.truthso.ip360.activity.BindEmialActivity;
import com.truthso.ip360.activity.BindPhoNumActivity;
import com.truthso.ip360.activity.LoginActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.ReBindEmailBindNewActivity;
import com.truthso.ip360.activity.ReBindPhoNumActivity;
import com.truthso.ip360.activity.RealNameCertification;
import com.truthso.ip360.activity.findPwdActivity;
import com.truthso.ip360.bean.PersonalMsgBean;
import com.truthso.ip360.bean.product;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

/**
 * @despriction :个人中心
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午2:56:08
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class PersonalCenter extends BaseFragment implements OnClickListener {
	private PersonalMsgBean bean;
	private Dialog alertDialog;
	private RelativeLayout ll_count_pay, rl_Certification, rl_bind_phonum,
			rl_bind_mail, rl_amend_psd,rl_about_us;
	private Button btn_logout;
	//账户余额 ,实名认证状态，已绑定的手机号，已绑定的邮箱
	private TextView tv_account_balance,tv_realname,tv_bindphonenum,tv_bindemail;
	
	private List<product> list;//业务余量的集合

	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		ll_count_pay = (RelativeLayout) view.findViewById(R.id.rl_count_pay);
		ll_count_pay.setOnClickListener(this);

		rl_Certification = (RelativeLayout) view
				.findViewById(R.id.rl_Certification);
		rl_Certification.setOnClickListener(this);

		rl_bind_phonum = (RelativeLayout) view
				.findViewById(R.id.rl_bind_phonum);
		rl_bind_phonum.setOnClickListener(this);

		rl_bind_mail = (RelativeLayout) view.findViewById(R.id.rl_bind_mail);
		rl_bind_mail.setOnClickListener(this);

		rl_amend_psd = (RelativeLayout) view.findViewById(R.id.rl_amend_psd);
		rl_amend_psd.setOnClickListener(this);

		rl_about_us = (RelativeLayout) view.findViewById(R.id.rl_about_us);
		rl_about_us.setOnClickListener(this);
		
		btn_logout = (Button) view.findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
		
		tv_account_balance = (TextView) view.findViewById(R.id.tv_account_balance);
		tv_realname = (TextView) view.findViewById(R.id.tv_realname);
		tv_bindphonenum = (TextView) view.findViewById(R.id.tv_bindphonenum);
		tv_bindemail = (TextView) view.findViewById(R.id.tv_bindemail);
		
		getPersonalMsg();

	}
	//获取个人信息概要
	private void getPersonalMsg() {
		showProgress();
		ApiManager.getInstance().getPersonalMsg(new ApiCallback() {
	
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				bean = (PersonalMsgBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						list = bean.getProductBalance();//业务余量
						if (bean.getUserType() == 1) {//1-付费用户（C）；2-合同用户（B）
							tv_account_balance.setText(bean.getAccountBalance());
						}else{
							//跳转到另一个界面，展示B类用户	
						}
						tv_realname.setText(bean.getRealNameState());//实名认证状态
						if (!CheckUtil.isEmpty(bean.getBindedMobile())) {//为空时，是未绑定手机号
							tv_bindphonenum.setText(bean.getBindedMobile());
						}else {
							tv_bindphonenum.setText("未绑定");
						}
						if (!CheckUtil.isEmpty(bean.getBindedEmail())) {//为空时，是未绑定
							tv_bindemail.setText(bean.getBindedEmail());
						}else {
							tv_bindemail.setText("未绑定");
						}
					
						
					}else{
						Toaster.showToast(getActivity(),bean.getMsg());
					}
				}else{
					Toaster.showToast(getActivity(), "获取信息失败");
				}
				
			}
		});
	}

	@Override
	public int setViewId() {
		return R.layout.fragment_percenter;
	}

	@Override
	protected void initData() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.rl_count_pay:// 用户充值
			Intent intent1 = new Intent(getActivity(),AccountPayActivity.class);
			startActivity(intent1);
			break;
		case R.id.rl_Certification:// 实名认证
			Intent intent2 = new Intent(getActivity(),RealNameCertification.class);
			startActivity(intent2);
			break;

		case R.id.rl_bind_phonum:// 绑定手机
			if (CheckUtil.isEmpty(bean.getBindedMobile())) {//为空是未绑定
				Intent intent3 = new Intent(getActivity(),BindPhoNumActivity.class);
				startActivity(intent3);
			}else{//已绑定跳转到更改绑定
				Intent intent4 = new Intent(getActivity(),ReBindPhoNumActivity.class);
				startActivity(intent4);
			}
			
			break;

		case R.id.rl_bind_mail:// 绑定邮箱
			if (CheckUtil.isEmpty(bean.getBindedEmail())) {//为空是未绑定
				Intent intent4 = new Intent(getActivity(),BindEmialActivity.class);
				startActivity(intent4);
			}else{//已绑定跳转到更改绑定
				Intent intent5 = new Intent(getActivity(),ReBindEmailBindNewActivity.class);
				startActivity(intent5);
			}
			
			break;

		case R.id.rl_amend_psd:// 修改密码
			Intent intent5 = new Intent(getActivity(),AemndPsdActivity.class);
			startActivity(intent5);
			break;
		case R.id.rl_about_us:// 关于我们
			Intent intent = new Intent(getActivity(),AboutUsAcctivity.class);
			startActivity(intent);
			break;
		case R.id.btn_logout://退出登录
			
			showDialog();
		default:
			break;
		}
	}

	private void showDialog() {
		alertDialog = new AlertDialog.Builder(getActivity()).
        	    setTitle("温馨提示").
        	    setMessage("是否确认退出登录？").
        	    setIcon(R.drawable.ww).
        	    setPositiveButton("确定", new DialogInterface.OnClickListener() {
        	     
        	     @Override
        	     public void onClick(DialogInterface dialog, int which) {
        	    	 logOut();
        	    	
        	    	 }

			
        	    }).
        	    setNegativeButton("取消", new DialogInterface.OnClickListener() {
        	     
        	     @Override
        	     public void onClick(DialogInterface dialog, int which) {
        	    	 alertDialog.dismiss();
        	     }
        	    }).
        	    create();
				alertDialog.show();
        	 }
	//退出登录
	private void logOut() {
		showProgress();
		ApiManager.getInstance().LogOut(new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Toaster.showToast(getActivity(),"已退出登录");
						 Intent intent = new Intent(getActivity(),LoginActivity.class);
	        	    	 startActivity(intent);
					}else{
						Toaster.showToast(getActivity(),response.getMsg());
					}
				}else{
					Toaster.showToast(getActivity(), "退出登录失败");
				}
			}
		});
	}
	}
