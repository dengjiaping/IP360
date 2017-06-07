package com.truthso.ip360.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.truthso.ip360.activity.DocumentDetailActivity;
import com.truthso.ip360.activity.NotarFileDetail;
import com.truthso.ip360.activity.PhotoDetailActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.RecordDetailActivity;
import com.truthso.ip360.activity.VideoDetailActivity;
import com.truthso.ip360.bean.File;
import com.truthso.ip360.bean.NotarMsg;
import com.truthso.ip360.fragment.UpdateItem;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * @despriction :公证详情的adapter
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/6/6 22:12
 * @version 1.3
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class NotarFileDetailAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    protected List<File> mDatas;
    private String  url,date,formatType;
    private int mobileType,type,pkValue;
    public NotarFileDetailAdapter(Context context, List<File> mDatas) {
        super();
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
    public void addData(List<File> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }
    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
       File file = mDatas.get(position);



        url=   file.getFileUrl();
        mobileType = file.getMobileType();
        pkValue =  file.getPkValue();
       type = file.getType();
        formatType = file.getFileFormatType();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_zhengju_detail, null);
            vh = new ViewHolder();
            vh.tv_filename = (TextView) convertView.findViewById(R.id.tv_filename);
            vh.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            vh.tv_filesize = (TextView) convertView.findViewById(R.id.tv_filesize);
            vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            vh.iv_zhengjiuyulan = (ImageView) convertView.findViewById(R.id.iv_zhengjiuyulan);
            vh.iv_ckzs = (ImageView) convertView.findViewById(R.id.iv_ckzs);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tv_filename.setText(file.getFileTitle());
        vh.tv_date.setText(file.getFileDate());
        vh.tv_filesize.setText( file.getFileSize());
        if (formatType.equals("1")){//文本
                vh.iv_icon.setBackgroundResource(R.drawable.icon_bq);
            }else if(formatType.equals("2")){//图片
            vh.iv_icon.setBackgroundResource(R.drawable.icon_tp);
            }else if(formatType.equals("3")){//音视频
                    if (mobileType==50002){
                        vh.iv_icon.setBackgroundResource(R.drawable.icon_yp);//音频
                    }else if(mobileType==50003){
                        vh.iv_icon.setBackgroundResource(R.drawable.icon_sp);//视频
                    }
        }
        vh.iv_zhengjiuyulan.setOnClickListener(this);
        vh.iv_ckzs.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.iv_zhengjiuyulan://证据预览
                if (formatType.equals("1")){//文本
                    Intent intentDoc = new Intent(context, DocumentDetailActivity.class);
                    intentDoc.putExtra("url", url);
                    context.startActivity(intentDoc);
                }else if(formatType.equals("2")){//图片
                    Intent intent2 = new Intent(context,
                            PhotoDetailActivity.class);
                    intent2.putExtra("url", url);
                    context.startActivity(intent2);
                }else if(formatType.equals("3")){//音视频
                    if (mobileType==50002){
                        Intent intent2 = new Intent(context,
                                RecordDetailActivity.class);
                        intent2.putExtra("url", url);
                        context.startActivity(intent2);
                    }else if(mobileType==50003){
                        Intent intent2 = new Intent(context,
                                VideoDetailActivity.class);
                        intent2.putExtra("url", url);
                        context.startActivity(intent2);
                    }
                }   else {
            Toaster.showToast(context, "不支持预览该格式的文件，请下载后查看");
        }
                break;
            case R.id.iv_ckzs://查看证书


                break;
        }
    }

    public class ViewHolder{
        private TextView tv_filename,tv_date,tv_filesize;
        private ImageView iv_icon,iv_zhengjiuyulan,iv_ckzs;
    }
}
