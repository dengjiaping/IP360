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
import com.truthso.ip360.event.UpEvent;
import com.truthso.ip360.event.UpLoadFaileEvent;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.utils.CheckUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UpLoadListPager extends BasePager {
	private ListView listView;
	private UpLoadAdapter adapter;
	private UpDownLoadDao dao;


	public UpLoadListPager(Context ctx) {
		super(ctx);
	}

	@Override
	public void initData(int position) {
		
	}

	@Override
	public View initView() {
		dao=UpDownLoadDao.getDao();
		List<FileInfo> queryUpLoadList = dao.queryUpLoadList();
		Log.i("djj",queryUpLoadList.size()+"size");
		listView = new ListView(ctx);
		adapter=new UpLoadAdapter(ctx,formatList(queryUpLoadList));
		listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；
	
		ctx.getContentResolver().registerContentObserver(Uri.parse("content://com.truthso.ip360/updownloadlog/up"), true, new MyContentObserver(new Handler()));
		return listView;
	}

	//将已完成的条目放到集合最后
	private List<FileInfo> formatList(List<FileInfo> list){
		List<FileInfo> temp=new ArrayList<>();
		for (int i=0;i<list.size();i++){
			FileInfo fileInfo = list.get(i);
			if(fileInfo.getStatus()==0){
				temp.add(list.remove(i));
			}
		}
		list.addAll(temp);
		return list;
	}

	public class MyContentObserver extends ContentObserver{

		public MyContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			List<FileInfo> queryUpLoadList = dao.queryUpLoadList();
			adapter.notifyChange(formatList(queryUpLoadList));
		}
	}

}
