package com.truthso.ip360.adapter;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.viewholder.ViewHolder;

public  class NativeEvidenceAdapter extends CommonAdapter<DbBean> implements OnCheckedChangeListener {
	private Context context;
	private LayoutInflater inflater;
	private Boolean isAllSelect=false;
	private boolean isChoice=false;
	private CheckBox cb_choice;
	private CheckBox cb_option;
	private LinearLayout ll_option;
	public NativeEvidenceAdapter(Context context, List<DbBean> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		View view = inflater.inflate(itemLayoutId, null);

		cb_choice = (CheckBox)view.findViewById(R.id.cb_choice);
		cb_option = (CheckBox) view.findViewById(R.id.cb_option);
		ll_option = (LinearLayout) view.findViewById(R.id.ll_option);
		
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
	public void convert(ViewHolder helper, DbBean item, int position) {
		item = mDatas.get(position);
		helper.setText(R.id.tv_filename, item.getTitle());
		helper.setText(R.id.tv_filesize, item.getFileSize());
		helper.setText(R.id.tv_date, item.getCreateTime());
		
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
			
			cb_option.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.cb_option:
						if(ll_option.getVisibility()==View.VISIBLE){
							ll_option.setVisibility(View.GONE);
						}else{
							ll_option.setVisibility(View.VISIBLE);
						}
						break;

					default:
						break;
					}
				}
			});
	
		}
	}



	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			int positon = (Integer) buttonView.getTag();
			
		}
	}

}