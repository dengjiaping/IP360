package com.truthso.ip360.service;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class GpsService {
	private Context context;
	private static final GpsService instance = new GpsService();

	private LocationClient mLocationClient;

	private static int error_times = 0;
	/**
	 * 定位模式
	 * 
	 * @param LocationMode
	 *            .Hight_Accuracy 高精度定位模式下，会同时使用GPS、Wifi和基站定位，返回的是当前条件下精度最好的定位结果
	 * @param LocationMode
	 *            .Battery_Saving
	 *            低功耗定位模式下，仅使用网络定位即Wifi和基站定位，返回的是当前条件下精度最好的网络定位结果
	 * @param LocationMode
	 *            .Device_Sensors
	 *            仅用设备定位模式下，只使用用户的GPS进行定位。这个模式下，由于GPS芯片锁定需要时间，首次定位速度会需要一定的时间
	 * 
	 */
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	/**
	 * 坐标系
	 * 
	 * @param gcj02
	 *            国测局加密经纬度坐标
	 * @param bd09ll
	 *            百度加密经纬度坐标 (默认)
	 * @param bd09
	 *            百度加密墨卡托坐标
	 */
	private String tempcoor = "bd09ll";
	public MyLocationListener mMyLocationListener;
	/** 发起定位请求的间隔时间(ms) */
	private int interval = 90000;

	private GpsService() {
	}

	public static GpsService getInstance() {
		return instance;
	}

	public void init(Context context) {
		this.context = context;
	}

	/**
	 * 开启GPS定位
	 */
	public void startReceivingLocationUpdates() {
		try {
			interval = 30* 1000;
		} catch (Exception e) {
			e.printStackTrace();
			interval = 30000;
		}
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		InitLocation();
		mLocationClient.start();

	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			// 61 ： GPS定位结果
			// 62 ： 扫描整合定位依据失败。此时定位结果无效。
			// 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
			// 65 ： 定位缓存的结果。
			// 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
			// 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
			// 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
			// 161： 表示网络定位结果
			// 162~167： 服务端定位失败
			// 502：key参数错误
			// 505：key不存在或者非法
			// 601：key服务被开发者自己禁用
			// 602：key mcode不匹配
			// 501～700：key验证失败
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			Log.i("BaiduLocation", sb.toString());
			int code[] = { 61, 65, 66, 68, 161 };
			int i = 0;
			for (i = 0; i < code.length; i++) {
				if (location.getLocType() == code[i]) {
					// Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG)
					// .show();
					saveLocation(location, false);
					// updateCaseGis(location);
					error_times = 0;
					break;
				}
			}
			// 错误
			if (i == code.length) {
				error_times++;
				if (error_times > 5) {
					location.setLatitude(0.0);
					location.setLongitude(0.0);
					saveLocation(location, true);
					error_times = 0;
				}

			}

		}

	}

	/**
	 * 设置定位的一些参数
	 */
	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 否使用GPS定位
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(interval);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 是否为反地理编码,为true可以返回位置信息
		mLocationClient.setLocOption(option);
	}

	/**
	 * 将当前的位置信息保存到本地
	 * 
	 * @param location
	 */
	private void saveLocation(BDLocation location, boolean bError) {
		if (location != null) {
			if (!bError) {
				double lat = location.getLatitude();// 当前纬度
				double lng = location.getLongitude();// 当前经度
				Log.i("djj", location.getAddrStr());
			}
		}
	}

	/**
	 * 停止获取位置
	 */
	public void stopReceivingLocationUpdates() {
		if (mLocationClient != null) {
			mLocationClient.stop();
			mLocationClient = null;
		}
	}

}
