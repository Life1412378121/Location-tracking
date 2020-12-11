/*
 * Copyright (C) 2012-2016 YunBo(ShenZhen) Co.,Ltd. All right reserved.
 * @version V1.0  
 */
package com.example.mlocation.impl;

import com.example.mapapp.utils.Func;
import com.example.mlocation.been.MapInfo;
import com.example.mlocation.been.UserAndMapInfo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class MapHandler implements Runnable {

	public Context mContext;

	public String mUrlString, mPostParam;

	public LauncherListener mLauncherListener;

	public MapHandler(Context context, String url,
			LauncherListener launcherListener, String postParam) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mUrlString = url;
		mPostParam = postParam;
		mLauncherListener = launcherListener;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String result = Func.postImplRequest(mContext, mUrlString, mPostParam);
		if (!TextUtils.isEmpty(result)) {
			MapInfo mapInfo = Func.mapInfo(result);
			if (mapInfo != null) {
				mLauncherListener.onUpdateMapInfo(mapInfo);
			} else {
				Log.i("test", "Map请求数据为空");
			}
		}

	}

}
