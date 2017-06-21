package com.truthso.ip360.fragment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.truthso.ip360.activity.AboutUsAcctivity;
import com.truthso.ip360.activity.AccountMagActivity;
import com.truthso.ip360.activity.AccountPayActivity;
import com.truthso.ip360.activity.AemndPsdActivity;
import com.truthso.ip360.activity.BindEmialActivity;
import com.truthso.ip360.activity.BindPhoNumActivity;
import com.truthso.ip360.activity.ChargeRulerActivity;
import com.truthso.ip360.activity.LoginActivity;
import com.truthso.ip360.activity.MyNotarFile;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.ReBindEmailActivity;
import com.truthso.ip360.activity.ReBindPhoNumActivity;
import com.truthso.ip360.activity.RealNameCertification;
import com.truthso.ip360.activity.RealNameInfoActivity;
import com.truthso.ip360.activity.SettingActivity;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.GiftsProduct;
import com.truthso.ip360.bean.PersonalMsgBean;
import com.truthso.ip360.bean.product;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.event.LoginEvent;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.DownLoadManager;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :个人中心
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午2:56:08
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class PersonalCenter extends BaseFragment implements OnClickListener{
	private PersonalMsgBean bean;
//	private int usedCount_photo, usedCount_video, usedCount_record;// 累积使用量
//	private String buyCount_photo,buyCount_video,buyCount_record;//买的量
//	private int type;// 取证类型
private ImageView iv_next_renzheng;
	private String  accountBalance,str;
	private ImageView iv_next_yue;
	private RelativeLayout  rl_Certification, rl_bind_phonum, rl_bind_mail, rl_my_notar,rl_account,rl_charge_rules;
	private Button btn_logout,btn_count_pay;
	// 账户余额 ,实名认证状态，已绑定的手机号，已绑定的邮箱
	private TextView tv_account_balance, tv_realname, tv_bindphonenum,
			tv_bindemail, tv_account,tv_cache_size;
	private String contractStart_photo,contractStart_video,contractStart_record,contractEnd_video,contractEnd_photo,contractEnd_record;
	private String unit_photo,unit_video,unit_record;
	private List<product> list;// 业务余量的集合
	private List<GiftsProduct> listZSong;//赠送业务量
	private boolean isOk,isContractUser,isRefreshAccount;
	private boolean isHaveCombo = true;
	private String Tag;
	private List<product> productBalance;
	private ImageView btn_shezhi;
	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		btn_shezhi = (ImageView) view.findViewById(R.id.btn_shezhi);
		btn_shezhi.setOnClickListener(this);
		tv_account = (TextView) view.findViewById(R.id.tv_account);

		btn_count_pay = (Button) view.findViewById(R.id.btn_count_pay);
		btn_count_pay.setOnClickListener(this);
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

		rl_my_notar = (RelativeLayout) view.findViewById(R.id.rl_my_notar);
		rl_my_notar.setOnClickListener(this);

		rl_charge_rules= (RelativeLayout) view.findViewById(R.id.rl_charge_rules);
		rl_charge_rules.setOnClickListener(this);

		tv_account_balance = (TextView) view.findViewById(R.id.tv_account_balance);
		tv_realname = (TextView) view.findViewById(R.id.tv_realname);
		tv_bindphonenum = (TextView) view.findViewById(R.id.tv_bindphonenum);
		tv_bindemail = (TextView) view.findViewById(R.id.tv_bindemail);
		iv_next_renzheng = (ImageView) view.findViewById(R.id.iv_next_renzheng);
		getPersonalMsg();
		EventBus.getDefault().register(this);
	}

	@Subscribe
	public void updatePersonalMsg(LoginEvent event){
		getPersonalMsg();
	}


	// 获取个人信息概要
	public void getPersonalMsg() {
		String userAccount = (String) SharePreferenceUtil.getAttributeByKey(getActivity(), MyConstants.SP_USER_KEY, "userAccount", SharePreferenceUtil.VALUE_IS_STRING);
		tv_account.setText(userAccount);
		showProgress("正在获取信息，请稍后...");
		ApiManager.getInstance().getPersonalMsg(new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				bean = (PersonalMsgBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						isOk = true;
						// 账户余额
						int balance = bean.getDatas().getAccountBalance();
						double account = balance*0.01;
						DecimalFormat dec = new DecimalFormat("0.00");
						accountBalance = "￥"+ dec.format(account);
						tv_account_balance.setText(accountBalance);

						list=bean.getDatas().getProductBalance();
						if (list.size()!=0){
							 productBalance = bean.getDatas().getProductBalance();

						}else{//当前无套餐
							isHaveCombo = false;
						}

						// 是否已实名认证
						if (bean.getDatas().getRealNameState() == 1||bean.getDatas().getRealNameState() == 4) {// 1是未认证
																		// ，2是已认证4-已打回
							tv_realname.setText("未实名认证");// 实名认证状态
							iv_next_renzheng.setVisibility(View.VISIBLE);
						} else if (bean.getDatas().getRealNameState() == 2) {
							tv_realname.setText("已实名认证");// 实名认证状态
							iv_next_renzheng.setVisibility(View.VISIBLE);
						}else if(bean.getDatas().getRealNameState() == 3){//3-认证中
							tv_realname.setText("认证中");
							iv_next_renzheng.setVisibility(View.INVISIBLE);
						}

						// 是否已绑定手机号
						if (!CheckUtil.isEmpty(bean.getDatas()
								.getBindedMobile())) {// 为空时，是未绑定手机号
//							tv_account.setText(bean.getDatas().getBindedMobile());
							tv_bindphonenum.setText(bean.getDatas()
									.getBindedMobile());
						} else {
//							tv_account.setText("真相取证");
							tv_bindphonenum.setText("未绑定");
						}

						// 是否绑定邮箱
						if (!CheckUtil.isEmpty(bean.getDatas().getBindedEmail())) {// 为空时，是未绑定
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
	public void onHiddenChanged(boolean hidden) {
		if(!hidden&&tv_cache_size!=null){
			String dirSize=FileSizeUtil.getAutoFileOrFilesSize(MyConstants.DOWNLOAD_PATH);
			tv_cache_size.setText(dirSize);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_shezhi://设置
			startActivity(new Intent(getActivity(), SettingActivity.class));

			break;
		case R.id.rl_account:// 账号信息
			startActivity(new Intent(getActivity(),AccountMagActivity.class));
			break;
		case R.id.btn_count_pay:// 用户充值
			if (isOk){
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
					}else if(bean.getDatas().getRealNameState() ==1||bean.getDatas().getRealNameState() ==4 ){//1未认证4-已打回
						Intent intent2 = new Intent(getActivity(),
								RealNameCertification.class);
						startActivityForResult(intent2,
								MyConstants.REALNAME_VERTIFICATION);
					}else if(bean.getDatas().getRealNameState() ==3 ){//认证中3-审核中
						//认证中，不跳

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
			case R.id.rl_my_notar://我的公证
				Intent intent_myfile = new Intent(getActivity(),MyNotarFile.class);
				startActivity(intent_myfile);
				break;
			case R.id.rl_charge_rules://计费规则
				Intent intent = new Intent(getActivity(), ChargeRulerActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
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



	/**
	 * 禁用返回键
	 *//*
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}*/

/*	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		String dirSize=FileSizeUtil.getAutoFileOrFilesSize(MyConstants.CACHE_PATH);
		tv_cache_size.setText(dirSize);
	}*/

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
