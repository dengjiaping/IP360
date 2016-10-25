package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import android.content.Context;
import android.util.Log;
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
import com.truthso.ip360.updownload.UpLoadInfo;
import com.truthso.ip360.updownload.UpLoadListener;
import com.truthso.ip360.updownload.UpLoadManager;
import com.truthso.ip360.updownload.UpLoadRunnable;

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
	private List<UpLoadInfo> list;
	public UpLoadAdapter(Context context, List<UpLoadInfo> list) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list=list;
	}

	public void setChoice(Boolean isChoice) {
		this.isAllSelect = false;
		this.isChoice = isChoice;
	}

	public void notifyChange(List<UpLoadInfo> datas){
		list.clear();
		list.addAll(datas);
		this.notifyDataSetChanged();
	}
	
	public void setAllSelect(Boolean isAllSelect) {
		this.isChoice = true;
		this.isAllSelect = isAllSelect;
	}

	@Override
	public int getCount() {
		return list.size();
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
			vh.tv_status=(TextView) convertView.findViewById(R.id.tv_status);
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

		final UpLoadInfo upLoadInfo = list.get(position);
		vh.tv_fileName.setText(upLoadInfo.getFileName());
		vh.probar.setMax(Integer.parseInt(upLoadInfo.getFileSize()));
		vh.probar.setProgress(upLoadInfo.getPosition());
		instanse.setOnUpLoadProgressListener(upLoadInfo.getResourceId(),new UpLoadListener() {

			@Override
			public void onUpLoadComplete() {
				// TODO Auto-generated method stub
				Log.i("progress", "complete");
			}

			@Override
			public void onProgress(int progress) {
				vh.probar.setProgress(progress);
			}
		});

		vh.btn_upload_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
<<<<<<< HEAD
//			 instanse.pauseOrStratUpLoad(future,upLoadRunnable.getResourceId(),upLoadRunnable.getUrl());
=======
			instanse.pauseOrStratUpLoad(upLoadInfo.getResourceId());
			 int result = instanse.getCurrentStatus(upLoadInfo.getResourceId());
			 if(result==1){
				 vh.tv_status.setText("暂停中");
			 }else{
				 //获取实时网速或者正在等待中
				 vh.tv_status.setText("230b/s");
			 }
>>>>>>> 7af251c8e28ee24f47c2c7ecf10ec9ffaa43646d
			}
		});
		
		int currentStatus = instanse.getCurrentStatus(upLoadInfo.getResourceId());
		 if(currentStatus==1){
			 vh.tv_status.setText("暂停中");
		 }else if(currentStatus==0){
			 //获取实时网速或者正在等待中
			 vh.tv_status.setText("等待中");
		 }else if(currentStatus==2){
			 vh.tv_status.setText("230b/s");
		 }else{
			 vh.tv_status.setText("上传失败");
		 }
		return convertView;
	}

	class ViewHolder {
		private CheckBox cb_choice;
		private TextView tv_fileName,tv_status;
		private ProgressBar probar;
		private Button btn_upload_download;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		
		
		
	}
}
