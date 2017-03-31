package com.truthso.ip360.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.adapter.CommonAdapter;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.event.DownEvent;
import com.truthso.ip360.event.UpEvent;
import com.truthso.ip360.pager.BasePager;
import com.truthso.ip360.pager.DownLoadListPager;
import com.truthso.ip360.pager.UpLoadListPager;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.DisplayUtil;
import com.truthso.ip360.view.MainActionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @despriction :传输列表的fragment
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午2:53:56
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class TransList extends BaseFragment implements OnClickListener {
	private MainActionBar actionBar;
	private View line;
	private TextView tv_right_text, tv_left_text;
	private ViewPager viewPager;
	private MyPageAdapter mPageAdapter;
	private int screanWidth;
	private TranslateAnimation moveLeft, moveRight;
	// private CommonAdapter<DbBean> adapter;
	// private ListView listView;
	private List<DbBean> mDatas;
	private List<BasePager> pagerList;
	private int position;
	private boolean isDownEmpty,isUpEmpty;

	@Override
	protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		actionBar = (MainActionBar) view.findViewById(R.id.actionbar_tranlist);
		actionBar.setTitle("传输列表");
		actionBar.setRightGone();
		actionBar.setActionBarOnClickListener(this);

		line = view.findViewById(R.id.line);
		line.setLayoutParams(new LinearLayout.LayoutParams(DisplayUtil.getScreenWidth(getActivity())/2,DisplayUtil.dip2px(getActivity(),3)));

		tv_right_text = (TextView) view.findViewById(R.id.tv_right_text);
		tv_left_text = (TextView) view.findViewById(R.id.tv_left_text);
		tv_left_text.setOnClickListener(this);
		tv_right_text.setOnClickListener(this);
		// line.startAnimation(moveLeft);
		tv_left_text.setTextColor(getResources().getColor(R.color.button_color));
		tv_right_text.setTextColor(getResources().getColor(R.color.black));


		viewPager = (ViewPager) view.findViewById(R.id.viewPager);

		downLoadListPager = new DownLoadListPager(getActivity());
		upLoadListPager = new UpLoadListPager(getActivity());
		pagerList = new ArrayList<BasePager>();
		pagerList.add(upLoadListPager);
		pagerList.add(downLoadListPager);

		mPageAdapter = new MyPageAdapter();
		viewPager.setAdapter(mPageAdapter);

		 // 初始化viewPager 中第一页的数据 pagerList.get(0).initData(0);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				TransList.this.position = position;
				viewPager.setCurrentItem(position);
				// 初始化本页数据
			pagerList.get(position).initData(position);
				if (position == 1) {//下载
					line.startAnimation(moveRight);
					tv_right_text.setTextColor(getResources().getColor(R.color.button_color));
					tv_left_text.setTextColor(getResources().getColor(R.color.black));
				} else {//上传不让用户删除
					line.startAnimation(moveLeft);
					tv_left_text.setTextColor(getResources().getColor(R.color.button_color));
					tv_right_text.setTextColor(getResources().getColor(R.color.black));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		initAnimation();
	}

	private void initAnimation() {
		screanWidth = MyApplication.getInstance().getScreanWidth();
		moveLeft = new TranslateAnimation(screanWidth / 2, 0, 0, 0);
		moveLeft.setDuration(300);
		moveLeft.setFillAfter(true);
		moveRight = new TranslateAnimation(0, screanWidth / 2, 0, 0);
		moveRight.setDuration(300);
		moveRight.setFillAfter(true);
	}


	@Override
	public int setViewId() {
		return R.layout.home_translist;
	}

	@Override
	protected void initData() {
		EventBus.getDefault().register(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_left_text:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tv_right_text:
			viewPager.setCurrentItem(1);
			break;
		}
	}


	private DownLoadListPager downLoadListPager;
	private UpLoadListPager upLoadListPager;

	private class MyPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (View) object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// 获得 viewPager 中的一个页面
			View view = pagerList.get(position).getView();
			container.addView(view);

			return view;
		}

	}

}
