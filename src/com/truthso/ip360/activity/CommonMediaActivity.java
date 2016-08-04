package com.truthso.ip360.activity;

import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.MainActionBar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
/**
 * 拍照，录像，录音，文件上传，截屏，网页存证公用的activity
 * @author wsx_summer  Email:wangshaoxia@truthso.com
 * @date 创建时间：2016年6月2日下午3:00:08
 * @version 1.0  
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public abstract class CommonMediaActivity extends Activity implements OnClickListener {
	
	private MainActionBar actionBar;
	private View line;
	private TextView tv_right_text,tv_left_text ;
	private ViewPager viewPager;
	private MyPageAdapter mPageAdapter;
	private int screanWidth;
	private TranslateAnimation moveLeft,moveRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common);
		
		actionBar = (MainActionBar)findViewById(R.id.actionbar);		
		actionBar.setActionBarOnClickListener(this);
		

		line = findViewById(R.id.line);

		tv_right_text=(TextView) findViewById(R.id.tv_right_text);
		tv_left_text=(TextView) findViewById(R.id.tv_left_text);
		
		viewPager=(ViewPager) findViewById(R.id.viewPager);
		mPageAdapter =new MyPageAdapter();
		viewPager.setAdapter(mPageAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if(position==0){
					line.startAnimation(moveLeft);
					tv_left_text.setTextColor(getResources().getColor(R.color.blue));
					tv_right_text.setTextColor(getResources().getColor(R.color.black));
				}else{
					line.startAnimation(moveRight);
					tv_right_text.setTextColor(getResources().getColor(R.color.blue));
					tv_left_text.setTextColor(getResources().getColor(R.color.black));
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		initData();
		initView();
		initAnimation();
	}
    public abstract void  initData();
    public abstract void  initView();

	private void initAnimation() {
		screanWidth=MyApplication.getInstance().getScreanWidth();
		moveLeft = new TranslateAnimation(screanWidth/2, 0, 0, 0);
		moveLeft.setDuration(500);
		moveLeft.setFillAfter(true);
		moveRight = new TranslateAnimation(0, screanWidth/2, 0, 0);
		moveRight.setDuration(500);
		moveRight.setFillAfter(true);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.acition_bar_left:
			finish();
			break;
		case R.id.acition_bar_right:
			showPopupWindow();
			break;
		case R.id.tv_left_text:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tv_right_text:
			viewPager.setCurrentItem(1);
			break;
		}
	}
	
	private void showPopupWindow() {
		
	}
	
	
	private class MyPageAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return  view==(object); 
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			View view=instantiateView(position);
			if (view != null) {
				container.addView(view);
			}
			
			return view;
		}
		
		
	}
     public abstract View instantiateView(int position);
	
	
}
