package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.activity.CertificationActivity;
import com.truthso.ip360.activity.DocumentDetailActivity;
import com.truthso.ip360.activity.FileRemarkActivity;
import com.truthso.ip360.activity.PhotoDetailActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.RecordDetailActivity;
import com.truthso.ip360.activity.VideoDetailActivity;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.DownLoadFileBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.event.CEListRefreshEvent;
import com.truthso.ip360.fragment.UpdateItem;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.FileUtil;

import org.greenrobot.eventbus.EventBus;

import cz.msebera.android.httpclient.Header;

public class CloudEvidenceAdapter extends BaseAdapter implements
		OnCheckedChangeListener, OnClickListener {
	private Context context;
	private LayoutInflater inflater;
	private boolean isAllSelect = false;
	private boolean isChoice = false;
	protected List<CloudEviItemBean> mDatas;
	private int pkValue, type, mobileType;
	private int count;// 当次消费条数
	private int lastPosition = 1;
	private String fileName, format, date, size, mode, remarkText;
	private String size1;
	private List<CloudEviItemBean> selectedList = new ArrayList<CloudEviItemBean>();
	private UpdateItem updateItem;
	private int isOpen=Integer.MAX_VALUE;
	private Dialog alertDialog;
	public CloudEvidenceAdapter(Context context, List<CloudEviItemBean> mDatas,
			int type, int mobileType) {
		super();
		this.context = context;
		this.mDatas = mDatas;
		this.type = type;
		this.mobileType = mobileType;
		inflater = LayoutInflater.from(context);
	}

	public void setUpdateItem(UpdateItem updateItem){
		this.updateItem=updateItem;
	}
	
	public void setisOpen(int position){
		this.isOpen=position;
	}
	
	public void addData(List<CloudEviItemBean> mDatas) {
		this.mDatas.clear();
		this.mDatas.addAll(mDatas);
		notifyDataSetChanged();
	}

	public void setChoice(Boolean isChoice) {
		this.isAllSelect = false;
		this.isChoice = isChoice;
		selectedList.clear();
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
		CloudEviItemBean cloudEviItemBean = mDatas.get(position);
		pkValue =cloudEviItemBean.getPkValue();
		count = cloudEviItemBean.getCount();
		fileName = cloudEviItemBean.getFileTitle();
		format = cloudEviItemBean.getFileFormat();
		date =cloudEviItemBean.getFileDate();
		size = cloudEviItemBean.getFileSize();
		long l_size = Long.parseLong(size);
		size1 = FileSizeUtil.FormetFileSize(l_size);
		mode = cloudEviItemBean.getFileMode();
		remarkText = cloudEviItemBean.getRemarkText();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_cloudevidence, null);
			vh = new ViewHolder();

			vh.cb_choice = (CheckBox) convertView.findViewById(R.id.cb_choice);
			vh.cb_option = (CheckBox) convertView.findViewById(R.id.cb_option);
			vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			vh.tv_filename = (TextView) convertView.findViewById(R.id.tv_filename);
			vh.tv_filedate = (TextView) convertView.findViewById(R.id.tv_filedate);
			vh.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
			vh.tv_downloaded=(TextView) convertView.findViewById(R.id.tv_downloaded);
			vh.tv_delete=(TextView) convertView.findViewById(R.id.tv_delete);
			vh.tv_download=(TextView) convertView.findViewById(R.id.tv_download);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();

		}
		String fileName= mDatas.get(position).getFileTitle();
			if (!CheckUtil.isEmpty(remarkText)){//有备注的，文件名显示备注
				vh.tv_filename.setText(remarkText);
			}else{
				vh.tv_filename.setText(fileName);
			}

		vh.tv_filedate.setText(cloudEviItemBean.getFileDate());
		//long l_size = Long.parseLong(cloudEviItemBean.getFileSize());
		String s_size = FileSizeUtil.setFileSize(l_size);

		vh.tv_size.setText(s_size);
		String format = cloudEviItemBean.getFileFormat();
		format = format.toLowerCase();// 格式变小写
		// if (CheckUtil.isEmpty(format)) {
		if (CheckUtil.isFormatPhoto(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_tp);
		} else if (CheckUtil.isFormatVideo(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_sp);
		} else if (CheckUtil.isFormatRadio(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_yp);
		} else if (CheckUtil.isFormatDoc(format)) {
			vh.iv_icon.setBackgroundResource(R.drawable.icon_bq);
		}

		boolean queryByPkValue=isDownloaded(cloudEviItemBean.getPkValue());//是否已下载

		if(queryByPkValue){
			vh.tv_downloaded.setVisibility(View.VISIBLE);
			vh.tv_delete.setVisibility(View.VISIBLE);
			vh.tv_download.setVisibility(View.GONE);
		}else{
			vh.tv_downloaded.setVisibility(View.GONE);
			vh.tv_delete.setVisibility(View.GONE);
			vh.tv_download.setVisibility(View.VISIBLE);
		}
		//boolean queryByPkValue1 = UpDownLoadDao.getDao().queryByPkValue(cloudEviItemBean.getPkValue());//正在下载

		changeState(position, convertView, vh.cb_choice, vh.cb_option);

		return convertView;
	}

	//检查是否已下载
	private boolean isDownloaded(int pkValue) {
		DbBean dbBean = SqlDao.getSQLiteOpenHelper().searchByPkValue(pkValue);//已经下载
		//FileInfo fileInfo = UpDownLoadDao.getDao().queryDownLoadInfoByResourceId(pkValue);

		if(!CheckUtil.isEmpty(dbBean)&&dbBean.getResourceUrl()!=null){
			return FileUtil.IsFileEmpty(dbBean.getResourceUrl());
		}
		return false;
	}
	//检查是否正在下载
	private boolean isDownloading(int pkValue) {
		//DbBean dbBean = SqlDao.getSQLiteOpenHelper().searchByPkValue(pkValue);//已经下载
		FileInfo fileInfo = UpDownLoadDao.getDao().queryDownLoadInfoByResourceId(pkValue);

		if(!CheckUtil.isEmpty(fileInfo)&&fileInfo.getStatus()!=0){
			return true;
		}
		return false;
	}

	public class ViewHolder {
		public CheckBox cb_choice, cb_option;
		private ImageView iv_icon;
		private TextView tv_filename, tv_filedate, tv_size,tv_downloaded,tv_delete,tv_download;
	}

	private void changeState(final int position, View view, CheckBox cb_choice,
			final CheckBox cb_option) {
		if (isChoice) {
			cb_choice.setVisibility(View.VISIBLE);
			cb_option.setVisibility(View.GONE);
			//本地证据已经存在或者在传输列表里面或者欠费，禁止选择
		//	boolean queryByPkValue = SqlDao.getSQLiteOpenHelper().queryByPkValue(mDatas.get(position).getPkValue());
		//	boolean queryByPkValue1 = UpDownLoadDao.getDao().queryByPkValue(mDatas.get(position).getPkValue());
		//	Log.i("djj",""+queryByPkValue+queryByPkValue1+mDatas.get(position).getArreaStatus());
			/*if(queryByPkValue||queryByPkValue1||mDatas.get(position).getArreaStatus()==0){
				cb_choice.setBackgroundResource(R.drawable.cbox);
				cb_choice.setClickable(false);
			}else {
				cb_choice.setClickable(true);
				cb_choice.setBackgroundResource(R.drawable.cb_selector);
			}*/
			//1.1版本本地已经下载有的，不再下载，但是让能选中！！醉了，什么逻辑~
			cb_choice.setClickable(true);
			cb_choice.setBackgroundResource(R.drawable.cb_selector);
			if (isAllSelect&&cb_choice.isClickable()) {
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
			cb_option.setChecked(false);
			ll_option.setVisibility(View.GONE);
			if(position==isOpen){
				cb_option.setChecked(true);
				ll_option.setVisibility(View.VISIBLE);
			}		
			TextView tv_remark = (TextView) view.findViewById(R.id.tv_remark);
			TextView tv_download = (TextView) view
					.findViewById(R.id.tv_download);
			TextView tv_file_preview = (TextView) view
					.findViewById(R.id.tv_file_preview);
			TextView tv_certificate_preview = (TextView) view
					.findViewById(R.id.tv_certificate_preview);
			TextView tv_delete = (TextView) view
					.findViewById(R.id.tv_delete);

			tv_remark.setOnClickListener(this);
			tv_remark.setTag(position);
			tv_certificate_preview.setTag(position);
			tv_certificate_preview.setOnClickListener(this);
			tv_download.setTag(position);
			tv_download.setOnClickListener(this);
			tv_file_preview.setTag(position);
			tv_file_preview.setOnClickListener(this);
			tv_delete.setTag(position);
			tv_delete.setOnClickListener(this);

			LinearLayout ll_item = (LinearLayout) view .findViewById(R.id.ll_item);
			ll_item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(isChoice){
						return;
					}
					if(ll_option.getVisibility()==View.VISIBLE){
						ll_option.setVisibility(View.GONE);
						isOpen=Integer.MAX_VALUE;
						cb_option.setChecked(false);
					}else{
						ll_option.setVisibility(View.VISIBLE);
						updateItem.update(position);
						isOpen=position;
						cb_option.setChecked(true);
					}			
				}
			});
		
			cb_option.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(ll_option.getVisibility()==View.VISIBLE){
						ll_option.setVisibility(View.GONE);
						isOpen=Integer.MAX_VALUE;
					}else{
						ll_option.setVisibility(View.VISIBLE);
						updateItem.update(position);
						isOpen=position;
					}
				}
			});
		}
	}

	public List<CloudEviItemBean> getSelected() {
		return selectedList;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int position = (Integer) buttonView.getTag();
		if (isChecked) {
			selectedList.add(mDatas.get(position));
		} else {
			selectedList.remove(mDatas.get(position));
		}
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
			case R.id.tv_delete:// 删除
				showDialog((int)v.getTag());
				break;
		case R.id.tv_remark:// 备注
			int position = (Integer) v.getTag();
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
			intent.putExtra("dataType", cloudEviItemBean.getDataType());
			context.startActivity(intent);
			break;
		case R.id.tv_download:// 下载
			final CloudEviItemBean data = mDatas.get((Integer) v.getTag());
			boolean queryByPkValue = isDownloaded(data.getPkValue());//已经下载
			boolean queryByPkValue1 = isDownloading(data.getPkValue());//正在下载
			if (data.getArreaStatus()==1){//不欠费
			if(queryByPkValue){
				Toast.makeText(MyApplication.getApplication(),"文件已经下载到本地",Toast.LENGTH_SHORT).show();
		   		return;}
				if(queryByPkValue1){
					Toast.makeText(MyApplication.getApplication(),"文件正在下载",Toast.LENGTH_SHORT).show();
					return;
				}
			ApiManager.getInstance().downloadFile(data.getPkValue(), type,data.getDataType(),
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
//									String nativePath = MyConstants.DOWNLOAD_PATH+ "/" + data.getFileTitle();
									String fileUrl = bean.getDatas().getFileUrl();//阿里云的objectKey
									String fileUrlformat= fileUrl.replace("/","-");
									//因为有文件名相同的情况，把阿里云的objectkey路径当成文件名
									String nativePath = MyConstants.DOWNLOAD_PATH+ "/" + fileUrlformat;
									info.setFilePath(nativePath);// 在本地的路径
									info.setFileName(bean.getDatas().getFileName());
									info.setFileUrlFormatName(fileUrlformat);
									info.setType(type);// 取证类型
									info.setMobiletype(mobileType);// 现场取证的类型
									long l_size = Long.parseLong(data
											.getFileSize());
									String s_size = FileSizeUtil
											.setFileSize(l_size);
									info.setFileSize(s_size);
									info.setLlsize(data.getFileSize());
									info.setPosition(0);
									info.setFileTime(data.getFileTime());
									info.setResourceId(data.getPkValue());
									info.setFileLoc(data.getFileLocation());
									info.setFileCreatetime(data.getFileDate());
									info.setPkValue(data.getPkValue());
									info.setFileFormat(data.getFileFormat());
									info.setDataType(data.getDataType());
									String url = bean.getDatas().getFileUrl();//文件的下载路径
									info.setObjectKey(url);
									info.setReMark(data.getRemarkText());
									// 下载
									DownLoadHelper.getInstance().downloadFile(
											info);


								} else {
									Toaster.toast(context, bean.getMsg(), 1);
								}
							} else {
								Toaster.toast(context, "获取数据失败", 1);
							}
						}
					});
			}else if (data.getArreaStatus()==0){//欠费
				Toast.makeText(MyApplication.getApplication(),"该文件欠费不能进行下载，请补全费用后下载！",Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		case R.id.tv_certificate_preview:// 证书预览
			int position1 = (Integer) v.getTag();
			CloudEviItemBean cloudEviItemBean1 = mDatas.get(position1);
				if (cloudEviItemBean1.getArreaStatus() ==1){//不欠费
					Intent intent1 = new Intent(context, CertificationActivity.class);
					intent1.putExtra("pkValue", cloudEviItemBean1.getPkValue());// 唯一标识
					intent1.putExtra("dataType", cloudEviItemBean1.getDataType());//类型
					intent1.putExtra("type", type);// 类型 1-确权 2-现场取证 3-pc取证
					context.startActivity(intent1);
					break;
				}else if(cloudEviItemBean1.getArreaStatus() ==0){//欠费
					Toast.makeText(MyApplication.getApplication(),"该文件欠费不能查看证书，请补全费用后查看！",Toast.LENGTH_SHORT).show();
					return;
				}

		case R.id.tv_file_preview://文件预览

			if (mDatas.size() > 0) {
				final CloudEviItemBean data1 = mDatas.get((Integer) v.getTag());
				String url = null;
				String format = null;
				if (data1.getArreaStatus() == 1){//不欠费
					FileInfo fileInfo = UpDownLoadDao.getDao().queryDownLoadInfoByResourceId(data1.getPkValue());
					if(fileInfo!=null&&FileUtil.IsFileEmpty(fileInfo.getFilePath())){
						url=fileInfo.getFilePath();
						format=fileInfo.getFileFormat();
					}else{
						 url = data1.getOssUrl();
						 format = data1.getFileFormat();
					}
					format = format.toLowerCase();// 格式变小写
					Log.i("djj", data1.getOssUrl());
					if (CheckUtil.isFormatVideo(format)) {// 视频
						Intent intent2 = new Intent(context,
								VideoDetailActivity.class);
						intent2.putExtra("url", url);
						context.startActivity(intent2);
					} else if (CheckUtil.isFormatPhoto(format)) {// 照片
						Intent intent2 = new Intent(context,
								PhotoDetailActivity.class);
						intent2.putExtra("url", url);
						context.startActivity(intent2);
					} else if (CheckUtil.isFormatRadio(format)) {// 音频
						Intent intent2 = new Intent(context,
								RecordDetailActivity.class);
						intent2.putExtra("url", url);
						context.startActivity(intent2);

					}/*else if(format.equals(format)){
						Intent intentDoc = new Intent(context, DocumentDetailActivity.class);
						intentDoc.putExtra("url", url);
						context.startActivity(intentDoc);
					}*/ else {
						Toaster.showToast(context, "不支持预览该格式的文件，请下载后查看");
					}
				}else if(data1.getArreaStatus() == 0){//欠费
					Toast.makeText(MyApplication.getApplication(),"该文件欠费不能在线查看，请补全费用后查看！",Toast.LENGTH_SHORT).show();
				}

			}

		default:
			break;
		}
	}

	public void notifyDataChange(List<CloudEviItemBean> list, int type,
			int mobileType) {
		if (list != null) {
			this.type = type;
			this.mDatas = list;
			this.mobileType = mobileType;
			isOpen=Integer.MAX_VALUE;
			notifyDataSetChanged();
		}
	}

	//删除本地文件
	private void showDialog(final int position) {
		alertDialog = new AlertDialog.Builder(context).
				setTitle("温馨提示").
				setMessage("是否确认删除？").
				setIcon(R.drawable.ww).
				setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String filePaht=null;
						DbBean dbBean = SqlDao.getSQLiteOpenHelper().searchByPkValue(mDatas.get(position).getPkValue());
						if(!CheckUtil.isEmpty(dbBean)){
							filePaht=dbBean.getResourceUrl();
						}
						int count=SqlDao.getSQLiteOpenHelper().deleteByPkValue(MyConstants.TABLE_MEDIA_DETAIL,mDatas.get(position).getPkValue());
						Log.i("djj",mDatas.get(position).getPkValue()+"PkValue");

						FileInfo fileInfo = UpDownLoadDao.getDao().queryDownLoadInfoByResourceId(mDatas.get(position).getPkValue());
						if(!CheckUtil.isEmpty(fileInfo)&&fileInfo.getFilePath()!=null){
								try {
									FileUtil.deleteFile(filePaht);
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
						UpDownLoadDao.getDao().deleteDownInfoByResourceId(mDatas.get(position).getPkValue()+"");
						EventBus.getDefault().post(new CEListRefreshEvent());
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
