package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.truthso.ip360.utils.ImageLoader;

/**
 * @Description: 图片适配器
 * @author http://blog.csdn.net/finddreams
 */ 
public class ImagePagerAdapter extends BaseAdapter {

	private Context context;
	private int size;
	private boolean isInfiniteLoop;
	private ImageLoader imageLoader;
//	private DisplayImageOptions options;
	private List<Integer> imageList = new ArrayList<Integer>();

	public ImagePagerAdapter(Context context, List<Integer> list) {
		this.context = context;
		
		this.size = list.size();
		this.imageList=list;
		isInfiniteLoop = false;
		// 初始化imageLoader 否则会报错
//		imageLoader = ImageLoader.getInstance();
//		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//		options = ImageLoaderUtil.setOptions(R.drawable.ablogo01, R.drawable.ablogo01, R.drawable.ablogo01, 0, true, true);
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : imageList.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
	
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = new ImageView(context);
			holder.imageView.setBackgroundColor(0xffffff);
			holder.imageView
					.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
//		imageLoader.displayImage(imageUrlList.get(getPosition(position)),holder.imageView, options);
		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//SkipPageLogicUtil.getInstance().getHomeModeEntry(topCarouselArea, getPosition(position));

			}
		});
		holder.imageView.setBackgroundResource(imageList.get(getPosition(position)));
		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
