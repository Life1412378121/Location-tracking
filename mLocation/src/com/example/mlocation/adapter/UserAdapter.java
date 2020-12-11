package com.example.mlocation.adapter;

import com.example.mlocation.R;
import com.example.mlocation.been.UserAndMapInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * GridView适配器
 * 
 * @author zihao
 * 
 */
public class UserAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	UserAndMapInfo userAndMapInfo;

	public UserAdapter(Context context, UserAndMapInfo mUserAndMapInfo) {
		inflater = LayoutInflater.from(context);
		this.userAndMapInfo = mUserAndMapInfo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userAndMapInfo.userInfo.size() > 0 ? userAndMapInfo.userInfo
				.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userAndMapInfo.userInfo.size() > 0 ? userAndMapInfo.userInfo
				.get(position) : "";
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_user_list, null);
			holder = new ViewHolder();
			holder.titleName = (TextView) convertView
					.findViewById(R.id.list_tv_tvtype);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		holder.titleName.setText(userAndMapInfo.userInfo.get(position).appIp);

		return convertView;
	}

	/**
	 * 工具类
	 */
	private class ViewHolder {
		TextView titleName = null;
	}

}
