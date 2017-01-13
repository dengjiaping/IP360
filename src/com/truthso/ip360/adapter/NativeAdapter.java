package com.truthso.ip360.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.db.annotation.Check;
import com.lidroid.xutils.util.LogUtils;
import com.truthso.ip360.activity.CertificationActivity;
import com.truthso.ip360.activity.LoginActivity;
import com.truthso.ip360.activity.PhotoDetailActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.RecordDetailActivity;
import com.truthso.ip360.activity.VideoDetailActivity;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.CertificateInfoBean;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.fragment.NativeEvidence;
import com.truthso.ip360.fragment.UpdateItem;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.NetStatusUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import cz.msebera.android.httpclient.Header;

public class NativeAdapter extends BaseAdapter implements OnCheckedChangeListener ,OnClickListener{
	private Dialog alertDialog;
	private Context context;
	private LayoutInflater inflater;
	private Boolean isAllSelect=false;
	private boolean isChoice=false;
	protected List<DbBean> mDatas;
	private DbBean dbBean;
	private List<Integer> selectedList=new ArrayList<Integer>();
	private Map<String, String> formatMap=new HashMap<String, String>();
	private UpdateItem updateItem;
	private int isOpen=Integer.MAX_VALUE;
	private  int type1;
	private NativeEvidence nativeEvidence;
	public NativeAdapter(NativeEvidence nativeEvidence, List<DbBean> mDatas) {
		super();
		this.context = nativeEvidence.getActivity();
		this.mDatas=mDatas;
		this.nativeEvidence=nativeEvidence;
		inflater=LayoutInflater.from(context);
		formatMap.put("txt", "text/plain");
		formatMap.put("rtf", "application/rtf");
		formatMap.put("doc", "application/msword");
		formatMap.put("xls", "application/vnd.ms-excel");
		formatMap.put("ppt", "application/vnd.ms-powerpoint");
		formatMap.put("htm", "text/html");
		formatMap.put("html", "text/html");
		//formatMap.put("wpd", "text/plain");
		formatMap.put("pdf", "application/pdf");
		//formatMap.put("chm", "text/plain");
		//formatMap.put("pdg", "text/plain");
		//formatMap.put("wdl", "text/plain");
	//	formatMap.put("hlp", "text/plain");
		formatMap.put("wps", "application/vnd.ms-works");
		formatMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		//formatMap.put("docm", "text/plain");
		//formatMap.put("dotm", "text/plain");
		//formatMap.put("dot", "text/plain");
		//formatMap.put("xps", "text/plain");
		//formatMap.put("mht", "text/plain");
		//formatMap.put("mhtml", "text/plain");
		formatMap.put("xml", "text/plain");
		//formatMap.put("odt", "text/plain");
		formatMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		//formatMap.put("xlsm", "text/plain");
		//formatMap.put("xlsb", "text/plain");
		//formatMap.put("xltx", "text/plain");
		//formatMap.put("xltm", "text/plain");
		//formatMap.put("xlt", "text/plain");
		//formatMap.put("csv", "text/plain");
		//formatMap.put("prn", "text/plain");
		//formatMap.put("dif", "text/plain");
		//formatMap.put("slk", "text/plain");
		//formatMap.put("xlam", "text/plain");
		//formatMap.put("ods", "text/plain");
		formatMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
		//formatMap.put("pptm", "text/plain");
		//formatMap.put("potx", "text/plain");
		//formatMap.put("potm", "text/plain");
		//formatMap.put("pot", "text/plain");
		//formatMap.put("thmx", "text/plain");
		//formatMap.put("ppsx", "text/plain");
		//formatMap.put("ppsm", "text/plain");
		formatMap.put("pps", "application/vnd.ms-powerpoint");
		//formatMap.put("ppam", "text/plain");
		//formatMap.put("ppa", "text/plain");
		//formatMap.put("odp", "text/plain");
	
	}
	
	public void setUpdateItem(UpdateItem updateItem){
		this.updateItem=updateItem;
	}
	
	public void setisOpen(int position){
		this.isOpen=position;
	}
	public void addData(List<DbBean> list){
		isOpen=Integer.MAX_VALUE;
		this.mDatas.clear();
		this.mDatas.addAll(list);
		notifyDataSetChanged();
	}
	
