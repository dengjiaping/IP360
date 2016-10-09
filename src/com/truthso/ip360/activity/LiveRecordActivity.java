package com.truthso.ip360.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.truthso.ip360.adapter.CommonAdapter;
import com.truthso.ip360.bean.CommonBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.config.Log;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.viewholder.ViewHolder;

/**
 * 首页面跳转到的现场录音列表的activity
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-13上午10:48:56
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class LiveRecordActivity extends CommonMediaActivity {
	private Button btnRecord;
	private ListView listView;
	private List<DbBean> mDatas;
 	private CommonAdapter< DbBean> adapter;
 
	public void initData() {
		mDatas = GroupDao.getInstance(this).queryByFileType(MyConstants.RECORD);
		adapter = new CommonAdapter<DbBean>(this,mDatas,R.layout .item_record_list) {

			@Override
			public void convert(ViewHolder helper, DbBean item, int position) {
				item = mDatas.get(position);
				helper.setText(R.id.tv_size, item.getFileSize());
				helper.setText(R.id.tv_total_time, item.getRecordTime());
				helper.setText(R.id.tv_date, item.getCreateTime());
			}
		};
		
		
	}

	@Override
	public void initView() {
		
		
		btnRecord = (Button) findViewById(R.id.btn_takephoto);
		btnRecord.setText("进入录音界面");
		btnRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LiveRecordActivity.this,
						LiveRecordImplementationActivity.class);
				startActivity(intent);
			}
		});
		
		listView = new ListView(this);
		listView.setAdapter(adapter);
		
	}

	@Override
	public View instantiateView(int position) {
		return listView;
	}

}
