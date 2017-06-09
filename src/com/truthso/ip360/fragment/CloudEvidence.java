package com.truthso.ip360.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.loopj.android.http.RequestHandle;
import com.truthso.ip360.activity.CertificationActivity;
import com.truthso.ip360.activity.CommitMsgActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.activity.SearchCloudEvidenceActivity;
import com.truthso.ip360.activity.SecordLevelActivity;
import com.truthso.ip360.activity.SettingActivity;
import com.truthso.ip360.adapter.CloudEvidenceAdapter;
import com.truthso.ip360.adapter.NativeAdapter;
import com.truthso.ip360.adapter.NativeAdapter.ViewHolder;
import com.truthso.ip360.application.MyApplication;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.CloudEvidenceBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.DownLoadFileBean;
import com.truthso.ip360.bean.GetLinkCountBean;
import com.truthso.ip360.bean.NotarAccountBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.event.CEListRefreshEvent;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.ossupload.DownLoadHelper;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.utils.NetStatusUtil;
import com.truthso.ip360.utils.SharePreferenceUtil;
import com.truthso.ip360.view.MainActionBar;
import com.truthso.ip360.view.RefreshListView;
import com.truthso.ip360.view.RefreshListView.OnRefreshListener;
import com.truthso.ip360.view.RefreshListView.OnloadListener;
import com.truthso.ip360.view.xrefreshview.XRefreshView;
import com.truthso.ip360.view.xrefreshview.XRefreshView.XRefreshViewListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cz.msebera.android.httpclient.Header;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.0
 * @despriction :云端证据
 * @date 创建时间：2016-7-21下午2:49:04
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class CloudEvidence extends BaseFragment implements OnClickListener,
        UpdateItem, OnItemClickListener, OnRefreshListener, OnloadListener {
    private int pagerNumber = 1;
    private MainActionBar actionBar;
    private CloudEvidenceAdapter adapter;
    private RefreshListView listView;
    private int CODE_SEARCH = 101;
    private LayoutInflater inflater;
    private Dialog alertDialog;
    private TextView tv_photo, tv_video, tv_record, tv_pc, tv_allevi;
    private CloudEviItemBean cloudEviItemBean;
    private String keywork;//搜索框里的搜索内容
    private int type, mobileType;//类型，取证类型
    private String searchText;
    private boolean tag = true;
    private EditText et_find_service;
    private PopupWindow cloudWindow;
    private PopupWindow downLoadwindow;
    private View contentView;
    private View popview;
    private Button btn_download, btn_delete, btn_sqgz;
    private CheckBox acition_bar_left;
    private List<CloudEviItemBean> list = new ArrayList<CloudEviItemBean>();
    private List<CloudEviItemBean> datas;
    private boolean isRefresh;
    private int vCode;
    private CloudEvidenceBean bean;
    private int leiBieTag = 1;//1拍照取证，2录像取证 ，3录音取证，4线上取证，5全部文件

    @Override
    protected void initView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vCode = getVersion();
        actionBar = (MainActionBar) view.findViewById(R.id.actionbar_cloudevidence);
        actionBar.setLeftText("全部证据");
        actionBar.setTitle("证据列表");
        actionBar.setRightText("选择");
        actionBar.setActionBarOnClickListener(this);
        acition_bar_left = (CheckBox) actionBar.findViewById(R.id.acition_bar_left);
        listView = (RefreshListView) view.findViewById(R.id.lv_cloudevidence);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnLoad(true);
        listView.setOnRefresh(true);

        View headView = LayoutInflater.from(getActivity()).inflate(
                R.layout.head_cloudevidence, null);
        listView.addHeaderView(headView);
        et_find_service = (EditText) headView.findViewById(R.id.et_find_service);

        if (tag) {
//			进来显示第一个
            type = 4;//全部文件
            mobileType = 0;
            pagerNumber = 1;
            leiBieTag = 5;
            getAllData();
        }

        adapter = new CloudEvidenceAdapter(getActivity(), list, type, mobileType);
        adapter.setUpdateItem(this);
        listView.setAdapter(adapter);
        setSearchMode();

        EventBus.getDefault().register(this);
    }

    private void setSearchMode() {
        // 自动弹出软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_find_service.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_find_service, 0);
            }
        }, 300);
        et_find_service.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CheckUtil.isEmpty(s.toString())) {
                    list.clear();
                    getDatas(null, type, mobileType, 1);

                } else {
                    mHandler.removeMessages(101);
                    if (!CheckUtil.isEmpty(requestHandle)) {
                        requestHandle.cancel(true);
                    }
                    Message msg = new Message();
                    msg.what = 101;
                    msg.obj = s.toString();
                    mHandler.sendMessageDelayed(msg, 500);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            list.clear();
            getDatas((String) (msg.obj), type, mobileType, 1);
        }
    };

    private RequestHandle requestHandle;

    @Override
    public int setViewId() {
        return R.layout.fragment_clouddevidence;
    }

    @Override
    protected void initData() {
        inflater = LayoutInflater.from(getActivity());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acition_bar_right:
                if (list.size() > 0) {
                    choice();
                }
                break;
            case R.id.acition_bar_left:
                if (!CheckUtil.isEmpty(cloudWindow) && cloudWindow.isShowing()) {
                    actionBar.setRightEnable();
                    cloudWindow.dismiss();
                } else {
                    actionBar.setRightGone();
                    showPop();
                }
                break;
            case R.id.btn_download://下载
                downloadAll();
                break;
            case R.id.btn_delete://清除缓存
                deleteAll();
                break;
            case R.id.btn_sqgz://申请公证
                //无网络
                if(!NetStatusUtil.isNetValid(getActivity())){
                    Toaster.showToast(getActivity(),"当前无网络，请稍后重试！");
                    return;
                }
                getPkValue();
                break;
            default:
                break;
        }

    }
    private  volatile  int count,size;
    private StringBuffer pkValueSb;
    private  List<CloudEviItemBean> secordLevelAllItems;
    List<CloudEviItemBean> secordItems;

    /**
     * 申请公证，申请人的账号信息
     */
    private void getPkValue() {
        List<CloudEviItemBean> selected = adapter.getSelected();
        secordLevelAllItems=new ArrayList<>();
        pkValueSb=new StringBuffer();
        count=0;
        size=0;
        if (selected.size() != 0) {
             secordItems = new ArrayList<>();
            for (int i = 0; i < selected.size(); i++) {
                if (selected.get(i).getLinkCount() > 1) {
                    secordItems.add(selected.remove(i));
                } else {
                    pkValueSb.append(selected.get(i).getType()+"-"+selected.get(i).getPkValue()+",");
                }
            }
            count+=selected.size();
            if(secordItems.size()>0){
                for (int i=0;i<secordItems.size();i++){
                    getsecordItemsPkValue(secordItems.get(i));
                }
            }else{
                getLinkCount(pkValueSb.toString());
            }

            adapter.setChoice(false);
            adapter.notifyDataSetChanged();

            if (!CheckUtil.isEmpty(cloudWindow) && cloudWindow.isShowing()) {
                actionBar.setRightEnable();
                cloudWindow.dismiss();
            }
            if (!CheckUtil.isEmpty(downLoadwindow)
                    && downLoadwindow.isShowing()) {
                cancelChoose();
            }
        } else {
            Toaster.showToast(getActivity(), "请选择申请公证的条目");
        }
    }


    public void getsecordItemsPkValue(CloudEviItemBean bean) {
        showProgress("正在加载...");
        ApiManager.getInstance().getSubEvidence(bean.getType(), bean.getPkValue(), 1, 999999, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                size++;
                CloudEvidenceBean bean = (CloudEvidenceBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        secordLevelAllItems.addAll(bean.getDatas()) ;
                      if(size==secordItems.size()){
                          if (secordLevelAllItems.size() != 0) {
                              for (int i=0;i<secordLevelAllItems.size();i++){
                                  pkValueSb.append(secordLevelAllItems.get(i).getType()+"-"+secordLevelAllItems.get(i).getPkValue()+",");
                              }
                              count+=secordLevelAllItems.size();
                              pkValueSb.deleteCharAt(pkValueSb.length()-1);
                              getLinkCount(pkValueSb.toString());
                          }
                      }
                    } else {
                        Toaster.showToast(getActivity(), bean.getMsg());
                    }
                } else {
                    Toaster.showToast(getActivity(), "数据加载失败请刷新重试");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    //获取申请公证的证据数量
    private void getLinkCount(final String pkValue) {
        showProgress("正在加载...");
        ApiManager.getInstance().getLinkCount(pkValue, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                GetLinkCountBean bean=(GetLinkCountBean)response;
                if(bean!=null&&bean.getCode()==200){
                    Log.i("djj","pkValue"+pkValueSb.toString()+":"+count);
                    showGetLinkDialog(bean.getDatas().getShowText(),bean.getDatas().getStatus());
                    if(bean.getDatas().getStatus()!=0){
                        count=Integer.parseInt(bean.getDatas().getFileCount());
                    }
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }

    private void showGetLinkDialog(String showText, final int status) {
        alertDialog = new AlertDialog.Builder(getActivity()).setTitle("温馨提示")
                .setMessage(showText).setIcon(R.drawable.ww)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(status!=0){
                            getAccountMsg();
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        alertDialog.show();
    }

    private void getAccountMsg() {
        showProgress("正在加载...");
        ApiManager.getInstance().getAccountMsg(new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                NotarAccountBean bean = (NotarAccountBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        int iscer = bean.getDatas().getIscertified();//0-未实名 1-已实名
                        if (iscer == 1) {//已实名
//							已经选择的申请公证，要type跟pkvalue
                            //跳转到提交信息页面
                            Intent intent = new Intent(getActivity(), CommitMsgActivity.class);
                            intent.putExtra("pkValue",pkValueSb.toString());
                            intent.putExtra("linkcount",count);//申请公证的数量
                            intent.putExtra("requestName", bean.getDatas().getRequestName());
                            intent.putExtra("requestCardId", bean.getDatas().getRequestCardId());
                            intent.putExtra("requestPhoneNum", bean.getDatas().getRequestPhoneNum());
                            intent.putExtra("requestEmail", bean.getDatas().getRequestEmail());
                            startActivity(intent);
                        } else if (iscer == 0) {//未实名
                            showDialog("是实名认证后才能申请公证，是否立即认证？");
                        }
                    } else {
                        Toaster.showToast(getActivity(), bean.getMsg());
                    }
                } else {
                    Toaster.showToast(getActivity(), "获取数据失败");
                }
            }
            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    /**
     * 删除选择的
     */
    private void deleteAll() {
        List<CloudEviItemBean> selected = adapter.getSelected();
        if (selected.size() != 0) {
            List<CloudEviItemBean> secordItems = new ArrayList<>();
            for (int i = 0; i < selected.size(); i++) {
                if (selected.get(i).getLinkCount() > 1) {
                    secordItems.add(selected.remove(i));
                } else {
                    delete(selected.get(i));
                }
            }
            deleteSecordItem(secordItems);
            adapter.setChoice(false);
            adapter.notifyDataSetChanged();

            if (!CheckUtil.isEmpty(cloudWindow) && cloudWindow.isShowing()) {
                actionBar.setRightEnable();
                cloudWindow.dismiss();
            }
            if (!CheckUtil.isEmpty(downLoadwindow)
                    && downLoadwindow.isShowing()) {
                cancelChoose();
            }
        } else {
            Toaster.showToast(getActivity(), "请选择删除的条目");
        }
    }

    //删除二级页面的条目
    private void deleteSecordItem(List<CloudEviItemBean> secordItems) {
        for (int i = 0; i < secordItems.size(); i++) {
            getSubEvidence(secordItems.get(i), 2);
        }
    }

    private void delete(CloudEviItemBean cloudEviItemBean) {
        String filePath = null;
        //删除本地缓存
        DbBean dbBean = SqlDao.getSQLiteOpenHelper().searchByPkValue(cloudEviItemBean.getPkValue());
        if (!CheckUtil.isEmpty(dbBean)) {
            if (dbBean.getResourceUrl() != null) {
                filePath = dbBean.getResourceUrl();
                try {
                    FileUtil.deleteFile(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        SqlDao.getSQLiteOpenHelper().deleteByPkValue(MyConstants.TABLE_MEDIA_DETAIL, cloudEviItemBean.getPkValue());
        //	UpDownLoadDao.getDao().deleteDownInfoByResourceId(cloudEviItemBean.getPkValue()+"");
        EventBus.getDefault().post(new CEListRefreshEvent());
    }

    /**
     * 下载选择的
     */
    private void downloadAll() {

        if (!NetStatusUtil.isNetValid(getActivity())) {//无网络
            Toaster.showToast(getActivity(), "网络无连接，请连接网络后重试");
            return;
        }
        List<CloudEviItemBean> selected = adapter.getSelected();

        if (selected.size() != 0) {
            List<CloudEviItemBean> secordLevelItems = new ArrayList<>();
            for (int i = 0; i < selected.size(); i++) {
                //有二级页面的先调接口再下载
                if(selected.get(i).getLinkCount() > 0){
                    secordLevelItems.add(selected.remove(i));
                }else{
                    if (isDownloaded(selected.get(i).getPkValue())) {//文件已经下载到本地
                        Toaster.showToast(getActivity(), "文件已经下载到本地");
                        continue;
                    }
                    if (isDownloading(selected.get(i).getPkValue())) {
                        Toaster.showToast(getActivity(), "文件正在下载");
                        continue;
                    }
                    download(selected.get(i));
                }
            }

            downloadSecordLevelItems(secordLevelItems);

            adapter.setChoice(false);
            adapter.notifyDataSetChanged();

            if (!CheckUtil.isEmpty(cloudWindow) && cloudWindow.isShowing()) {
                actionBar.setRightEnable();
                cloudWindow.dismiss();
            }
            if (!CheckUtil.isEmpty(downLoadwindow)
                    && downLoadwindow.isShowing()) {
                cancelChoose();
            }
        } else {
            Toaster.showToast(getActivity(), "没有要下载的文件");
        }
    }

    //下载二级页面中的证据
    private void downloadSecordLevelItems(List<CloudEviItemBean> secordLevelItems) {
        for (int i = 0; i < secordLevelItems.size(); i++) {
            getSubEvidence(secordLevelItems.get(i), 1);
        }
    }

    //检查是否已下载
    private boolean isDownloaded(int pkValue) {
        DbBean dbBean = SqlDao.getSQLiteOpenHelper().searchByPkValue(pkValue);
        if (!CheckUtil.isEmpty(dbBean) && dbBean.getResourceUrl() != null) {
            return FileUtil.IsFileEmpty(dbBean.getResourceUrl());
        }
        return false;
    }

    //检查是否正在下载
    private boolean isDownloading(int pkValue) {
        FileInfo fileInfo = UpDownLoadDao.getDao().queryDownLoadInfoByResourceId(pkValue);

        if (!CheckUtil.isEmpty(fileInfo) && fileInfo.getStatus() != 0) {
            return true;
        }
        return false;
    }


    /**
     * 下载的方法
     *
     * @param data
     */
    private void download(final CloudEviItemBean data) {

        ApiManager.getInstance().downloadFile(data.getPkValue(), data.getType(), data.getDataType(),
                new ApiCallback() {

                    @Override
                    public void onApiResultFailure(int statusCode,
                                                   Header[] headers, byte[] responseBody,
                                                   Throwable error) {
                        Toaster.toast(getActivity(), "获取数据失败", 1);
                    }

                    @Override
                    public void onApiResult(int errorCode, String message,
                                            BaseHttpResponse response) {
                        DownLoadFileBean bean = (DownLoadFileBean) response;
                        if (!CheckUtil.isEmpty(bean)) {
                            if (bean.getCode() == 200) {
                                FileInfo info = new FileInfo();
                                String fileUrl = bean.getDatas().getFileUrl();//阿里云的objectKey
                                String fileUrlformat = fileUrl.replace("/", "-");
                                //因为有文件名相同的情况，把阿里云的objectkey路径当成文件名
                                String nativePath = MyConstants.DOWNLOAD_PATH + "/" + fileUrlformat;
                                info.setFilePath(nativePath);// 在本地的路径
//									info.setFileName(data.getFileTitle());
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
                                info.setResourceId(data.getPkValue());
                                info.setFileFormat(data.getFileFormat());
                                String url = bean.getDatas().getFileUrl();//文件的下载路径
                                info.setObjectKey(url);
                                info.setReMark(data.getRemarkText());
                                // 下载
                                DownLoadHelper.getInstance().downloadFile(
                                        info);
                            } else {
                                Toaster.toast(getActivity(), bean.getMsg(), 1);
                            }
                        } else {
                            Toaster.toast(getActivity(), "获取数据失败", 1);
                        }
                    }
                });
    }

    /**
     * 获取二级链接的数据
     * downloadOrDelete 1-下载 2-删除
     */
    public void getSubEvidence(CloudEviItemBean bean, final int downloadOrDelete) {
        showProgress("正在加载...");
        ApiManager.getInstance().getSubEvidence(bean.getType(), bean.getPkValue(), 1, 999999, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                CloudEvidenceBean bean = (CloudEvidenceBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        List<CloudEviItemBean> secordLevelItems = bean.getDatas();

                        if (secordLevelItems.size() != 0) {
                            if (downloadOrDelete == 1) {
                                for (int i = 0; i < secordLevelItems.size(); i++) {
                                    if (isDownloaded(secordLevelItems.get(i).getPkValue())) {//文件已经下载到本地
                                        Toaster.showToast(getActivity(), "文件已经下载到本地");
                                        continue;
                                    }
                                    if (isDownloading(secordLevelItems.get(i).getPkValue())) {
                                        Toaster.showToast(getActivity(), "文件正在下载");
                                        continue;
                                    }
                                    download(secordLevelItems.get(i));
                                }
                            } else {
                                for (int i = 0; i < secordLevelItems.size(); i++) {
                                    delete(secordLevelItems.get(i));
                                }
                            }
                        }
                    } else {
                        Toaster.showToast(getActivity(), bean.getMsg());
                    }
                } else {
                    Toaster.showToast(getActivity(), "数据加载失败请刷新重试");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    // 显示类别popwindow
    private void showPop() {

        popview = inflater.inflate(R.layout.activity_category_cloudcvidence, null);
        tv_photo = (TextView) popview.findViewById(R.id.tv_photo);

        tv_video = (TextView) popview.findViewById(R.id.tv_video);

        tv_record = (TextView) popview.findViewById(R.id.tv_record);

        tv_pc = (TextView) popview.findViewById(R.id.tv_pc);

        tv_allevi = (TextView) popview.findViewById(R.id.tv_allevi);

        FrameLayout fl_empty = (FrameLayout) popview.findViewById(R.id.fl_empty);
        cloudWindow = new PopupWindow(popview, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景 this.setBackgroundDrawable(dw);
        cloudWindow.setBackgroundDrawable(dw);
        cloudWindow.setTouchable(true);
        cloudWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                acition_bar_left.setChecked(false);
                actionBar.setRightVisible();
            }
        });

        tv_photo.setOnClickListener(new OnClickListener() {//拍照
            @Override
            public void onClick(View arg0) {
                //et_find_service.setText("");
                actionBar.setLeftText("拍照取证");
                leiBieTag = 1;
                if (cloudWindow.isShowing()) {
                    actionBar.setRightEnable();
                    cloudWindow.dismiss();
                }
                adapter.clearData();
                list.clear();
                type = 2;//现场取证
                mobileType = 50001;
                pagerNumber = 1;
                getDatas(keywork, type, mobileType, pagerNumber);
            }
        });
        tv_video.setOnClickListener(new OnClickListener() {//录像
            @Override
            public void onClick(View arg0) {
                //et_find_service.setText("");
                actionBar.setLeftText("录像取证");
                leiBieTag = 2;
                if (cloudWindow.isShowing()) {
                    actionBar.setRightEnable();
                    cloudWindow.dismiss();
                }
                adapter.clearData();
                list.clear();
                tag = false;
                type = 2;//现场取证
                mobileType = 50003;
                pagerNumber = 1;
                getDatas(keywork, type, mobileType, pagerNumber);
            }
        });

        tv_record.setOnClickListener(new OnClickListener() {//录音

            @Override
            public void onClick(View arg0) {
                //et_find_service.setText("");
                actionBar.setLeftText("录音取证");
                leiBieTag = 3;
                if (cloudWindow.isShowing()) {
                    actionBar.setRightEnable();
                    cloudWindow.dismiss();
                }
                adapter.clearData();
                list.clear();
                tag = false;
                type = 2;//现场取证
                mobileType = 50002;
                pagerNumber = 1;
                getDatas(keywork, type, mobileType, pagerNumber);
            }
        });
        tv_pc.setOnClickListener(new OnClickListener() {//线上取证

            @Override
            public void onClick(View arg0) {
                //et_find_service.setText("");
                actionBar.setLeftText("线上取证");
                leiBieTag = 4;
                if (cloudWindow.isShowing()) {
                    actionBar.setRightEnable();
                    cloudWindow.dismiss();
                }
                adapter.clearData();
                list.clear();
                tag = false;
                type = 3;//线上取证
                mobileType = 0;
                pagerNumber = 1;
                getDatas(keywork, type, mobileType, pagerNumber);
            }

        });
        //全部 证据
        tv_allevi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                actionBar.setLeftText("全部证据");
                leiBieTag = 5;
                if (cloudWindow.isShowing()) {
                    actionBar.setRightEnable();
                    cloudWindow.dismiss();
                }
                adapter.clearData();
                list.clear();
                tag = false;
                type = 4;//全部文件
                mobileType = 0;
                pagerNumber = 1;
                getAllData();

            }
        });

        fl_empty.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (cloudWindow.isShowing()) {
                    actionBar.setRightEnable();
                    cloudWindow.dismiss();
                }
            }
        });
        cloudWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        cloudWindow.showAsDropDown(getActivity().findViewById(
                R.id.actionbar_cloudevidence));
    }

    /**
     * 类别中，全部，获取全部类型的云端数据
     */
    private void getAllData() {
        showProgress("正在加载...");
        ApiManager.getInstance().getCloudEvidenceAll(null, pagerNumber, 10, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                //停止刷新
                listView.onRefreshFinished();
                listView.onLoadFinished();
                hideProgress();
                bean = (CloudEvidenceBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        datas = bean.getDatas();
                        if (!CheckUtil.isEmpty(datas)) {
                            actionBar.setRightEnable();
                            actionBar.setRightText("选择");
                            list.addAll(datas);
                        } else {
                            if (list.size() == 0) {
                                actionBar.setRightDisEnable();
                                actionBar.setRightText("");
                            } else {
                                listView.setLoadComplete("没有更多数据了");
                            }
                        }

                        adapter.notifyDataChange(list, type, mobileType);
                    } else {
                        Toaster.showToast(getActivity(), bean.getMsg());
                    }
                } else {
                    Toaster.showToast(getActivity(), "数据加载失败请刷新重试");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    // 点击多选按钮
    private void choice() {
        acition_bar_left.setCompoundDrawables(null, null, null, null);
        actionBar.setLeftText("全选");
        actionBar.setRightText("取消");
        actionBar.setActionBarOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.acition_bar_right:// 取消
                        cancelChoose();
                        break;
                    case R.id.acition_bar_left:// 全选
                        adapter.setAllSelect(true);
                        listView.invalidateViews();
                        break;
                    default:
                        break;
                }
            }
        });
        showDownLoadPop();
        adapter.setChoice(true);
        listView.invalidateViews();
        listView.setOnLoad(false);
        listView.setOnRefresh(false);
    }

    // 取消多选状态
    private void cancelChoose() {
        if (downLoadwindow.isShowing()) {
            downLoadwindow.dismiss();
        }
        adapter.setChoice(false);
        listView.invalidateViews();
        actionBar.setRightText("选择");
        if (leiBieTag == 1) {
            actionBar.setLeftText("拍照取证");
        } else if (leiBieTag == 2) {
            actionBar.setLeftText("录像取证");
        } else if (leiBieTag == 3) {
            actionBar.setLeftText("录音取证");
        } else if (leiBieTag == 4) {
            actionBar.setLeftText("线上取证");
        } else if (leiBieTag == 5) {
            actionBar.setLeftText("全部");
        }

        Drawable dra = getResources().getDrawable(R.drawable.leibie_selector);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        acition_bar_left.setCompoundDrawables(null, null, dra, null);
        actionBar.setActionBarOnClickListener(CloudEvidence.this);
        listView.setOnLoad(true);
        listView.setOnRefresh(true);
    }

    // 显示底部按钮条
    private void showDownLoadPop() {
        if (CheckUtil.isEmpty(downLoadwindow)) {
            contentView = inflater.inflate(R.layout.pop_download, null);
            btn_download = (Button) contentView.findViewById(R.id.btn_download);
            btn_delete = (Button) contentView.findViewById(R.id.btn_delete);
            btn_sqgz = (Button) contentView.findViewById(R.id.btn_sqgz);

            btn_download.setOnClickListener(this);
            btn_sqgz.setOnClickListener(this);
            btn_delete.setOnClickListener(this);
            downLoadwindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 进入退出的动画
        downLoadwindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_SEARCH) {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isRefresh) {
            adapter.setisOpen(Integer.MAX_VALUE);
            adapter.notifyDataSetInvalidated();
        }
    }

    @Subscribe
    public void refreshList(CEListRefreshEvent event) {
        adapter.setisOpen(Integer.MAX_VALUE);
        adapter.notifyDataSetInvalidated();
    }

    @Override
    public void onStop() {
        super.onStop();
        isRefresh = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == event.KEYCODE_BACK) {

            if (cloudWindow != null && cloudWindow.isShowing()) {
                cloudWindow.dismiss();
                return true;
            }
            if (!CheckUtil.isEmpty(downLoadwindow)
                    && downLoadwindow.isShowing()) {
                cancelChoose();
                return true;
            }
            if (listView != null) {
                listView.onRefreshFinished();
                listView.onLoadFinished();
            }
        }
        return false;
    }

    /**
     * 除全部的类型外，其他类型的，云端证据的接口
     */
    private void getDatas(String keywork, final int type, final int mobileType, int pagerNumber) {
        showProgress("正在加载数据...");
        if (requestHandle != null && !requestHandle.isFinished()) {
            requestHandle.cancel(true);
        }
        requestHandle = ApiManager.getInstance().getCloudEvidence(keywork, type, mobileType, pagerNumber, 10, vCode, new ApiCallback() {

            @Override
            public void onApiResult(int errorCode, String message,
                                    BaseHttpResponse response) {
                //停止刷新
                listView.onRefreshFinished();
                listView.onLoadFinished();
                hideProgress();

                bean = (CloudEvidenceBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        datas = bean.getDatas();
                        if (!CheckUtil.isEmpty(datas)) {
                            actionBar.setRightEnable();
                            actionBar.setRightText("选择");
                            list.addAll(datas);
                        } else {
                            if (list.size() == 0) {
                                actionBar.setRightDisEnable();
                                actionBar.setRightText("");
                            } else {
                                listView.setLoadComplete("没有更多数据了");
                            }
                        }

                        adapter.notifyDataChange(list, type, mobileType);
                    } else {
                        Toaster.showToast(getActivity(), bean.getMsg());
                    }
                } else {
                    Toaster.showToast(getActivity(), "数据加载失败请刷新重试");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers,
                                           byte[] responseBody, Throwable error) {

            }
        });
    }

    private int lastPosition;


    @Override//关闭上一条展开的item
    public void update(int position) {
        if (position == adapter.getCount() - 1) {
            listView.setSelection(position);
        }
        if (position != lastPosition) {
            //listview添加headerview后，adapter中getview方法的position=0是第一个条目，而监听方法中position=0是headerview
            int fir = listView.getFirstVisiblePosition() - 2;
            int las = listView.getLastVisiblePosition() - 1;
            CloudEviItemBean cloudEviItemBean = list.get(lastPosition);
            if (lastPosition >= fir && lastPosition <= las) {
                View view = listView.getChildAt(lastPosition - fir);
                if (view != null) {
                    LinearLayout ll_option;
                    if (cloudEviItemBean.getLinkCount() > 1) {
                        ll_option = (LinearLayout) view.findViewById(R.id.ll_option1);
                    } else {
                        ll_option = (LinearLayout) view.findViewById(R.id.ll_option);
                    }
                    //LinearLayout ll_option=	(LinearLayout) view.findViewById(R.id.ll_option);
                    if (ll_option.getVisibility() == View.VISIBLE) {
                        ll_option.setVisibility(View.GONE);
                        CloudEvidenceAdapter.ViewHolder vh = (CloudEvidenceAdapter.ViewHolder) view.getTag();
                        vh.cb_option.setChecked(false);
                    }
                }
            }
        }
        lastPosition = position;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void toOnLoad() {
        lastPosition = 0;
        pagerNumber++;
        getDatas(searchText, type, mobileType, pagerNumber);
    }

    @Override
    public void toRefresh() {
        lastPosition = 0;
        searchText = et_find_service.getText().toString().trim();
        pagerNumber = 1;
        list.clear();
        listView.setLoadStart("查看更多");
        if (leiBieTag == 5) {
            getAllData();
        } else {
            getDatas(searchText, type, mobileType, pagerNumber);
        }

    }

    private int getVersion() {
        try {
            // 通过PackageManager获取安装包信息
            PackageInfo packageInfo = MyApplication.getInstance().getPackageManager().getPackageInfo(
                    MyApplication.getInstance().getPackageName(), 0);

            // 返回版本信息
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void show(String str) {
        alertDialog = new AlertDialog.Builder(getActivity()).setTitle("温馨提示")
                .setMessage(str).setIcon(R.drawable.ww)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳转到实名认证页面
                        startActivity(new Intent(getActivity(), CertificationActivity.class));
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

    private void showDialog(String str) {
        alertDialog = new AlertDialog.Builder(getActivity()).setTitle("温馨提示")
                .setMessage(str).setIcon(R.drawable.ww)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳转到实名认证页面
                        startActivity(new Intent(getActivity(), CertificationActivity.class));
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
