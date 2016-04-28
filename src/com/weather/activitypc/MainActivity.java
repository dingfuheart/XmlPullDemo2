package com.weather.activitypc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.parsexml.GetWeInFrBaidu;
import com.baidu.parsexml.ParseBaiDu_Xml;
import com.baidu.parsexml.WeatherInfo;
import com.example.xmlpull.R;

public class MainActivity extends Activity {
	Button saveBtn, getBtn, XMLBtn;
	TextView showInfoTextView;
	List<WeatherInfo> all;
	public static String temp;
	public static StringBuffer buffer;
	List<WeatherInfo> weatherInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		getBtn = (Button) findViewById(R.id.HttpGet);
		XMLBtn = (Button) findViewById(R.id.XMLGet);
		showInfoTextView = (TextView) findViewById(R.id.showInfo);
		saveBtn.setOnClickListener(new BtnOnclickListener());
		getBtn.setOnClickListener(new BtnOnclickListener());
		XMLBtn.setOnClickListener(new BtnOnclickListener());
	}

	private final class BtnOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == saveBtn) {
				showInfoTextView.setText(temp);
				String dString = showInfoTextView.getText().toString();
				if (!showInfoTextView.getText().toString().trim().equals("")) {
					String path = Environment.getExternalStorageDirectory()
							.toString()
							+ File.separator
							+ "天气"
							+ File.separator + "百度天气XML.xml";
					File file = new File(path);
					OutputStream out = null;
					try {
						out = new FileOutputStream(file, true);
						try {
							out.write(dString.getBytes());
							Toast.makeText(MainActivity.this, "保存成功：" + file,
									1000).show();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}

			} else if (v == getBtn) {
				System.err.println("事件监听----->" + "getBtn");
				new Thread() {
					public void run() {
						try {
							temp = GetWeInFrBaidu.myPostFun("泰兴");
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					};
				}.start();
				showInfoTextView.setText(temp);
			} else if (v == XMLBtn) {
				try {
					System.err.println("事件监听----->" + "XMLBtn");
					xmlJieX();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void xmlJieX() throws Exception {
		InputStream input = GetWeInFrBaidu.getStringInputStream(temp);
		ParseBaiDu_Xml xml = new ParseBaiDu_Xml(input);
		weatherInfos = xml.getWeatherInfos();
		StringBuffer infobuffer = new StringBuffer();
		for (int i = 0; i < weatherInfos.size(); i++) {
			infobuffer.append("---------weatherInfos------\n"
					+ weatherInfos.get(i).getDate() + " \n"
					+ weatherInfos.get(i).getWeather() + " \n"
					+ weatherInfos.get(i).getWind() + " \n"
					+ weatherInfos.get(i).getTemperature() + " \n"
					+ weatherInfos.get(i).getDayPictureUrl() + " \n"
					+ weatherInfos.get(i).getNightPictureUrl() + "\n");
		}
		showInfoTextView.setText(infobuffer);
	}
}