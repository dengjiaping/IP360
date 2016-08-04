package com.truthso.ip360.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @despriction :基类fragment
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午3:04:30
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public abstract class BaseFragment extends Fragment {
	protected View mLayout;
	protected FragmentManager fragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(setViewId(), container, false);
		fragmentManager = getFragmentManager();
		if (mLayout != null) {
			mLayout.setOnClickListener(null);
		}
		View view = mLayout;
		initView(view, inflater, container, savedInstanceState);
		return mLayout;
	}

	protected abstract void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	public abstract int setViewId();

	protected abstract void initData();

}
