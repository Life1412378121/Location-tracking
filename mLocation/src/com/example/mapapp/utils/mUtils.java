package com.example.mapapp.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class mUtils {
	/**
	 * 判断Service是否正在打开
	 * 
	 * @param context
	 * @param serviceClass
	 * @return
	 */
	public static boolean isServiceRunning(Context context,
			Class<?> serviceClass) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (serviceList == null || serviceList.size() == 0)
			return false;
		for (RunningServiceInfo info : serviceList) {
			if (info.service.getClassName().equals(serviceClass.getName()))
				return true;
		}
		return false;
	}
}
