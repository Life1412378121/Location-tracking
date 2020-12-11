package com.example.mapapp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

/***
 * 日志�?
 * 
 * @author @gmail.com
 */
public class DebugUtil {
	/** 日志保存路径 */
	public static String LOG_SAVE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/download/";
	private static int currentDayLogFileIndex = 0;
	public static final String TAG = "android___debug";
	@SuppressLint("SimpleDateFormat")
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	public static File endfile;

	public enum DebugType {
		log2file, log2console, closeDebugLog
	}

	public static DebugType MODEL_DEBUG = DebugType.log2file;// 0保存成文�? 1控制�? 2

	public void setLogPath(String filePath) {
		LOG_SAVE_PATH = filePath;
	}

	public static void debug(String tag, String msg) {
		if (MODEL_DEBUG == DebugType.log2file) {
			File file = checkLogFileIsExist();
			if (file == null)
				return;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file, true);
				fos.write((new Date().toLocaleString() + "  " + msg)
						.getBytes("utf-8"));
				fos.write("\r\n".getBytes("utf-8"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null)
						fos.close();
				} catch (IOException e) {
				}
				fos = null;
				file = null;
			}
		} else if (MODEL_DEBUG == DebugType.log2console) {
			Log.d(tag, msg);
		} else {

		}
	}

	public static void debug(String msg) {
		debug(TAG, msg);
	}

	/** �?查日志文件是否存�? */
	private static File checkLogFileIsExist() {
		// if(!SystemUtils.isSDCardPresent()){
		// return null;
		// }
		endfile = new File(LOG_SAVE_PATH);
		if (!endfile.exists()) {
			endfile.mkdirs();
		}
		String dateStr = sdf.format(new Date());
		endfile = new File(LOG_SAVE_PATH + dateStr + currentDayLogFileIndex
				+ ".txt");
		if (endfile.exists()) {
			long fileSize = endfile.length();
			long maxLogFileSize = 1024 * 1024;// �?大不能超�?1MB
			if (fileSize > maxLogFileSize) {
				do {
					currentDayLogFileIndex++;
					endfile = new File(LOG_SAVE_PATH + dateStr
							+ currentDayLogFileIndex + ".txt");
				} while (endfile.exists());
				return endfile;
			} else {
				return endfile;
			}
		} else {
			try {
				endfile.createNewFile();
			} catch (IOException e) {
			}
		}
		return endfile;
	}

}
