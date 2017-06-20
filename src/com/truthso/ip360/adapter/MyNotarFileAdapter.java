package com.truthso.ip360.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.truthso.ip360.activity.MackCardActivity;
import com.truthso.ip360.activity.MsgCheckActivity;
import com.truthso.ip360.activity.MyNotarFile;
import com.truthso.ip360.activity.NotarFileDetail;
import com.truthso.ip360.activity.NotarPayActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.NotarMsg;
import com.truthso.ip360.bean.NotarMsgBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.dao.UpDownLoadDao;
import com.truthso.ip360.event.CEListRefreshEvent;
import com.truthso.ip360.fragment.UpdateItem;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.updownload.FileInfo;
import com.truthso.ip360.utils.CheckUtil;
import com.truthso.ip360.utils.FileUtil;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @version 1.3
 * @despriction : 我的公证列表的adapter
 * @date 创建时间：2017/6/6 15:58
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class MyNotarFileAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private UpdateItem updateItem;
    protected List<NotarMsg> mDatas;
    private Dialog alertDialog;
    private String notarName;
    private String notarOfficeName;
    private String notaryOfficeAddress;
    private String receiver;//本人领取或自然人领取
    private String notarDate;
    private String notaryNum;
    private String applicantCard;//申请人证件号
    private String requestName;
    private String receiverName;
    private String receiverCard;//领取人证件号
    private String notaryPageNum;//公证书份数
    private String notarStatus;//0审核拒绝1等待提交2等待审核3等待付费4等待制证5已公证
    private String noReason;//审核没有通过的原因
    private String fileSize;
    private String fileMount;
    private String monery;
    private String receiverDate;
    private String pkValue;//此条公证服务的id


    public MyNotarFileAdapter(Context context, List<NotarMsg> mDatas) {
        super();
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }

    public void setUpdateItem(UpdateItem updateItem) {
        this.updateItem = updateItem;
    }

    public void addData(List<NotarMsg> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }

    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
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
        NotarMsg notarMsg = mDatas.get(position);
        notarStatus = notarMsg.getNotarStatus();
        notarName = notarMsg.getNotarName();
        notarOfficeName = notarMsg.getNotarOfficeName();//公证处名称
        notaryOfficeAddress = notarMsg.getNotaryOfficeAddress();//公证处地址
        receiver = notarMsg.getReceiver();//本人领取还是自然人领取
        notarDate = notarMsg.getNotarDate();//公证日期
        notaryNum = notarMsg.getNotaryNum();//公证编号
        applicantCard = notarMsg.getApplicantCard();//申请人证件号
        requestName = notarMsg.getRequestName();//申请人姓名
        receiverName = notarMsg.getReceiverName();//领取者姓名
        receiverCard = notarMsg.getReceiverCard();//领取人证件号
        notaryPageNum = notarMsg.getNotaryPageNum();//公证书份数
        noReason = notarMsg.getNoReason();//审核没通过的原因
        fileSize = notarMsg.getFileSize();//文件总大小
        fileMount = notarMsg.getFileMount();//文件总数量
        monery = notarMsg.getMonery();//钱数
        receiverDate = notarMsg.getReceiverDate();//领取日期
        pkValue = notarMsg.getPkValue();//此条公证服务的id
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_notar_file, null);
            vh = new ViewHolder();
            vh.tv_filename = (TextView) convertView.findViewById(R.id.tv_filename);
            vh.tv_gongzhengchu = (TextView) convertView.findViewById(R.id.tv_gongzhengchu);
            vh.tv_gzbh = (TextView) convertView.findViewById(R.id.tv_gzbh);
            vh.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            vh.tv_sehnqingren = (TextView) convertView.findViewById(R.id.tv_sehnqingren);
            vh.tv_lingquren = (TextView) convertView.findViewById(R.id.tv_lingquren);
            vh.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            vh.iv_chexiao = (ImageView) convertView.findViewById(R.id.iv_chexiao);
            vh.iv_file_detail = (ImageView) convertView.findViewById(R.id.iv_file_detail);
            vh.iv_gongzhengxinxi = (ImageView) convertView.findViewById(R.id.iv_gongzhengxinxi);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.iv_chexiao.setTag(position);
        vh.iv_file_detail.setTag(position);
        vh.iv_gongzhengxinxi.setTag(position);

        vh.tv_filename.setText(notarName);
        vh.tv_gongzhengchu.setText(notarOfficeName);
        vh.tv_gzbh.setText(notaryNum);
        vh.tv_date.setText(notarDate);
        vh.tv_sehnqingren.setText(requestName);
        vh.tv_lingquren.setText(receiverName);

        if (notarStatus.equals("0")) {//0审核拒绝
            vh.tv_status.setText("审核拒绝");
        } else if (notarStatus.equals("1")) {//1等待提交（审核没通过直接跳重新提交页面，信息带过去）
            vh.tv_status.setText("等待提交");
        } else if (notarStatus.equals("2")) {//2等待审核
            vh.tv_status.setText("等待审核");
        } else if (notarStatus.equals("3")) {//3等待付费
            vh.tv_status.setText("等待付费");
        } else if (notarStatus.equals("4")) {//4等待制证
            vh.tv_status.setText("等待制证");
        } else if (notarStatus.equals("5")) {//5已公证
            vh.tv_status.setText("已公证");
        }
        if(notarStatus.equals("5")||notarStatus.equals("4")){
            vh.iv_chexiao.setVisibility(View.GONE);
        }else{
            vh.iv_chexiao.setVisibility(View.VISIBLE);
        }


        vh.iv_chexiao.setOnClickListener(this);
        vh.iv_file_detail.setOnClickListener(this);
        vh.iv_gongzhengxinxi.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chexiao://撤销公证
                int position = (Integer) v.getTag();
                NotarMsg notarnum = mDatas.get(position);
                showDialog(notarnum.getNotaryNum());
                break;
            case R.id.iv_file_detail://公证详情
                int position1 = (Integer) v.getTag();
                NotarMsg notarnum1 = mDatas.get(position1);
                String pkValue1 = notarnum1.getPkValue();
                Intent intent = new Intent(context, NotarFileDetail.class);
                intent.putExtra("pkValue", pkValue1);
                context.startActivity(intent);
                break;
            case R.id.iv_gongzhengxinxi://公证信息(公证到哪一步了 )
                int position2 = (Integer) v.getTag();
                NotarMsg notarnum2 = mDatas.get(position2);
                String status = notarnum2.getNotarStatus();
                String reason = notarnum2.getNoReason();
                String notarName = notarnum2.getNotarName();//申请公证的名称
                String notarOfficeName = notarnum2.getNotarOfficeName();//公证处名称
                String notarOfferAddress = notarnum2.getNotaryOfficeAddress();//公证处地址
                String receiver = notarnum2.getReceiver();//领取人是本人还是自然人
                String requestName = notarnum2.getRequestName();//申请人姓名
                String applicationCard = notarnum2.getApplicantCard();//申请人证件号
                String receiverName = notarnum2.getReceiverName();//领取人名称
                String receiverCard = notarnum2.getReceiverCard();//领取人身份证号
                String fileMount = notarnum2.getFileMount();//申请文件的个数
                String pkValue = notarnum2.getPkValue();//此条公证服务的id

                if (status.equals("0") || status.equals("2")) {//0审核拒绝
                    Intent intent_0 = new Intent(context, MsgCheckActivity.class);
                    intent_0.putExtra("reason", reason);//审核拒绝的原因
                    intent_0.putExtra("status", status);
                    intent_0.putExtra("notarName", notarName);//公证名称
                    intent_0.putExtra("notarOfficeName", notarOfficeName);//公证处名称
                    intent_0.putExtra("notarOfferAddress", notarOfferAddress);//公证处地址
                    intent_0.putExtra("requestName", requestName);//申请人姓名
                    intent_0.putExtra("receiver", receiver);//领取人是本人还是其他自然人
                    intent_0.putExtra("applicationCard", applicationCard);//申请人证件号
                    intent_0.putExtra("receiverName", receiverName);//领取人姓名
                    intent_0.putExtra("receiverCard", receiverCard);//领取人身份证号
                    intent_0.putExtra("fileMount", fileMount);//文件个数
                    intent_0.putExtra("pkValue", pkValue);//此条公证服务的id
                    context.startActivity(intent_0);
                } else if (status.equals("3")) {//3等待付费
                    Intent intent_3 = new Intent(context, NotarPayActivity.class);
                    intent_3.putExtra("notarName", notarName);//公证名称
                    intent_3.putExtra("notarNum", notarnum2.getNotaryNum());//公正编号
                    intent_3.putExtra("notarOfficeName", notarOfficeName);//公证处名称
                    intent_3.putExtra("requestName", requestName);//申请人姓名
                    intent_3.putExtra("receiverName", receiverName);//领取人姓名
                    intent_3.putExtra("fenshu", notarnum2.getNotaryPageNum());//公证书份数
                    intent_3.putExtra("fileMount", fileMount);//文件个数
                    intent_3.putExtra("monery", notarnum2.getMonery());//待支付费用
                    context.startActivity(intent_3);
                } else if (status.equals("4")) {//4等待制证
                    Intent intent_4 = new Intent(context, MackCardActivity.class);
                    intent_4.putExtra("status", "等待制证");
                    context.startActivity(intent_4);
                } else if (status.equals("5")) {//5已公证
                    Intent intent_5 = new Intent(context, MackCardActivity.class);
                    intent_5.putExtra("status", "已公证");
                    intent_5.putExtra("receiverDate", notarnum2.getReceiverDate());//公证书领取时间
                    intent_5.putExtra("notarOfficeName", notarOfficeName);//公证处名称
                    intent_5.putExtra("notaryOfficeAddress", notarnum2.getNotaryOfficeAddress());
                    context.startActivity(intent_5);
                }
                break;

        }
    }

    /**
     * 撤销公证
     */
    private void chexiaoNotar(String notaryNum) {
        ApiManager.getInstance().backoutNotary(notaryNum, new ApiCallback() {
            @Override
            public void onApiResult(int errorCode, String message, BaseHttpResponse response) {
                //撤销后刷新页面
                ((MyNotarFile) context).refreshPage();
            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public class ViewHolder {
        private TextView tv_filename, tv_gongzhengchu, tv_gzbh, tv_date, tv_sehnqingren, tv_lingquren, tv_status;
        private ImageView iv_chexiao, iv_file_detail, iv_gongzhengxinxi;
    }

    public void notifyDataChange(List<NotarMsg> list) {
        if (list != null) {
            this.mDatas = list;
            notifyDataSetChanged();
        }
    }

    private void showDialog(final String notarNum) {
        alertDialog = new AlertDialog.Builder(context).
                setTitle("温馨提示").
                setMessage("撤销公证将删除此条公证申请，是否确认？").
                setIcon(R.drawable.ww).
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //撤销
                        chexiaoNotar(notarNum);
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

}
