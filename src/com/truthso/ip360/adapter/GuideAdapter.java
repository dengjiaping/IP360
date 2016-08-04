package com.truthso.ip360.adapter;


import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 
 * 引导页的adapter
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-21上午11:02:59
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class GuideAdapter extends PagerAdapter {

	// 引导的界面
	private List<View> list;

	public GuideAdapter(List<View> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		if (null != list) {
			return list.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	// 销毁界面
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(list.get(position));
	}

	// 初始化界面
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(list.get(position), 0);
		return list.get(position);
	}

}
