package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.view.SpeedView;

/**
 * @despriction :下载列表的adapter
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-10下午4:46:38
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class DownLoadAdapter extends BaseAdapter implements OnCheckedChangeListener {
	private LayoutInflater inflater;
	private Context context;
	private boolean isAllSelect = false;
	private boolean isChoice = false;
    private DownLoadHelper helper=DownLoadHelper.getInstance();
	private List<String> selectedList=new ArrayList<String>();
	private List<FileInfo> list;
	private long progress,lastProgress;
	private String foramt1;
	private ImageView iv_icon;
	public DownLoadAdapter(Context context, List<FileInfo> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);

	}

	public void setChoice(Boolean isChoice) {
		this.isAllSelect = false;
		this.isChoice = isChoice;
	}

	public void setAllSelect(Boolean isAllSelect) {
		this.isChoice = true;
		this.isAllSelect = isAllSelect;
	}

	public void notifyChange(List<FileInfo> datas) {
		list.clear();
		list.addAll(datas);
		this.notifyDataSetChanged();
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
			vh.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			vh.cb_choice = (CheckBox) convertView.findViewById(R.id.cb_choice);
			vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			vh.tv_fileName = (TextView) convertView.findViewById(R.id.tv_fileName);
			vh.probar = (ProgressBar) convertView.findViewById(R.id.probar);
			vh.btn_upload_download = (Button) convertView.findViewById(R.id.btn_upload_download);
			vh.tv_status = (SpeedView) convertView.findViewById(R.id.tv_status);
			vh.cb_choice.setOnCheckedChangeListener(this);
			vh.cb_choice.setTag(position);
			
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

		FileInfo info = list.get(position);
		vh.probar.setProgress(info.getPosition());
		vh.tv_fileName.setText(info.getFileName());
		vh.tv_size.setText(info.getFileSize());
		//带格式的
		vh.probar.setMax(Integer.parseInt(info.getLlsize()));		
		
		helper.setOnprogressListener(info.getObjectKey(), new com.truthso.ip360.ossupload.ProgressListener() {
			
			@Override
			public void onProgress(long progress) {
				Log.i("djj", "progress"+progress);
				vh.probar.setProgress((int)progress);
				vh.tv_status.setProgress(progress);
			}
			@Override
			public void onComplete() {
				Log.i("djj", "downComplete");
				
			}
			@Override
			public void onFailure() {
				vh.tv_status.setStatus(false);
			}
		});

		String str =info.getFileName();
		foramt1 = str.substring(str.lastIndexOf(".")+1);
		String format= foramt1.toLowerCase();// 格式变小写

		if (CheckUtil.isFormatPhoto(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_tp);
		} else if (CheckUtil.isFormatVideo(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_sp);
		} else if (CheckUtil.isFormatRadio(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_yp);
		} else if (CheckUtil.isFormatDoc(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_bq);
		}

		return convertView;
	}

	class ViewHolder {
		private CheckBox cb_choice;
		private TextView tv_fileName ,tv_size;
		private SpeedView tv_status;
		private ProgressBar probar;
		private Button btn_upload_download;
		private ImageView iv_icon;
	}


	public List<String> getSelected(){
		return selectedList;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		int position = (Integer) arg0.getTag();
		Log.i("djj", position+"");
		if(arg1){
			if(position<list.size()){
				selectedList.add(list.get(position).getObjectKey());
			}

		}else{
			if(position<list.size()) {
				selectedList.remove(list.get(position).getObjectKey());
			}
		}	 
	}
}
