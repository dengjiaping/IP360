package com.truthso.ip360.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.Province;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */

public class NotarLocAdapter extends BaseAdapter {

    private List<Province> datas;
    private Context context;

    public NotarLocAdapter(List<Province> datas, Context context) {
        this.datas = datas;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        NotarLocAdapter.ViewHolder vh = null;
        if (convertView == null) {
            vh = new NotarLocAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_province, parent,false);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(vh);
        } else {
            vh = (NotarLocAdapter.ViewHolder) convertView.getTag();
        }
        vh.tv_name.setText(datas.get(position).getProvinceName());
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_name;
    }
}
