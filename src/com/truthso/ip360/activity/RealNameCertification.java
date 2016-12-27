package com.truthso.ip360.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.megvii.livenessdetection.ui.LiveDectActivity;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.net.ApiCallback;
import com.truthso.ip360.net.ApiManager;
import com.truthso.ip360.net.BaseHttpResponse;
import com.truthso.ip360.system.Toaster;
import com.truthso.ip360.utils.CheckUtil;

import cz.msebera.android.httpclient.Header;

/**
 * @despriction :个人中心-> 实名认证
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午8:25:08
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class RealNameCertification extends BaseActivity implements
		OnClickListener {
	private Button btn_ver;
	private ImageView iv_face;
	private EditText et_cardid, et_realname;
	private String cardId, realName;
	private File faceFile;
	private String  picControl;//人像控制版本
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		iv_face = (ImageView) findViewById(R.id.iv_face);
		iv_face.setOnClickListener(this);
		btn_ver = (Button) findViewById(R.id.btn_ver);
		btn_ver.setOnClickListener(this);
		et_cardid = (EditText) findViewById(R.id.et_cardid);
		et_realname = (EditText) findViewById(R.id.et_realname);

	}

	@Override
	public int setLayout() {
		return R.layout.activity_realname_certification;
	}

	@Override
	public String setTitle() {
		return "实名认证";
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_face:// 采集照片
			startActivityForResult(new Intent(this, LiveDectActivity.class), 20);
			 picControl = LiveDectActivity.getVersion();//活体检测的版本号
			break;
		case R.id.btn_ver:// 去认证
			cardId = et_cardid.getText().toString().trim();
			realName = et_realname.getText().toString().trim();
			if (CheckUtil.isEmpty(cardId) || CheckUtil.isEmpty(realName)) {
				Toaster.showToast(RealNameCertification.this, "身份证号或姓名不能为空");
			} else if(!CheckUtil.iscardNum(cardId)){// 身份证正则
					Toaster.showToast(RealNameCertification.this, "请输入正确的身份证号");
			}else if(!faceFile.exists()){
				Toaster.showToast(RealNameCertification.this, "请先采集照片！");
			}else{
				// 调认证的接口
				realNameVertification();
			}

			break;
		default:
			break;
		}

	}

	private void realNameVertification() {
		showProgress("正在认证");
		ApiManager.getInstance().setRealName(picControl,1+"",cardId, realName, faceFile,
				new ApiCallback() {

					@Override
					public void onApiResult(int errorCode, String message,
							BaseHttpResponse response) {
						hideProgress();
						if (!CheckUtil.isEmpty(response)) {
							if (response.getCode() == 200) {
								setResult(MyConstants.REALNAME_VERTIFICATION);
								Toaster.showToast(RealNameCertification.this, response.getMsg());
								finish();
							} else {
								Toaster.showToast(RealNameCertification.this,
										"认证失败");
							}

						} else {
							Toaster.showToast(RealNameCertification.this,
									"认证失败");
						}

					}

					@Override
					public void onApiResultFailure(int statusCode,
							Header[] headers, byte[] responseBody,
							Throwable error) {

					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 20 && !CheckUtil.isEmpty(data)) {
			Bundle result = data.getBundleExtra("result");
			boolean check_pass = result.getBoolean("check_pass");
			if (check_pass) {
				byte[] byteArray = result.getByteArray("pic_result");
				Bitmap decodeByteArray = BitmapFactory.decodeByteArray(
						byteArray, 0, byteArray.length);
				iv_face.setImageBitmap(decodeByteArray);
				String path = MyConstants.PHOTO_PATH;
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				faceFile = new File(dir, "verPhoto.jpg");
				BufferedOutputStream bos;
				try {
					bos = new BufferedOutputStream(new FileOutputStream(
							faceFile));
					decodeByteArray.compress(Bitmap.CompressFormat.JPEG, 100,
							bos);
					bos.flush();
					bos.close();
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}

			} else {
				String mBadReason = result.getString("check_nopass_reason");
				switch (Integer.parseInt(mBadReason)) {
				case 1:
					mBadReason = "无人脸";
					break;
				case 2:
					mBadReason = "多人脸";
					break;
				case 3:
					mBadReason = "动作不符合";
					break;
				case 4:
					mBadReason = "图片损毁";
					break;
				case 5:
					mBadReason = "代表光线过暗";
					break;
				case 6:
					mBadReason = "代表光线过暗";
					break;
				case 8:
					mBadReason = "超时";
					break;
				default:
					break;
				}
				String str= "检测失败/n/n原因:" + mBadReason;
				Toaster.showToast(this,str);

			}
		}
	}

}
