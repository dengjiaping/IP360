package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.adapter.CloudEvidenceAdapter.ViewHolder;
import com.truthso.ip360.updownload.UpLoadListener;
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
public class UpLoadAdapter extends BaseAdapter implements OnCheckedChangeListener{
	private LayoutInflater inflater;
	private Context context;
	private boolean isAllSelect=false;
	private boolean isChoice=false;

	private List<UpLoadTashInfo> datas=new ArrayList<UpLoadTashInfo>();
	
	public UpLoadAdapter(Context context,List<UpLoadTashInfo> list) {
		super();
		this.context = context;
		inflater=LayoutInflater.from(context);
		for (int i = 0; i < list.size(); i++) {
			 UpLoadTashInfo upLoadTashInfo = list.get(i);
				 if(!upLoadTashInfo.getFuture().isDone()){
					 datas.add(upLoadTashInfo);
				 }
			}
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
	public View getView(final int position, View convertView , ViewGroup parent) {
		final ViewHolder vh;
		if(convertView==null){		
			convertView = inflater.inflate(R.layout.item_updownload, null);
			vh=new ViewHolder();
			vh.cb_choice= (CheckBox) convertView.findViewById(R.id.cb_choice);
			vh.tv_fileName=(TextView) convertView.findViewById(R.id.tv_fileName);
			vh.probar=(ProgressBar) convertView.findViewById(R.id.probar);
			
			
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
	  
	    String filePath = datas.get(position).getFilePath();
		String name=filePath.substring(filePath.lastIndexOf("/"),filePath.length());
		vh.tv_fileName.setText(name);
		UpLoadRunnable runnable = datas.get(position).getRunnable();
		vh.probar.setMax(100);
		runnable.setOnProgressListener(new UpLoadListener() {
			
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
			  
		return convertView;
	}

	class ViewHolder{
		private CheckBox cb_choice;	
		private TextView tv_fileName;
		private ProgressBar probar;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
}
