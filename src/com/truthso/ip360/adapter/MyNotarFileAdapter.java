package com.truthso.ip360.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.truthso.ip360.activity.NotarFileDetail;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.CloudEviItemBean;
import com.truthso.ip360.bean.DbBean;
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

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction : 我的公证列表的adapter
 *
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2017/6/6 15:58
 * @version 1.3
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
    private int notarStatus;//0审核拒绝1等待提交2等待审核3等待付费4等待制证5已公证
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
    public void setUpdateItem(UpdateItem updateItem){
        this.updateItem=updateItem;
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
            vh.iv_chexiao.setTag(position);
            vh.iv_file_detail = (ImageView) convertView.findViewById(R.id.iv_file_detail);
            vh.iv_file_detail.setTag(position);
            vh.iv_gongzhengxinxi = (ImageView) convertView.findViewById(R.id.iv_gongzhengxinxi);
            vh.iv_gongzhengxinxi.setTag(position);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_filename.setText(notarName);
        vh.tv_gongzhengchu.setText(notarOfficeName);
        vh.tv_gzbh.setText(notaryNum);
        vh.tv_date.setText(notarDate);
        vh.tv_sehnqingren.setText(requestName);
        vh.tv_lingquren.setText(receiverName);

        if(notarStatus==0){//0审核拒绝
            vh.tv_status.setText("审核拒绝");
        }else if(notarStatus==1){//1等待提交（审核没通过直接跳重新提交页面，信息带过去）
            vh.tv_status.setText("等待提交");
        }else if(notarStatus==2){//2等待审核
            vh.tv_status.setText("等待审核");
        }else if(notarStatus==3){//3等待付费
            vh.tv_status.setText("等待付费");
        }else if(notarStatus==4){//4等待制证
            vh.tv_status.setText("等待制证");
        }else if(notarStatus==5){//5已公证
            vh.tv_status.setText("已公证");
        }

        vh.iv_chexiao.setOnClickListener(this);
        vh.iv_file_detail.setOnClickListener(this);
        vh.iv_gongzhengxinxi.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_chexiao://撤销公证
               int position = (Integer) v.getTag();
               NotarMsg  notarnum =  mDatas.get(position);
                showDialog(notarnum.getNotaryNum());
                break;
            case R.id.iv_file_detail://公证详情
                int position1 = (Integer) v.getTag();
                NotarMsg  notarnum1 =  mDatas.get(position1);
                Intent intent = new Intent(context,NotarFileDetail.class);
                intent.putExtra("pkValue",notarnum1.getPkValue());
                context.startActivity(intent);
                break;
            case R.id.iv_gongzhengxinxi://公证信息(公证到哪一步了 )
                int position2 = (Integer) v.getTag();
                NotarMsg  notarnum2 =  mDatas.get(position2);
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

            }

            @Override
            public void onApiResultFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public class ViewHolder {
        private TextView tv_filename,tv_gongzhengchu,tv_gzbh,tv_date,tv_sehnqingren,tv_lingquren,tv_status;
        private ImageView iv_chexiao,iv_file_detail,iv_gongzhengxinxi;
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
