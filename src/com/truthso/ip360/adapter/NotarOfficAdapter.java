package com.truthso.ip360.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.truthso.ip360.activity.NotarLocActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.Notary;
import com.truthso.ip360.bean.Province;
import com.truthso.ip360.event.NotaryOfficEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */

public class NotarOfficAdapter extends BaseAdapter {

    private List<Notary> datas;
    private NotarLocActivity notarLocActivity;

    public NotarOfficAdapter(List<Notary> datas,  NotarLocActivity notarLocActivity) {
        this.datas = datas;
        this.notarLocActivity = notarLocActivity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        NotarOfficAdapter.ViewHolder vh = null;
        if (convertView == null) {
            vh = new NotarOfficAdapter.ViewHolder();
            convertView = LayoutInflater.from(notarLocActivity).inflate(R.layout.item_city, parent,false);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(vh);
        } else {
            vh = (NotarOfficAdapter.ViewHolder) convertView.getTag();
        }
        vh.tv_name.setText(datas.get(position).getNotaryName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotaryOfficEvent event=new NotaryOfficEvent();
                event.setName(datas.get(position).getNotaryName());
                EventBus.getDefault().post(event);
                notarLocActivity.finish();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_name;
    }
}
