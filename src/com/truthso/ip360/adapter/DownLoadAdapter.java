package com.truthso.ip360.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.adapter.CloudEvidenceAdapter.ViewHolder;
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
	
	public DownLoadAdapter(Context context) {
		super();
		this.context = context;
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
		return 6;
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
	public View getView(int position, View convertView , ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){		
			convertView = inflater.inflate(R.layout.item_updownload, null);
			vh=new ViewHolder();
			vh.cb_choice= (CheckBox) convertView.findViewById(R.id.cb_choice);
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
		
		
		return convertView;
	}

	class ViewHolder{
		private CheckBox cb_choice;	
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
}
