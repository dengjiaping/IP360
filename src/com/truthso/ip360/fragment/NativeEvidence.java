package com.truthso.ip360.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.truthso.ip360.activity.PhotoDetailActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.RecordDetailActivity;
import com.truthso.ip360.activity.SearchCloudEvidenceActivity;
import com.truthso.ip360.activity.VideoDetailActivity;
import com.truthso.ip360.adapter.NativeAdapter;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.MainActionBar;

/**
 * @despriction :本地证据
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午2:52:13
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class NativeEvidence extends BaseFragment implements OnClickListener,
		OnItemClickListener {
	private MainActionBar actionBar;
	private ListView listView;
	private int CODE_SEARCH = 101;
	private NativeAdapter adapter;
	private PopupWindow window, downLoadwindow;
	private List<DbBean> mDatas;

	private LayoutInflater inflater;
	private Activity mActivity;

	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		actionBar = (MainActionBar) view
				.findViewById(R.id.actionbar_nativeevidence);
		actionBar.setLeftText("类别");
		actionBar.setTitle("本地证据");
		actionBar.setRightText("多选");
		actionBar.setActionBarOnClickListener(this);

		listView = (ListView) view.findViewById(R.id.lv_nativeevidence);
		mDatas = GroupDao.getInstance(getActivity()).queryAll();

		adapter = new NativeAdapter(getActivity(), mDatas);
		listView.setAdapter(adapter);

		View headView = LayoutInflater.from(getActivity()).inflate(
				R.layout.head_cloudevidence, null);
		listView.addHeaderView(headView);
		listView.setOnItemClickListener(this);		
        getActivity().getContentResolver().registerContentObserver(Uri.parse("content://com.truthso.ip360/IP360_media_detail"), true, MyObserver);

	}

	private ContentObserver MyObserver=new ContentObserver(new Handler()) {
		@SuppressLint("NewApi")
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			super.onChange(selfChange, uri);
			mDatas = GroupDao.getInstance(getActivity()).queryAll();
			adapter.addData(mDatas);
		}
		
	};
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		   getActivity().getContentResolver().unregisterContentObserver(MyObserver);
	}
	
	@Override
	public int setViewId() {
		return R.layout.fragment_native_evidence;
	}

	@Override
	protected void initData() {
		mActivity=getActivity();
		inflater = LayoutInflater.from(mActivity);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acition_bar_right:
			choice();
			break;
		case R.id.acition_bar_left:
			// startActivityForResult(new Intent(getActivity(),
			// CategoryCloudEvidenceActivity.class), CODE_SEARCH);
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
					listView.invalidateViews();
					break;
				default:
					break;
				}

			}
		});
		showDownLoadPop();
		adapter.setChoice(true);
		listView.invalidateViews();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			startActivityForResult(new Intent(getActivity(),
					SearchCloudEvidenceActivity.class), CODE_SEARCH);
		}else {
	       DbBean dbBean = mDatas.get(position-1);
			if (dbBean.getType()==MyConstants.PHOTO) {//条目类型照片
				Intent intent = new Intent(getActivity(),PhotoDetailActivity.class);
				intent.putExtra("url", dbBean.getResourceUrl());
				startActivity(intent);
			}else if (dbBean.getType()==MyConstants.VIDEO) {//条目类型录像
				Intent videoIntent = new Intent(getActivity(),VideoDetailActivity.class);
				videoIntent.putExtra("url",dbBean.getResourceUrl() );
				startActivity(videoIntent);
			}else if (dbBean.getType()==MyConstants.RECORD) {//条目类型录音
				Intent recordIntent = new Intent(getActivity(),RecordDetailActivity.class);
				recordIntent.putExtra("url", dbBean.getResourceUrl());
				recordIntent.putExtra("recordTime", dbBean.getRecordTime());
				
				startActivity(recordIntent);
			}
		}	
		
	}

	// 取消多选状态
	private void cancelChoose() {
		if (downLoadwindow.isShowing()) {
			downLoadwindow.dismiss();
		}
		adapter.setChoice(false);
		listView.invalidateViews();
		actionBar.setRightText("多选");
		actionBar.setLeftText("类别");
		actionBar.setActionBarOnClickListener(NativeEvidence.this);
	}

	// 显示类别popwindow
	private void showPop() {
		if (CheckUtil.isEmpty(window)) {
			View view = inflater.inflate(
					R.layout.activity_category_cloudcvidence, null);
			FrameLayout fl_empty = (FrameLayout) view
					.findViewById(R.id.fl_empty);
			TextView tv = (TextView) view.findViewById(R.id.tv_all);
			window = new PopupWindow(view,
					WindowManager.LayoutParams.MATCH_PARENT,
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
						window.dismiss();
					}
				}
			});
		}

		window.showAsDropDown(mActivity.findViewById(
				R.id.actionbar_cloudevidence));
	}

	// 显示底部下载按钮
	private void showDownLoadPop() {
		View contentView = inflater.inflate(R.layout.pop_download, null);
		Button btn_delete= (Button) contentView.findViewById(R.id.btn_download);
		btn_delete.setText("删除");
		downLoadwindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// 进入退出的动画
		// downLoadwindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		downLoadwindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
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
