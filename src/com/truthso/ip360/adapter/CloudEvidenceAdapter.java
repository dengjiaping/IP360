package com.truthso.ip360.adapter;

import com.truthso.ip360.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class CloudEvidenceAdapter extends BaseAdapter implements OnCheckedChangeListener {

	private Context context;
	private LayoutInflater inflater;
	private Boolean isAllSelect=false;
	private boolean isChoice=false;
	public CloudEvidenceAdapter(Context context) {
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
		return 5;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.item_cloudevidence, null);
		CheckBox cb_choice = (CheckBox) view.findViewById(R.id.cb_choice);
		CheckBox cb_option = (CheckBox) view.findViewById(R.id.cb_option);
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
			final LinearLayout ll_option = (LinearLayout) view.findViewById(R.id.ll_option);
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
		return view;
	}

	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			int positon = (Integer) buttonView.getTag();
			System.out.println("positon"+positon);
		}
	}

}
