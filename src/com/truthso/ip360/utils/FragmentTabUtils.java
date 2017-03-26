package com.truthso.ip360.utils;

import java.util.List;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.fragment.PersonalCenter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FragmentTabUtils implements RadioGroup.OnCheckedChangeListener {
	private List<Fragment> fragments; // �?个tab页面对应�?个Fragment
	private RadioGroup rgs; // 用于切换tab
	private FragmentManager fragmentManager; // Fragment�?属的Activity
	private int fragmentContentId; // Activity中所要被替换的区域的id
	private int currentTab; // 当前Tab页面索引
	private PersonalCenter pc;
	private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用�?�在切换tab时�?�增加新的功�?

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public FragmentTabUtils(FragmentManager fragmentManager,
			List<Fragment> fragmentList, int fragmentContentId, RadioGroup rgs) {
		this.fragments = fragmentList;
		this.rgs = rgs;
		this.fragmentManager = fragmentManager;
		this.fragmentContentId = fragmentContentId;
		// 默认显示第一�?
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.add(fragmentContentId, fragmentList.get(0));
		ft.commit();
		rgs.setOnCheckedChangeListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		for (int i = 0; i < rgs.getChildCount(); i++) {
			((RadioButton)rgs.getChildAt(i)).setTextColor(Color.parseColor("#868688"));
			if (rgs.getChildAt(i).getId() == checkedId) {
				((RadioButton)rgs.getChildAt(i)).setTextColor(Color.parseColor("#a12935"));
				Fragment fragment = fragments.get(i);

				FragmentTransaction ft = obtainFragmentTransaction(i);

				// getCurrentFragment().onPause(); // 暂停当前tab
				getCurrentFragment().onStop(); // 暂停当前tab
				if (fragment.isAdded()) {
					fragment.onStart(); // 启动目标tab的fragment onStart()
					// fragment.onResume(); // 启动目标tab的onResum
					// e()
				} else {
					ft.add(fragmentContentId, fragment, "fragment");
					ft.commit();
				}
				showTab(i); // 显示目标tab

				// 如果设置了切换tab额外功能功能接口
				if (null != onRgsExtraCheckedChangedListener) {
					onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(radioGroup, checkedId, i);
				
				}

			}

		}

	}

	/**
	 * 切换tab
	 * 
	 * @param idx
	 */
	private void showTab(int idx) {
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			FragmentTransaction ft = obtainFragmentTransaction(idx);
			if (idx == i) {
				ft.show(fragment);
			} else {
				ft.hide(fragment);
			}
			ft.commit();
		}
		currentTab = idx; // 更新目标tab为当前tab
	}

	/**
	 * 获取�?个带动画的FragmentTransaction
	 * 
	 * @param index
	 * @return
	 */
	private FragmentTransaction obtainFragmentTransaction(int index) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// 设置切换动画
		// if (index > currentTab) {
		// ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
		// } else {
		// ft.setCustomAnimations(R.anim.slide_right_in,
		// R.anim.slide_right_out);
		// }
		return ft;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
		return onRgsExtraCheckedChangedListener;
	}

	public void setOnRgsExtraCheckedChangedListener(
			OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
		this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
	}

	/**
	 * 切换tab额外功能功能接口
	 */
	public static interface OnRgsExtraCheckedChangedListener {
		public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
				int checkedId, int index);
	}

}
