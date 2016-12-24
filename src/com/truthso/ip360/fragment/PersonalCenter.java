package com.truthso.ip360.fragment;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.activity.AboutUsAcctivity;
import com.truthso.ip360.activity.AccountMagActivity;
import com.truthso.ip360.activity.AccountPayActivity;
import com.truthso.ip360.activity.AemndPsdActivity;
import com.truthso.ip360.activity.BindEmialActivity;
import com.truthso.ip360.activity.BindPhoNumActivity;
import com.truthso.ip360.activity.LoginActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.ReBindEmailActivity;
import com.truthso.ip360.activity.ReBindPhoNumActivity;
import com.truthso.ip360.activity.RealNameCertification;
import com.truthso.ip360.activity.RealNameInfoActivity;
import com.truthso.ip360.bean.PersonalMsgBean;
import com.truthso.ip360.bean.product;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.constants.URLConstant;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :个人中心
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午2:56:08
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class PersonalCenter extends BaseFragment implements OnClickListener,CompoundButton.OnCheckedChangeListener {
	private PersonalMsgBean bean;
	private int usedCount_photo, usedCount_video, usedCount_record;// 累积使用量
	private String buyCount_photo,buyCount_video,buyCount_record;//买的量
	private int type;// 取证类型
	private Dialog alertDialog;
	private String  accountBalance;
	private ImageView iv_next_yue;
	private RelativeLayout ll_count_pay, rl_Certification, rl_bind_phonum,
			rl_bind_mail, rl_amend_psd, rl_about_us, rl_account;
	private Button btn_logout;
	// 账户余额 ,实名认证状态，已绑定的手机号，已绑定的邮箱
	private TextView tv_account_balance, tv_realname, tv_bindphonenum,
			tv_bindemail, tv_title;
	private String contractStart_photo,contractStart_video,contractStart_record,contractEnd_video,contractEnd_photo,contractEnd_record;
	private String unit_photo,unit_video,unit_record;
	private List<product> list;// 业务余量的集合
	private boolean isOk,isContractUser;
	private CheckBox cb_iswifi;
	private boolean isHaveCombo = true;
	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		ll_count_pay = (RelativeLayout) view.findViewById(R.id.rl_count_pay);
		ll_count_pay.setOnClickListener(this);
		iv_next_yue = (ImageView) view.findViewById(R.id.iv_next_yue);
		rl_account = (RelativeLayout) view.findViewById(R.id.rl_account);
		rl_account.setOnClickListener(this);
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

		tv_account_balance = (TextView) view
				.findViewById(R.id.tv_account_balance);
		tv_realname = (TextView) view.findViewById(R.id.tv_realname);
		tv_bindphonenum = (TextView) view.findViewById(R.id.tv_bindphonenum);
		tv_bindemail = (TextView) view.findViewById(R.id.tv_bindemail);
		iv_next_yue.setVisibility(View.INVISIBLE);
		cb_iswifi= (CheckBox) view.findViewById(R.id.cb_iswifi);
		cb_iswifi.setChecked(true);
		cb_iswifi.setOnCheckedChangeListener(this);
		boolean isWifi= (Boolean) SharePreferenceUtil.getAttributeByKey(getActivity(),MyConstants.SP_USER_KEY,MyConstants.ISWIFI,SharePreferenceUtil.VALUE_IS_BOOLEAN);
		cb_iswifi.setChecked(isWifi);
		getPersonalMsg();

	}

	// 获取个人信息概要
	public void getPersonalMsg() {
		showProgress("正在获取信息，请稍后...");
		ApiManager.getInstance().getPersonalMsg(new ApiCallback() {

			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				bean = (PersonalMsgBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						list = bean.getDatas().getProductBalance();//业务量的集合
						isOk = true;
						// 账户余额
							int balance = bean.getDatas().getAccountBalance();
							accountBalance = "余额￥" + balance / 100 + "." + balance
									% 100/10 +balance%100%10+ "元";
//							tv_account_balance.setText(accountBalance);
//							isContractUser = true;
//							contractStart = bean.getDatas().getContractStart();
//							contractEnd = bean.getDatas().getContractEnd();
//							contractEnd = contractEnd.replace("-", ".");
//							tv_account_balance.setText("合同用户" + contractEnd
//									+ "到期");
						if (list.size()!=0){
							for (int i = 0; i < list.size(); i++) {
								// 取证类型
								type = list.get(i).getType();

								if (type == MyConstants.PHOTOTYPE) {// 拍照
									// 累积使用量
									usedCount_photo = list.get(i).getUsedCount();
									//总共购买的量
									buyCount_photo = list.get(i).getBuyCount();
									contractStart_photo= list.get(i).getContractStart();
									contractEnd_photo = list.get(i).getContractEnd();
								    unit_photo = list.get(i).getUnit();
								} else if (type == MyConstants.VIDEOTYPE) {// 录像
									usedCount_video = list.get(i)
											.getUsedCount();
									buyCount_video = list.get(i).getBuyCount();
									contractStart_video= list.get(i).getContractStart();
									contractEnd_video = list.get(i).getContractEnd();
									unit_video = list.get(i).getUnit();
								} else if (type == MyConstants.RECORDTYPE) {// 录音
									usedCount_record = list.get(i)
											.getUsedCount();
									buyCount_record = list.get(i).getBuyCount();
									contractStart_record= list.get(i).getContractStart();
									contractEnd_record = list.get(i).getContractEnd();
									unit_record= list.get(i).getUnit();
								}

							}
						}else{//当前无套餐
							isHaveCombo = false;
						}



					/*	// 是否已实名认证
						if (bean.getDatas().getRealNameState() == 1) {// 1是未认证
																		// ，2是已认证
							tv_realname.setText("未实名认证");// 实名认证状态
						} else if (bean.getDatas().getRealNameState() == 2) {
							tv_realname.setText("已实名认证");// 实名认证状态
						}*/

						// 是否已绑定手机号
						if (!CheckUtil.isEmpty(bean.getDatas()
								.getBindedMobile())) {// 为空时，是未绑定手机号
							tv_title.setText(bean.getDatas().getBindedMobile());
							tv_bindphonenum.setText(bean.getDatas()
									.getBindedMobile());
						} else {
							tv_title.setText("IP360");
							tv_bindphonenum.setText("未绑定");
						}

						// 是否绑定邮箱
						if (!CheckUtil
								.isEmpty(bean.getDatas().getBindedEmail())) {// 为空时，是未绑定
							tv_bindemail.setText(bean.getDatas()
									.getBindedEmail());
						} else {
							tv_bindemail.setText("未绑定");
						}

					} else {
						Toaster.showToast(getActivity(), bean.getMsg());
					}
				} else {
					Toaster.showToast(getActivity(), "获取信息失败");
				}

			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub

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
		case R.id.rl_account:// 账号信息
			if (isOk) {
					Intent intent = new Intent(getActivity(), AccountMagActivity.class);
					// 传参
					//余额
				    intent.putExtra("accountBalance", accountBalance);
					intent.putExtra("isHaveCombo",isHaveCombo);
				if (isHaveCombo){//有套餐了传参
					//业务的开始时间与结束时间
					intent.putExtra("contractStart_photo", contractStart_photo);
					intent.putExtra("contractEnd_photo", contractEnd_photo);

					intent.putExtra("contractStart_video", contractStart_video);
					intent.putExtra("contractEnd_video", contractEnd_video);

					intent.putExtra("contractStart_record", contractStart_record);
					intent.putExtra("contractEnd_record", contractEnd_record);
					//套餐购买的量
					intent.putExtra("buyCount_photo", buyCount_photo + "");
					intent.putExtra("buyCount_video", buyCount_video + "");
					intent.putExtra("buyCount_record", buyCount_record + "");
					//业务已经使用的量
					intent.putExtra("usedCount_photo", usedCount_photo + "");
					intent.putExtra("usedCount_video", usedCount_video + "");
					intent.putExtra("usedCount_record", usedCount_record + "");
					//业务量单位
					intent.putExtra("unit_photo", unit_photo);
					intent.putExtra("unit_video", unit_video);
					intent.putExtra("unit_record", unit_record);
				}

					startActivity(intent);

			} else {
				getPersonalMsg();
			}

			break;
		case R.id.rl_count_pay:// 用户充值
			if (isOk) {
				// 合同用户不能充值
					Intent intent1 = new Intent(getActivity(), AccountPayActivity.class);
					intent1.putExtra("accountBalance",accountBalance);
					startActivityForResult(intent1, MyConstants.ACCOUNT_YUE);

			} else {
				getPersonalMsg();
			}

			break;
		case R.id.rl_Certification:// 实名认证
			if (isOk) {
				if (bean.getDatas().getAccountType() == 1) {// 1 个人 2 企业
					if(bean.getDatas().getRealNameState() ==2 ){//2-已认证
							Intent intent = new Intent(getActivity(), RealNameInfoActivity.class);
						intent.putExtra("realName",bean.getDatas().getRealName());
						intent.putExtra("cardId",bean.getDatas().getCardId());
							startActivity(intent);
					}else if(bean.getDatas().getRealNameState() ==1 ){//1未认证
						Intent intent2 = new Intent(getActivity(),
								RealNameCertification.class);
						startActivityForResult(intent2,
								MyConstants.REALNAME_VERTIFICATION);
					}

				} else {
					if (bean.getDatas().getRealNameState() ==2 ){//已认证
						Intent intent = new Intent(getActivity(), RealNameInfoActivity.class);
						intent.putExtra("realName",bean.getDatas().getRealName());
						intent.putExtra("cardId",bean.getDatas().getCardId());
						startActivity(intent);
					}else if (bean.getDatas().getRealNameState() ==1 ){//未认证
						Toaster.showToast(getActivity(), "企业用户请登录www.ip360.net.cn进行实名认证");
					}

				}
			} else {
				getPersonalMsg();
			}
			break;
		case R.id.rl_bind_phonum:// 绑定手机
			if (isOk) {
				if (CheckUtil.isEmpty(bean.getDatas().getBindedMobile())) {// 为空是未绑定
					Intent intent3 = new Intent(getActivity(),
							BindPhoNumActivity.class);
					startActivityForResult(intent3, MyConstants.BINDNEWEMOBILE);
				} else {// 已绑定跳转到更改绑定
					String bindedMonile = bean.getDatas().getBindedMobile();
					Intent intent4 = new Intent(getActivity(),
							ReBindPhoNumActivity.class);
					intent4.putExtra("bindedMonile", bindedMonile);
					startActivityForResult(intent4, MyConstants.OFFBIND_BINDNEWEMOBILE);
					// startActivity(intent4);
				}
			} else {
				getPersonalMsg();
			}

			break;

		case R.id.rl_bind_mail:// 绑定邮箱
			if (isOk) {
				if (CheckUtil.isEmpty(bean.getDatas().getBindedEmail())) {// 为空是未绑定
					Intent intent4 = new Intent(getActivity(),
							BindEmialActivity.class);
					startActivityForResult(intent4, MyConstants.BINDNEWEMAIL);
				} else {// 已绑定跳转到更改绑定
					String bindedEmail = bean.getDatas().getBindedEmail();
					Intent intent5 = new Intent(getActivity(),
							ReBindEmailActivity.class);
					intent5.putExtra("bindedEmail", bindedEmail);
					startActivityForResult(intent5,
							MyConstants.OFFBIND_BINDNEWEMAIL);
				}
			} else {
				getPersonalMsg();
			}

			break;

		case R.id.rl_amend_psd:// 修改密码

			Intent intent5 = new Intent(getActivity(), AemndPsdActivity.class);
			startActivity(intent5);
			break;
		case R.id.rl_about_us:// 关于我们

			Intent intent6 = new Intent(getActivity(), AboutUsAcctivity.class);
			startActivity(intent6);
			break;
		case R.id.btn_logout:// 退出登录
			showDialog();
		default:
			break;
		}
	}

	private void showDialog() {
		alertDialog = new AlertDialog.Builder(getActivity()).setTitle("温馨提示")
				.setMessage("是否确认退出登录？").setIcon(R.drawable.ww)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						logOut();
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

	// 退出登录
	private void logOut() {
		showProgress("正在退出...");
		ApiManager.getInstance().LogOut(new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				if (!CheckUtil.isEmpty(response)) {
					if (response.getCode() == 200) {
						Toaster.showToast(getActivity(), "已退出登录");
						Intent intent = new Intent(getActivity(),
								LoginActivity.class);
						startActivity(intent);
						getActivity().finish();
						// 清空登录的token
						SharePreferenceUtil.saveOrUpdateAttribute(
								getActivity(), MyConstants.SP_USER_KEY,
								"token", null);
					} else {
						Toaster.showToast(getActivity(), response.getMsg());
					}
				} else {
					Toaster.showToast(getActivity(), "退出登录失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 调接口 因为调的是同一个接口，所以就不需要再判断resultCode是哪个码
		getPersonalMsg();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		SharePreferenceUtil.saveOrUpdateAttribute(getActivity(),MyConstants.SP_USER_KEY,MyConstants.ISWIFI,isChecked);
	}

	/**
	 * 禁用返回键
	 *//*
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}*/
}
