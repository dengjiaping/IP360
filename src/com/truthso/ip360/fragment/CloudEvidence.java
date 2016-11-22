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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.loopj.android.http.RequestHandle;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.SearchCloudEvidenceActivity;
import com.truthso.ip360.adapter.CloudEvidenceAdapter;
import com.truthso.ip360.adapter.NativeAdapter;
import com.truthso.ip360.adapter.NativeAdapter.ViewHolder;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.CloudEvidenceBean;
import com.truthso.ip360.bean.DownLoadFileBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.view.MainActionBar;
import com.truthso.ip360.view.RefreshListView;
import com.truthso.ip360.view.RefreshListView.OnRefreshListener;
import com.truthso.ip360.view.RefreshListView.OnloadListener;
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
		 UpdateItem, OnItemClickListener, XRefreshViewListener, OnRefreshListener, OnloadListener {
	private int pagerNumber = 1;
	private MainActionBar actionBar;
	//private ListView lv_cloudevidence;
	private CloudEvidenceAdapter adapter;
	private XRefreshView xRefresh;
	private RefreshListView listView;
	private int CODE_SEARCH = 101;
	private LayoutInflater inflater;
	private TextView tv_photo,tv_video,tv_record,tv_pc,tv_file;
	private CloudEviItemBean cloudEviItemBean;
	private String keywork;//搜索框里的搜索内容
	private int type,mobileType;//类型，取证类型
	private String searchText;
	private boolean tag = true;
	private EditText et_find_service;
	private PopupWindow cloudWindow;
	private PopupWindow downLoadwindow;
	private View contentView;
	private View popview;
	private Button btn_download;
	private List<CloudEviItemBean> list=new ArrayList<CloudEviItemBean>();
	private List<CloudEviItemBean> datas;
	private boolean isRefresh;
	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		cloudEviItemBean = new CloudEviItemBean();
		actionBar = (MainActionBar) view.findViewById(R.id.actionbar_cloudevidence);
		actionBar.setLeftText("类别");
		actionBar.setTitle("云端证据");
		actionBar.setRightText("选择");
		actionBar.setActionBarOnClickListener(this);

		/*xRefresh = (XRefreshView) view
				.findViewById(R.id.xrefresh_cloudevidence);
		xRefresh.setPullRefreshEnable(false);
		xRefresh.setPullLoadEnable(false);
		xRefresh.setAutoRefresh(false);
		xRefresh.setXRefreshViewListener(this);*/

		
		
		listView =  (RefreshListView) view.findViewById(R.id.lv_cloudevidence);
		//listView= (RefreshListView) view.findViewById(R.id.lv_cloud);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);

		View headView = LayoutInflater.from(getActivity()).inflate(
				R.layout.head_cloudevidence, null);
		listView.addHeaderView(headView);
		et_find_service = (EditText)headView.findViewById(R.id.et_find_service);
		
		//lv_cloudevidence.setOnItemClickListener(this);

		if (tag) {
//			进来显示第一个
			type = 2;//现场取证
			mobileType = 50001;
			getDatas(keywork,type,mobileType,pagerNumber);
		}


		 adapter=new CloudEvidenceAdapter(getActivity(),list,type,mobileType);
		 adapter.setUpdateItem(this);
		 listView.setAdapter(adapter);
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
			if (!CheckUtil.isEmpty(cloudWindow) && cloudWindow.isShowing()) {
				actionBar.setRightEnable();
				cloudWindow.dismiss();
			} else {
				actionBar.setRightDisEnable();
				showPop();
			}
			break;
		case R.id.btn_download:
			downloadAll();
			break;
		default:
			break;
		}

	}

