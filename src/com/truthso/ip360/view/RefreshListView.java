package com.truthso.ip360.view;

import java.lang.ref.WeakReference;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.truthso.ip360.view.xrefreshview.XRefreshViewFooter;
import com.truthso.ip360.view.xrefreshview.XRefreshViewHeader;
import com.truthso.ip360.view.xrefreshview.XRefreshViewState;


/**
 * 自定义拉下刷新ListView，常规箭头旋转，系统进度条，无回弹动画
 * @author 
 * @Date 2014-4-28
 * @ClassInfo com.zf.pulldown_listview.cusview-MyCusListView.java
 * @Description
 */
public class RefreshListView extends ListView implements OnScrollListener {
	private static final String TAG = "MyCusListView===>";
	/** 操作状态:下拉刚开始、回退到顶、一次刷新结束 */
	private static final int DONE = 0x1;
	/** 操作状态:松开即可刷新 */
	private final static int RELEASE_TO_REFRESH = 0x2;
	/** 操作状态:下拉可以刷新 */
	private final static int PULL_TO_REFRESH = 0x3;
	/** 操作状态:正在刷新 */
	private final static int REFRESHING = 0x4;
	/** 自定义ListView头布局 */
	private XRefreshViewHeader headView;
	/** headView宽 */
	private int headContentWidth;
	/** headView高 */
	private int headContentHeight;
	/** 标识各种刷新状态 */
	private int refreshState;
	private int onLoadState;
	/** 首次触摸屏幕设为true,松手时设为false,控制一次触摸事件的记录状态 */
	private boolean isRecored = false;
	/** 手指首次触摸屏幕时Y位置 */
	private int startY;
	/** 手指移动的距离和headView的padding距离的比例,防止移动时headView下拉过长 */
	private final static int RATIO = 3;
	/** 表示已经下拉到可以刷新状态,可以拉回 */
	private boolean isBack = false;
	/** 刷新监听回调接口 */
	private OnRefreshListener refreshListener;
	private OnloadListener onLoadListener;
	/** 列表在屏幕顶端第一个完整可见项的position */
	private int firstItemIndex;
	//是否为最后一个item
	private boolean isLastItem;
	private XRefreshViewFooter footerView;
	private int footerContentWidth;
	private int footerContentHeight;
	private FrameLayout footerLayoutHolder;
	private int FOOTER_VISIBLE = 0x5;
	private int FOOTER_LOADING = 0x6;
	private int FOOTER_HIDE = 0x7;
	private boolean isOnLoad;
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);		
		init(context);
	}

	/** 初始化 */
	private void init(Context context) {
		Log.i(TAG, "init()...");
		// 获取自定义头view
		onLoadState = FOOTER_HIDE;
		headView = new XRefreshViewHeader(context);
		footerView = new XRefreshViewFooter(context);
		footerLayoutHolder = new FrameLayout(context);
		footerLayoutHolder.addView(footerView, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
		    FrameLayout.LayoutParams.WRAP_CONTENT));
		
//		 预估headView宽高
		measureView(headView);
		measureView(footerView);
		// 获取headView宽高
		headContentWidth = headView.getMeasuredWidth();
		headContentHeight = headView.getMeasuredHeight();
		footerContentWidth = footerView.getMeasuredWidth();
		footerContentHeight = footerView.getMeasuredHeight();
		Log.i(TAG, "headView宽:[" + headContentWidth + "],高:["
				+ headContentHeight + "]");
		// 设置headView的padding值,topPadding为其本身的负值,达到在屏幕中隐藏的效果
		headView.setPadding(0, -headContentHeight, 0, 0);
//		footerView.setPadding(0, -footerContentHeight, 0, 0);
		footerView.setVisibility(View.GONE);
		// 重绘headView
		headView.invalidate();
		// 将headView添加到自定义的ListView头部
		this.addHeaderView(headView, null, false);
		this.addFooterView(footerLayoutHolder, null, false);
