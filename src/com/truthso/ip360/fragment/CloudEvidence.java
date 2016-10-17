package com.truthso.ip360.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.SearchCloudEvidenceActivity;
import com.truthso.ip360.adapter.CloudEvidenceAdapter;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.CloudEvidenceBean;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.MainActionBar;
import com.truthso.ip360.view.xrefreshview.XRefreshView;
import com.truthso.ip360.view.xrefreshview.XRefreshView.XRefreshViewListener;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :云端证据
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午2:49:04
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class CloudEvidence extends BaseFragment implements OnClickListener,
		OnItemClickListener, XRefreshViewListener {
	private int pagerNumber = 1;
	private MainActionBar actionBar;
	private ListView lv_cloudevidence;
	private CloudEvidenceAdapter adapter;
	private XRefreshView xRefresh;
	private int CODE_SEARCH = 101;
	private LayoutInflater inflater;
	private TextView tv_photo,tv_video,tv_record,tv_pc,tv_file;
	private CloudEviItemBean cloudEviItemBean;
	private String keywork;//搜索框里的搜索内容
	private int type,mobileType;//类型，取证类型
	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		cloudEviItemBean = new CloudEviItemBean();
		actionBar = (MainActionBar) view
				.findViewById(R.id.actionbar_cloudevidence);
		actionBar.setLeftText("类别");
		actionBar.setTitle("云端证据");
		actionBar.setRightText("选择");
		actionBar.setActionBarOnClickListener(this);

		xRefresh = (XRefreshView) view
				.findViewById(R.id.xrefresh_cloudevidence);
		xRefresh.setPullRefreshEnable(true);
		xRefresh.setPullLoadEnable(false);
		xRefresh.setAutoRefresh(false);
		xRefresh.setXRefreshViewListener(this);

		lv_cloudevidence = (ListView) view.findViewById(R.id.lv_cloudevidence);
		adapter = new CloudEvidenceAdapter(getActivity(), null);
		lv_cloudevidence.setAdapter(adapter);
		View headView = LayoutInflater.from(getActivity()).inflate(
				R.layout.head_cloudevidence, null);
		lv_cloudevidence.addHeaderView(headView);
		lv_cloudevidence.setOnItemClickListener(this);
	}

	@Override
	public int setViewId() {
		return R.layout.fragment_clouddevidence;
	}

	@Override
	protected void initData() {
		inflater = LayoutInflater.from(getActivity());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acition_bar_right:
			choice();
			break;
		case R.id.acition_bar_left:
			/*
			 * startActivityForResult(new Intent(getActivity(),
			 * CategoryCloudEvidenceActivity.class), CODE_SEARCH);
			 */
			if (!CheckUtil.isEmpty(window) && window.isShowing()) {
				actionBar.setRightEnable();
				window.dismiss();
			} else {
				actionBar.setRightDisEnable();
				showPop();
			}
//		case R.id.tv_photo://pop中的拍照取证
////			window.dismiss();
//			break;
//		case R.id.tv_video://pop中的录像取证
//			break;
//		case R.id.tv_record://pop中的录音取证
//			break;
//		case R.id.tv_pc://pop线上取证
//			break;
//		case R.id.tv_file://确权文件
//			break;
		

		default:
			break;
		}

	}

	// 显示类别popwindow
	private void showPop() {

		popview = inflater.inflate(R.layout.activity_category_cloudcvidence,
				null);
		tv_photo = (TextView) popview.findViewById(R.id.tv_photo);
	
		tv_video = (TextView) popview.findViewById(R.id.tv_video);
		
		 tv_record = (TextView) popview.findViewById(R.id.tv_record);
		 
		 tv_pc = (TextView) popview.findViewById(R.id.tv_pc);
		 
		 tv_file = (TextView) popview.findViewById(R.id.tv_file);
		
		FrameLayout fl_empty = (FrameLayout) popview.findViewById(R.id.fl_empty);
		window = new PopupWindow(popview, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置弹出窗体的背景 this.setBackgroundDrawable(dw);
		window.setBackgroundDrawable(dw);
		window.setTouchable(true);
		
		tv_photo.setOnClickListener(new OnClickListener() {//拍照
			
			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}
				keywork = null;
				type = 2;//现场取证
				mobileType = 50001;
				getDatas();
			}

		
		});
		
		tv_video.setOnClickListener(new OnClickListener() {//录像			
			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}
				keywork = null;
				type = 2;//现场取证
				mobileType = 50003;
				getDatas();
			}
		});
		
		tv_record.setOnClickListener(new OnClickListener() {//录音
			
			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}
				keywork = null;
				type = 2;//现场取证
				mobileType = 50002;
				getDatas();
			}
		});
		tv_pc.setOnClickListener(new OnClickListener() {//线上取证
			
			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}
				keywork = null;
				type = 3;//线上取证
				mobileType = (Integer) null;
				getDatas();
			}
		});
		 tv_file.setOnClickListener(new OnClickListener() {//确权文件
			
			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}
				keywork = null;
				type = 1;//确权文件
				mobileType = (Integer) null;
				getDatas();
			}
		});
		fl_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}
			}
		});
		window.showAsDropDown(getActivity().findViewById(
				R.id.actionbar_cloudevidence));
	}

	// 点击多选按钮
	private void choice() {
		actionBar.setLeftText("全选");
		actionBar.setRightText("取消");
		actionBar.setActionBarOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.acition_bar_right:// 取消
					cancelChoose();
					break;
				case R.id.acition_bar_left:// 全选
					adapter.setAllSelect(true);
					lv_cloudevidence.invalidateViews();
					break;
				default:
					break;
				}

			}

		});
		showDownLoadPop();
		adapter.setChoice(true);
		lv_cloudevidence.invalidateViews();

	}

	// 取消多选状态
	private void cancelChoose() {
		if (downLoadwindow.isShowing()) {
			downLoadwindow.dismiss();
		}
		adapter.setChoice(false);
		lv_cloudevidence.invalidateViews();
		actionBar.setRightText("多选");
		actionBar.setLeftText("类别");
		actionBar.setActionBarOnClickListener(CloudEvidence.this);
	}

	// 显示底部下载按钮
	private void showDownLoadPop() {
		if (CheckUtil.isEmpty(downLoadwindow)) {
			contentView = inflater.inflate(R.layout.pop_download, null);
			downLoadwindow = new PopupWindow(contentView,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// 进入退出的动画
		// downLoadwindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		downLoadwindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			startActivityForResult(new Intent(getActivity(),
					SearchCloudEvidenceActivity.class), CODE_SEARCH);
		}

	}

	@Override
	public void onRefresh() {
		handler.sendEmptyMessageDelayed(0, 1000);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			xRefresh.stopRefresh();
		}
	};
	private PopupWindow window;
	private PopupWindow downLoadwindow;
	private View contentView;
	private View popview;

	@Override
	public void onLoadMore() {

	}

	@Override
	public void onRelease(float direction) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			if (!CheckUtil.isEmpty(window) && window.isShowing()) {
				actionBar.setRightEnable();
				window.dismiss();
				return true;
			}
			if (!CheckUtil.isEmpty(downLoadwindow)
					&& downLoadwindow.isShowing()) {
				cancelChoose();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 调接口获取数据
	 */
	private void getDatas() {
		showProgress("正在加载数据...");
		ApiManager.getInstance().getCloudEvidence(keywork, type, mobileType, pagerNumber, 10, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				hideProgress();
				CloudEvidenceBean bean = (CloudEvidenceBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						cloudEviItemBean.setFileName(bean.getFileTitle());
						cloudEviItemBean.setCreateTime(bean.getFileDate());
						cloudEviItemBean.setFileSize(bean.getFileSize());
					}else{
						Toaster.showToast(getActivity(), bean.getMsg());
					}
				}else{
					Toaster.showToast(getActivity(), "数据加载失败请刷新重试");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
