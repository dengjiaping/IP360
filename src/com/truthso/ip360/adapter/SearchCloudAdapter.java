package com.truthso.ip360.adapter;

import java.util.List;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.CloudEvidenceBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchCloudAdapter extends BaseAdapter {
	private List<CloudEviItemBean> list;
	private LayoutInflater inFlater;

	public SearchCloudAdapter(Context context, List<CloudEviItemBean> list) {
		this.list = list;
		inFlater = LayoutInflater.from(context);
	}

	public void addData(List<CloudEviItemBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void clearData() {
		list.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder vh = null;
		if (convertView == null) {
			convertView = inFlater.inflate(R.layout.item_cloudevidence, null);
			vh = new viewHolder();
			vh.tv_filename = (TextView) convertView.findViewById(R.id.tv_filename);
			vh.tv_filedate = (TextView) convertView.findViewById(R.id.tv_filedate);
			vh.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			convertView.setTag(vh);
		} else {
			vh = (viewHolder) convertView.getTag();
		}
		CloudEviItemBean cloudEviItemBean = list.get(position);
		vh.tv_filename.setText(cloudEviItemBean.getFileTitle());
		vh.tv_filedate.setText(cloudEviItemBean.getFileDate());
		vh.tv_size.setText(cloudEviItemBean.getFileSize());

		return convertView;
	}

	class viewHolder {
		TextView tv_filename, tv_filedate, tv_size;
	}
}
