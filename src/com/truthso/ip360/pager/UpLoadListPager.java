package com.truthso.ip360.pager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObservable;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.truthso.ip360.activity.R;
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

public class UpLoadListPager extends BasePager implements AdapterView.OnItemLongClickListener {
	private ListView listView;
	private UpLoadAdapter adapter;
	private List<FileInfo> queryUpLoadList;
	private Dialog alertDialog;

	public UpLoadListPager(Context ctx) {
		super(ctx);
	}

	@Override
	public void initData(int position) {
		
	}

	@Override
	public View initView() {
		queryUpLoadList = UpDownLoadDao.getDao().queryUpLoadListOrder();
		listView = new ListView(ctx);
		listView.setOnItemLongClickListener(this);
		adapter=new UpLoadAdapter(ctx,queryUpLoadList);
		listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；
	
		ctx.getContentResolver().registerContentObserver(Uri.parse("content://com.truthso.ip360/updownloadlog/up"), true, new MyContentObserver(new Handler()));
		return listView;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if(queryUpLoadList.get(position).getStatus()!=0){
			return true;
		}
		showDialog(position);
		return true;
	}


	//删除本地文件
	private void showDialog(final int position) {
		alertDialog = new AlertDialog.Builder(ctx).
				setTitle("温馨提示").
				setMessage("是否确认删除？").
				setIcon(R.drawable.ww).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						UpDownLoadDao.getDao().deleteUpInfoByResourceId(queryUpLoadList.get(position).getResourceId()+"");
					}
				}).
				setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				}).
				create();
		alertDialog.show();
	}

	public class MyContentObserver extends ContentObserver{

		public MyContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			queryUpLoadList = UpDownLoadDao.getDao().queryUpLoadListOrder();
			adapter.notifyChange(queryUpLoadList);
		}
	}

}