//		footerView.setVisibility(View.GONE);
		// 设置ListView的滑动监听
		this.setOnScrollListener(this);
		// 初始刷新状态
		refreshState = DONE;
	}

	/**
	 * 预估headView的宽高
	 * 
	 * @param child
	 */
	private void measureView(View child) {
		Log.i(TAG, "measureView()...");
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 记录滚动时列表第一个完整可见项的position
		firstItemIndex = firstVisibleItem;
		 // 判断是否滚动到底部  
        if (view.getLastVisiblePosition() == view.getCount() - 1) {  
            //加载更多功能的代码  
        	isLastItem=true;
        }  
	}

	/** 监听触摸事件 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:// 第一次触摸时
			if (firstItemIndex == 0) {
				// 开始记录
				isRecored = true;
				// 获取首次Y位置
				startY = (int) ev.getY();
				Log.i(TAG, "从首次触摸点就开始记录...");
			} else {
				Log.i(TAG, "首次触摸时firstItemIndex不为0,不执行下拉刷新");
			}
			Log.i(TAG, "记录状态isRecored:" + isRecored);
			break;
		case MotionEvent.ACTION_UP:// 松开屏幕时
			// 移除记录
			isRecored = false;
			Log.i(TAG, "停止记录..." + ",isRecored:" + isRecored);
			if (refreshState == PULL_TO_REFRESH) {
				refreshState = DONE;
				changeHeadView();
				Log.i(TAG, "PULL_TO_REFRESH状态松手,回到原始状态");
			} else if (refreshState == RELEASE_TO_REFRESH) {
				refreshState = REFRESHING;
				changeHeadView();
				onRefreshing();
				Log.i(TAG, "RELEASE_TO_REFRESH状态松手,进入REFRESHING状态");
			} else if (refreshState == REFRESHING) {
				if (firstItemIndex == 0) {
					// 保持刷新状态
					headView.setPadding(0, 0, 0, 0);
					Log.i(TAG, "REFRESHING状态松手,保持该状态,headView仍在顶部");
				} else {
					Log.i(TAG, "REFRESHING状态松手,保持该状态,headView被推出顶部");
				}
			}
			//上拉加载更多
			if(onLoadState == FOOTER_VISIBLE&&isLastItem==true){
				footerView.setVisibility(View.VISIBLE);
				footerView.loading();
				onLoadState = FOOTER_LOADING;
				onloading();
			}
			break;
		case MotionEvent.ACTION_MOVE:// 手势移动时
			// 记录实时的手指移动时在屏幕的Y位置,用于和startY比较
			int curY = (int) ev.getY();

			
			//设置下拉刷新
			setRefreshState(curY);
			//设置上拉加载更多
			setOnLoadState(curY);
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void setRefreshState(int curY) {
		// TODO Auto-generated method stub
		if (!isRecored && firstItemIndex == 0) {
			isRecored = true;
			startY = curY;
		}

		if (isRecored) {
			// 开始或结束状态
			if (refreshState == DONE) {
				if (curY - startY > 0) {// 表示向下拉了
					if(firstItemIndex==0){
						// 状态改为下拉刷新
						refreshState = PULL_TO_REFRESH;
						changeHeadView();
					}				
				}
			}

			// 下拉刷新状态
			if (refreshState == PULL_TO_REFRESH) {
				setSelection(0);
				// 不断改变headView的高度
				headView.setPadding(0, (curY - startY) / RATIO
						- headContentHeight, 0, 0);
				// 下拉到RELEASE_TO_REFRESH状态
				if ((curY - startY) / RATIO >= headContentHeight) {
					refreshState = RELEASE_TO_REFRESH;
					isBack = true;
					changeHeadView();
				} else if ((curY - startY) <= 0) {
					// 上推到顶
					refreshState = DONE;
					changeHeadView();
				}
			}

			// 松手可以刷新状态
			if (refreshState == RELEASE_TO_REFRESH) {
				setSelection(0);
				// 不断改变headView的高度
				headView.setPadding(0, (curY - startY) / RATIO
						- headContentHeight, 0, 0);
				// 又往上推
				if ((curY - startY) / RATIO < headContentHeight) {
					refreshState = PULL_TO_REFRESH;
					changeHeadView();
				}
			}

			// 正在刷新状态
			if (refreshState == REFRESHING) {
				if (curY - startY > 0) {
					// 只改变padding值,不做其余处理
					headView.setPadding(0, (curY - startY) / RATIO, 0, 0);
				}
			}
		}
		
	}

	private void setOnLoadState(int curY) {
		if (!isRecored && isLastItem==true) {
			isRecored = true;
			startY = curY;
		}
		if(isRecored && isLastItem==true&&isOnLoad==true){
		Log.i("MyCusListView===>", curY - startY+"");
			if (curY - startY < -120&&onLoadState==FOOTER_HIDE) {// 表示向上拉了
					// 状态改为下拉刷新
					footerView.setVisibility(View.VISIBLE);
//					footerView.show();
					onLoadState = FOOTER_VISIBLE;
					
			}
		}
//		startY = curY;
	}

	/** 进入刷新的方法 */
	private void onRefreshing() {
		// 调用回调接口中的刷新方法
		if (refreshListener != null) {
			refreshListener.toRefresh();
		}
	}
	/** 进入上拉刷新的方法 */
	private void onloading() {
		// 调用回调接口中的刷新方法
		if (onLoadListener != null) {
			onLoadListener.toOnLoad();
		}
	}

	public boolean isOnLoad() {
		return isOnLoad;
	}

	public void setOnLoad(boolean isOnLoad) {
		this.isOnLoad = isOnLoad;
	}

	/** 使用界面传递给此ListView的回调接口,用于两者间通信 */
	public interface OnRefreshListener {
		public void toRefresh();
	}
	public interface OnloadListener{
		public void toOnLoad();
	}
	/**
	 * 注册一个用于刷新的回调接口
	 * 
	 * @param refreshListener
	 */
	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		// 获取传递过来的回调接口
		this.refreshListener = refreshListener;
	}
	/**
	 * 注册一个用于上拉刷新的回调接口
	 * 
	 * @param refreshListener
	 */
	public void setOnLoadListener(OnloadListener onLoadListener) {
		isOnLoad = true;
		// 获取传递过来的回调接口
		this.onLoadListener = onLoadListener;
	}
	/** 使用界面执行完上拉刷新操作时调用此方法 */
	public void onRefreshFinished() {
		refreshState = DONE;
		changeHeadView();
	}
	/** 使用界面执行完刷新操作时调用此方法 */
	public void onLoadFinished() {
		footerView.setVisibility(View.GONE);
		onLoadState = FOOTER_HIDE;
		isLastItem = false;

	}

	/** 根据下拉的状态改变headView */
	private void changeHeadView() {
		switch (refreshState) {
		case DONE:// 开始或结束状态
			Log.i(TAG, "当前状态:DONE");
			// 回退状态清除
			isBack = false;
			// 回复原始高度
			headView.setPadding(0, -headContentHeight, 0, 0);
			headView.setState(XRefreshViewState.STATE_NORMAL);
			break;
		case PULL_TO_REFRESH:// 下拉刷新状态
			Log.i(TAG, "当前状态:PULL_TO_REFRESH");
			// 从RELEASE_TO_REFRESH回到PULL_TO_REFRESH状态
			Log.i(TAG, "是否从松开刷新回到下拉刷新...isBack:" + isBack);
			if (isBack) {
				headView.setState(XRefreshViewState.STATE_NORMAL);
			}
			break;
		case RELEASE_TO_REFRESH:
			Log.i(TAG, "当前状态:RELEASE_TO_REFRESH");
			headView.setState(XRefreshViewState.STATE_READY);
			break;
		case REFRESHING:
			// 保持headView在屏幕顶端显示
			headView.setPadding(0, 0, 0, 0);
			headView.setState(XRefreshViewState.STATE_REFRESHING);
			Log.i(TAG, "当前状态:REFRESHING");
			break;
		}
	}
	public int getFirstItemIndex() {
		return firstItemIndex;
	}

	public void setFirstItemIndex(int firstItemIndex) {
		this.firstItemIndex = firstItemIndex;
	}
	
	public boolean isLastItem() {
		return isLastItem;
	}

	public void setLastItem(boolean isLastItem) {
		this.isLastItem = isLastItem;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}
}
