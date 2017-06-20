package com.truthso.ip360.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.loopj.android.http.RequestHandle;
import com.truthso.ip360.adapter.CloudEvidenceAdapter;
import com.truthso.ip360.adapter.SearchCloudAdapter;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.CloudEvidenceBean;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SearchCloudEvidenceActivity extends NotTitleBaseActivity implements OnClickListener, OnItemClickListener {

	private EditText et_find_service;
private RelativeLayout rl_search_cloudevidence;
private ListView lv_content;
private List<CloudEviItemBean> list = new ArrayList<CloudEviItemBean>();
//private SearchCloudAdapter adapter;
private CloudEvidenceAdapter adapter;
private int type,mobileType;
private String from;//类型，取证类型
	private  int vCode;
	@Override
	public void initData() {
		type=getIntent().getIntExtra("type", 0);
		mobileType=getIntent().getIntExtra("mobileType", 0);
		from=getIntent().getStringExtra("from");
	}

	@Override
	public void initView() {
		vCode = getVersion();
		et_find_service = (EditText) findViewById(R.id.et_find_service);
		rl_search_cloudevidence=(RelativeLayout) findViewById(R.id.rl_search_cloudevidence);
		rl_search_cloudevidence.setOnClickListener(this);
	
		lv_content=(ListView) findViewById(R.id.lv_content);
		//adapter=new SearchCloudAdapter(this,list);
		//加了type
		adapter=new CloudEvidenceAdapter(this, list, mobileType,type,0);
		lv_content.setAdapter(adapter);
		lv_content.setOnItemClickListener(this);
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
					adapter.clearData();
					lv_content.setVisibility(View.GONE);
					
				} else {
					lv_content.setVisibility(View.VISIBLE);
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
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if(from.equals("cloud")){
				Log.i("djj", (String)(msg.obj)+type+mobileType);
				getDatas((String)(msg.obj),type,mobileType,1);
			}else{
				//搜索本地证据
			}
		};

	};
	private RequestHandle requestHandle;
	
	
	@Override
	public int setLayout() {

		return R.layout.activity_search_cloudevidence;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_search_cloudevidence:
			finish();
			break;

		default:
			break;
		}
		
	}

	/**
	 * 调接口获取数据
	 */
	private void getDatas(String keywork,final int type,final int mobileType,int pagerNumber) {
		requestHandle = ApiManager.getInstance().getCloudEvidence(keywork, type, mobileType, pagerNumber, 10,vCode, new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message,
					BaseHttpResponse response) {
							
				CloudEvidenceBean bean = (CloudEvidenceBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						List<CloudEviItemBean> datas = bean.getDatas();
						
						if(!CheckUtil.isEmpty(datas)){
							Log.i("djj", datas.size()+"");
							list.addAll(datas);					   
						}			
						 adapter.notifyDataChange(list,type,mobileType);
					}else{
						Toaster.showToast(SearchCloudEvidenceActivity.this, bean.getMsg());
					}
				}else{
					Toaster.showToast(SearchCloudEvidenceActivity.this, "数据加载失败请刷新重试");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CloudEviItemBean cloudEviItemBean = list.get(position);
		
	
	}
	private int getVersion() {
		try {
			// 通过PackageManager获取安装包信息
			PackageInfo packageInfo = MyApplication.getInstance().getPackageManager().getPackageInfo(
					MyApplication.getInstance().getPackageName(), 0);

			// 返回版本信息
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			return 0;
		}

	}
}
