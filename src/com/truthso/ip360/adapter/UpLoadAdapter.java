package com.truthso.ip360.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.truthso.ip360.activity.R;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.dao.WaituploadDao;
import com.truthso.ip360.ossupload.FileUploadHelper;
import com.truthso.ip360.ossupload.ProgressListener;
import com.truthso.ip360.ossupload.UpLoadManager;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.view.SpeedView;

import static com.truthso.ip360.utils.UIUtils.getResources;

/**
 * @despriction :下载列表的adapter
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-10下午4:46:38
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class UpLoadAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private Context context;
	private UpLoadManager instanse = UpLoadManager.getInstance();
	private List<FileInfo> list;
	private ImageView iv_icon;
	private int lastStatus;
	private FileUploadHelper fileUploadHelper;
	private Dialog alertDialog;
	public UpLoadAdapter(Context context, List<FileInfo> list) {
		super();
		this.context = context;
		fileUploadHelper=new FileUploadHelper((Activity)context);
		inflater = LayoutInflater.from(context);
		this.list=list;
	}

	public void notifyChange(List<FileInfo> datas){
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
			vh.iv_icon =(ImageView) convertView.findViewById(R.id.iv_icon);
			vh.tv_fileName = (TextView) convertView.findViewById(R.id.tv_fileName);
			vh.probar = (ProgressBar) convertView.findViewById(R.id.probar);
			vh.btn_upload_download_again = (Button) convertView.findViewById(R.id.btn_upload_download_again);
			vh.tv_status=(SpeedView) convertView.findViewById(R.id.tv_status);
			vh.tv_desc=(TextView) convertView.findViewById(R.id.tv_desc);
			vh.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
			vh.rl_progress= (RelativeLayout) convertView.findViewById(R.id.rl_progress);
			vh.ll_item_updownload= (LinearLayout) convertView.findViewById(R.id.ll_item_updownload);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.ll_item_updownload.setOnLongClickListener(null);
		vh.ll_item_updownload.setOnClickListener(null);

		final FileInfo info = list.get(position);
		vh.tv_fileName.setText(info.getFileName());

		if(position==0||(info.getStatus()==0&&lastStatus!=0)||(info.getStatus()==2&&lastStatus==4)){
			vh.tv_title.setVisibility(View.VISIBLE);
			if(info.getStatus()==2){
				vh.tv_title.setText("正在上传");
			}else if(info.getStatus()==4){
				vh.tv_title.setText("等待上传");
			}else {
				vh.tv_title.setText("上传成功("+(list.size()-position)+")");
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

				vh.tv_desc.setTextColor(getResources().getColor(R.color.huise_66666));
				vh.tv_desc.setText(info.getCompleteDate());
				vh.tv_size.setText(FileSizeUtil.setFileSize(Long.parseLong(info.getFileSize())));
				break;
			case 1://失败
				vh.rl_progress.setVisibility(View.GONE);
				vh.btn_upload_download_again.setVisibility(View.VISIBLE);
				vh.tv_desc.setVisibility(View.VISIBLE);
				vh.tv_size.setVisibility(View.GONE);

				vh.tv_desc.setTextColor(context.getResources().getColor(R.color.jiuhong));
				vh.tv_desc.setText("上传失败");
				vh.btn_upload_download_again.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						UpLoadManager.getInstance().resuambleUploadAgain(info);
					}
				});
				break;
			case 2://运行
				vh.rl_progress.setVisibility(View.VISIBLE);
				vh.btn_upload_download_again.setVisibility(View.GONE);
				vh.tv_desc.setVisibility(View.GONE);
				vh.tv_size.setVisibility(View.GONE);

				vh.probar.setMax(Integer.parseInt(info.getFileSize()));
				instanse.setOnUpLoadProgressListener(info.getResourceId(), new com.truthso.ip360.ossupload.ProgressListener() {

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
			case 4://等待上传
				vh.rl_progress.setVisibility(View.GONE);
				vh.btn_upload_download_again.setVisibility(View.GONE);
				vh.tv_desc.setVisibility(View.VISIBLE);
				vh.tv_size.setVisibility(View.VISIBLE);
				vh.tv_size.setText(FileSizeUtil.setFileSize(Long.parseLong(info.getFileSize())));
				vh.tv_desc.setTextColor(getResources().getColor(R.color.huise_66666));
				vh.tv_desc.setText("点击保全");
				vh.ll_item_updownload.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						fileUploadHelper.uploadFileAgain(info);
					}
				});
				vh.ll_item_updownload.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {

						showDialog(info);
						return true;
					}
				});
				break;
		}

		String str = info.getFileName();
	String	foramt1 = str.substring(str.lastIndexOf(".")+1);
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
		private TextView tv_fileName,tv_size,tv_title,tv_desc;
		private SpeedView tv_status;
		private ProgressBar probar;
		private ImageView iv_icon;
		private Button btn_upload_download_again;
		private RelativeLayout rl_progress;
		private LinearLayout ll_item_updownload;
	}


	//删除本地文件
	private void showDialog(final FileInfo info) {
	            alertDialog = new AlertDialog.Builder(context).
				setTitle("温馨提示").
				setMessage("是否确认删除？").
				setIcon(R.drawable.ww).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						WaituploadDao.getDao().delete(info.getId());
						try {
							FileUtil.deletefile(info.getFilePath());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).
				setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				}).
				create();
		alertDialog.show();
	}


}
