package com.example.mapapp.service;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.mapapp.utils.DebugUtil;
import com.example.mapapp.utils.Func;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

public class mService extends Service {
	public Context context;
	public String url = "http://47.98.147.48:8080/map/service/data?type=init";
	public LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		context = this;

		flags = START_STICKY;
		DebugUtil.debug("mService:", "服务已启动");

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener((BDLocationListener) myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		// option.setServiceName("com.baidu.location.service_v2.9");
		// option.setPoiExtraInfo(true);
		// option.setAddrType("all");
		// option.setPriority(LocationClientOption.NetWorkFirst);
		// option.setPriority(LocationClientOption.GpsFirst); // gps
		// option.setPoiNumber(10);

		/**
		 * 地址，位置描述
		 */
		// 可选，是否需要地址信息，默认为不需要，即参数为false
		// 如果开发者需要获得当前点的地址信息，此处必须为true
		option.setIsNeedAddress(true);
		// 可选，是否需要位置描述信息，默认为不需要，即参数为false
		// 如果开发者需要获得当前点的位置信息，此处必须为true
		option.setIsNeedLocationDescribe(true);

		/**
		 * 经纬度
		 */
		option.disableCache(true);
		// 可选，设置返回经纬度坐标类型，默认GCJ02
		// GCJ02：国测局坐标；
		// BD09ll：百度经纬度坐标；
		// BD09：百度墨卡托坐标；
		// 海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
		option.setCoorType("bd09ll");
		// 可选，设置发起定位请求的间隔，int类型，单位ms
		// 如果设置为0，则代表单次定位，即仅定位一次，默认为0
		// 如果设置非0，需设置1000ms以上才有效
		option.setScanSpan(1000 * 3);
		// 可选，定位SDK内部是一个service，并放到了独立进程。
		// 设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
		option.setIgnoreKillProcess(false);
		// 可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
		option.setEnableSimulateGps(false);

		mLocClient.setLocOption(option);
		mLocClient.start();

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 获取位置监听
	 */
	String time;
	String latitude;
	String longitude;
	String addr, addr2;

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			// Address address = location.getAddress();

			DebugUtil.debug("mService:", location.getAddrStr());
			time = location.getTime();
			latitude = location.getLatitude() + "";
			longitude = location.getLongitude() + "";
			// Log.i("test", "location.getLatitude()==" +
			// location.getLatitude());
			// Log.i("test", "location.getLongitude()==" +
			// location.getLongitude());
			// Log.i("test", "Describe:" + location.getLocationDescribe());

			String buildingID = null;
			String buildingName = null;
			String floor = null;
			if (location.getFloor() != null) {
				// 当前支持高精度室内定位
				buildingID = location.getBuildingID();// 百度内部建筑物ID
				buildingName = location.getBuildingName();// 百度内部建筑物缩写
				floor = location.getFloor();// 室内定位的楼层信息，如 f1,f2,b1,b2
				mLocClient.startIndoorMode();// 开启室内定位模式（重复调用也没问题），开启后，定位SDK会融合各种定位信息（GPS,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
				Log.i("test", "buildingID:" + buildingID + "\nbuildingName:"
						+ buildingName + "\nfloor:" + floor);
			}
			addr = "详细地址：" + location.getAddrStr() + "\n位置描述："
					+ location.getLocationDescribe() + "" + "\n建筑物ID："
					+ buildingID + "\n内部建筑物：" + buildingName + "\n楼层：" + floor;

			// 如果经纬度不为空,并且位置信息发生了改变再上传至服务器
			if (location != null && !TextUtils.isEmpty(location.getAddrStr())
					&& !addr.equals(addr2)) {
				String postStr = Func.postParam(context, "1", time, latitude,
						longitude, addr, Func.getMac());
				Func.getUserAndMapInfos(context, url, null, postStr);
				addr2 = "详细地址：" + location.getAddrStr() + "\n位置描述："
						+ location.getLocationDescribe() + "" + "\n建筑物ID："
						+ buildingID + "\n内部建筑物：" + buildingName + "\n楼层："
						+ floor;
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mLocClient != null) {
			mLocClient.stop();
		}
		Intent sevice = new Intent(this, mService.class);
		this.startService(sevice);
		DebugUtil.debug("mService:", "服务已关闭");
		super.onDestroy();
		// handler.removeCallbacks(runnable);
		// // 停止线程
		// thread.mStop = true;
		// thread.interrupt();

	}
}