	public void setChoice(Boolean isChoice){
		this.isAllSelect=false;
		this.isChoice=isChoice;
	}
	public void setAllSelect(Boolean isAllSelect){
		this.isChoice=true;
		this.isAllSelect=isAllSelect;
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
	public View getView(int position, View convertView , ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){		
			convertView = inflater.inflate(R.layout.item_native_evidence, null);
			vh=new ViewHolder();
			vh.cb_choice= (CheckBox) convertView.findViewById(R.id.cb_choice);
			vh.cb_option= (CheckBox) convertView.findViewById(R.id.cb_option);
			vh.tv_filename=(TextView) convertView.findViewById(R.id.tv_filename);
			vh.tv_filesize=(TextView) convertView.findViewById(R.id.tv_filesize);
			vh.tv_status=(TextView) convertView.findViewById(R.id.tv_status);
			vh.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
			vh.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			
			vh.cb_choice.setOnCheckedChangeListener(this);
			convertView.setTag(vh);
		}else{
			vh=	(ViewHolder) convertView.getTag();		
		}	
		changeState(position, convertView, vh.cb_choice, vh.cb_option);	
		vh.tv_filename.setText(mDatas.get(position).getTitle());
		
		vh.tv_filesize.setText(mDatas.get(position).getFileSize());
			
		vh.tv_date.setText(mDatas.get(position).getCreateTime());
		String str = mDatas.get(position).getStatus();
		if (!CheckUtil.isEmpty(str)) {
			if(str.equals("0")){
				vh.tv_status.setText("正在上传");
			}else if(str.equals("1")){
				vh.tv_status.setText("已上传");
			}else if(str.equals("2")){
				vh.tv_status.setText("已下载");
			}else if(str.equals("3")){
//				vh.tv_status.setText("上传失败");
				vh.tv_status.setText("正在上传");
			}
		}
		String format = mDatas.get(position).getFileFormat();
		if (!CheckUtil.isEmpty(format)) {
			if (CheckUtil.isFormatPhoto(format)) {
				vh.iv_icon.setBackgroundResource(R.drawable.icon_tp);
			}else if (CheckUtil.isFormatVideo(format)) {
				vh.iv_icon.setBackgroundResource(R.drawable.icon_sp);	
			}else if (CheckUtil.isFormatRadio(format)) {
				vh.iv_icon.setBackgroundResource(R.drawable.icon_yp);	
			}else if (CheckUtil.isFormatDoc(format)) {
				vh.iv_icon.setBackgroundResource(R.drawable.icon_bq);	
			}
		}
	
		return convertView;
	}

	public class ViewHolder{
		public CheckBox cb_choice,cb_option;	
		private ImageView iv_icon;
		private TextView tv_filename,tv_date,tv_filesize,tv_status,tv_file_preview;
	}
	
	
	
