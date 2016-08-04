package com.truthso.ip360.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.truthso.ip360.activity.CategoryCloudEvidenceActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.SearchCloudEvidenceActivity;
import com.truthso.ip360.adapter.NativeEvidenceAdapter;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.view.MainActionBar;

/**
 * @despriction :本地证据
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午2:52:13
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class NativeEvidence extends BaseFragment implements OnClickListener, OnItemClickListener {
	private MainActionBar actionBar;
	private ListView listView;
	private int CODE_SEARCH=101;
	private NativeEvidenceAdapter adapter;
	private   List<DbBean> mDatas = new ArrayList<DbBean>();
	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		actionBar = (MainActionBar) view.findViewById(R.id.actionbar_nativeevidence);
		actionBar.setLeftText("类别");
		actionBar.setTitle("本地证据");
		actionBar.setRightText("多选");
		actionBar.setActionBarOnClickListener(this);
		
		listView=(ListView) view.findViewById(R.id.lv_nativeevidence);
		mDatas = GroupDao.getInstance(getActivity()).queryAll();
		
		adapter = new NativeEvidenceAdapter(getActivity(), mDatas, R.layout.item_native_evidence);
		listView.setAdapter(adapter);
		
		View headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_cloudevidence, null);
		listView.addHeaderView(headView);
		listView.setOnItemClickListener(this);
		
	}

	@Override
	public int setViewId() {
		return R.layout.fragment_native_evidence;
	}
	
	@Override
	protected void initData() {
		
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
						listView.invalidateViews();
						actionBar.setRightText("多选");
						actionBar.setLeftText("类别");
						actionBar.setActionBarOnClickListener(NativeEvidence.this);
						break;
			        case R.id.acition_bar_left://全选
			        	adapter.setAllSelect(true);
			        	listView.invalidateViews();
						break;
					default:
						break;
					}
					
				}
			});
			adapter.setChoice(true);
			listView.invalidateViews();
		}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position==0){
			startActivityForResult(new Intent(getActivity(), SearchCloudEvidenceActivity.class), CODE_SEARCH);
		}
	}

	
}
