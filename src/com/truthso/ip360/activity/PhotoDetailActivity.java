package com.truthso.ip360.activity;

import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.truthso.ip360.activity.BaseActivity;
import com.truthso.ip360.activity.R;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.utils.ImageLoaderUtil;

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
	private String url,from;
	@Override
	public void initData() {

		url=getIntent().getStringExtra("url");
		from=getIntent().getStringExtra("from");

	}

	@Override
	public void initView() {
		 iv_photo = (ImageView) findViewById(R.id.iv_photo);
		/* BitmapUtils bitmap = new BitmapUtils(this);
         bitmap.display(iv_photo, url);*/
        if(from.equals("cloud")){
			ImageLoaderUtil.dispalyImage(url,iv_photo);
		}else {
			ImageLoaderUtil.displayFromSDCardopt(url,iv_photo,null);
		}

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
