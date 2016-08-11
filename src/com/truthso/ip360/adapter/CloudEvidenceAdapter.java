package com.truthso.ip360.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.activity.CertificationActivity;
import com.truthso.ip360.activity.FileRemarkActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.DbBean;

public class CloudEvidenceAdapter extends BaseAdapter implements
		OnCheckedChangeListener, OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private Boolean isAllSelect = false;
	private boolean isChoice = false;
	protected List<DbBean> mDatas;

	public CloudEvidenceAdapter(Context context, List<DbBean> mDatas) {
		super();
		this.context = context;
		this.mDatas = mDatas;
		inflater = LayoutInflater.from(context);
	}

	public void addData(List<DbBean> mDatas) {
		this.mDatas.clear();
		this.mDatas.addAll(mDatas);
		notifyDataSetChanged();
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
		return 10;
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
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_cloudevidence, null);
			vh = new ViewHolder();
			vh.cb_choice = (CheckBox) convertView.findViewById(R.id.cb_choice);
			vh.cb_option = (CheckBox) convertView.findViewById(R.id.cb_option);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();

		}

		changeState(position, convertView, vh.cb_choice, vh.cb_option);

		return convertView;
	}

	class ViewHolder {
		private CheckBox cb_choice, cb_option;
	}

	private void changeState(int position, View view, CheckBox cb_choice,
			CheckBox cb_option) {
		if (isChoice) {
			cb_choice.setVisibility(View.VISIBLE);
			cb_option.setVisibility(View.GONE);
			if (isAllSelect) {
				cb_choice.setChecked(true);
			} else {
				cb_choice.setChecked(false);
			}
			cb_choice.setTag(position);
			cb_choice.setOnCheckedChangeListener(this);
		} else {
			cb_choice.setVisibility(View.GONE);
			cb_option.setVisibility(View.VISIBLE);
			final LinearLayout ll_option = (LinearLayout) view
					.findViewById(R.id.ll_option);
			TextView tv_download = (TextView) view
					.findViewById(R.id.tv_download);
			TextView tv_remark = (TextView) view.findViewById(R.id.tv_remark);
			TextView tv_certificate_preview = (TextView) view
					.findViewById(R.id.tv_certificate_preview);

			tv_download.setOnClickListener(this);
			tv_certificate_preview.setOnClickListener(this);
			tv_remark.setOnClickListener(this);
			cb_option.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.cb_option:
						if (ll_option.getVisibility() == View.VISIBLE) {
							ll_option.setVisibility(View.GONE);
						} else {
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
		if (isChecked) {
			int positon = (Integer) buttonView.getTag();
			System.out.println("positon" + positon);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_remark:// 备注
			Intent intent = new Intent(context, FileRemarkActivity.class);
			context.startActivity(intent);
			break;
		case R.id.tv_download:// 下载
			Toast toast = new Toast(context);
			toast.makeText(context, "文件开始下载到本地证据", 1).show();
			toast.setGravity(Gravity.CENTER, 0, 0);
			break;
		case R.id.tv_certificate_preview:// 证书预览
			Intent intent1 = new Intent(context, CertificationActivity.class);
			context.startActivity(intent1);
			break;

		default:
			break;
		}
	}

}
