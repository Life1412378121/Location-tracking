package com.example.mlocation;

import com.example.mapapp.utils.DebugUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity extends Activity {
	boolean is;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Log.i("test", "onCreate");
		Log.i("test", "bbbbb");
		// Intent intent = new Intent(Intent.ACTION_MAIN);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.addCategory(Intent.CATEGORY_HOME);
		// startActivity(intent);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.i("test", "onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i("test", "onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i("test", "onPause");
		// appIntent(MainActivity.this);
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i("test", "onStop");
		// Intent intent = new Intent("com.example.mapapp.service.destroy");
		// sendBroadcast(intent);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("test", "onDestroy");
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

	@SuppressLint("InlinedApi")
	public void appIntent(Context context) {
		try {
			Intent intent = context.getPackageManager()
					.getLaunchIntentForPackage("com.example.mapapp");
			if (intent != null) {
				intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			} else {
				// 开启服务--上传位置信息到服务器
				// Intent intentService = new Intent(context, mService.class);
				// context.startService(intentService);
			}

			DebugUtil.debug("mBroadcastReceiver:", "广播打开程序");
		} catch (ActivityNotFoundException e) {
			DebugUtil.debug("mBroadcastReceiver:", "广播打开程序失败");
		}
	}
}
