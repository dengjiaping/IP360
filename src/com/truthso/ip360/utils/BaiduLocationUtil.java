package com.truthso.ip360.utils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.truthso.ip360.view.xrefreshview.LogUtils;

import android.content.Context;
import android.util.Log;

/**
 * 获取地理位置信息的工具类
 * 
 * @author wsx_summer
 * @date 创建时间：2015年10月19日 下午6:34:57
 * @version 1.0
 * @Copyright (c) 2016 真相网络科技（北京）.Co.Ltd. All rights reserved.
 */
public class BaiduLocationUtil {
	private static String s;
	private static LocationClient mLocationClient;
	private static MyLocationListener mMyLocationListener;
	private static locationListener mlocationListener;

	public static void getLocation(Context context, locationListener mlocationListener) {
		BaiduLocationUtil.mlocationListener = mlocationListener;
		// 百度定位功能
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		initLocation();
		mLocationClient.start();
		// mLocationClient.stop();
		// if(s!= null && s.length() > 0){
		// mLocationClient.stop();
		// }
	}

	private static void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("gcj02");// 可选，默认gcj02，设置返回的定位结果坐标系，
		// option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		// option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		mLocationClient.setLocOption(option);
	}

	private static class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			StringBuffer sb = new StringBuffer(128);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			Log.i("location", location.getLocType() + "");

			switch (location.getLocType()) {
			// case BDLocation.TypeGpsLocation:
			// case BDLocation.TypeNetWorkLocation:
			case BDLocation.TypeOffLineLocation:
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getLocationDescribe());
				// sb.append("\ngetCity : ");
				// sb.append(location.getCity());
				// sb.append("\ngetDistrict : ");
				// sb.append(location.getDistrict());
				break;

			default:
				sb.append("无法获取您的位置信息");

			}
			s = location.getAddrStr() + location.getLocationDescribe();
			mlocationListener.location(s);
			// DebugLog.e("location1",s);
			// System.out.println(location.getAddrStr()+"");

		}

	}

	public interface locationListener {
		void location(String s);
	}
	
	public static void cancelLocation(){
		mLocationClient.stop();
	}
}
