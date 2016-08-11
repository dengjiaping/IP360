package com.truthso.ip360.pager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.truthso.ip360.adapter.DownLoadAdapter;

public class DownLoadListPager extends BasePager {
	private ListView listView;
	private DownLoadAdapter adapter;
	public DownLoadListPager(Context ctx) {
		super(ctx);
	}

	@Override
	public View initView() {
		listView = new ListView(ctx);
		adapter=new DownLoadAdapter(ctx);
		listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；
		return listView;
	}

	@Override
	public void initData(int position) {
		switch (position) {
		case 0://下载列表
			listView.setAdapter(adapter);
			break;
		case 1://文件上传

			break;
		default:
			break;
		}
	}
}
