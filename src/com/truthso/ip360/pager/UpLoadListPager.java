package com.truthso.ip360.pager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.truthso.ip360.adapter.UpLoadAdapter;

public class UpLoadListPager extends BasePager {
	private ListView listView;
	private UpLoadAdapter adapter;
	public UpLoadListPager(Context ctx) {
		super(ctx);
	}

	@Override
	public void initData(int position) {
	
	}

	@Override
	public View initView() {
		listView = new ListView(ctx);			
		adapter=new UpLoadAdapter(ctx);
	    listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；
	
		return listView;
	}
	
	public void refresh(){
		adapter.notifyDataChanged();
	}
	
	
	public void setChoice(boolean b) {
		adapter.setChoice(b);
		listView.invalidateViews();
	}
	public void setAllSelect(Boolean isAllSelect){
		adapter.setAllSelect(isAllSelect);
		listView.invalidateViews();
	}
}
