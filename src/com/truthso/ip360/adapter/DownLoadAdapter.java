package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.DateUtil;
import com.truthso.ip360.view.SpeedView;

/**
 * @despriction :下载列表的adapter
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-10下午4:46:38
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class DownLoadAdapter extends BaseAdapter  {
	private LayoutInflater inflater;
	private Context context;
    private DownLoadHelper helper=DownLoadHelper.getInstance();
	private List<FileInfo> list;
	private String foramt1;
	private int lastStatus;

	public DownLoadAdapter(Context context, List<FileInfo> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);

	}

	public void notifyChange(List<FileInfo> datas) {
		list.clear();
		list.addAll(datas);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
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
		final ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_updownload, null);
			vh = new ViewHolder();
			vh.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			vh.tv_fileName = (TextView) convertView.findViewById(R.id.tv_fileName);
			vh.probar = (ProgressBar) convertView.findViewById(R.id.probar);
			vh.btn_upload_download_again = (Button) convertView.findViewById(R.id.btn_upload_download_again);
			vh.tv_status = (SpeedView) convertView.findViewById(R.id.tv_status);
			vh.tv_desc=(TextView) convertView.findViewById(R.id.tv_desc);
			vh.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
			vh.rl_progress= (RelativeLayout) convertView.findViewById(R.id.rl_progress);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final FileInfo info = list.get(position);
		if(!CheckUtil.isEmpty(info.getReMark())){
			vh.tv_fileName.setText(info.getReMark());
		}else{
			vh.tv_fileName.setText(info.getFileName());
		}

		if(position==0||(info.getStatus()==0&&lastStatus!=0)){
			vh.tv_title.setVisibility(View.VISIBLE);
			if(info.getStatus()!=0){
				vh.tv_title.setText("正在下载");
			}else {
				vh.tv_title.setText("下载成功("+(list.size()-position)+")");
			}
		}else{
			vh.tv_title.setVisibility(View.GONE);
		}

		switch (info.getStatus()){
			case 0://成功
				vh.rl_progress.setVisibility(View.GONE);
				vh.btn_upload_download_again.setVisibility(View.GONE);
				vh.tv_size.setVisibility(View.VISIBLE);
				vh.tv_desc.setVisibility(View.VISIBLE);

				vh.tv_desc.setTextColor(context.getResources().getColor(R.color.black));
				String date = new DateFormat().format("yyyy-MM-dd HH:mm:ss", Calendar.getInstance(Locale.CHINA)).toString();
				vh.tv_desc.setText(date);
				vh.tv_size.setText(info.getFileSize());
				break;
			case 1://失败
				vh.rl_progress.setVisibility(View.GONE);
				vh.btn_upload_download_again.setVisibility(View.VISIBLE);
				vh.tv_desc.setVisibility(View.VISIBLE);
				vh.tv_size.setVisibility(View.GONE);

				vh.tv_desc.setTextColor(context.getResources().getColor(R.color.jiuhong));
				vh.tv_desc.setText("下载失败");
				vh.btn_upload_download_again.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(DownLoadHelper.getInstance().downloadFile(info)){
							UpDownLoadDao.getDao().deleteDownInfoByResourceId(info.getResourceId()+"");
						}
					}
				});
				break;
			case 2://运行
				vh.rl_progress.setVisibility(View.VISIBLE);
				vh.btn_upload_download_again.setVisibility(View.GONE);
				vh.tv_desc.setVisibility(View.GONE);
				vh.tv_size.setVisibility(View.GONE);

				vh.probar.setProgress(info.getPosition());
				vh.probar.setMax(Integer.parseInt(info.getLlsize()));
				helper.setOnprogressListener(info.getObjectKey(), new com.truthso.ip360.ossupload.ProgressListener() {

					@Override
					public void onProgress(long progress) {
						vh.probar.setProgress((int)progress);
						vh.tv_status.setProgress(progress);
					}
					@Override
					public void onComplete() {

					}
					@Override
					public void onFailure() {
						vh.tv_status.setStatus(false);
					}
				});
				break;
			case 3://等待wifi
				vh.rl_progress.setVisibility(View.GONE);
				vh.btn_upload_download_again.setVisibility(View.VISIBLE);
				vh.tv_desc.setVisibility(View.VISIBLE);
				vh.tv_size.setVisibility(View.GONE);

				vh.tv_desc.setTextColor(context.getResources().getColor(R.color.black));
				vh.tv_desc.setText("等待wifi");
				break;
		}


		String str =info.getFileName();
		foramt1 = str.substring(str.lastIndexOf(".")+1);
		String format= foramt1.toLowerCase();// 格式变小写
		if (CheckUtil.isFormatPhoto(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_tp);
		} else if (CheckUtil.isFormatVideo(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_sp);
		} else if (CheckUtil.isFormatRadio(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_yp);
		} else if (CheckUtil.isFormatDoc(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_bq);
		}
		lastStatus=info.getStatus();
		return convertView;
	}

	class ViewHolder {
		private TextView tv_fileName ,tv_size,tv_title,tv_desc;
		private SpeedView tv_status;
		private ProgressBar probar;
		private Button btn_upload_download_again;
		private ImageView iv_icon;
		private RelativeLayout rl_progress;
	}
}
