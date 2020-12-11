/*
 * Copyright (C) 2012-2016 YunBo(ShenZhen) Co.,Ltd. All right reserved.
 * @version V1.0  
 */
package com.example.mlocation.impl;

import com.example.mlocation.been.BaiDuMapByIp;
import com.example.mlocation.been.MapInfo;
import com.example.mlocation.been.UserAndMapInfo;

public abstract class LauncherListener implements ILauncherListener {

	@Override
	public void onNetConnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetDisConnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetSignalLevel(int level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateUserAndMapinfos(UserAndMapInfo userAndMapInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateMapInfo(MapInfo mapInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpdateBaiDuMapByIp(BaiDuMapByIp baiDuMapByIp) {
		// TODO Auto-generated method stub

	}
}
