package com.truthso.ip360.fragment;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.truthso.ip360.activity.PhotoDetailActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.RecordDetailActivity;
import com.truthso.ip360.activity.SearchCloudEvidenceActivity;
import com.truthso.ip360.activity.VideoDetailActivity;
import com.truthso.ip360.adapter.NativeAdapter;
import com.truthso.ip360.adapter.NativeAdapter.ViewHolder;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.updownload.FileInfo;
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
		OnItemClickListener,UpdateItem {
	private MainActionBar actionBar;
	private ListView listView;
	private int CODE_SEARCH = 101;
	private NativeAdapter adapter;
	private PopupWindow nativeWindow, downLoadwindow;
	private List<DbBean> mDatas;
	private LayoutInflater inflater;
	private Activity mActivity;
    private TextView tv_photo,tv_video,tv_record,tv_pc,tv_file;
	private String type;
	private int lastPosition;
	private boolean isRefresh;
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
		mDatas =SqlDao.getSQLiteOpenHelper().queryAll();

		adapter = new NativeAdapter(getActivity(), mDatas);
		adapter.setUpdateItem(this);
		View headView = LayoutInflater.from(getActivity()).inflate(
				R.layout.head_cloudevidence, null);
		listView.addHeaderView(headView);
		listView.setAdapter(adapter);

		
		et_find_service = (EditText) headView.findViewById(R.id.et_find_service);

		listView.setOnItemClickListener(this);		
        getActivity().getContentResolver().registerContentObserver(Uri.parse("content://com.truthso.ip360/IP360_media_detail"), true, MyObserver);
       //getActivity().getContentResolver().registerContentObserver(Uri.parse("content://com.truthso.ip360/updownloadlog/down"), true, MyObserver1);
        setSearchMode();
	}

	private void setSearchMode() {
		// 自动弹出软键盘
		/*Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) et_find_service.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et_find_service, 0);
			}
		  }, 300);*/
		et_find_service.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (CheckUtil.isEmpty(s.toString())) {
					
					mDatas.clear();
					adapter.addData(mDatas);
					
				} else {
					mHandler.removeMessages(101);
					if (!CheckUtil.isEmpty(requestHandle)) {
						requestHandle.cancel(true);
					}
					Message msg = new Message();
					msg.what = 101;
					msg.obj = s.toString();
					mHandler.sendMessageDelayed(msg, 500);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
				List<DbBean> searchByKey = SqlDao.getSQLiteOpenHelper().searchByKey((String)(msg.obj));
				
				adapter.addData(searchByKey);
			}
		};
		
	private ContentObserver MyObserver=new ContentObserver(new Handler()) {
		@SuppressLint("NewApi")
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			super.onChange(selfChange, uri);
			mDatas = SqlDao.getSQLiteOpenHelper().queryAll();
			adapter.addData(mDatas);
		}
		
	};
	private EditText et_find_service;
	
	/*private ContentObserver MyObserver1=new ContentObserver(new Handler()) {
		@SuppressLint("NewApi")
		@Override
		public void onChange(boolean selfChange, Uri uri) {
			super.onChange(selfChange, uri);
			adapter.addData(mDatas);
		}
		
	};*/

	@Override
	public void onStart() {
		super.onStart();
	if(isRefresh){
		adapter.setisOpen(Integer.MAX_VALUE);
		adapter.notifyDataSetInvalidated();
	}
		
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isRefresh=true;
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		   getActivity().getContentResolver().unregisterContentObserver(MyObserver);
		  // getActivity().getContentResolver().unregisterContentObserver(MyObserver);
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
			if(mDatas.size()>0){
				choice();
			}
			break;
		case R.id.acition_bar_left:
			// startActivityForResult(new Intent(getActivity(),
			// CategoryCloudEvidenceActivity.class), CODE_SEARCH);
			if (!CheckUtil.isEmpty(nativeWindow) && nativeWindow.isShowing()) {
				actionBar.setRightEnable();
				nativeWindow.dismiss();
			} else {
				actionBar.setRightDisEnable();
				showPop(v);
			}
			break;
		case R.id.btn_download:
			deleteAll();
			break;
			
		default:
			break;
		}
	}

	private void deleteAll() {
		List<Integer> selected = adapter.getSelected();
		for (int i = 0; i < selected.size(); i++) {
			SqlDao.getSQLiteOpenHelper().delete(MyConstants.TABLE_MEDIA_DETAIL, selected.get(i));
		}
		adapter.setChoice(false);
		adapter.notifyDataSetChanged();
		
		if (!CheckUtil.isEmpty(nativeWindow) && nativeWindow.isShowing()) {
			actionBar.setRightEnable();
			nativeWindow.dismiss();
		}
		if (!CheckUtil.isEmpty(downLoadwindow)
				&& downLoadwindow.isShowing()) {
			cancelChoose();
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
			Intent intent=new Intent(getActivity(),	SearchCloudEvidenceActivity.class);
			intent.putExtra("type", 1);
			intent.putExtra("mobileType", 50001);
			intent.putExtra("from", "native");
			startActivityForResult(intent, CODE_SEARCH);
		}else {/*
	       DbBean dbBean = mDatas.get(position-1);
	       Log.i("djj", dbBean.toString());
			if (dbBean.getType()==MyConstants.PHOTO||dbBean.getType() == MyConstants.CLOUD_PHOTO) {//条目类型照片
				Intent intent = new Intent(getActivity(),PhotoDetailActivity.class);
				intent.putExtra("url", dbBean.getResourceUrl());
				intent.putExtra("from","native");
				startActivity(intent);
			}else if (dbBean.getType()==MyConstants.VIDEO||dbBean.getType() == MyConstants.CLOUD_VIDEO) {//条目类型录像
				Intent videoIntent = new Intent(getActivity(),VideoDetailActivity.class);
				videoIntent.putExtra("url",dbBean.getResourceUrl() );
				LogUtils.e(dbBean.getResourceUrl()+"录像跳转时候的路径");
				startActivity(videoIntent);
			}else if (dbBean.getType()==MyConstants.RECORD||dbBean.getType() == MyConstants.CLOUD_RECORD) {//条目类型录音
				Intent recordIntent = new Intent(getActivity(),RecordDetailActivity.class);
				recordIntent.putExtra("url", dbBean.getResourceUrl());
				recordIntent.putExtra("recordTime", dbBean.getRecordTime());
				LogUtils.e(dbBean.getResourceUrl()+"录音跳转时候的路径"+dbBean.getRecordTime()+"录音时长");
				startActivity(recordIntent);
			}	
		*/}	
		
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
	private void showPop(View v) {
		if (CheckUtil.isEmpty(nativeWindow)) {
			View view = inflater.inflate(
					R.layout.activity_category_cloudcvidence, null);
			FrameLayout fl_empty = (FrameLayout) view
					.findViewById(R.id.fl_empty);
			TextView tv = (TextView) view.findViewById(R.id.tv_all);
			nativeWindow = new PopupWindow(view,
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置弹出窗体的背景 this.setBackgroundDrawable(dw);
			nativeWindow.setBackgroundDrawable(dw);
			nativeWindow.setTouchable(true);
			fl_empty.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (nativeWindow.isShowing()) {
						actionBar.setRightEnable();
						nativeWindow.dismiss();
					}
				}
			});


			tv_photo = (TextView) view.findViewById(R.id.tv_photo);

			tv_video = (TextView) view.findViewById(R.id.tv_video);

			tv_record = (TextView) view.findViewById(R.id.tv_record);

			tv_pc = (TextView) view.findViewById(R.id.tv_pc);

			tv_file = (TextView) view.findViewById(R.id.tv_file);
		}
		tv_photo.setOnClickListener(new OnClickListener() {//拍照

			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("拍照取证");
				if (nativeWindow.isShowing()) {
					actionBar.setRightEnable();
					nativeWindow.dismiss();
				}
				mDatas.clear();
				String[] arg=new String[]{"0","3"};
				mDatas=SqlDao.getSQLiteOpenHelper().queryByType(arg);
				adapter.addData(mDatas);
			}


		});

		tv_video.setOnClickListener(new OnClickListener() {//录像
			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("录像取证");
				if (nativeWindow.isShowing()) {
					actionBar.setRightEnable();
					nativeWindow.dismiss();
				}

				mDatas.clear();
				String[] arg=new String[]{"1","4"};
				mDatas=SqlDao.getSQLiteOpenHelper().queryByType(arg);
				adapter.addData(mDatas);
			}
		});

		tv_record.setOnClickListener(new OnClickListener() {//录音

			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("录音取证");
				if (nativeWindow.isShowing()) {
					actionBar.setRightEnable();
					nativeWindow.dismiss();
				}

				mDatas.clear();
				String[] arg=new String[]{"2","5"};
				mDatas=SqlDao.getSQLiteOpenHelper().queryByType(arg);
				adapter.addData(mDatas);
			}
		});
		tv_pc.setOnClickListener(new OnClickListener() {//线上取证

			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("线上取证");
				if (nativeWindow.isShowing()) {
					actionBar.setRightEnable();
					nativeWindow.dismiss();
				}
				mDatas.clear();
				String[] arg=new String[]{"6"};
				mDatas=SqlDao.getSQLiteOpenHelper().queryByType(arg);
				adapter.addData(mDatas);
			}
		});
		tv_file.setOnClickListener(new OnClickListener() {//确权文件

			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("确权文件");
				if (nativeWindow.isShowing()) {
					actionBar.setRightEnable();
					nativeWindow.dismiss();
				}
				mDatas.clear();
				String[] arg=new String[]{"7"};
				mDatas=SqlDao.getSQLiteOpenHelper().queryByType(arg);
				adapter.addData(mDatas);
			}
		});

		nativeWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		nativeWindow.showAsDropDown(v);
	}

	// 显示底部下载按钮
	private void showDownLoadPop() {
		View contentView = inflater.inflate(R.layout.pop_download, null);
		Button btn_delete= (Button) contentView.findViewById(R.id.btn_download);
		btn_delete.setText("删除");
		btn_delete.setOnClickListener(this);
		downLoadwindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// 进入退出的动画
//		 downLoadwindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		downLoadwindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == event.KEYCODE_BACK) {
			if (nativeWindow!=null && nativeWindow.isShowing()) {
				actionBar.setRightEnable();
				nativeWindow.dismiss();
				return true;
			}
			if (!CheckUtil.isEmpty(downLoadwindow)
					&& downLoadwindow.isShowing()) {
				cancelChoose();
				return true;
			}
		}
		return false;
	}

	@Override
	public void update(int position) {
		if(position==adapter.getCount()-1){
			listView.setSelection(position);
		}
		
		if(position!=lastPosition){
			int fir=listView.getFirstVisiblePosition();
			int las=listView.getLastVisiblePosition();
			if(lastPosition>=fir&&lastPosition<=las){
				View view = listView.getChildAt(lastPosition - fir+1);  
				if(view!=null){
					LinearLayout ll_option=	(LinearLayout) view.findViewById(R.id.ll_option);
					if(ll_option.getVisibility()==View.VISIBLE){
						ll_option.setVisibility(View.GONE);
						NativeAdapter.ViewHolder vh=(ViewHolder)view.getTag();
						vh.cb_option.setChecked(false);
					}		
				}					
			}			
		}
		lastPosition=position;
	}
}
