package com.truthso.ip360.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.truthso.ip360.activity.AccountMagActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.GiftsProduct;
import com.truthso.ip360.constants.MyConstants;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */

public class AccountMagAdapter extends BaseAdapter {

    private Context context;
    List<GiftsProduct> giftsProduct;

    public AccountMagAdapter(Context context, List<GiftsProduct> giftsProduct) {
        this.context = context;
        this.giftsProduct = giftsProduct;
    }

    @Override
    public int getCount() {
        return giftsProduct.size();
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
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_zengsong, null);
            vh.tv_yewu_name = (TextView) convertView.findViewById(R.id.tv_yewu_name);
            vh.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            vh.tv_liang = (TextView) convertView.findViewById(R.id.tv_liang);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        GiftsProduct giftsProduct = this.giftsProduct.get(position);
        int type = giftsProduct.getType();
        if (type == MyConstants.PHOTOTYPE) {
            vh.tv_yewu_name.setText("拍照取证");
        } else if (type == MyConstants.RECORDTYPE) {
            vh.tv_yewu_name.setText("录音取证");
        } else {
            vh.tv_yewu_name.setText("录像取证");
        }

        vh.tv_date.setText(giftsProduct.getContractStart() + "-" + giftsProduct.getContractEnd());
        vh.tv_liang.setText(giftsProduct.getUsedCount() + giftsProduct.getUnit() + "/" + giftsProduct.getGiftsCount() + giftsProduct.getUnit());

        return convertView;
    }

    public class ViewHolder {
        private TextView tv_yewu_name, tv_date, tv_liang;
    }
}