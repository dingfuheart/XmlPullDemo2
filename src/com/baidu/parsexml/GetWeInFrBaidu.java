package com.baidu.parsexml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Environment;

public class GetWeInFrBaidu {

	private static final String BAIDUAPI_URL = "http://api.map.baidu.com/telematics/v3/weather?location=";// 接口网站前缀
	private static final String OUTPUT_TYPE = "&output=xml";// 输出格式，貌似百度目前既能输出XML格式又能输出json格式
	private static final String APP_KEY = "&ak=6tYzTvGZSOpYB5Oc2YGGOKt8";// 开发密钥

	/**
	 * @param cityName
	 *            传入一个城市名称
	 * */
	public static String myPostFun(String cityName)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		String weatherResult = null;// 天气结果
		HttpGet get;// 定义一个Get对象,用于请求对象
		String url = BAIDUAPI_URL + cityName + OUTPUT_TYPE + APP_KEY;
		System.err.println("链接为：" + url);
		get = new HttpGet(url); // 将连接的地址通过Get提交
		HttpResponse response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() == 200) {
			weatherResult = EntityUtils.toString(response.getEntity());
			return weatherResult;
		} else {
			return "取值错误";
		}
	}

	/**
	 * 将字符串转化为输入流
	 * 
	 * @param s
	 *            传入一个字符串
	 **/
	public static InputStream getStringInputStream(String s) {
		if (s != null && !s.equals("")) {
			try {
				ByteArrayInputStream stringInput = new ByteArrayInputStream(
						s.getBytes());
				return stringInput;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 判断SD卡是否存在的方法
	 * ***/
	public static boolean isSdExist() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return false;
		} else {
			return true;
		}
	}

}
