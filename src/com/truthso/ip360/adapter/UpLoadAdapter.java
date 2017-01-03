package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.ossupload.ProgressListener;
import com.truthso.ip360.ossupload.UpLoadManager;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.view.SpeedView;

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
	private UpLoadManager instanse = UpLoadManager.getInstance();
	private List<FileInfo> list;
	private List<Integer> selectedList=new ArrayList<Integer>();
	private long progress,lastProgress;
	private ImageView iv_icon;
	public UpLoadAdapter(Context context, List<FileInfo> list) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list=list;
	}

	public void setChoice(Boolean isChoice) {
		this.isAllSelect = false;
		this.isChoice = isChoice;
	}

	public void notifyChange(List<FileInfo> datas){
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
			vh.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			vh.cb_choice = (CheckBox) convertView.findViewById(R.id.cb_choice);
			vh.iv_icon =(ImageView) convertView.findViewById(R.id.iv_icon);
			vh.tv_fileName = (TextView) convertView.findViewById(R.id.tv_fileName);
			vh.probar = (ProgressBar) convertView.findViewById(R.id.probar);
			vh.btn_upload_download = (Button) convertView.findViewById(R.id.btn_upload_download);
			vh.tv_status=(SpeedView) convertView.findViewById(R.id.tv_status);
			vh.cb_choice.setTag(position);
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

		final FileInfo upLoadInfo = list.get(position);
		vh.tv_fileName.setText(upLoadInfo.getFileName());
		long l_size = Long.parseLong(upLoadInfo.getFileSize());
		String s_size = FileSizeUtil.setFileSize(l_size);
		vh.tv_size.setText(s_size);
		
		vh.probar.setMax(Integer.parseInt(upLoadInfo.getFileSize()));
		vh.probar.setProgress(upLoadInfo.getPosition());
		final int resourceId = upLoadInfo.getResourceId();
		instanse.setOnUpLoadProgressListener(upLoadInfo.getResourceId(),new ProgressListener() {

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				Log.i("progress", "complete");
			}

			@Override
			public void onFailure() {
				Log.i("djj","onFailure");
				vh.tv_status.setStatus(false);
			}

			@Override
			public void onProgress(long progress) {
				vh.probar.setProgress((int)progress);
				vh.tv_status.setProgress(progress);
			}
		});
      /*  TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
			final long speed=(progress-lastProgress)/1024;
			UpLoadAdapter.this.lastProgress=progress;
			vh.tv_status.post(new Runnable() {
				
				@Override
				public void run() {
					vh.tv_status.setText(speed+"k/s");
				}
			});
			}
		};
		Timer timer=new Timer();
		timer.schedule(task,0,1000);*/
		int satus=upLoadInfo.getStatus();
		//int currentStatus = instanse.getCurrentStatus(resourceId);
		 if(satus==1){
			 vh.tv_status.setText("暂停中");
			// vh.btn_upload_download.setSelected(true);
		 }else if(satus==0){
			 //获取实时网速或者正在等待中
			 vh.tv_status.setText("0b/s");
			// vh.btn_upload_download.setSelected(false);
		 }else if(satus==2){
			 Log.i("djj","上传失败");
			 vh.tv_status.setText("上传失败");
			 //vh.btn_upload_download.setSelected(true);
		 }
		
		vh.btn_upload_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
			
				if(vh.btn_upload_download.isSelected()){					
					//vh.btn_upload_download.setSelected(false);
					//instanse.restart(resourceId);
				}else{
					//vh.btn_upload_download.setSelected(true);
					//instanse.pause(resourceId);
				}
			}
		});
	String str = upLoadInfo.getFileName();
	String	foramt1 = str.substring(str.lastIndexOf(".")+1);
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
		private TextView tv_fileName,tv_size;
		private SpeedView tv_status;
		private ProgressBar probar;
		private ImageView iv_icon;
		private Button btn_upload_download;
	}

	public List<Integer> getSelected(){
		return selectedList;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		int position = (Integer) arg0.getTag();
		if(arg1){
			selectedList.add(list.get(position).getResourceId());
		}else{
			selectedList.remove((Integer)list.get(position).getResourceId());
		}	 
	}
}