/*	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			Intent intent=new Intent(getActivity(),	SearchCloudEvidenceActivity.class);
			intent.putExtra("type", type);
			intent.putExtra("mobileType", mobileType);
			intent.putExtra("from", "cloud");
			startActivityForResult(intent, CODE_SEARCH);
		}else{
			CloudEviItemBean cloudEviItemBean2 = datas.get(position-1);
		}

	}*/
	
	private void downloadAll() {
		List<CloudEviItemBean> selected = adapter.getSelected();
		for (int i = 0; i < selected.size(); i++) {
			download(selected.get(i));
		}
		adapter.setChoice(false);
		adapter.notifyDataSetChanged();
		
		if (!CheckUtil.isEmpty(cloudWindow) && cloudWindow.isShowing()) {
			actionBar.setRightEnable();
			cloudWindow.dismiss();
		}
		if (!CheckUtil.isEmpty(downLoadwindow)
				&& downLoadwindow.isShowing()) {
			cancelChoose();
		}
	}

	private void download(final CloudEviItemBean data){

		ApiManager.getInstance().downloadFile(data.getPkValue(), type,
				new ApiCallback() {

					@Override
					public void onApiResultFailure(int statusCode,
							Header[] headers, byte[] responseBody,
							Throwable error) {
						Toaster.toast(getActivity(), "获取数据失败", 1);
					}

					@Override
					public void onApiResult(int errorCode, String message,
							BaseHttpResponse response) {
						DownLoadFileBean bean = (DownLoadFileBean) response;
						if (!CheckUtil.isEmpty(bean)) {
							if (bean.getCode() == 200) {
								FileInfo info = new FileInfo();
								String nativePath = MyConstants.DOWNLOAD_PATH+"/"+data.getFileTitle();
								info.setFilePath(nativePath);//在本地的路径
								info.setFileName(data.getFileTitle());
								info.setType(type);//取证类型
								info.setMobiletype(mobileType);//现场取证的类型
								long l_size = Long.parseLong(data.getFileSize());
								String s_size = FileSizeUtil.setFileSize(l_size);
								info.setFileSize(s_size);
								info.setLlsize(data.getFileSize());
								info.setPosition(0);
								info.setFileTime(data.getFileTime());
								info.setResourceId(data.getPkValue());
								info.setFileLoc(data.getFileLocation());
								info.setFileCreatetime(data.getFileDate());
								String url = bean.getDatas().getFileUrl();
								// String
								// objectKey=url.substring(url.indexOf("/")+1);
								info.setObjectKey(url);
								// 下载
								DownLoadHelper.getInstance().downloadFile(
										info);

								Toast toast = new Toast(getActivity());
								toast.makeText(getActivity(), "文件开始下载到本地证据", Toast.LENGTH_SHORT)
										.show();
								toast.setGravity(Gravity.CENTER, 0, 0);
							} else {
								Toaster.toast(getActivity(), bean.getMsg(), 1);
							}
						} else {
							Toaster.toast(getActivity(), "获取数据失败", 1);
						}
					}
				});
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
		cloudWindow = new PopupWindow(popview, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置弹出窗体的背景 this.setBackgroundDrawable(dw);
		cloudWindow.setBackgroundDrawable(dw);
		cloudWindow.setTouchable(true);
		
		tv_photo.setOnClickListener(new OnClickListener() {//拍照
			
			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("拍照取证");
				if (cloudWindow.isShowing()) {
					actionBar.setRightEnable();
					cloudWindow.dismiss();
				}
                list.clear();
				type = 2;//现场取证
				mobileType = 50001;
				pagerNumber=1;
				getDatas(keywork,type,mobileType,pagerNumber);
			}

		
		});
		
		tv_video.setOnClickListener(new OnClickListener() {//录像			
			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("录像取证");
				if (cloudWindow.isShowing()) {
					actionBar.setRightEnable();
					cloudWindow.dismiss();
				}

				 list.clear();

				tag = false;

			    type = 2;//现场取证
				mobileType = 50003;
				pagerNumber=1;
				getDatas(keywork,type,mobileType,pagerNumber);
			}
		});
		
		tv_record.setOnClickListener(new OnClickListener() {//录音
			
			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("录音取证");
				if (cloudWindow.isShowing()) {
					actionBar.setRightEnable();
					cloudWindow.dismiss();
				}

				 list.clear();

				tag = false;

				type = 2;//现场取证
				mobileType = 50002;
				pagerNumber=1;
				getDatas(keywork,type,mobileType,pagerNumber);
			}
		});
		tv_pc.setOnClickListener(new OnClickListener() {//线上取证
			
			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("线上取证");
				if (cloudWindow.isShowing()) {
					actionBar.setRightEnable();
					cloudWindow.dismiss();
				}

				 list.clear();

				tag = false;

				type = 3;//线上取证
   			    mobileType = 0;
				pagerNumber=1;
				getDatas(keywork,type,mobileType,pagerNumber);
			}
			
		});
		 tv_file.setOnClickListener(new OnClickListener() {//确权文件
			
			@Override
			public void onClick(View arg0) {
				actionBar.setLeftText("确权文件");
				if (cloudWindow.isShowing()) {
					actionBar.setRightEnable();
					cloudWindow.dismiss();
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
				if (cloudWindow.isShowing()) {
					actionBar.setRightEnable();
					cloudWindow.dismiss();
				}
			}
		});
		cloudWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		cloudWindow.showAsDropDown(getActivity().findViewById(
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

	// 取消多选状态
	private void cancelChoose() {
		if (downLoadwindow.isShowing()) {
			downLoadwindow.dismiss();
		}
		adapter.setChoice(false);
		listView.invalidateViews();
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==CODE_SEARCH){
			
		}
	}



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
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == event.KEYCODE_BACK) {
			
			if (cloudWindow!=null && cloudWindow.isShowing()) {
				actionBar.setRightEnable();
				cloudWindow.dismiss();
				return true;
			}
			if (!CheckUtil.isEmpty(downLoadwindow)
					&& downLoadwindow.isShowing()) {
				cancelChoose();
				return true;
			}
			if(listView!=null){
				listView.onRefreshFinished();
				listView.onLoadFinished();
			}
		}
		return false;
	}
	/**
	 * 调接口获取数据
	 */
	private void getDatas(String keywork,final int type,final int mobileType,int pagerNumber) {
		showProgress("正在加载数据...");
		ApiManager.getInstance().getCloudEvidence(keywork, type, mobileType, pagerNumber, 10, new ApiCallback() {


			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
				//停止刷新
				/*xRefresh.stopRefresh();
				xRefresh.stopLoadMore();*/
				listView.onRefreshFinished();
				listView.onLoadFinished();
				hideProgress();
				
				CloudEvidenceBean bean = (CloudEvidenceBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						datas = bean.getDatas();
						
						if(!CheckUtil.isEmpty(datas)){
							list.addAll(datas);					   
						}			
//						LogUtils.e(type+"type");
						if(list.size()>=10){
							listView.setOnLoad(true);
						}else{
							listView.setOnLoad(false);
						}
						 adapter.notifyDataChange(list,type,mobileType);
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

	private int lastPosition;
	@Override
	public void update(int position) {
		if(position==adapter.getCount()-1){
			listView.setSelection(position);
		}
		
		if(position!=lastPosition){
			int fir=listView.getFirstVisiblePosition();
			int las=listView.getLastVisiblePosition();
			if(lastPosition>=fir&&lastPosition<=las){
				View view = listView.getChildAt(lastPosition - fir+2);
				if(view!=null){
					LinearLayout ll_option=	(LinearLayout) view.findViewById(R.id.ll_option);
					if(ll_option.getVisibility()==View.VISIBLE){
						ll_option.setVisibility(View.GONE);
						CloudEvidenceAdapter.ViewHolder vh=(com.truthso.ip360.adapter.CloudEvidenceAdapter.ViewHolder) view.getTag();
						vh.cb_option.setChecked(false);
					}		
				}					
			}			
		}
		lastPosition=position;
	}


/*	@Override
	public void toRefresh() {
		pagerNumber=1;
		list.clear();
		getDatas(searchText,type,mobileType,pagerNumber);
	}

	@Override
	public void toOnLoad() {
		Log.i("djj","haha");
		pagerNumber++;
		getDatas(searchText,type,mobileType,pagerNumber);
	}*/


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          Log.i("djj", position+"");
	}

	@Override
	public void onRefresh() {
		pagerNumber=1;
		list.clear();
		getDatas(searchText,type,mobileType,pagerNumber);
	}

	@Override
	public void onLoadMore() {
		pagerNumber++;
		getDatas(searchText,type,mobileType,pagerNumber);
	}

	@Override
	public void onRelease(float direction) {

	}

	@Override
	public void toOnLoad() {
		// TODO Auto-generated method stub
	
		pagerNumber++;
		getDatas(searchText,type,mobileType,pagerNumber);
	}

	@Override
	public void toRefresh() {
		// TODO Auto-generated method stub
		
		pagerNumber=1;
		list.clear();
		getDatas(searchText,type,mobileType,pagerNumber);
	}
}
