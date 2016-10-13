package com.truthso.ip360.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.megvii.livenessdetection.ui.LiveDectActivity;
import com.truthso.ip360.constants.MyConstants;
import com.truthso.ip360.utils.CheckUtil;

/**
 * @despriction :个人中心-> 实名认证
 * 
 * @author wsx_summer Email:wangshaoxia@truthso.com
 * @date 创建时间：2016-7-22下午8:25:08
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */

public class RealNameCertification extends BaseActivity implements OnClickListener {
	private Button btn_ver;
	private ImageView iv_face;
	@Override
	public void initData() {

	}

	@Override
	public void initView() {
		iv_face=(ImageView) findViewById(R.id.iv_face);
		iv_face.setOnClickListener(this);
		btn_ver = (Button) findViewById(R.id.btn_ver);
		btn_ver.setOnClickListener(this);
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
		case R.id.iv_face://采集照片		
			startActivityForResult(new Intent(this, LiveDectActivity.class), 20);
			break;
		case R.id.btn_ver://去认证
			
			//调认证的接口
			
			setResult(MyConstants.REALNAME_VERTIFICATION);
			finish();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==20&&!CheckUtil.isEmpty(data)){
			Bundle result = data.getBundleExtra("result");
			boolean check_pass =result.getBoolean("check_pass");	
			if(check_pass){
				byte[] byteArray = result.getByteArray("pic_result");
				Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);			
				iv_face.setImageBitmap(decodeByteArray);
			}else{
				String mBadReason = result.getString("check_nopass_reason");
				switch (Integer.parseInt(mBadReason)) {
				case 1:
					mBadReason="无人脸";
					break;
				case 2:
					mBadReason="多人脸";
					break;
				case 3:
					mBadReason="动作不符合";
					break;
				case 4:
					mBadReason="图片损毁";
					break;
				case 5:
					mBadReason="代表光线过暗";
					break;
				case 6:
					mBadReason="代表光线过暗";
					break;
				case 8:
					mBadReason="超时";
					break;
				default:
					break;
				}
				Toast.makeText(this, "检测失败/n/n原因:"+mBadReason, 0).show();
			}
		}
	}
	
}
