package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.adapter.CloudEvidenceAdapter.ViewHolder;
import com.truthso.ip360.updownload.UpLoadListener;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.updownload.UpLoadRunnable;
import com.truthso.ip360.updownload.UpLoadTashInfo;

/**
 * @despriction :下载列表的adapter
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-10下午4:46:38
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class UpLoadAdapter extends BaseAdapter implements OnCheckedChangeListener {
	private LayoutInflater inflater;
	private Context context;
	private boolean isAllSelect = false;
	private boolean isChoice = false;
	private List<Future<String>> datas = new ArrayList<Future<String>>();
	private UpLoadManager instanse = UpLoadManager.getInstance();
	private LinkedHashMap<Future<String>, UpLoadRunnable> map;

	public UpLoadAdapter(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		map = instanse.getMap();
		for (Map.Entry<Future<String>, UpLoadRunnable> info : map.entrySet()) {
			if (!info.getKey().isDone()) {
				datas.add(info.getKey());
			}
		}
	}

	public void setChoice(Boolean isChoice) {
		this.isAllSelect = false;
		this.isChoice = isChoice;
	}

	public void setAllSelect(Boolean isAllSelect) {
		this.isChoice = true;
		this.isAllSelect = isAllSelect;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_updownload, null);
			vh = new ViewHolder();
			vh.cb_choice = (CheckBox) convertView.findViewById(R.id.cb_choice);
			vh.tv_fileName = (TextView) convertView.findViewById(R.id.tv_fileName);
			vh.probar = (ProgressBar) convertView.findViewById(R.id.probar);
			vh.btn_upload_download = (Button) convertView.findViewById(R.id.btn_upload_download);

			vh.cb_choice.setOnCheckedChangeListener(this);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();

		}

		if (isChoice) {
			if (isAllSelect) {
				vh.cb_choice.setChecked(true);
			} else {
				vh.cb_choice.setChecked(false);
			}
			vh.cb_choice.setVisibility(View.VISIBLE);
		} else {
			vh.cb_choice.setVisibility(View.GONE);
		}

		Future<String> future = datas.get(position);
		UpLoadRunnable upLoadRunnable = map.get(future);
		String filePath = upLoadRunnable.getUrl();
		String name = filePath.substring(filePath.lastIndexOf("/"), filePath.length());
		vh.tv_fileName.setText(name);

		vh.probar.setMax(100);
		upLoadRunnable.setOnProgressListener(new UpLoadListener() {

			@Override
			public void onUpLoadComplete() {
				// TODO Auto-generated method stub
				datas.remove(position);
				UpLoadAdapter.this.notifyDataSetChanged();
			}

			@Override
			public void onProgress(int progress) {
				vh.probar.setProgress(progress);
			}
		});

		vh.btn_upload_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return convertView;
	}

	class ViewHolder {
		private CheckBox cb_choice;
		private TextView tv_fileName;
		private ProgressBar probar;
		private Button btn_upload_download;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	public void notifyDataChanged() {
		datas.clear();
		map = instanse.getMap();
		for (Map.Entry<Future<String>, UpLoadRunnable> info : map.entrySet()) {
			if (!info.getKey().isDone()) {
				datas.add(info.getKey());
			}
		}
		this.notifyDataSetChanged();
	}
}
