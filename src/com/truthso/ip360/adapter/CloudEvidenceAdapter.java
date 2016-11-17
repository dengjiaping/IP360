package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.truthso.ip360.activity.CertificationActivity;
import com.truthso.ip360.activity.FileRemarkActivity;
import com.truthso.ip360.activity.PhotoDetailActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.RecordDetailActivity;
import com.truthso.ip360.activity.VideoDetailActivity;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.DownLoadFileBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.NetStatusUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

import cz.msebera.android.httpclient.Header;

public class CloudEvidenceAdapter extends BaseAdapter implements
		OnCheckedChangeListener, OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private boolean isAllSelect = false;
	private boolean isChoice = false;
	protected List<CloudEviItemBean> mDatas;
	private int pkValue, type,mobileType;
	private int count;// 当次消费条数
	private String fileName, format, date, size, mode,remarkText;
	private String size1;
	private List<CloudEviItemBean> selectedList=new ArrayList<CloudEviItemBean>();
	public CloudEvidenceAdapter(Context context, List<CloudEviItemBean> mDatas,
			int type,int mobileType) {
		super();
		this.context = context;
		this.mDatas = mDatas;
		this.type = type;
		this.mobileType = mobileType;
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
//		RelativeLayout	rl_item=(RelativeLayout) convertView.findViewById(R.id.rl_item);
//		rl_item.setTag(position);
//		rl_item.setOnClickListener(this);
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
//			format = fileName.substring(fileName.indexOf("."));// 格式
			format = mDatas.get(position).getFileFormat();
			date = mDatas.get(position).getFileDate();
			size = mDatas.get(position).getFileSize();
			long l_size = Long.parseLong(size);
			size1 = FileSizeUtil.FormetFileSize(l_size);
			mode = mDatas.get(position).getFileMode();
			remarkText= mDatas.get(position).getRemarkText();
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
			TextView tv_file_preview = (TextView) view
					.findViewById(R.id.tv_file_preview);
			
			TextView tv_certificate_preview = (TextView) view
					.findViewById(R.id.tv_certificate_preview);
			tv_remark.setOnClickListener(this);
			tv_remark.setTag(position);
			tv_certificate_preview.setTag(position);
			tv_certificate_preview.setOnClickListener(this);
			tv_download.setTag(position);
			tv_download.setOnClickListener(this);
			tv_file_preview.setTag(position);
			tv_file_preview.setOnClickListener(this);
			
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

	public List<CloudEviItemBean> getSelected(){
		return selectedList;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int position = (Integer) buttonView.getTag();;
		if(isChecked){
			selectedList.add(mDatas.get(position));
		}else{
			selectedList.remove(mDatas.get(position));
		}	 
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.tv_remark:// 备注
			int position =  (Integer) v.getTag();
			CloudEviItemBean cloudEviItemBean = mDatas.get(position);
			size = cloudEviItemBean.getFileSize();
			long l_size = Long.parseLong(size);
			size1 = FileSizeUtil.FormetFileSize(l_size);
			Intent intent = new Intent(context, FileRemarkActivity.class);
			intent.putExtra("count", cloudEviItemBean.getCount()); // 当次消费钱数
			intent.putExtra("fileName", cloudEviItemBean.getFileTitle());
			intent.putExtra("format", cloudEviItemBean.getFileFormat());
			intent.putExtra("date", cloudEviItemBean.getFileDate());
			intent.putExtra("size", size1);
			intent.putExtra("mode", cloudEviItemBean.getFileMode());
			intent.putExtra("type", type);
			intent.putExtra("remarkText", cloudEviItemBean.getRemarkText());
			intent.putExtra("pkValue", cloudEviItemBean.getPkValue());
			context.startActivity(intent);
			break;
		case R.id.tv_download:// 下载
			final CloudEviItemBean data = mDatas.get((Integer) v.getTag());
			// final CloudEviItemBean data = mDatas.get(v.getId());
			LogUtils.e("下载的pkvalue"+data.getPkValue()+"下载的type"+type);
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
									String nativePath = MyConstants.DOWNLOAD_PATH+"/"+data.getFileTitle();
									info.setFilePath(nativePath);//在本地的路径
									info.setFileName(data.getFileTitle());
									info.setType(type);//取证类型
									info.setMobiletype(mobileType);//现场取证的类型
									long l_size = Long.parseLong(data.getFileSize());
									String s_size = FileSizeUtil.setFileSize(l_size);
									info.setFileSize(s_size);
									info.setLlsize(data.getFileSize());
									info.setPosition(0);
									info.setFileTime(data.getFileTime());
									info.setResourceId(data.getPkValue());
									info.setFileLoc(data.getFileLocation());
									info.setFileCreatetime(data.getFileDate());
									info.setPkValue(data.getPkValue());
									info.setFileFormat(data.getFileFormat());
									String url = bean.getDatas().getFileUrl();
									// String
									// objectKey=url.substring(url.indexOf("/")+1);
									info.setObjectKey(url);
									// 下载
									DownLoadHelper.getInstance().downloadFile(
											info);

									Toast toast = new Toast(context);
									toast.makeText(context, "文件开始下载到本地证据", Toast.LENGTH_SHORT)
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
			//仅wifi下可查看证书
			/*boolean isWifi=(Boolean) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY,MyConstants.ISWIFI,SharePreferenceUtil.VALUE_IS_BOOLEAN);
			if(isWifi&&!NetStatusUtil.isWifiValid(MyApplication.getApplication())){
				Toaster.showToast(MyApplication.getApplication(),"仅WIFI网络可查看证书");
				return;
			}*/
			int position1 =  (Integer) v.getTag();
			CloudEviItemBean cloudEviItemBean1 = mDatas.get(position1);

			Intent intent1 = new Intent(context, CertificationActivity.class);
			intent1.putExtra("pkValue", cloudEviItemBean1.getPkValue());// 唯一标识
			intent1.putExtra("type", type);// 类型 1-确权 2-现场取证 3-pc取证
			context.startActivity(intent1);
			break;
		case R.id.tv_file_preview:
			//仅wifi下可查看证书
			boolean isWifi=(Boolean) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY,MyConstants.ISWIFI,SharePreferenceUtil.VALUE_IS_BOOLEAN);
			if(isWifi&&!NetStatusUtil.isWifiValid(MyApplication.getApplication())){
				Toaster.showToast(MyApplication.getApplication(),"仅WIFI网络下可查看");
				return;
			}
			if (mDatas.size()>0) {
				final CloudEviItemBean data1 = mDatas.get((Integer) v.getTag());
				String url = "http://"+data1.getOssUrl();
				String format=data1.getFileFormat();
				format = format.toLowerCase();//格式变小写
				Log.i("djj", data1.getOssUrl());
				if(CheckUtil.isFormatVideo(format)){//视频
					Intent intent2 = new Intent(context, VideoDetailActivity.class);
					intent2.putExtra("url", url);
					context.startActivity(intent2);
				}else if(CheckUtil.isFormatPhoto(format)) {//照片
					Intent intent2 = new Intent(context, PhotoDetailActivity.class);
					intent2.putExtra("url", url);
					intent2.putExtra("from","cloud");//给个标记知道是云端的照片查看，不是本地的
					context.startActivity(intent2);
				}else if (CheckUtil.isFormatRadio(format)) {//音频
					Intent intent2 = new Intent(context, RecordDetailActivity.class);
					intent2.putExtra("url", url);
					//intent2.putExtra("from","cloud");//给个标记知道是云端的照片查看，不是本地的
					context.startActivity(intent2);
				}else{
					Toaster.showToast(context, "不支持预览该格式的文件，请下载后查看");
				}
			}
		
		default:
			break;
		}
	}

	public void notifyDataChange(List<CloudEviItemBean> list,int mobileType) {
		if (list != null) {
			this.mDatas = list;
            this.mobileType=mobileType;
			notifyDataSetChanged();
		}
	}
}
