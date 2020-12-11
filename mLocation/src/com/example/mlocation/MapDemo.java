package com.example.mlocation;

import java.util.Timer;
import java.util.TimerTask;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.mapapp.service.mService;
import com.example.mapapp.utils.DebugUtil;
import com.example.mapapp.utils.Func;
import com.example.mlocation.adapter.UserAdapter;
import com.example.mlocation.been.BaiDuMapByIp;
import com.example.mlocation.been.MapInfo;
import com.example.mlocation.been.UserAndMapInfo;
import com.example.mlocation.impl.LauncherListener;

/**
 * 此demo用来展示定位SDK与地图结合显示位置 author zhh
 */
public class MapDemo extends Activity implements SensorEventListener {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
	private static final int accuracyCircleFillColor = 0xAAFFFF88;
	private static final int accuracyCircleStrokeColor = 0xAA00FF00;
	private SensorManager mSensorManager;
	private Double lastX = 0.0;
	private int mCurrentDirection = 0;
	private double mCurrentLat = 0.0;
	private double mCurrentLon = 0.0;
	private float mCurrentAccracy;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton, userButton;
	boolean isFirstLoc = true; // 是否首次定位
	private MyLocationData locData;

	private Context context;
	private String url = "http://47.98.147.48:8080/map/service/data?type=init";
	public Timer mTimer = new Timer();
	public TimerTask mTask;
	private TextView mTv;
	private ListView listView;
	private UserAdapter adapter;
	public static int width;
	private int height;
	private UserAndMapInfo mUserAndMapInfo;
	boolean isDestroy = false;// 如果不返回或者主页退出，启动广播重启程序

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);

		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		context = this;
		requestLocButton = (Button) findViewById(R.id.button1);
		userButton = (Button) findViewById(R.id.button2);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// 获取传感器管理服务
		requestLocButton.setText("普通");
		mCurrentMode = LocationMode.NORMAL;

		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setText("跟随");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfiguration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					MapStatus.Builder builder = new MapStatus.Builder();
					builder.overlook(0);
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory
							.newMapStatus(builder.build()));
					break;
				case COMPASS:
					requestLocButton.setText("普通");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfiguration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					MapStatus.Builder builder1 = new MapStatus.Builder();
					builder1.overlook(0);
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory
							.newMapStatus(builder1.build()));
					break;
				case FOLLOWING:
					requestLocButton.setText("罗盘");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfiguration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				default:
					break;
				}
			}
		};

		requestLocButton.setOnClickListener(btnClickListener);

		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioButtonListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.defaulticon) {
					// 传入null则，恢复默认图标
					mCurrentMarker = null;
					mBaiduMap
							.setMyLocationConfiguration(new MyLocationConfiguration(
									mCurrentMode, true, null));
				}
				if (checkedId == R.id.customicon) {
					// 修改为自定义marker
					mCurrentMarker = BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher);
					mBaiduMap
							.setMyLocationConfiguration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker,
									accuracyCircleFillColor,
									accuracyCircleStrokeColor));
				}
			}
		};
		group.setOnCheckedChangeListener(radioButtonListener);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);

		mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				// 点击地图某个位置获取经纬度latLng.latitude、latLng.longitude
			}

			@Override
			public boolean onMapPoiClick(MapPoi mapPoi) {
				// 点击地图上的poi图标获取描述信息：mapPoi.getName()，经纬度：mapPoi.getPosition()
				return false;
			}
		});

		listView = (ListView) findViewById(R.id.userlist);
		// listView.getLayoutParams().width = width / 4;
		listView.setOnItemClickListener(userListener);
		mTv = (TextView) findViewById(R.id.textview);
		userUuid = Func.getMac();
		// 开启服务--上传位置信息到服务器
		Intent intentService = new Intent(this, mService.class);
		startService(intentService);
		// 请求用户列表和
		Func.getUserAndMapInfos(context, url, new OnImplInfosListener(),
				Func.postParam(context, "2", "", "", "", "", Func.getMac()));
		// 根据uuid来请求位置信息（查询其他用户位置信息）
		getMapByUuid(Func.getMac());
		/**
		 * 监听跟踪他人按钮
		 */
		userButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listView.setVisibility(View.VISIBLE);
			}
		});
		// 定位初始化
		// mLocClient = new LocationClient(this);
		// mLocClient.registerLocationListener(myListener);
		// LocationClientOption option = new LocationClientOption();
		// option.setOpenGps(true); // 打开gps
		// option.setCoorType("bd09ll"); // 设置坐标类型
		// option.setScanSpan(1000);
		// // option.setNeedDeviceDirect(true);
		// mLocClient.setLocOption(option);
		// mLocClient.start();

	}

	/**
	 * 根据uuid来请求位置信息（查询其他用户位置信息）
	 */
	public void getMapByUuid(final String uuid) {
		// 实时请求和滚动图片广告
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		mTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isUuid) {
					Func.getMapInfos(context, url, new OnImplInfosListener(),
							Func.postParam(context, "3", "", "", "", "", uuid));
				}
			}
		};
		mTimer.schedule(mTask, 1 * 1000, 5 * 1000);
	}

	/**
	 * 根据ip来请求大致位置 &ak=jEWspme9xp7GApsMdCRoBmnLH6MELfln&mcode=14:
	 * 2E:AF:59:AA:70:31:15:7E:DB:BD:52:AF:4A:97:04:A6:7E:F6:F7;com.example.mloc
	 * a t i
	 * &ak=MGCB3cRjSs9xrjDrbtz81VeKxX1qyY3F&mcode=8F:16:20:81:A5:AD:A1:31:F0:
	 * 09:13:C4:42:7F:0A:EE:89:FD:98:B6;com.example.mlocation
	 * 
	 * @param ip
	 */
	String userIp, userUuid;

	public void getMapByIp() {
		if (!isUuid) {
			Log.i("test", "开始通过Ip请求位置信息：" + userIp);
			String getUrl = "http://api.map.baidu.com/location/ip?ip="
					+ userIp
					+ "&ak=jEWspme9xp7GApsMdCRoBmnLH6MELfln&mcode=14:2E:AF:59:AA:70:31:15:7E:DB:BD:52:AF:4A:97:04:A6:7E:F6:F7;com.example.mlocation";
			Func.getBaiDuMapByIpInfos(context, getUrl,
					new OnImplInfosListener());
		}
	}

	/**
	 * 用户列表以及位置监听
	 * 
	 * @author Administrator
	 * 
	 */
	public class OnImplInfosListener extends LauncherListener {
		public OnImplInfosListener() {
			// TODO Auto-generated constructor stub
		}

		/**
		 * 请求所有用户信息和当前uuid所对应的位置信息
		 */
		@Override
		public void onUpdateUserAndMapinfos(final UserAndMapInfo userAndMapInfo) {
			// TODO Auto-generated method stub
			super.onUpdateUserAndMapinfos(userAndMapInfo);
			MapDemo.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (userAndMapInfo != null
							&& userAndMapInfo.userInfo.size() > 0) {
						mUserAndMapInfo = userAndMapInfo;
						adapter = new UserAdapter(context, userAndMapInfo);
						listView.setAdapter(adapter);
					}
				}
			});

		}

		/**
		 * 根据uuid来请求实时位置信息
		 */

		@Override
		public void onUpdateMapInfo(final MapInfo mapInfo) {
			// TODO Auto-generated method stub
			super.onUpdateMapInfo(mapInfo);

			MapDemo.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					if (mapInfo != null && !mapInfo.equals("")
							&& !mapInfo.getLatitude().equals("")
							&& !mapInfo.getLongitude().equals("")) {
						Log.i("text",
								mapInfo.getLatitude() + "--"
										+ mapInfo.getLongitude() + "--"
										+ mapInfo.getAddr());

						mTv.setText(mapInfo.getAddr() + "\n获取时间："
								+ mapInfo.getTime() + "\n本机机器码："
								+ Func.getMac() + "\n跟踪机器码：" + userUuid);

						mCurrentLat = Double.parseDouble(mapInfo.getLatitude());
						mCurrentLon = Double.parseDouble(mapInfo.getLongitude());
						mCurrentAccracy = (float) 1.0;
						setMap(Double.parseDouble(mapInfo.getLatitude()),
								Double.parseDouble(mapInfo.getLongitude()));
					}

				}
			});
		}

		/**
		 * 通过ip获取位置信息
		 */
		@Override
		public void onUpdateBaiDuMapByIp(final BaiDuMapByIp baiDuMapByIp) {
			// TODO Auto-generated method stub
			super.onUpdateBaiDuMapByIp(baiDuMapByIp);

			MapDemo.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					if (baiDuMapByIp != null
							&& !TextUtils.isEmpty(baiDuMapByIp.getAddress())) {
						Log.i("test", "通过ip获取位置：" + baiDuMapByIp.getAddress());

						mTv.setText("通过ip获取位置：" + baiDuMapByIp.getAddress()
								+ "\n本机机器码：" + Func.getMac() + "\n跟踪机器码："
								+ userUuid);

						// mCurrentLat = Double
						// .parseDouble(baiDuMapByIp.content.point.x);
						// mCurrentLon = Double
						// .parseDouble(baiDuMapByIp.content.point.y);
						// setMap(mCurrentLat, mCurrentLon);
					}
				}
			});
		}
	}

	/**
	 * 监听用户列表
	 */
	OnItemClickListener userListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			isFirstLoc = true;
			userIp = mUserAndMapInfo.userInfo.get(position).appIp;
			userUuid = mUserAndMapInfo.userInfo.get(position).uuid;
			AlertDialog(context);
			listView.setVisibility(View.GONE);
		}
	};

	/**
	 * 选择跟踪模式对话框
	 * 
	 * @param context
	 */
	boolean isUuid = true;

	public void AlertDialog(Context context) {
		/* 创建AlertDialog对象并显示 */
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.show();
		/* 添加对话框自定义布局 */
		alertDialog.setContentView(R.layout.dialog_style);
		/* 获取对话框窗口 */
		Window window = alertDialog.getWindow();
		/* 设置显示窗口的宽高 */
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		/* 设置窗口显示位置 */
		// if (i == 1) {
		// window.setGravity(Gravity.BOTTOM);
		// } else {
		window.setGravity(Gravity.CENTER);
		// }
		/* 还可以设置窗口显示动画 */
		// window.setWindowAnimations(R.style.AlertDialog_AppCompat);
		/* 通过window找布局里的控件 */
		window.findViewById(R.id.d_btn_ok).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 隐藏对话框
						isUuid = false;
						getMapByIp();
						alertDialog.dismiss();
					}
				});
		window.findViewById(R.id.d_btn_no).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						isUuid = true;
						getMapByUuid(userUuid);
						// 隐藏对话框
						alertDialog.dismiss();
					}
				});
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		// 每次方向改变，重新给地图设置定位数据，用上一次onReceiveLocation得到的经纬度、精度
		double x = sensorEvent.values[SensorManager.DATA_X];
		if (Math.abs(x - lastX) > 1.0) {// 方向改变大于1度才设置，以免地图上的箭头转动过于频繁
			mCurrentDirection = (int) x;
			locData = new MyLocationData.Builder().accuracy(mCurrentAccracy)
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(mCurrentDirection).latitude(mCurrentLat)
					.longitude(mCurrentLon).build();
			mBaiduMap.setMyLocationData(locData);

		}
		lastX = x;

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			mCurrentLat = location.getLatitude();
			mCurrentLon = location.getLongitude();
			mCurrentAccracy = location.getRadius();
			// setMap(mCurrentLat, mCurrentLon);
		}

	}

	/**
	 * 设置位置信息
	 */
	public void setMap(double Lat, double Lon) {
		locData = new MyLocationData.Builder()
				.accuracy((float) mCurrentAccracy)
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(mCurrentDirection).latitude(Lat).longitude(Lon)
				.build();
		mBaiduMap.setMyLocationData(locData);
		if (isFirstLoc) {
			isFirstLoc = false;
			LatLng ll = new LatLng(Lat, Lon);
			MapStatus.Builder builder = new MapStatus.Builder();
			builder.target(ll).zoom(18.0f);
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory
					.newMapStatus(builder.build()));
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
		// 为系统的方向传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	protected void onStop() {
		// 取消注册传感器监听
		mSensorManager.unregisterListener(this);
		if (!isDestroy) {
			Log.i("test", "onStop--打开1像素");
			DebugUtil.debug("MainActivity:", "onPause--界面程序关闭");
			Intent intent = new Intent("com.example.mapapp.service.destroy");
			sendBroadcast(intent);
			System.exit(0);
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		if (mLocClient != null) {
			mLocClient.unRegisterLocationListener(myListener);
			mLocClient.stop();
		}
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		Log.i("test", "onDestroy----");
		DebugUtil.debug("MainActivity:", "onDestroy---界面程序关闭");
		Intent intent = new Intent("com.example.mapapp.service.destroy");
		sendBroadcast(intent);
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isDestroy = true;
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}