package com.truthso.ip360.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.truthso.ip360.adapter.CommonAdapter;
import com.truthso.ip360.bean.CommonBean;
import com.truthso.ip360.bean.DbBean;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.dao.GroupDao;
import com.truthso.ip360.dao.SqlDao;
import com.truthso.ip360.utils.FileSizeUtil;
import com.truthso.ip360.utils.MyVideoThumbLoader;
import com.truthso.ip360.viewholder.ViewHolder;

/**
 * 
 * 录像页面的activity
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-6-10下午2:50:05
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class VideoActivity extends CommonMediaActivity {
	protected static final int VIDEO = 1;
	private ListView listView;
	private CommonAdapter<DbBean> adapter;
	private List<DbBean> mDatas;
	private Button btnVideo;
	private Bitmap bitMap;
	private ImageView imageView ;
	 private String filePath = "";
	private MyVideoThumbLoader mVideoThumbLoader;
	

	@Override
	public void initView() {
		listView = new ListView(this);
		listView.setAdapter(adapter);
		btnVideo = (Button) findViewById(R.id.btn_takephoto);
		btnVideo.setText("按下录像");
		mVideoThumbLoader = new MyVideoThumbLoader();
		btnVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivityForResult(intent, VIDEO);
			}
		});
	}
	@Override
	public void initData() {
		mDatas = GroupDao.getInstance(this).queryByFileType(MyConstants.VIDEO);
		adapter = new CommonAdapter<DbBean>(this, mDatas,
				R.layout.item_photos_video_list) {

			@Override
			public void convert(ViewHolder helper, DbBean item, int position) {
				item = mDatas.get(position);
				String path=item.getResourceUrl();
				helper.setText(R.id.tv_filename, item.getTitle());
				helper.setText(R.id.tv_time, item.getCreateTime());
				imageView=	(ImageView)helper.getView(R.id.iv_image);
				imageView.setTag(path);
				mVideoThumbLoader .showThumbByAsynctack(path, imageView);
			}
		};
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 long duration=0;
		if (requestCode == VIDEO && resultCode == Activity.RESULT_OK
				&& null != data) {
			Uri uri = data.getData();
		
			if (uri == null) {
				return;
			} else {
				Cursor c = this.getContentResolver().query(uri,
						new String[] { MediaStore.MediaColumns.DATA }, null,
						null, null);
				if (c != null && c.moveToFirst()) {
					filePath = c.getString(0);
				}
		    duration=c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
			}
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy年MM月dd日    HH:mm:ss     ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String date = formatter.format(curDate);
			String name = filePath.substring(filePath.lastIndexOf("/"));
			String fileSize = FileSizeUtil.getAutoFileOrFilesSize(filePath);
			saveData(date, fileSize, name, filePath,duration+"");

		}
		mDatas.clear();
		mDatas.addAll(GroupDao.getInstance(this).queryByFileType(MyConstants.VIDEO));
		adapter.notifyDataSetChanged();
	}

	@Override
	public View instantiateView(int position) {
		return listView;
	}

	/**
	 * 将数据保存到数据库
	 */
	private void saveData(String date, String fileSize, String name, String path,String recordTime) {
		SqlDao sqlDao = SqlDao.getSQLiteOpenHelper();
		DbBean dbBean = new DbBean();
		dbBean.setType(MyConstants.VIDEO);// 文件类别
		dbBean.setCreateTime(date);// 生成时间
		dbBean.setFileSize(fileSize);// 文件大小
		dbBean.setResourceUrl(path);
		dbBean.setTitle(name);// 名称
		dbBean.setRecordTime(recordTime);
		sqlDao.save(dbBean, "IP360_media_detail");// 存入数据库
	}



}
