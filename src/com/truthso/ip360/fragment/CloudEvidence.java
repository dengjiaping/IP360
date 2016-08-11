package com.truthso.ip360.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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

import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.SearchCloudEvidenceActivity;
import com.truthso.ip360.adapter.CloudEvidenceAdapter;
import com.truthso.ip360.adapter.NativeAdapter;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.MainActionBar;
import com.truthso.ip360.view.xrefreshview.XRefreshView;
import com.truthso.ip360.view.xrefreshview.XRefreshView.XRefreshViewListener;

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
	private MainActionBar actionBar;
	private ListView lv_cloudevidence;
	private CloudEvidenceAdapter adapter;
	private XRefreshView xRefresh;
	private int CODE_SEARCH = 101;
	private LayoutInflater inflater;

	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
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
		adapter = new CloudEvidenceAdapter(getActivity(),null);
		lv_cloudevidence.setAdapter(adapter);
		View headView = LayoutInflater.from(getActivity()).inflate(
				R.layout.head_cloudevidence, null);
		lv_cloudevidence.addHeaderView(headView);
		lv_cloudevidence.setOnItemClickListener(this);
	}

	@Override
	public int setViewId() {
		// TODO Auto-generated method stub
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

			break;
		default:
			break;
		}

	}

	// 显示类别popwindow
	private void showPop() {

		View view = inflater.inflate(R.layout.activity_category_cloudcvidence,
				null);
		TextView tv = (TextView) view.findViewById(R.id.tv_all);
		FrameLayout fl_empty = (FrameLayout) view.findViewById(R.id.fl_empty);
		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置弹出窗体的背景 this.setBackgroundDrawable(dw);
		window.setBackgroundDrawable(dw);
		window.setTouchable(true);
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
		if(CheckUtil.isEmpty(downLoadwindow)){
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

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRelease(float direction) {
		// TODO Auto-generated method stub

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
}
