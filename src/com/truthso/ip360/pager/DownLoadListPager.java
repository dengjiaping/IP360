package com.truthso.ip360.pager;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.truthso.ip360.activity.PhotoDetailActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.RecordDetailActivity;
import com.truthso.ip360.activity.VideoDetailActivity;
import com.truthso.ip360.adapter.DownLoadAdapter;
import com.truthso.ip360.adapter.UpLoadAdapter;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.DownLoadInfo;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.event.CEListRefreshEvent;
import com.truthso.ip360.event.DownEvent;
import com.truthso.ip360.fragment.BaseFragment;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.pager.UpLoadListPager.MyContentObserver;
import com.truthso.ip360.updownload.DownLoadManager;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileUtil;

import org.greenrobot.eventbus.EventBus;

public class DownLoadListPager extends BasePager implements AdapterView.OnItemLongClickListener,  AdapterView.OnItemClickListener {
	private ListView listView;
	private DownLoadAdapter adapter;

	public DownLoadListPager(Context ctx) {
		super(ctx);
	}

	private List<FileInfo> queryDownLoadList;
	private Dialog alertDialog;

	@Override
	public View initView() {
		queryDownLoadList = UpDownLoadDao.getDao().queryDownLoadList();
		listView = new ListView(ctx);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		adapter = new DownLoadAdapter(ctx, formatList(queryDownLoadList));
		listView.setAdapter(adapter);  //new 这个DownLoadListPager时候执行这个方法 这时候都要设置listview的adapter 要不返回的是个空listview；

		ctx.getContentResolver().registerContentObserver(Uri.parse("content://com.truthso.ip360/updownloadlog/down"), true, new MyContentObserver(new Handler()));
		return listView;
	}

	@Override
	public void initData(int position) {

	}


	//将已完成的条目放到集合最后
	private List<FileInfo> formatList(List<FileInfo> list) {
		List<FileInfo> temp = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			FileInfo fileInfo = list.get(i);
			if (fileInfo.getStatus() != 0) {
				temp.add(list.remove(i));
			}
		}
		list.addAll(0, temp);
		return list;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
						UpDownLoadDao.getDao().deleteDownInfoByResourceId(queryDownLoadList.get(position).getResourceId() + "");
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

	/**
	 * 点击查看详情
	 *
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		FileInfo info = queryDownLoadList.get(position);
//		DbBean dbBean = SqlDao.getSQLiteOpenHelper().searchByPkValue(info.getPkValue());
		String format = info.getFileFormat().toLowerCase();
		if (CheckUtil.isFormatPhoto(format)) {//条目类型照片
			Intent intent = new Intent(ctx, PhotoDetailActivity.class);
			intent.putExtra("url", info.getFilePath());
			intent.putExtra("from", "native");
			ctx.startActivity(intent);
		} else if (CheckUtil.isFormatVideo(format)) {//条目类型录像
			Intent videoIntent = new Intent(ctx, VideoDetailActivity.class);
			videoIntent.putExtra("url", info.getFilePath());
			ctx.startActivity(videoIntent);
		} else if (CheckUtil.isFormatRadio(format)) {//条目类型录音
			Intent recordIntent = new Intent(ctx, RecordDetailActivity.class);
			recordIntent.putExtra("url", info.getFilePath());
			recordIntent.putExtra("recordTime", info.getFileTime());
			ctx.startActivity(recordIntent);
		}
	}

		public class MyContentObserver extends ContentObserver {

			public MyContentObserver(Handler handler) {
				super(handler);
			}

			@Override
			public void onChange(boolean selfChange) {
				super.onChange(selfChange);
				queryDownLoadList = UpDownLoadDao.getDao().queryDownLoadList();
				adapter.notifyChange(formatList(queryDownLoadList));
			}
		}

	}

