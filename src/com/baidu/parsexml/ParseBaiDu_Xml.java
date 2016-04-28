package com.baidu.parsexml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/***
 * @author pickle丶咸菜
 * @category 此类是基于XMLParser的解析，用于解析从百度获取的天气XML信息,不可用于解析其他数据
 * ***/
public class ParseBaiDu_Xml {
	private InputStream input = null;
	private WeatherInfo weatherInfo = null;
	private int dateFlag = 0;// 判断是第几个date元素
	private String nodeName = null;// 当前元素节点名称

	/**
	 * @param input
	 *            通过构造方法将输入流传入
	 * */
	public ParseBaiDu_Xml(InputStream input) {
		this.input = input;
	}

	/**
	 * @return 一个包含当前日期与未来三天天气信息的List对象
	 * @throws XmlPullParserException
	 * @throws IOException
	 * */
	public List<WeatherInfo> getWeatherInfos() throws XmlPullParserException,
			IOException {
		List<WeatherInfo> allInfos = null;// 保存解析得来的所有数据
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser xmlPullParser = factory.newPullParser();// 创建XmlPullParser
		xmlPullParser.setInput(input, "UTF-8");// 设置输入流与编码格式
		// ....操作开始
		int eventType = xmlPullParser.getEventType();// 取得事件的操作码
		while (eventType != XmlPullParser.END_DOCUMENT) {// 如果文档没有结束
			nodeName = xmlPullParser.getName();// 取得当前读取的节点名称
			switch (eventType) {
			// ...文档开始
			case XmlPullParser.START_DOCUMENT:
				System.out.println("文档开始");
				allInfos = new ArrayList<WeatherInfo>();
				break;
			// ...开始读取标记
			case XmlPullParser.START_TAG:
				if ("date".equals(nodeName)) {
					if (dateFlag == 0) {// 表示第一个date节点，无视 之~~~
						dateFlag++;
					} else {
						weatherInfo = new WeatherInfo();
						String temp = xmlPullParser.nextText();
						weatherInfo.setDate(temp);
					}
				} else if ("weather".equals(nodeName)) {
					// ...天气
					String temp = xmlPullParser.nextText();
					weatherInfo.setWeather(temp);
				} else if ("wind".equals(nodeName)) {
					// ...风力
					String temp = xmlPullParser.nextText();
					weatherInfo.setWind(temp);
				} else if ("dayPictureUrl".equals(nodeName)) {
					// ...白天的图片
					String temp = xmlPullParser.nextText();
					weatherInfo.setDayPictureUrl(temp);
				} else if ("nightPictureUrl".equals(nodeName)) {
					// ...晚上的图片
					String temp = xmlPullParser.nextText();
					weatherInfo.setNightPictureUrl(temp);
				} else if ("temperature".equals(nodeName)) {
					// ...气温
					String temp = xmlPullParser.nextText();
					weatherInfo.setTemperature(temp);
					allInfos.add(weatherInfo);
				}
				break;
			// ...结束标记
			case XmlPullParser.END_TAG:
				nodeName = xmlPullParser.getName();
				if ("weather_data".equals(nodeName)) {
					weatherInfo = null;// 清空对象
				}
				break;
			default:
				break;
			}
			eventType = xmlPullParser.next();// 将操作事件指到下一个标记
		}
		return allInfos;
	}
}
