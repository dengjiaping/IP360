package com.truthso.ip360.pager;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.truthso.ip360.adapter.DownLoadAdapter;
import com.truthso.ip360.bean.DownLoadInfo;
import com.truthso.ip360.dao.UpDownLoadDao;

public class DownLoadListPager extends BasePager {
	private ListView listView;
	private DownLoadAdapter adapter;
	private List<DownLoadInfo> queryDownLoadList;
	public DownLoadListPager(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView() {
		listView = new ListView(ctx);
		if(queryDownLoadList!=null&&queryDownLoadList.size()>0){			
			adapter=new DownLoadAdapter(ctx,queryDownLoadList);
			listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；
		}	
		return listView;
	}

	@Override
	public void initData(int position) {
		queryDownLoadList = UpDownLoadDao.getDao().queryDownLoadList();
	}

	public void setChoice(boolean b) {
		adapter.setChoice(b);
		listView.invalidateViews();
	}
	

	@Override
	public void setAllSelect(boolean isAllSelect) {
		// TODO Auto-generated method stub
		adapter.setAllSelect(isAllSelect);
		listView.invalidateViews();
	}
	
}
