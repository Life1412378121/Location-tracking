package com.example.mapapp.receiver;

import com.example.mapapp.utils.DebugUtil;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class mBroadcastReceiver extends BroadcastReceiver {
	// private Intent intentService;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		DebugUtil.debug("mBroadcastReceiver:", "已进入广播");
		/**
		 * 自定义广播，服务关闭时
		 */
		if (intent.getAction().equals("com.example.mapapp.service.destroy")) {

			DebugUtil.debug("mBroadcastReceiver:", "自定义广播，服务关闭重启程序11");
			// intentService = new Intent(context, mService.class);
			// context.startService(intentService);
			appIntent(context);
		}
		/**
		 * 手机开机
		 */
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			DebugUtil.debug("mBroadcastReceiver:", "开机启动程序");
			appIntent(context);
			// if (!mUtils.isServiceRunning(context, mService.class)) {
			// intentService = new Intent(context, mService.class);
			// context.startService(intentService);
			// }
		}

		/**
		 * 按电源键
		 */
		if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			DebugUtil.debug("mBroadcastReceiver:", "唤醒屏幕启动程序");
			appIntent(context);
			// if (!mUtils.isServiceRunning(context, mService.class)) {
			// intentService = new Intent(context, mService.class);
			// context.startService(intentService);
			// }
		}
		/**
		 * 开锁
		 */
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			DebugUtil.debug("mBroadcastReceiver:", "正在解锁");
			appIntent(context);
		}
		/**
		 * 锁屏
		 */
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			DebugUtil.debug("mBroadcastReceiver:", "正在锁屏");
		}
		/**
		 * SD挂载
		 */
		if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
			DebugUtil.debug("mBroadcastReceiver:", "SD挂载");
			appIntent(context);
		}
		/**
		 * SD
		 */
		if (intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
			DebugUtil.debug("mBroadcastReceiver:", "SD不挂载");
		}
	}

	public void appIntent(Context context) {
		try {
			Intent intent = context.getPackageManager()
					.getLaunchIntentForPackage("com.example.mapapp");
			if (intent != null) {
				intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
			DebugUtil.debug("mBroadcastReceiver:", "广播打开程序");
		} catch (ActivityNotFoundException e) {
			DebugUtil.debug("mBroadcastReceiver:", "广播打开程序失败");
		}
	}

}
