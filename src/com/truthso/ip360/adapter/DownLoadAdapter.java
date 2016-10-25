package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

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
import com.truthso.ip360.bean.DownLoadInfo;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.updownload.DownLoadListener;
import com.truthso.ip360.updownload.DownLoadManager;
import com.truthso.ip360.updownload.DownLoadRunnable;
/**
 * @despriction :下载列表的adapter
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-10下午4:46:38
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class DownLoadAdapter extends BaseAdapter implements OnCheckedChangeListener{
	private LayoutInflater inflater;
	private Context context;
	private boolean isAllSelect=false;
	private boolean isChoice=false;
	private DownLoadManager downLoadManager=DownLoadManager.getInstance();
	private List<DownLoadRunnable> datas=new ArrayList<DownLoadRunnable>();
	private List<DownLoadInfo> list;
	public DownLoadAdapter(Context context,List<DownLoadInfo> list) {
		super();
		this.context = context;
		this.list=list;
		inflater=LayoutInflater.from(context);
	
		
	}

	public void setChoice(Boolean isChoice){
		this.isAllSelect=false;
		this.isChoice=isChoice;
	}
	public void setAllSelect(Boolean isAllSelect){
		this.isChoice=true;
		this.isAllSelect=isAllSelect;
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
	public View getView(final int position, View convertView , ViewGroup parent) {
		final ViewHolder vh;
		if(convertView==null){		
			convertView = inflater.inflate(R.layout.item_updownload, null);
			vh=new ViewHolder();
			vh.cb_choice= (CheckBox) convertView.findViewById(R.id.cb_choice);
			vh.tv_fileName=(TextView) convertView.findViewById(R.id.tv_fileName);
			vh.probar=(ProgressBar) convertView.findViewById(R.id.probar);
			vh.btn_upload_download=(Button) convertView.findViewById(R.id.btn_upload_download);
			
			vh.cb_choice.setOnCheckedChangeListener(this);
			convertView.setTag(vh);
		}else{
			vh=	(ViewHolder) convertView.getTag();
			
		}
	
	  if(isChoice){
		  if(isAllSelect){
			vh.	cb_choice.setChecked(true);
			}else{
				vh.cb_choice.setChecked(false);
			}
		  vh.cb_choice.setVisibility(View.VISIBLE);
	   }else{
		vh.cb_choice.setVisibility(View.GONE);
	   }
	  
	  DownLoadInfo downLoadInfo = list.get(position);
	  vh.tv_fileName.setText(downLoadInfo.getFileName());
	  vh.probar.setMax(Integer.parseInt(downLoadInfo.getFileSize()));
	  final String url=downLoadInfo.getDownLoadUrl();
	  downLoadManager.setOnDownLoadProgressListener(url, new DownLoadListener() {
		
		@Override
		public void oncomplete() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgress(int progress) {
			vh.probar.setProgress(progress);
			
		}
	});
	 
	  vh.btn_upload_download.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			downLoadManager.pauseOrStratDownLoad(url);
		}
	});
		return convertView;
	}

	class ViewHolder{
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
		   LinkedHashMap<Future<String>, DownLoadRunnable> map = downLoadManager.getMap();
			for (Map.Entry<Future<String>, DownLoadRunnable> info : map.entrySet()) {
				if(!info.getKey().isDone()){
					datas.add(info.getValue());
				}
			}
			this.notifyDataSetChanged();
	}
}
