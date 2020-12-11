/*
 * Copyright (C) 2012-2016 YunBo(ShenZhen) Co.,Ltd. All right reserved.
 * @version V1.0  
 */
package com.example.mlocation.been;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class UserAndMapInfo implements Parcelable {

	public String msg;
	public String code;

	public List<UserInfo> userInfo = new ArrayList<UserInfo>();

	public List<MapInfo> userMapInfo = new ArrayList<MapInfo>();

	public static class UserInfo implements Parcelable {
		public String appIp;
		public String userName;
		public String uuid;

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel out, int arg1) {
			// TODO Auto-generated method stub
			out.writeString(appIp);
			out.writeString(userName);
			out.writeString(uuid);
		}

		public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
			public UserInfo createFromParcel(Parcel in) {
				return new UserInfo(in);
			}

			public UserInfo[] newArray(int size) {
				return new UserInfo[size];
			}
		};

		private UserInfo(Parcel in) {
			appIp = in.readString();
			userName = in.readString();
			uuid = in.readString();
		}

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		// TODO Auto-generated method stub
		out.writeString(msg);
		out.writeString(code);
		out.writeTypedList(userInfo);
		// out.writeTypedList(zhiBoTypes);

	}

	public static final Parcelable.Creator<UserAndMapInfo> CREATOR = new Parcelable.Creator<UserAndMapInfo>() {
		public UserAndMapInfo createFromParcel(Parcel in) {
			return new UserAndMapInfo(in);
		}

		public UserAndMapInfo[] newArray(int size) {
			return new UserAndMapInfo[size];
		}
	};

	private UserAndMapInfo(Parcel in) {
		msg = in.readString();
		code = in.readString();
		in.readTypedList(userInfo, UserInfo.CREATOR);
		// in.readTypedList(zhiBoTypes, ZhiBoTypes.CREATOR);
	}
}
