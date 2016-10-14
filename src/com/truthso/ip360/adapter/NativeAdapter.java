package com.truthso.ip360.adapter;

import java.util.List;

import com.truthso.ip360.activity.CertificationActivity;
import com.truthso.ip360.activity.LoginActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.dao.SqlDao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NativeAdapter extends BaseAdapter implements OnCheckedChangeListener ,OnClickListener{
	private Dialog alertDialog;
	private Context context;
	private LayoutInflater inflater;
	private Boolean isAllSelect=false;
	private boolean isChoice=false;
	protected List<DbBean> mDatas;
	private DbBean dbBean;
	public NativeAdapter(Context context,List<DbBean> mDatas) {
		super();
		this.context = context;
		this.mDatas=mDatas;
		inflater=LayoutInflater.from(context);
	}
	
	public void addData(List<DbBean> mDatas){
		this.mDatas.clear();
		this.mDatas.addAll(mDatas);
		notifyDataSetChanged();
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
		return mDatas.size();
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
			convertView = inflater.inflate(R.layout.item_native_evidence, null);
			vh=new ViewHolder();
			vh.cb_choice= (CheckBox) convertView.findViewById(R.id.cb_choice);
			vh.cb_option= (CheckBox) convertView.findViewById(R.id.cb_option);
			vh.tv_filename=(TextView) convertView.findViewById(R.id.tv_filename);
			vh.tv_filesize=(TextView) convertView.findViewById(R.id.tv_filesize);
			vh.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
			convertView.setTag(vh);
		}else{
			vh=	(ViewHolder) convertView.getTag();
			
		}
	
		changeState(position, convertView, vh.cb_choice, vh.cb_option);	
		vh.tv_filename.setText(mDatas.get(position).getTitle());
		vh.tv_filesize.setText(mDatas.get(position).getFileSize());
		vh.tv_date.setText(mDatas.get(position).getCreateTime());
		return convertView;
	}

	class ViewHolder{
		private CheckBox cb_choice,cb_option;	
		private TextView tv_filename,tv_date,tv_filesize;
	}
	
	
	
	private void changeState(int position, View view, CheckBox cb_choice,
			CheckBox cb_option) {
		if(isChoice){
			cb_choice.setVisibility(View.VISIBLE);
			cb_option.setVisibility(View.GONE);
			if(isAllSelect){
				cb_choice.setChecked(true);
			}else{
				cb_choice.setChecked(false);
			}
			cb_choice.setTag(position);
			cb_choice.setOnCheckedChangeListener(this);
		}else{
			cb_choice.setVisibility(View.GONE);
			cb_option.setVisibility(View.VISIBLE);
			cb_option.setChecked(false);
			
			final LinearLayout ll_option = (LinearLayout) view.findViewById(R.id.ll_option);
			ll_option.setVisibility(View.GONE);
			TextView tv_delete=(TextView) ll_option.findViewById(R.id.tv_delete);
			TextView tv_preview=(TextView) ll_option.findViewById(R.id.tv_preview);
			
			cb_option.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {		
						if(ll_option.getVisibility()==View.VISIBLE){
							ll_option.setVisibility(View.GONE);
						}else{
							ll_option.setVisibility(View.VISIBLE);
						}
				}
			});
			tv_delete.setTag(position);
			tv_delete.setOnClickListener(this);
			tv_preview.setTag(position);
			tv_preview.setOnClickListener(this);
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int positon = (Integer) buttonView.getTag();
		if(isChecked){	
			System.out.println("positon+"+positon);
		}else{
			System.out.println("positon-"+positon);
		}
	}

	@Override
	public void onClick( View v) {
		switch (v.getId()) {
		case R.id.tv_delete://删除
			showDialog();
			dbBean = mDatas.get((Integer) v.getTag());
			break;
		case R.id.tv_preview://查看证书
			Intent intent = new Intent(context,CertificationActivity.class);
			context.startActivity(intent);
			break;
			
		default:
			break;
		}
		
	}
	private void showDialog() {
		alertDialog = new AlertDialog.Builder(context).
        	    setTitle("温馨提示").
        	    setMessage("是否确认删除？").
        	    setIcon(R.drawable.ww).
        	    setPositiveButton("确定", new DialogInterface.OnClickListener() {
        	     @Override
        	     public void onClick(DialogInterface dialog, int which) {
        	    	 
        	    		
        	   	     new SqlDao(context).delete(MyConstants.TABLE_MEDIA_DETAIL,dbBean.getId());
        	    	
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

}
