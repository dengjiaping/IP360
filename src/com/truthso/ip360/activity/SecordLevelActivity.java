package com.truthso.ip360.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.truthso.ip360.adapter.CloudEvidenceAdapter;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.CloudEvidenceBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.bean.DownLoadFileBean;
import com.truthso.ip360.bean.NotarAccountBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.event.CEListRefreshEvent;
import com.truthso.ip360.event.SCEListRefreshEvent;
import com.truthso.ip360.fragment.CloudEvidence;
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
import com.truthso.ip360.utils.NetStatusUtil;
import com.truthso.ip360.view.MainActionBar;
import com.truthso.ip360.view.RefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.3
 * @despriction :证据列表 二级页面下拉条目的公证详情
 * @date 创建时间：2017/6/2 11:49
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class SecordLevelActivity extends BaseActivity implements RefreshListView.OnRefreshListener, UpdateItem, RefreshListView.OnloadListener, View.OnClickListener {
    private CloudEvidenceAdapter adapter;
    private int lastPosition;
    private List<CloudEviItemBean> list = new ArrayList<CloudEviItemBean>();
    private List<CloudEviItemBean> datas;
    private RefreshListView listView;
    private UpdateItem updateItem;
    private int pagerNumber = 1;
    private int type, pkValue;
    private PopupWindow downLoadwindow;
    private View contentView;
    private LayoutInflater inflater;
    private Button btn_download, btn_delete, btn_sqgz ;
    private TextView tv_title,tv_right,tv_left;

    @Override
    public void initData() {
        inflater = LayoutInflater.from(this);
    }

    @Override
    public void initView() {
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_left= (TextView) findViewById(R.id.tv_left);
        tv_right= (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);
        tv_left.setOnClickListener(this);
        tv_title.setText("证据详情");
        tv_right.setText("选择");

        listView = (RefreshListView) findViewById(R.id.lv_cloudevidence);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setOnLoad(true);
        listView.setOnRefresh(true);
        type = getIntent().getIntExtra("type", 0);
        pkValue = getIntent().getIntExtra("pkValue", 0);
        //获取二级页面的数据
        getSubEvidence(1);
        EventBus.getDefault().register(this);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_level_filedatail;
    }

    @Override
    public String setTitle() {
        return null;
    }

    /**
     * 获取二级链接的数据
     */
    public void getSubEvidence(int pagerNumber) {
        showProgress("正在加载...");
        //停止刷新
        listView.onRefreshFinished();
        listView.onLoadFinished();
        ApiManager.getInstance().getSubEvidence(type, pkValue, pagerNumber, 11, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                hideProgress();
                CloudEvidenceBean bean = (CloudEvidenceBean) response;
                if (!CheckUtil.isEmpty(bean)) {
                    if (bean.getCode() == 200) {
                        datas = bean.getDatas();
                        if (!CheckUtil.isEmpty(datas)) {
                            list.addAll(datas);
                            adapter = new CloudEvidenceAdapter(SecordLevelActivity.this, list, type, 0);
                            adapter.setUpdateItem(SecordLevelActivity.this);
                            listView.setAdapter(adapter);
                        } else {
                            if (list.size() == 0) {
//                                actionBar.setRightDisEnable();
//                                actionBar.setRightText("");
                            } else {
                                listView.setLoadComplete("没有更多数据了");
                            }
                        }
                        adapter.notifyDataChange(list, type, 0);
                    } else {
                        Toaster.showToast(SecordLevelActivity.this, bean.getMsg());
                    }
                } else {
                    Toaster.showToast(SecordLevelActivity.this, "数据加载失败请刷新重试");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void toRefresh() {
        lastPosition = 0;
        pagerNumber = 1;
        list.clear();
        listView.setLoadStart("查看更多");
        getSubEvidence(pagerNumber);
    }

    @Override
    public void toOnLoad() {
        lastPosition = 0;
        pagerNumber++;
        getSubEvidence(pagerNumber);
    }

    @Override
    public void update(int position) {
        if (position == adapter.getCount() - 1) {
            listView.setSelection(position);
        }

        if (position != lastPosition) {
            int fir = listView.getFirstVisiblePosition() - 1;
            int las = listView.getLastVisiblePosition() - 1;

            if (lastPosition >= fir && lastPosition <= las) {
                View view = listView.getChildAt(lastPosition - fir);
                if (view != null) {
                    LinearLayout ll_option = (LinearLayout) view.findViewById(R.id.ll_option);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (list.size() > 0) {
                    choice();
                }
                break;
            case R.id.tv_left:
                finish();
                break;

            case R.id.btn_download://下载
                downloadAll();
                break;
            case R.id.btn_delete://清除缓存
                deleteAll();
                break;
            case R.id.btn_sqgz://申请公证
                AccountMsg();
                break;
            default:
                break;
        }
    }

    /**
     * 申请公证，申请人的账号信息
     */
    private void AccountMsg() {
        final List<CloudEviItemBean> selected = adapter.getSelected();

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
                            StringBuffer sb=new StringBuffer();
                            sb.append(selected.get(0).getType()+"-"+ selected.get(0).getPkValue());
                            for (int i=1;i<selected.size();i++){
                                sb.append(","+selected.get(i).getType()+"-"+ selected.get(i).getPkValue());
                            }
//							已经选择的申请公证，要type跟pkvalue
                            //跳转到提交信息页面
                            Intent intent = new Intent(SecordLevelActivity.this, CommitMsgActivity.class);
							intent.putExtra("pkValue",sb.toString());
							intent.putExtra("count",selected.size());//申请公证的数量
                            intent.putExtra("requestName", bean.getDatas().getRequestName());
                            intent.putExtra("requestCardId", bean.getDatas().getRequestCardId());
                            intent.putExtra("requestPhoneNum", bean.getDatas().getRequestPhoneNum());
                            intent.putExtra("requestEmail", bean.getDatas().getRequestEmail());
                            startActivity(intent);

                        } else if (iscer == 0) {//未实名
                            showDialog("是实名认证后才能申请公证，是否立即认证？");
                        }
                    } else {
                        Toaster.showToast(SecordLevelActivity.this, bean.getMsg());
                    }

                } else {
                    Toaster.showToast(SecordLevelActivity.this, "获取数据失败");
                }
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private Dialog alertDialog;

    private void showDialog(String str) {
        alertDialog = new AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage(str).setIcon(R.drawable.ww)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳转到实名认证页面
                        startActivity(new Intent(SecordLevelActivity.this, CertificationActivity.class));
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

    /**
     * 删除选择的
     */
    private void deleteAll() {
        List<CloudEviItemBean> selected = adapter.getSelected();
        if (selected.size() != 0) {
            for (int i = 0; i < selected.size(); i++) {
                delete(selected.get(i));
            }
            adapter.setChoice(false);
            adapter.notifyDataSetChanged();


            if (!CheckUtil.isEmpty(downLoadwindow)
                    && downLoadwindow.isShowing()) {
                cancelChoose();
            }
        } else {
            Toaster.showToast(this, "请选择删除的条目");
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
        EventBus.getDefault().post(new SCEListRefreshEvent());
    }

    @Subscribe
    public void refreshList(CEListRefreshEvent event) {
        adapter.setisOpen(Integer.MAX_VALUE);
        adapter.notifyDataSetInvalidated();
    }

    // 点击多选按钮
    private void choice() {
        tv_left.setBackgroundDrawable(null);
        tv_left.setText("全选");
        tv_right.setText("取消");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelChoose();
            }
        });
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setAllSelect(true);
                listView.invalidateViews();
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
        tv_right.setText("选择");
        tv_left.setText("");
        tv_left.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 0) {
                    choice();
                }
            }
        });
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

    /**
     * 下载选择的
     */
    private void downloadAll() {
        if (!NetStatusUtil.isNetValid(this)) {//无网络
            Toaster.showToast(this, "网络无连接，请连接网络后重试");
            return;
        }
        List<CloudEviItemBean> selected = adapter.getSelected();
        if (selected.size() != 0) {
            for (int i = 0; i < selected.size(); i++) {
                if (isDownloaded(selected.get(i).getPkValue())) {//文件已经下载到本地
                    Toaster.showToast(this, "文件已经下载到本地");
                    continue;
                }
                if (isDownloading(selected.get(i).getPkValue())) {
                    Toaster.showToast(this, "文件正在下载");
                    continue;
                }
                download(selected.get(i));
            }
            adapter.setChoice(false);
            adapter.notifyDataSetChanged();

            if (!CheckUtil.isEmpty(downLoadwindow)
                    && downLoadwindow.isShowing()) {
                cancelChoose();
            }
        } else {
            Toaster.showToast(this, "没有要下载的文件");
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

        ApiManager.getInstance().downloadFile(data.getPkValue(), type, data.getDataType(),
                new ApiCallback() {

                    @Override
                    public void onApiResultFailure(int statusCode,
                                                   Header[] headers, byte[] responseBody,
                                                   Throwable error) {
                        Toaster.toast(SecordLevelActivity.this, "获取数据失败", 1);
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
                                //   info.setMobiletype(mobileType);// 现场取证的类型
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
                                Toaster.toast(SecordLevelActivity.this, bean.getMsg(), 1);
                            }
                        } else {
                            Toaster.toast(SecordLevelActivity.this, "获取数据失败", 1);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
