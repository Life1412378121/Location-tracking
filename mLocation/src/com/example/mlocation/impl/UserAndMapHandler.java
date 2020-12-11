/*
 * Copyright (C) 2012-2016 YunBo(ShenZhen) Co.,Ltd. All right reserved.
 * @version V1.0  
 */
package com.example.mlocation.impl;

import com.example.mapapp.utils.Func;
import com.example.mlocation.been.UserAndMapInfo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class UserAndMapHandler implements Runnable {

	public Context mContext;

	public String mUrlString, mPostParam;

	public LauncherListener mLauncherListener;

	public UserAndMapHandler(Context context, String url,
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
			UserAndMapInfo userAndMapInfo = Func.userAndMapInfo(result);
			if (userAndMapInfo != null && mLauncherListener != null) {
				mLauncherListener.onUpdateUserAndMapinfos(userAndMapInfo);
			} else {
				Log.i("test", "UserAndMap请求数据为空");
			}
		}

	}

}