	private void changeState(final int position, View view, CheckBox cb_choice,
							 final CheckBox cb_option) {
		if(isChoice){
			cb_choice.setVisibility(View.VISIBLE);
			cb_option.setVisibility(View.GONE);
			if(isAllSelect){
				cb_choice.setChecked(true);
			}else{
				cb_choice.setChecked(false);
			}
			cb_choice.setTag(position);
			cb_choice.setOnCheckedChangeListener(this);
		}else{
			cb_choice.setVisibility(View.GONE);
			cb_option.setVisibility(View.VISIBLE);
			cb_option.setChecked(false);
		
			
			final LinearLayout ll_option = (LinearLayout) view.findViewById(R.id.ll_option);
			ll_option.setVisibility(View.GONE);
			if(position==isOpen){
				cb_option.setChecked(true);
				ll_option.setVisibility(View.VISIBLE);
			}			
			TextView tv_delete=(TextView) ll_option.findViewById(R.id.tv_delete);
			TextView tv_preview=(TextView) ll_option.findViewById(R.id.tv_preview);
			TextView tv_file_preview=(TextView) ll_option.findViewById(R.id.tv_file_preview);

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
			RelativeLayout rl_item= (RelativeLayout)view.findViewById(R.id.rl_item);
			rl_item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
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
			tv_delete.setTag(position);
			tv_delete.setOnClickListener(this);
			tv_preview.setTag(position);
			tv_preview.setOnClickListener(this);
			tv_file_preview.setTag(position);
			tv_file_preview.setOnClickListener(this);
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (!CheckUtil.isEmpty(buttonView)){
			int position = (Integer) buttonView.getTag();
			if(isChecked){
				selectedList.add(mDatas.get(position).getId());
			}else{
				selectedList.remove((Integer)mDatas.get(position).getId());
			}
		}


	}

	public List<Integer> getSelected(){
		return selectedList;
	}
	
	
	@Override
	public void onClick( View v) {
		int position=(Integer) v.getTag();
		dbBean = mDatas.get(position);
		switch (v.getId()) {
		case R.id.tv_delete://删除
			showDialog();
			break;
		case R.id.tv_preview://查看证书
				type1=0;
			if(dbBean.getType()==7){
				type1=3;
			}else if(dbBean.getType()==6){
				type1=1;
			}else{
				type1=2;
			}
		getStatus(Integer.parseInt(dbBean.getPkValue()),type1);//是否欠费，是否具有查看证书的权限
//			int expstatus = dbBean.getExpStatus();//扣费状态 0-正常付款；1-欠费

			break;
		case R.id.tv_file_preview://预览文件
			//仅wifi下可预览
			/*boolean isWifi= (Boolean) SharePreferenceUtil.getAttributeByKey(MyApplication.getApplication(), MyConstants.SP_USER_KEY,MyConstants.ISWIFI,SharePreferenceUtil.VALUE_IS_BOOLEAN);
			if(isWifi&&!NetStatusUtil.isWifiValid(MyApplication.getApplication())){
				Toaster.showToast(MyApplication.getApplication(),"仅WIFI网络可查看证书");
				return;
			}*/
			
			if (mDatas.size()>0) {
				
			String url=dbBean.getResourceUrl();
			LogUtils.e(url+"视频 的url路径");
			String format=dbBean.getFileFormat();
			format = format.toLowerCase();//格式变小写
			Log.e("djj",format);
				if(CheckUtil.isFormatVideo(format)){//视频
					Intent intent2 = new Intent(context, VideoDetailActivity.class);
					intent2.putExtra("url", url);
					context.startActivity(intent2);
				}else if(CheckUtil.isFormatPhoto(format)) {//照片
					Intent intent2 = new Intent(context, PhotoDetailActivity.class);
					intent2.putExtra("url", url);
					intent2.putExtra("from","native");//给个标记知道是云端的照片查看，不是本地的
					context.startActivity(intent2);
				}else if (CheckUtil.isFormatRadio(format)) {//音频
					Intent intent2 = new Intent(context, RecordDetailActivity.class);
					intent2.putExtra("url", url);
					//intent2.putExtra("from","cloud");//给个标记知道是云端的照片查看，不是本地的
					context.startActivity(intent2);
				}else if(CheckUtil.isFormatDoc(format)){
					String type=getFileType(format);
					if(type!=null){
						 Intent intent2 = new Intent("android.intent.action.VIEW");
						 intent2.addCategory("android.intent.category.DEFAULT");
						 intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				         Uri uri = Uri.parse(url);
				         intent2.setDataAndType(uri, type);				         
				     	try {
							context.startActivity(intent2);
						} catch (Exception e) {
							e.printStackTrace();
							Toaster.showToast(context, "暂不支持打开此种类型的文件！");
						}					
					}else{
						Toaster.showToast(MyApplication.getApplication(),"手机暂不支持打开此类型文件");
					}
					
				}
			}
			break;
		default:
			break;
		}		
	}

	/**
	 * 是否欠费，能否查看证书
	 */
	private void getStatus(int pkvalue,int type) {
		nativeEvidence. showProgress("正在加载...");
		ApiManager.getInstance().getCertificateInfo(pkvalue, type, new ApiCallback() {

			@Override
			public void onApiResultFailure(int statusCode, Header[] headers,
										   byte[] responseBody, Throwable error) {
				nativeEvidence.hideProgress();
			}
			@Override
			public void onApiResult(int errorCode, String message,
									BaseHttpResponse response) {
				nativeEvidence.hideProgress();
				CertificateInfoBean bean = (CertificateInfoBean) response;
				if (!CheckUtil.isEmpty(bean)) {
					if (bean.getCode() == 200) {
						bean.getDatas().getCertificateUrl();
						String expstatus = bean.getDatas().getArreaStatus();
						if (expstatus.equals("1")){//不欠费 可以查看证书
							Intent intent = new Intent(context,CertificationActivity.class);
							intent.putExtra("pkValue",Integer.parseInt(dbBean.getPkValue()));
							intent.putExtra("type",type1);
							context.startActivity(intent);
						}else if(expstatus.equals("0")){//欠费 不可查看证书
							Toaster.showToast(context,"此文件为欠费文件，不能查看证书，请充值后查看！");
						}

					}else{
						Toaster.showToast(context,bean.getMsg());
					}
				}else{
					Toaster.showToast(context, "加载证书失败");
				}
			}
		});
	}

	private String getFileType(String format){
	if(formatMap.containsKey(format)){
	   return	formatMap.get(format);
	}
		return null;
	}
	
	private void showDialog() {
		alertDialog = new AlertDialog.Builder(context).
        	    setTitle("温馨提示").
        	    setMessage("是否确认删除？").
        	    setIcon(R.drawable.ww).
        	    setPositiveButton("确定", new DialogInterface.OnClickListener() {
        	     @Override
        	     public void onClick(DialogInterface dialog, int which) {
        	    	        	    		
        	    	 SqlDao.getSQLiteOpenHelper().delete(MyConstants.TABLE_MEDIA_DETAIL,dbBean.getId());
        	    	
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
