package com.example.mapapp.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

public class Func {
	/**
	 * ------------------------------------------------------------------------
	 * --------------------------------数据请求处理--------------------------------
	 * ------------------------------------------------------------------------
	 */
	private static final BlockingQueue<Runnable> POOL_QUEUE_TASK = new SynchronousQueue<Runnable>();
	private static final ThreadFactory TASK_FACTORY = new ThreadFactory() {
		private final AtomicInteger COUNT = new AtomicInteger(1);

		public Thread newThread(Runnable runnable) {
			int count = COUNT.getAndIncrement();
			return new Thread(runnable, "Func #" + count);
		}
	};
	public static final ExecutorService FUNC_TASK = new ThreadPoolExecutor(0,
			Integer.MAX_VALUE, 3L, TimeUnit.SECONDS, POOL_QUEUE_TASK,
			TASK_FACTORY);

	/**
	 * get请求
	 * 
	 * @param context
	 * @param urlString
	 * @return
	 */
	public static String getBackRequest(Context context, String urlString) {

		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// 设置请求的模式
			connection.setRequestMethod("GET");
			// 设置请求连接超时时间
			connection.setConnectTimeout(5000);
			// 设置访问时的超时时间
			connection.setReadTimeout(5000);
			// 开启连接
			connection.connect();
			InputStream inputStream = null;
			BufferedReader reader = null;
			// 如果应答码为200的时候，表示成功的请求带了，这里的HttpURLConnection.HTTP_OK就是200
			String result = null;
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 获得连接的输入流
				inputStream = connection.getInputStream();
				// 转换成一个加强型的buffered流
				reader = new BufferedReader(new InputStreamReader(inputStream));
				// 把读到的内容赋值给result
				result = reader.readLine();
			}
			reader.close();
			inputStream.close();
			connection.disconnect();

			Log.i("test", "get请求：" + result);
			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * post请求
	 * 
	 * @param context
	 * @param urlString
	 * @param postParam
	 * @return
	 */
	public static String postImplRequest(Context context, String urlString,
			String postParam) {
		try {
			HttpURLConnection hc = getHttpURLConnection(urlString);
			if (hc == null) {
				return null;
			}
			hc.setDoOutput(true);
			hc.setDoInput(true);
			hc.setUseCaches(false);
			hc.setRequestMethod("POST");
			hc.setRequestProperty("Content-Type", "application/octet-stream");

			byte[] bits = postParam.getBytes("UTF-8");
			if (bits != null) {
				hc.setRequestProperty("Content-Length", bits.length + "");
			}
			OutputStream out = hc.getOutputStream();
			if (bits != null) {
				out.write(bits);
				out.flush();
			}
			InputStream input = hc.getInputStream();
			ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int k = 0;
			while ((k = input.read(bytes)) != -1) {
				byteArr.write(bytes, 0, k);
			}
			byteArr.flush();
			byte[] returnDatas = byteArr.toByteArray();
			byteArr.close();
			input.close();
			out.close();
			String result = new String(returnDatas, "UTF-8");

			Log.i("test", "post请求：" + result);
			return result;
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static HttpURLConnection getHttpURLConnection(String urlString) {
		if (TextUtils.isEmpty(urlString)) {
			return null;
		}
		try {
			URL url = new URL(urlString);
			HttpURLConnection httpURLConnection = null;
			if (TextUtils.isEmpty(android.net.Proxy.getDefaultHost())) {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			} else {
				if (urlString.startsWith("http://127.0.0.1")) {
					Log.e("getHttpURLConnection", "http://127.0.0.1");
					httpURLConnection = (HttpURLConnection) url
							.openConnection(java.net.Proxy.NO_PROXY);
				} else {
					httpURLConnection = (HttpURLConnection) url
							.openConnection(new Proxy(Type.HTTP,
									new InetSocketAddress(android.net.Proxy
											.getDefaultHost(),
											android.net.Proxy.getDefaultPort())));
				}
			}
			httpURLConnection.setConnectTimeout(3000);
			httpURLConnection.setReadTimeout(3000);

			return httpURLConnection;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 请求参数处理
	 * 
	 * @param isAddMap
	 * @param time
	 * @param latitude
	 * @param longitude
	 * @param addr
	 * @param payState
	 * @param roomState
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String postParam(Context context, String isAddMap,
			String time, String latitude, String longitude, String addr,
			String uuid) {
		@SuppressWarnings("rawtypes")
		Map map = new HashMap();
		/**
		 * 1、 插入数据库 2、根据uuid查询用户列表和位置信息 3、根据uuid实时查询跟踪用户的位置
		 */
		map.put("isAddMap", isAddMap);
		/**
		 * 定位信息
		 */
		map.put("time", time);
		map.put("latitude", latitude);
		map.put("longitude", longitude);
		map.put("addr", addr);
		/**
		 * 用户信息
		 */
		map.put("appVersion", "1.0");
		map.put("pkg", "com.example.mlocation");// 搜索关键字
		map.put("uuid", uuid);

		JSONObject json = new JSONObject(map);
		return json.toString();
	}

	/**
	 * 获取MAC
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getMac() {
		String macString = "MAC";
		try {
			InputStream localInputStream = Runtime.getRuntime()
					.exec("cat /sys/class/net/eth0/address").getInputStream();
			InputStreamReader localInputStreamReader = new InputStreamReader(
					localInputStream);
			BufferedReader localBufferedReader = new BufferedReader(
					localInputStreamReader);
			String str2 = localBufferedReader.readLine();
			if (str2 != null) {
				macString = str2;
			}
			localBufferedReader.close();
			localInputStreamReader.close();
			localInputStream.close();
			String str3 = ((String) macString).toUpperCase();
			if (str3 != null) {
				macString = str3;
			}
			if (macString.equals("MAC")) {
				macString = Build.SERIAL;
			}

		} catch (IOException localIOException) {
			localIOException.printStackTrace();
			macString = Build.SERIAL;
		}
		Log.i("test", "mac:" + macString);
		return macString + "---get";
	}

}
