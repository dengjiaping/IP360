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
import android.widget.TextView;
import android.widget.Toast;

import com.truthso.ip360.activity.CertificationActivity;
import com.truthso.ip360.activity.CommitMsgActivity;
import com.truthso.ip360.activity.DocumentDetailActivity;
import com.truthso.ip360.activity.FileRemarkActivity;
import com.truthso.ip360.activity.PhotoDetailActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.RecordDetailActivity;
import com.truthso.ip360.activity.SecordLevelActivity;
import com.truthso.ip360.activity.VideoDetailActivity;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.DownLoadFileBean;
import com.truthso.ip360.bean.NotarAccountBean;
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
	private long clickTime;
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
		this.isOpen=Integer.MAX_VALUE;
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
			vh.tv_download1=(TextView) convertView.findViewById(R.id.tv_download1);
			vh.tv_delete1=(TextView) convertView.findViewById(R.id.tv_delete1);
			vh.tv_remark1=(TextView) convertView.findViewById(R.id.tv_remark1);
			vh.tv_file_detail=(TextView) convertView.findViewById(R.id.tv_file_detail);

			vh.tv_sqgz=(TextView) convertView.findViewById(R.id.tv_sqgz);
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
		if(cloudEviItemBean.getLinkCount()>1){
			vh.iv_icon.setBackgroundResource(R.drawable.wenjianjia);
		}else{
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
		}

		boolean queryByPkValue=isDownloaded(cloudEviItemBean.getPkValue());//是否已下载

		if(queryByPkValue){
			vh.tv_downloaded.setVisibility(View.VISIBLE);
			if(cloudEviItemBean.getLinkCount()>1){
				vh.tv_delete1.setVisibility(View.VISIBLE);
				vh.tv_download1.setVisibility(View.GONE);
			}else{
				vh.tv_delete.setVisibility(View.VISIBLE);
				vh.tv_download.setVisibility(View.GONE);
			}
		}else{
			vh.tv_downloaded.setVisibility(View.GONE);
			if(cloudEviItemBean.getLinkCount()>1){
				vh.tv_delete1.setVisibility(View.GONE);
				vh.tv_download1.setVisibility(View.VISIBLE);
			}else{
				vh.tv_delete.setVisibility(View.GONE);
				vh.tv_download.setVisibility(View.VISIBLE);
			}

		}

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
		if(!CheckUtil.isEmpty(fileInfo) && fileInfo.getStatus()!=0){
			return true;
		}
		return false;
	}

	public class ViewHolder {
		public CheckBox cb_choice, cb_option;
		private ImageView iv_icon;
		private TextView tv_filename, tv_filedate, tv_size,tv_downloaded,tv_delete,
				tv_download,tv_sqgz,tv_download1,tv_delete1,tv_remark1,tv_file_detail;
	}

	private void changeState(final int position, View view, CheckBox cb_choice,
			final CheckBox cb_option) {
		CloudEviItemBean cloudEviItemBean = mDatas.get(position);
		final LinearLayout ll_option;
		if(cloudEviItemBean.getLinkCount()>1){
			ll_option = (LinearLayout) view
					.findViewById(R.id.ll_option1);
		}else{
			ll_option= (LinearLayout) view
					.findViewById(R.id.ll_option);
		}

		if (isChoice) {
			cb_choice.setVisibility(View.VISIBLE);
			cb_option.setVisibility(View.GONE);
			//1.1版本本地已经下载有的，不再下载，但是让能选中！！醉了，什么逻辑~
			cb_choice.setClickable(true);
			cb_choice.setBackgroundResource(R.drawable.cb_selector);
			if (isAllSelect&&cb_choice.isClickable()) {
				cb_choice.setChecked(true);
			} else {
				cb_choice.setChecked(false);
			}
			if(ll_option.getVisibility()==View.VISIBLE){
				ll_option.setVisibility(View.GONE);
			}
			cb_choice.setTag(position);
			cb_choice.setOnCheckedChangeListener(this);
		} else {
			cb_choice.setVisibility(View.GONE);
			cb_option.setVisibility(View.VISIBLE);

			cb_option.setChecked(false);
			ll_option.setVisibility(View.GONE);
			if(position==isOpen){
				cb_option.setChecked(true);
				ll_option.setVisibility(View.VISIBLE);
			}		
			TextView tv_remark = (TextView) view.findViewById(R.id.tv_remark);
			TextView tv_remark1 = (TextView) view.findViewById(R.id.tv_remark1);
			TextView tv_download = (TextView) view.findViewById(R.id.tv_download);
			TextView tv_download1 = (TextView) view.findViewById(R.id.tv_download1);
			TextView tv_file_preview = (TextView) view.findViewById(R.id.tv_file_preview);
			TextView tv_certificate_preview = (TextView) view.findViewById(R.id.tv_certificate_preview);
			TextView tv_file_detail = (TextView) view.findViewById(R.id.tv_file_detail);
			TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
			TextView tv_sqgz = (TextView) view.findViewById(R.id.tv_sqgz);


			tv_file_detail.setOnClickListener(this);
			tv_file_detail.setTag(position);

			tv_remark.setOnClickListener(this);
			tv_remark.setTag(position);
			tv_remark1.setOnClickListener(this);
			tv_remark1.setTag(position);
			tv_certificate_preview.setTag(position);
			tv_certificate_preview.setOnClickListener(this);
			tv_download.setTag(position);
			tv_download.setOnClickListener(this);
			tv_download1.setTag(position);
			tv_download1.setOnClickListener(this);
			tv_file_preview.setTag(position);
			tv_file_preview.setOnClickListener(this);
			tv_delete.setTag(position);
			tv_delete.setOnClickListener(this);
			tv_sqgz.setTag(position);
			tv_sqgz.setOnClickListener(this);

			LinearLayout ll_item = (LinearLayout) view .findViewById(R.id.ll_item);
			ll_item.setOnClickListener(new OnClickListener() {//listView条目的点击事件

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
		case R.id.tv_remark1://二级页面的文件夹条目备注
			int position1 = (Integer) v.getTag();
			CloudEviItemBean cloudEviItemBean1 = mDatas.get(position1);
			size = cloudEviItemBean1.getFileSize();
			long l_size1 = Long.parseLong(size);
			size1 = FileSizeUtil.FormetFileSize(l_size1);
			Intent intent1 = new Intent(context, FileRemarkActivity.class);
			intent1.putExtra("count", cloudEviItemBean1.getCount()); // 当次消费钱数
			intent1.putExtra("fileName", cloudEviItemBean1.getFileTitle());
			intent1.putExtra("format", cloudEviItemBean1.getFileFormat());
			intent1.putExtra("date", cloudEviItemBean1.getFileDate());
			intent1.putExtra("size", size1);
			intent1.putExtra("mode", cloudEviItemBean1.getFileMode());
			intent1.putExtra("type", type);
			intent1.putExtra("remarkText", cloudEviItemBean1.getRemarkText());
			intent1.putExtra("pkValue", cloudEviItemBean1.getPkValue());
			intent1.putExtra("dataType", cloudEviItemBean1.getDataType());
			context.startActivity(intent1);
				break;
		case R.id.tv_download:// 下载
			//3s内按钮不能重复点击，不然上次点击的事件，文件还没在下载，重复点击，会下载多个相同文件
			if(System.currentTimeMillis()<clickTime+3000){
				Toast.makeText(MyApplication.getApplication(),"文件正在下载",Toast.LENGTH_SHORT).show();
				return;
			}
			clickTime=System.currentTimeMillis();
			if(!CheckUtil.canDownload(context)){
				return;
			}
			final CloudEviItemBean data = mDatas.get((Integer) v.getTag());
			boolean queryByPkValue = isDownloaded(data.getPkValue());//已经下载
			boolean queryByPkValue1 = isDownloading(data.getPkValue());//正在下载
			if (data.getArreaStatus()==1){//不欠费
			if(queryByPkValue){
				Toast.makeText(MyApplication.getApplication(),"文件已经下载到本地",Toast.LENGTH_SHORT).show();
		   		return;
			}
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
			case R.id.tv_download1://二级页面的下载
				//下载二级页面里的所有文件

				break;

		case R.id.tv_certificate_preview:// 证书预览
			int position2 = (Integer) v.getTag();
			CloudEviItemBean cloudEviItemBean2 = mDatas.get(position2);
				if (cloudEviItemBean2.getArreaStatus() ==1){//不欠费
					Intent intent2 = new Intent(context, CertificationActivity.class);
					intent2.putExtra("pkValue", cloudEviItemBean2.getPkValue());// 唯一标识
					intent2.putExtra("dataType", cloudEviItemBean2.getDataType());//类型
					intent2.putExtra("type", type);// 类型 1-确权 2-现场取证 3-pc取证
					context.startActivity(intent2);
					break;
				}else if(cloudEviItemBean2.getArreaStatus() ==0){//欠费
					Toast.makeText(MyApplication.getApplication(),"该文件欠费不能查看证书，请补全费用后查看！",Toast.LENGTH_SHORT).show();
					return;
				}

		case R.id.tv_file_preview://文件预览

			if (mDatas.size() > 0) {
				final CloudEviItemBean data1 = mDatas.get((Integer) v.getTag());
				String url = null;
				String format = null;
				if (data1.getArreaStatus() == 1){//不欠费
					DbBean dbBean = SqlDao.getSQLiteOpenHelper().searchByPkValue(data1.getPkValue());
					if(dbBean!=null&&FileUtil.IsFileEmpty(dbBean.getResourceUrl())){
						url=dbBean.getResourceUrl();
						format=dbBean.getFileFormat();
					}else{
						 url = data1.getOssUrl();
						 format = data1.getFileFormat();
					}
					format = format.toLowerCase();// 格式变小写
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

					}else if(format.equals(format)){
						Intent intentDoc = new Intent(context, DocumentDetailActivity.class);
						intentDoc.putExtra("url", url);
						context.startActivity(intentDoc);
					} else {
						Toaster.showToast(context, "不支持预览该格式的文件，请下载后查看");
					}
				}else if(data1.getArreaStatus() == 0){//欠费
					Toast.makeText(MyApplication.getApplication(),"该文件欠费不能在线查看，请补全费用后查看！",Toast.LENGTH_SHORT).show();
				}

			}
			case R.id.tv_file_detail://查看二级详情
				int position_sub = (Integer) v.getTag();
				CloudEviItemBean cloudEviItemBean_sub = mDatas.get(position_sub);
				Intent intent_sub = new Intent(context,SecordLevelActivity.class);
				intent_sub.putExtra("type",cloudEviItemBean_sub.getType());
				intent_sub.putExtra("pkValue",cloudEviItemBean_sub.getPkValue());
				context.startActivity(intent_sub);
				break;
			case R.id.tv_sqgz://申请公证
				int position3 = (Integer) v.getTag();
				CloudEviItemBean cloudEviItemBean3 = mDatas.get(position3);
				int pkValue = cloudEviItemBean3.getPkValue();

				//对否实名认证
				AccountMsg(pkValue);

				break;
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

						FileInfo fileInfo = UpDownLoadDao.getDao().queryDownLoadInfoByResourceId(mDatas.get(position).getPkValue());
						if(!CheckUtil.isEmpty(fileInfo)&&fileInfo.getFilePath()!=null){
								try {
									FileUtil.deleteFile(filePaht);
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
						//传输列表不删除
						UpDownLoadDao.getDao().deleteDownInfoByResourceId(mDatas.get(position).getPkValue()+"");
						EventBus.getDefault().post(new CEListRefreshEvent());
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				}).
				create();
		alertDialog.show();
	}

	/**
	 * 申请公证，申请人的账号信息
	 */
	private void AccountMsg(final int pkValue) {
//		showProgress("正在加载...");
		ApiManager.getInstance().getAccountMsg(new ApiCallback() {
			@Override
			public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
//				hideProgress();
				NotarAccountBean bean = (NotarAccountBean) response;
				if (!CheckUtil.isEmpty(bean)){
					if (bean.getCode() == 200){
						int iscer = bean.getDatas().getIscertified();//0-未实名 1-已实名
						if (iscer==1){//已实名
							//跳转到提交信息页面
							Intent intent = new Intent(context, CommitMsgActivity.class);
							intent.putExtra("type",type);
							intent.putExtra("pkValue",pkValue);
							intent.putExtra("count","1");//申请公证的数量
							intent.putExtra("requestName",bean.getDatas().getRequestName());
							intent.putExtra("requestCardId",bean.getDatas().getRequestCardId());
							intent.putExtra("requestPhoneNum",bean.getDatas().getRequestPhoneNum());
							intent.putExtra("requestEmail",bean.getDatas().getRequestEmail());
							context.startActivity(intent);
						}else if(iscer ==0){//未实名
							showDialog("是实名认证后才能申请公证，是否立即认证？");
						}
					}else{
						Toaster.showToast(context,bean.getMsg());
					}

				}else{
					Toaster.showToast(context,"获取数据失败");
				}
			}

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

			}
		});
	}

	private void showDialog(String str) {
		alertDialog = new AlertDialog.Builder(context).setTitle("温馨提示")
				.setMessage(str).setIcon(R.drawable.ww)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//跳转到实名认证页面
						context.startActivity(new Intent(context, CertificationActivity.class));
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				}).create();
		alertDialog.show();
	}
}
