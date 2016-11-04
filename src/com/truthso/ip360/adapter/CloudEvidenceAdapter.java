package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;
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
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.DownLoadFileBean;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.DownLoadHelper;
import com.truthso.ip360.updownload.DownLoadManager;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import cz.msebera.android.httpclient.Header;

public class CloudEvidenceAdapter extends BaseAdapter implements
		OnCheckedChangeListener, OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private boolean isAllSelect = false;
	private boolean isChoice = false;
	protected List<CloudEviItemBean> mDatas;
	private int pkValue, type;
	private int count;// 当次消费条数
	private String fileName, format, date, size, mode;
	private String size1;

	public CloudEvidenceAdapter(Context context, List<CloudEviItemBean> mDatas,
			int type) {
		super();
		this.context = context;
		this.mDatas = mDatas;
		this.type = type;
		inflater = LayoutInflater.from(context);
	}

	public void addData(List<CloudEviItemBean> mDatas) {
		this.mDatas.clear();
		this.mDatas.addAll(mDatas);
		notifyDataSetChanged();
	}

	public void setChoice(Boolean isChoice) {
		this.isAllSelect = false;
		this.isChoice = isChoice;
	}

	public void clearData() {
		mDatas.clear();
		notifyDataSetChanged();
	}

	public void setAllSelect(Boolean isAllSelect) {
		this.isChoice = true;
		this.isAllSelect = isAllSelect;
	}

	@Override
	public int getCount() {
		return mDatas.size();
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
			vh.tv_filename = (TextView) convertView
					.findViewById(R.id.tv_filename);
			vh.tv_filedate = (TextView) convertView
					.findViewById(R.id.tv_filedate);
			vh.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

			pkValue = mDatas.get(position).getPkValue();
			count = mDatas.get(position).getCount();
			fileName = mDatas.get(position).getFileTitle();
			format = fileName.substring(fileName.indexOf("."));// 格式
			date = mDatas.get(position).getFileDate();
			size = mDatas.get(position).getFileSize();
			long l_size = Long.parseLong(size);
			size1 = FileSizeUtil.FormetFileSize(l_size);
			mode = mDatas.get(position).getFileMode();
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();

		}

		vh.tv_filename.setText(mDatas.get(position).getFileTitle());
		vh.tv_filedate.setText(mDatas.get(position).getFileDate());
		long l_size = Long.parseLong(mDatas.get(position).getFileSize());
		String s_size = FileSizeUtil.setFileSize(l_size);
		
		vh.tv_size.setText(s_size);

		changeState(position, convertView, vh.cb_choice, vh.cb_option);

		return convertView;
	}

	class ViewHolder {
		private CheckBox cb_choice, cb_option;
		private TextView tv_filename, tv_filedate, tv_size;
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
			TextView tv_remark = (TextView) view.findViewById(R.id.tv_remark);
			TextView tv_download = (TextView) view
					.findViewById(R.id.tv_download);
			TextView tv_certificate_preview = (TextView) view
					.findViewById(R.id.tv_certificate_preview);
			tv_remark.setOnClickListener(this);
			tv_certificate_preview.setOnClickListener(this);
			tv_download.setTag(position);
			tv_download.setOnClickListener(this);

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
		}
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.tv_remark:// 备注
			Intent intent = new Intent(context, FileRemarkActivity.class);
			intent.putExtra("count", count); // 当次消费钱数
			intent.putExtra("fileName", fileName);
			intent.putExtra("format", format);
			intent.putExtra("date", date);
			intent.putExtra("size", size1);
			intent.putExtra("mode", mode);
			intent.putExtra("type", type);
			intent.putExtra("pkValue", pkValue);
			context.startActivity(intent);
			break;
		case R.id.tv_download:// 下载

			final CloudEviItemBean data = mDatas.get((Integer) v.getTag());
			// final CloudEviItemBean data = mDatas.get(v.getId());

			ApiManager.getInstance().downloadFile(data.getPkValue(), type,
					new ApiCallback() {

						@Override
						public void onApiResultFailure(int statusCode,
								Header[] headers, byte[] responseBody,
								Throwable error) {
							Toaster.toast(context, "获取数据失败", 1);
						}

						@Override
						public void onApiResult(int errorCode, String message,
								BaseHttpResponse response) {
							DownLoadFileBean bean = (DownLoadFileBean) response;
							if (!CheckUtil.isEmpty(bean)) {
								if (bean.getCode() == 200) {
									FileInfo info = new FileInfo();
									info.setFilePath(bean.getDatas()
											.getFileUrl());
									info.setFileName(data.getFileTitle());
									info.setFileSize(data.getFileSize());
									// info.setFileSize(size);
									info.setPosition(0);
									info.setResourceId(data.getPkValue());
									// DownLoadManager.getInstance().startDownload(info);
									String url = bean.getDatas().getFileUrl();
									// String
									// objectKey=url.substring(url.indexOf("/")+1);
									info.setObjectKey(url);
									// 下载
									DownLoadHelper.getInstance().downloadFile(
											info);

									Toast toast = new Toast(context);
									toast.makeText(context, "文件开始下载到本地证据", 1)
											.show();
									toast.setGravity(Gravity.CENTER, 0, 0);
								} else {
									Toaster.toast(context, bean.getMsg(), 1);
								}
							} else {
								Toaster.toast(context, "获取数据失败", 1);
							}
						}
					});
			break;
		case R.id.tv_certificate_preview:// 证书预览
			Intent intent1 = new Intent(context, CertificationActivity.class);
			intent1.putExtra("pkValue", pkValue);// 唯一标识
			intent1.putExtra("type", type);// 类型 1-确权 2-现场取证 3-pc取证
			context.startActivity(intent1);
			break;

		default:
			break;
		}
	}

	public void notifyDataChange(List<CloudEviItemBean> list) {
		if (list != null) {
			this.mDatas = list;

			notifyDataSetChanged();
		}
	}
}
