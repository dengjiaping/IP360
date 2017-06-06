package com.truthso.ip360.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.truthso.ip360.activity.NotarLocActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.City;
import com.truthso.ip360.event.CityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */

public class CityAdapter extends BaseAdapter {

    private List<City> cities;
    private NotarLocActivity notarLocActivity;
    private String provinceName;
    public CityAdapter(List<City> cities, NotarLocActivity notarLocActivity,String provinceName) {
        this.cities=cities;
        this.notarLocActivity=notarLocActivity;
        this.provinceName=provinceName;
    }

    @Override
    public int getCount() {
        return cities.size();
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
        CityAdapter.ViewHolder vh = null;
        if (convertView == null) {
            vh = new CityAdapter.ViewHolder();
            convertView = LayoutInflater.from(notarLocActivity).inflate(R.layout.item_city, parent,false);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(vh);
        } else {
            vh = (CityAdapter.ViewHolder) convertView.getTag();
        }
        vh.tv_name.setText(cities.get(position).getCityName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityEvent cityEvent=   new CityEvent();
                cityEvent.setCityCode(cities.get(position).getCityCode());
                cityEvent.setCityName(cities.get(position).getCityName());
                cityEvent.setProvinceCode(cities.get(position).getProvinceCode());
                cityEvent.setProvinceName(provinceName);
                EventBus.getDefault().post(cityEvent);
                notarLocActivity.finish();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_name;
    }
}
