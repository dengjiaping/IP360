package com.truthso.ip360.pager;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.truthso.ip360.adapter.UpLoadAdapter;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.updownload.UpLoadInfo;

public class UpLoadListPager extends BasePager {
	private ListView listView;
	private UpLoadAdapter adapter;
	private UpDownLoadDao dao;
	private List<UpLoadInfo> queryUpLoadList;
	public UpLoadListPager(Context ctx) {
		super(ctx);
	}

	@Override
	public void initData(int position) {
		dao=UpDownLoadDao.getDao();
		queryUpLoadList = dao.queryUpLoadList();
	}

	@Override
	public View initView() {
		listView = new ListView(ctx);	
		if(queryUpLoadList!=null&&queryUpLoadList.size()>0){
			adapter=new UpLoadAdapter(ctx,queryUpLoadList);
		    listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；
		}
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
