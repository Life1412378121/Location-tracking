/*
 * Copyright (C) 2012-2016 YunBo(ShenZhen) Co.,Ltd. All right reserved.
 * @version V1.0  
 */
package com.example.mlocation.impl;

import com.example.mapapp.utils.Func;
import com.example.mlocation.been.BaiDuMapByIp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class BaiDuMapHandler implements Runnable {

	public Context mContext;

	public String mUrlString;

	public LauncherListener mLauncherListener;

	public BaiDuMapHandler(Context context, String url,
			LauncherListener launcherListener) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mUrlString = url;
		mLauncherListener = launcherListener;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String result = Func.getBackRequest(mContext, mUrlString);
		if (!TextUtils.isEmpty(result)) {
			BaiDuMapByIp baiDuMapByIp = Func.baiDuMapByIp(result);
			if (baiDuMapByIp != null) {
				mLauncherListener.onUpdateBaiDuMapByIp(baiDuMapByIp);
			} else {
				Log.i("test", "BaiDuMap请求数据为空");
			}
		}

	}

}
