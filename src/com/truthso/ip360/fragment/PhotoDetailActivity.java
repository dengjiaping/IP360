package com.truthso.ip360.fragment;

import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.truthso.ip360.activity.BaseActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.dao.SqlDao;
/**
 * @despriction :照片详情(原图)
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-8-8下午3:00:50
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class PhotoDetailActivity extends BaseActivity {
	private ImageView iv_photo;
	private int id;
	private DbBean dbBean;
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		id = getIntent().getIntExtra("id", 1);
		SqlDao sqlDao = new SqlDao(this);
		dbBean = sqlDao.queryById(id);
		 BitmapUtils bitmap = new BitmapUtils(this);
         bitmap.display(iv_photo, dbBean.getResourceUrl());
	}

	@Override
	public int setLayout() {
		return R.layout.activity_photo_datail;
	}

	@Override
	public String setTitle() {
		return "证据查看";
	}

}
