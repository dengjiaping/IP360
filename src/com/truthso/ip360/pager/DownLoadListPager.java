package com.truthso.ip360.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.truthso.ip360.adapter.DownLoadAdapter;
import com.truthso.ip360.adapter.UpLoadAdapter;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.updownload.UpLoadTashInfo;

public class DownLoadListPager extends BasePager {
	private ListView listView;
	private DownLoadAdapter adapter;
	private List<UpLoadTashInfo> list=new ArrayList<UpLoadTashInfo>();
	public DownLoadListPager(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView() {
		listView = new ListView(ctx);
		adapter=new DownLoadAdapter(ctx,list);
		listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；
		return listView;
	}

	@Override
	public void initData(int position) {
		
	}

	public void setChoice(boolean b) {
		adapter.setChoice(b);
		listView.invalidateViews();
	}
	public void setAllSelect(Boolean isAllSelect){
		adapter.setAllSelect(isAllSelect);
		listView.invalidateViews();
	}
	
	public void refresh(){
		list= UpLoadManager.getInstance().getList();
		adapter.notifyDataChanged(list);
	}
}
