package com.truthso.ip360.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.loopj.android.http.RequestHandle;
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
	private String searchText;
	private boolean tag = true;
	private EditText et_find_service;

	private List<CloudEviItemBean> list=new ArrayList<CloudEviItemBean>();

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
		xRefresh.setPullLoadEnable(true);
		xRefresh.setAutoRefresh(false);
		xRefresh.setXRefreshViewListener(this);

		
		
		lv_cloudevidence = (ListView) view.findViewById(R.id.lv_cloudevidence);
	
		View headView = LayoutInflater.from(getActivity()).inflate(
				R.layout.head_cloudevidence, null);
		lv_cloudevidence.addHeaderView(headView);
		et_find_service = (EditText)headView.findViewById(R.id.et_find_service);
		
		lv_cloudevidence.setOnItemClickListener(this);

		if (tag) {
//			进来显示第一个
			type = 2;//现场取证
			mobileType = 50001;
			getDatas(keywork,type,mobileType,pagerNumber);
		}


		 adapter=new CloudEvidenceAdapter(getActivity(),list,type,mobileType);
		 lv_cloudevidence.setAdapter(adapter);
		 setSearchMode();
	}

	private void setSearchMode() {
		// 自动弹出软键盘
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) et_find_service.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(et_find_service, 0);
			}
		  }, 300);
		et_find_service.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (CheckUtil.isEmpty(s.toString())) {
					
					list.clear();
					getDatas(null,type,mobileType,1);
					
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
		
				Log.i("djj", (String)(msg.obj)+type+mobileType);
				list.clear();
				getDatas((String)(msg.obj),type,mobileType,1);
			}
		};

   private RequestHandle requestHandle;
	
	
	
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

			
		case R.id.btn_download:
		   
			break;
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
                list.clear();
				type = 2;//现场取证
				mobileType = 50001;
				getDatas(keywork,type,mobileType,pagerNumber);
			}

		
		});
		
		tv_video.setOnClickListener(new OnClickListener() {//录像			
			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}

				 list.clear();

				tag = false;

			    type = 2;//现场取证
				mobileType = 50003;
				getDatas(keywork,type,mobileType,pagerNumber);
			}
		});
		
		tv_record.setOnClickListener(new OnClickListener() {//录音
			
			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}

				 list.clear();

				tag = false;

				type = 2;//现场取证
				mobileType = 50002;
				getDatas(keywork,type,mobileType,pagerNumber);
			}
		});
		tv_pc.setOnClickListener(new OnClickListener() {//线上取证
			
			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}

				 list.clear();

				tag = false;

				type = 3;//线上取证
   			    mobileType = 0;
				getDatas(keywork,type,mobileType,pagerNumber);
			}
		});
		 tv_file.setOnClickListener(new OnClickListener() {//确权文件
			
			@Override
			public void onClick(View arg0) {
				if (window.isShowing()) {
					actionBar.setRightEnable();
					window.dismiss();
				}
		 list.clear();

				tag = false;

				type = 1;//确权文件
				mobileType = 0;
				getDatas(keywork,type,mobileType,pagerNumber);
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
		window.setAnimationStyle(R.style.mypopwindow_anim_style);
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
			btn_download = (Button) contentView.findViewById(R.id.btn_download);
			btn_download.setOnClickListener(this);
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
			/*Intent intent=new Intent(getActivity(),	SearchCloudEvidenceActivity.class);
			intent.putExtra("type", type);
			intent.putExtra("mobileType", mobileType);
			intent.putExtra("from", "cloud");
			startActivityForResult(intent, CODE_SEARCH);*/
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==CODE_SEARCH){
			
		}
	}
	
	
	@Override
	public void onRefresh() {
		pagerNumber=1;
		list.clear();
		getDatas(searchText,type,mobileType,pagerNumber);
	}

	private PopupWindow window;
	private PopupWindow downLoadwindow;
	private View contentView;
	private View popview;
	private Button btn_download;
	
	/**
	 *底部加载更多
	 */
	@Override
	public void onLoadMore() {
		pagerNumber++;
		getDatas(searchText,type,mobileType,pagerNumber);
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
		return false;
	}
	/**
	 * 调接口获取数据
	 */
	private void getDatas(String keywork,final int type,int mobileType,int pagerNumber) {
		showProgress("正在加载数据...");
		ApiManager.getInstance().getCloudEvidence(keywork, type, mobileType, pagerNumber, 10, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				//停止刷新
				xRefresh.stopRefresh();
				xRefresh.stopLoadMore();
				hideProgress();
				
				CloudEvidenceBean bean = (CloudEvidenceBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						List<CloudEviItemBean> datas = bean.getDatas();
						
						if(!CheckUtil.isEmpty(datas)){
							list.addAll(datas);					   
						}			
						 adapter.notifyDataChange(list);
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
				
			}
		});
	}
}
