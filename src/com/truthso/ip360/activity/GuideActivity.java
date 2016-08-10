package com.truthso.ip360.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.truthso.ip360.adapter.GuideAdapter;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.utils.SharePreferenceUtil;

/**
 * 
 * 引导页
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-17下午4:31:06
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class GuideActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	private Context mContext;
	private List<View> list;
	private ViewPager mViewPager;
	private GuideAdapter mGuideAdapter;
	private final int[] guideImages = { R.drawable.guide01, R.drawable.guide02,
			R.drawable.guide03 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_layout);
		initData();
		initView();

	}

	private void initData() {
		mContext = this;
		list = new ArrayList<View>();
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		for (int i = 0; i < guideImages.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(params);
			imageView.setImageResource(guideImages[i]);
			imageView.setScaleType(ScaleType.FIT_XY);
			list.add(imageView);
			if (i == guideImages.length - 1) {
				imageView.setOnClickListener(this);
			}
		}
		mGuideAdapter = new GuideAdapter(list);
		mViewPager.setAdapter(mGuideAdapter);
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onClick(View arg0) {
		SharePreferenceUtil.saveOrUpdateAttribute(mContext,
				MyConstants.SP_ISFIRST_IN_TAG, MyConstants.APP_ISFIRST_IN,
				false);
		startActivity(new Intent(mContext, LoginActivity.class));
		finish();
		}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {

	}

}
