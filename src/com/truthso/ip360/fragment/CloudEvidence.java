package com.truthso.ip360.fragment;

import com.truthso.ip360.activity.CategoryCloudEvidenceActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.SearchCloudEvidenceActivity;
import com.truthso.ip360.adapter.CloudEvidenceAdapter;
import com.truthso.ip360.view.MainActionBar;
import com.truthso.ip360.view.xrefreshview.XRefreshView;
import com.truthso.ip360.view.xrefreshview.XRefreshView.XRefreshViewListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @despriction :云端证据
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午2:49:04
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class CloudEvidence extends BaseFragment implements OnClickListener, OnItemClickListener, XRefreshViewListener {
	private MainActionBar actionBar;
	private ListView listView_cloudevidence;
	private CloudEvidenceAdapter adapter;
	private XRefreshView xRefresh;
	private int CODE_SEARCH=101;
	@Override
	protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		actionBar=(MainActionBar) view.findViewById(R.id.actionbar_cloudevidence);
		actionBar.setLeftText("类别");
		actionBar.setTitle("云端证据");
		actionBar.setRightText("多选");
		actionBar.setActionBarOnClickListener(this);
		
		xRefresh = (XRefreshView)view.findViewById(R.id.xrefresh_cloudevidence);
		xRefresh.setPullRefreshEnable(true);
		xRefresh.setPullLoadEnable(false);
		xRefresh.setAutoRefresh(false);
		xRefresh.setXRefreshViewListener(this);
		
		listView_cloudevidence=(ListView) view.findViewById(R.id.lv_cloudevidence);
		adapter = new CloudEvidenceAdapter(getActivity());
		listView_cloudevidence.setAdapter(adapter);
		View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_cloudevidence, null);
		listView_cloudevidence.addHeaderView(headView);
		listView_cloudevidence.setOnItemClickListener(this);
	}

	@Override
	public int setViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_clouddevidence;
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.acition_bar_right:
			choice();
			break;
		case R.id.acition_bar_left:
			startActivityForResult(new Intent(getActivity(), CategoryCloudEvidenceActivity.class), CODE_SEARCH);
			break;
		default:
			break;
		}
		
	}

	//点击多选按钮
	private void choice() {
		actionBar.setLeftText("全选");
		actionBar.setRightText("取消");
		actionBar.setActionBarOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.acition_bar_right://取消
					adapter.setChoice(false);
					listView_cloudevidence.invalidateViews();
					actionBar.setRightText("多选");
					actionBar.setLeftText("类别");
					actionBar.setActionBarOnClickListener(CloudEvidence.this);
					break;
		        case R.id.acition_bar_left://全选
		        	adapter.setAllSelect(true);
		        	listView_cloudevidence.invalidateViews();
					break;
				default:
					break;
				}
				
			}
		});
		adapter.setChoice(true);
		listView_cloudevidence.invalidateViews();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(position==0){
			startActivityForResult(new Intent(getActivity(), SearchCloudEvidenceActivity.class), CODE_SEARCH);
		}
		
	}

	@Override
	public void onRefresh() {
		handler.sendEmptyMessageDelayed(0, 1000);
	}

	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			xRefresh.stopRefresh();
		}
	};
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRelease(float direction) {
		// TODO Auto-generated method stub
		
	}

}
