package com.truthso.ip360.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.adapter.CommonAdapter;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.pager.BasePager;
import com.truthso.ip360.pager.DownLoadListPager;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.MainActionBar;

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
	private RelativeLayout rl_left, rl_right;
//	private CommonAdapter<DbBean> adapter;
//	private ListView listView;
	private List<DbBean> mDatas;
	private List<BasePager> pagerList;
    private int position;
	@Override
	protected void initView(View view, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		actionBar = (MainActionBar) view
				.findViewById(R.id.actionbar_tranlist);
		actionBar.setTitle("传输列表");
		actionBar.setRightText("选择");
		actionBar.setActionBarOnClickListener(this);
		
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

		pagerList = new ArrayList<BasePager>();
		pagerList.add(new DownLoadListPager(getActivity()));
		pagerList.add(new DownLoadListPager(getActivity()));
	
		
		mPageAdapter = new MyPageAdapter();
		viewPager.setAdapter(mPageAdapter);
		/*// 初始化viewPager 中第一页的数据
		pagerList.get(0).initData(0);*/  
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				TransList.this.position=position;
				viewPager.setCurrentItem(position);
				// 初始化本页数据
				pagerList.get(position).initData(position);
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

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acition_bar_right:
			choice();
			break;
		case R.id.acition_bar_left:
			// actionBar.setRightEnable();

			break;
		case R.id.tv_left_text:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tv_right_text:
			viewPager.setCurrentItem(1);
			break;

		}
	}

	// 点击多选按钮
	private void choice() {
	  currentPager = (DownLoadListPager) pagerList.get(position);						
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
					currentPager.setAllSelect(true);
					break;
				default:
					break;
				}

			}

		});
		currentPager.setChoice(true);
		showDownLoadPop();
	}

	//取消选择
	private void cancelChoose() {
		currentPager.setChoice(false);
		actionBar.setRightText("选择");
		actionBar.setLeftText("");
		actionBar.setActionBarOnClickListener(TransList.this);
		if (downLoadwindow.isShowing()) {
			downLoadwindow.dismiss();
		}
	}
	
	
	private PopupWindow downLoadwindow;
	private View contentView;
	private DownLoadListPager currentPager;
	// 显示底部下载按钮
		private void showDownLoadPop() {
			if(CheckUtil.isEmpty(downLoadwindow)){
			contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_download, null);
				downLoadwindow = new PopupWindow(contentView,
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}		
			// 进入退出的动画
			// downLoadwindow.setAnimationStyle(R.style.mypopwindow_anim_style);
			downLoadwindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
		}
		
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			
				if (!CheckUtil.isEmpty(downLoadwindow)
						&& downLoadwindow.isShowing()) {
					cancelChoose();
					return true;
				}
		
			return super.onKeyDown(keyCode, event);
		}

	private class MyPageAdapter extends PagerAdapter {

		

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view ==(View)object;
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
