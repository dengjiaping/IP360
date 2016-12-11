package com.truthso.ip360.pager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.truthso.ip360.adapter.UpLoadAdapter;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.utils.CheckUtil;

import org.greenrobot.eventbus.EventBus;

public class UpLoadListPager extends BasePager {
	private ListView listView;
	private UpLoadAdapter adapter;
	private UpDownLoadDao dao;
	private List<FileInfo> list;

	public UpLoadListPager(Context ctx) {
		super(ctx);
	}

	@Override
	public void initData(int position) {
		
	}

	@Override
	public View initView() {
		dao=UpDownLoadDao.getDao();
		list=new ArrayList<FileInfo>();
		List<FileInfo> queryUpLoadList = dao.queryUpLoadList();
		if(queryUpLoadList!=null){
			list.addAll(queryUpLoadList); 			
		}        
		
		listView = new ListView(ctx);

		if(list.size()>0){
			EventBus.getDefault().post(true);
		}else{
			EventBus.getDefault().post(false);
		}
		adapter=new UpLoadAdapter(ctx,list);
		listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；
	
		ctx.getContentResolver().registerContentObserver(Uri.parse("content://com.truthso.ip360/updownloadlog/up"), true, new MyContentObserver(new Handler()));
		return listView;
	}
	
	public class MyContentObserver extends ContentObserver{

		public MyContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			List<FileInfo> queryUpLoadList = dao.queryUpLoadList();
			if(queryUpLoadList.size()>0){
				EventBus.getDefault().post(true);
			}else{
				EventBus.getDefault().post(false);
			}
			adapter.notifyChange(queryUpLoadList);
		}
	}
	
	public void setChoice(boolean b) {
		adapter.setChoice(b);
		listView.invalidateViews();
	}
	
	

	@Override
	public void setAllSelect(boolean isAllSelect) {
		adapter.setAllSelect(isAllSelect);
		listView.invalidateViews();
	}


	@Override
	public void deleteAll() {
		UpLoadManager.getInstance().deleteAll( adapter.getSelected());
	}

	@Override
	public void pauseAll() {
		UpLoadManager.getInstance().pauseAll(adapter.getSelected());
	}

	@Override
	public void startAll() {
		UpLoadManager.getInstance().startAll(adapter.getSelected());
	}
}
