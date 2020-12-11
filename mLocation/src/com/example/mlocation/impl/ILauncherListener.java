/*
 * Copyright (C) 2012-2016 YunBo(ShenZhen) Co.,Ltd. All right reserved.
 * @version V1.0  
 */
package com.example.mlocation.impl;

import com.example.mlocation.been.BaiDuMapByIp;
import com.example.mlocation.been.MapInfo;
import com.example.mlocation.been.UserAndMapInfo;

public interface ILauncherListener {

	public void onNetConnected();

	public void onNetDisConnected();

	public void onNetSignalLevel(int level);

	public void onUpdateUserAndMapinfos(UserAndMapInfo userAndMapInfo);

	public void onUpdateMapInfo(MapInfo mapInfo);

	public void onUpdateBaiDuMapByIp(BaiDuMapByIp baiDuMapByIp);
}
