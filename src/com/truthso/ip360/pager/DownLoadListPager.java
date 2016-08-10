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
