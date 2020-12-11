/*
 * add by wu 20180511 全局提示对话狂
 */
package com.example.mapapp.utils;

import com.example.mlocation.MapDemo;
import com.example.mlocation.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class BaseDialog {

	public static void Dialog(Context context, String dStr,
			final boolean isCancelable) {
		View view = View.inflate(context, R.layout.dialog_style, null);
		AlertDialog.Builder b = new AlertDialog.Builder(context);
		b.setView(view);
		final AlertDialog d = b.create();
		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		// //窗口可以获得焦点，响应操作
		// d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
		// //窗口不可以获得焦点，点击时响应窗口后面的界面点击事件
		// d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
		// // 系统中关机对话框就是这个属性
		d.setCancelable(false);
		d.show();
		TextView str = (TextView) view.findViewById(R.id.d_tv_str);
		str.setText(dStr);
		Button yesButton = (Button) view.findViewById(R.id.d_btn_ok);
		Button noButton = (Button) view.findViewById(R.id.d_btn_no);
		yesButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				d.dismiss();
				if (isCancelable) {
					// System.exit(0);
				}
			}
		});
		noButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				d.dismiss();
				if (isCancelable) {
					// System.exit(0);
				}
			}
		});

		Window window = d.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		// 设置透明度为0.8
		lp.alpha = 0.6f;
		lp.width = MapDemo.width / 2;
		window.setAttributes(lp);

	}

	public static void AlertDialog(Context context) {
		/* 创建AlertDialog对象并显示 */
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.show();
		/* 添加对话框自定义布局 */
		alertDialog.setContentView(R.layout.dialog_style);
		/* 获取对话框窗口 */
		Window window = alertDialog.getWindow();
		/* 设置显示窗口的宽高 */
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		/* 设置窗口显示位置 */
		// if (i == 1) {
		// window.setGravity(Gravity.BOTTOM);
		// } else {
		window.setGravity(Gravity.CENTER);
		// }
		/* 还可以设置窗口显示动画 */
		// window.setWindowAnimations(R.style.AlertDialog_AppCompat);
		/* 通过window找布局里的控件 */
		window.findViewById(R.id.d_btn_ok).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 隐藏对话框
						alertDialog.dismiss();
					}
				});
		window.findViewById(R.id.d_btn_no).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				});
	}
}
