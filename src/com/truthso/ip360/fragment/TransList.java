package com.truthso.ip360.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.adapter.CommonAdapter;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.viewholder.ViewHolder;

/**
 * @despriction :传输列表的fragment
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-21下午2:53:56
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class TransList extends BaseFragment implements OnClickListener {
	private Button btn_title_right;
	private Button btn_title_left;
	private View line;
	private TextView tv_right_text, tv_left_text;
	private ViewPager viewPager;
	private MyPageAdapter mPageAdapter;
	private int screanWidth;
	private TranslateAnimation moveLeft, moveRight;
	private RelativeLayout rl_left, rl_right;
	private CommonAdapter<DbBean> adapter;
	private ListView listView;
	private List<DbBean> mDatas;
	
	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		btn_title_right = (Button) view.findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.VISIBLE);
		btn_title_right.setText("选择");
		btn_title_right.setOnClickListener(this);

		btn_title_left = (Button) view.findViewById(R.id.btn_title_left);
		btn_title_left.setVisibility(View.INVISIBLE);
		btn_title_left.setOnClickListener(this);

		line = view.findViewById(R.id.line);
		rl_left = (RelativeLayout) view.findViewById(R.id.rl_left);
		rl_right = (RelativeLayout) view.findViewById(R.id.rl_right);

		tv_right_text = (TextView) view.findViewById(R.id.tv_right_text);
		tv_left_text = (TextView) view.findViewById(R.id.tv_left_text);

		// line.startAnimation(moveLeft);
		rl_left.setBackgroundColor(getResources().getColor(
				R.color.title_bg_color));
		tv_left_text.setTextColor(getResources().getColor(R.color.white));
		rl_right.setBackgroundColor(getResources().getColor(R.color.white));
		tv_right_text.setTextColor(getResources().getColor(R.color.black));

		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mPageAdapter = new MyPageAdapter();
		viewPager.setAdapter(mPageAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if (position == 0) {
					line.startAnimation(moveLeft);
					rl_left.setBackgroundColor(getResources().getColor(
							R.color.title_bg_color));
					tv_left_text.setTextColor(getResources().getColor(
							R.color.white));
					rl_right.setBackgroundColor(getResources().getColor(
							R.color.white));
					tv_right_text.setTextColor(getResources().getColor(
							R.color.black));
				} else {
					line.startAnimation(moveRight);
					rl_right.setBackgroundColor(getResources().getColor(
							R.color.title_bg_color));
					tv_right_text.setTextColor(getResources().getColor(
							R.color.white));
					rl_left.setBackgroundColor(getResources().getColor(
							R.color.white));
					tv_left_text.setTextColor(getResources().getColor(
							R.color.black));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		initAnimation();
	}

	private void initAnimation() {
		screanWidth = MyApplication.getInstance().getScreanWidth();
		moveLeft = new TranslateAnimation(screanWidth / 2, 0, 0, 0);
		moveLeft.setDuration(500);
		moveLeft.setFillAfter(true);
		moveRight = new TranslateAnimation(0, screanWidth / 2, 0, 0);
		moveRight.setDuration(500);
		moveRight.setFillAfter(true);
	}

	@Override
	public int setViewId() {
		return R.layout.home_translist;
	}

	@Override
	protected void initData() {
//		mDatas = GroupDao.getInstance(getActivity()).queryAll();
//		adapter = new CommonAdapter<DbBean>(getActivity(),mDatas,R.layout.item_native_evidence) {
//
//			@Override
//			public void convert(ViewHolder helper, DbBean item, int position) {
//				
//			}
//		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_right:// 选择
			btn_title_left.setVisibility(View.VISIBLE);
			btn_title_left.setText("全选");
			btn_title_right.setText("取消");

			break;
		case R.id.btn_title_left:// 全选

			break;
		case R.id.tv_left_text:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tv_right_text:
			viewPager.setCurrentItem(1);
			break;

		}
	}

	private class MyPageAdapter extends PagerAdapter {

		private ListView listView;

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return null;

//			listView = new ListView(getActivity());
//			if (position == 0) {
//				listView.setAdapter(adapter);
//			}
//			return listView;
		}

	}
}
