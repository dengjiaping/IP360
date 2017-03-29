package com.truthso.ip360.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.truthso.ip360.adapter.DownLoadAdapter;
import com.truthso.ip360.adapter.UpLoadAdapter;
import com.truthso.ip360.bean.DownLoadInfo;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.event.DownEvent;
import com.truthso.ip360.fragment.BaseFragment;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.pager.UpLoadListPager.MyContentObserver;
import com.truthso.ip360.updownload.DownLoadManager;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.updownload.UpLoadManager;

import org.greenrobot.eventbus.EventBus;

public class DownLoadListPager extends BasePager {
	private ListView listView;
	private DownLoadAdapter adapter;
	public DownLoadListPager(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView() {
		List<FileInfo> queryDownLoadList = UpDownLoadDao.getDao().queryDownLoadList();

		listView = new ListView(ctx);
		if(queryDownLoadList.size()>0){
			EventBus.getDefault().post(new DownEvent(true));
		}else{
			EventBus.getDefault().post(new DownEvent(false));
		}
		adapter=new DownLoadAdapter(ctx,formatList(queryDownLoadList));
		listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；
	
		ctx.getContentResolver().registerContentObserver(Uri.parse("content://com.truthso.ip360/updownloadlog/down"), true, new MyContentObserver(new Handler()));
		return listView;
	}

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
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			List<FileInfo> queryDownLoadList = UpDownLoadDao.getDao().queryDownLoadList();
			if(queryDownLoadList.size()>0){
				EventBus.getDefault().post(new DownEvent(true));
			}else{
				EventBus.getDefault().post(new DownEvent(false));
			}
			adapter.notifyChange(formatList(queryDownLoadList));
		}
	}
	
	@Override
	public void initData(int position) {
		
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

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		DownLoadHelper.getInstance().deleteAll(adapter.getSelected());
	}

	@Override
	public void pauseAll() {
		// TODO Auto-generated method stub
	}

	@Override
	public void startAll() {
		// TODO Auto-generated method stub
	}
}
