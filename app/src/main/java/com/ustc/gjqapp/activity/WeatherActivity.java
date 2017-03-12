package com.ustc.gjqapp.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.ustc.gjqapp.R;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	private TextView cityNameText;
	private TextView dateText;
	private TextView tempratureText;
	private TextView txtText;
	private TextView minTempText;
	private TextView maxTempText;
	private TextView dressSuggestionText;
	private Button chooseButton;
	private Button transportButton;
	private Button courseButton;
	private Button buslineButton;
	private Button nearbyButton;

	String locationString = null;//
	String dateString = null;
	String presentTempString = null;
	String weatherString = null;
	String maxminTempString = null;
	String dressSuggetString = null; //
	String cityName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_whether);
		SDKInitializer.initialize(getApplicationContext());
		cityNameText = (TextView) findViewById(R.id.citynameTextView);
		dateText = (TextView) findViewById(R.id.dateTextView);
		tempratureText = (TextView) findViewById(R.id.tempratureTextView);
		txtText = (TextView) findViewById(R.id.txtTextView);
		minTempText = (TextView) findViewById(R.id.mintempTextView);
		maxTempText = (TextView) findViewById(R.id.maxtempTextView);
		dressSuggestionText = (TextView) findViewById(R.id.dressSuggestTextView);
		chooseButton = (Button) findViewById(R.id.addCityBtn);
		cityName=cityNameText.getText().toString();


		chooseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeatherActivity.this,
						WeatherChooseActivity.class);
				startActivity(intent);
				finish();
			}
		});
		getWeatherInfo("苏州");
		Intent intent = getIntent();
		if(intent.getStringExtra("cityname") != null){
			cityName = intent.getStringExtra("cityname");
			getWeatherInfo(cityName);
		}




		/*transportButton = (Button) findViewById(R.id.transport_mainBtn);
		transportButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeatherActivity.this,
						RoutePlanDemo.class);
				startActivity(intent);
			}
		});
		courseButton = (Button) findViewById(R.id.timetable_mainBtn);
		courseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						CourseTableActivity.class);
				startActivity(intent);
			}
		});
		buslineButton = (Button) findViewById(R.id.busline_mainBtn);
		buslineButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						BusLineSearchDemo.class);
				startActivity(intent);
			}
		});
		nearbyButton = (Button) findViewById(R.id.nearby_mainBtn);
		nearbyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						PoiSearchFragmentActivity.class);
				startActivity(intent);
			}
		});*/

	}// onCreate

	// 从api获取天气信息并解析,输入为城市中文名
	public void getWeatherInfo(String cityName) {

		final String path = getURLString(cityName);

		// 子线程解析Json
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(path);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					int code = connection.getResponseCode();
					Log.d("respondCode", code + "");
					if (code == 200) {
						Reader in = null;
						in = new InputStreamReader(connection.getInputStream());
						BufferedReader bufferedIn = new BufferedReader(in);
						String line = bufferedIn.readLine();
						StringBuffer stringBuffer = new StringBuffer();
						while (line != null) { // 会不会nullpointerexception?
							stringBuffer.append(line);
							line = bufferedIn.readLine();
						}
						String json = stringBuffer.toString();
						Log.d("Json", json);
						Gson gson = new Gson();
						Status status = gson.fromJson(json, Status.class);
						locationString = status.getResults().get(0)
								.getCurrentCity();
						// Log.d("get 0 ",
						// status.getResults().get(0).toString()); 打印出了线程号= =

						// 打印weather 和 maxmin
						int count1 = 0;
						for (Weather wea : status.getResults().get(0)
								.getWeather_data()) {
							if (count1 == 0) {
								weatherString = wea.getWeather() + " ";
								maxminTempString = wea.getTemperature();
								Log.d("weather", weatherString);
								Log.d("maxmin", maxminTempString);
								String tempString = wea.getDate();
								Log.d("date", tempString);
								// tempString:周日 05月29日 (实时：21℃)
								String[] tempArr = tempString.split("\\("); // 左右括号分割需转译
								for (int i = 0; i < tempArr.length; i++) {
									Log.d("Arr", tempArr[i]);
								}
								dateString = tempArr[0];
								Log.d("今天日期", dateString);
								// 实时：21℃)
								String[] tempArr2 = tempArr[1].split("："); // 注意这里是中文冒号
								// T^T
								for (int i = 0; i < tempArr2.length; i++) {
									Log.d("Arr2", tempArr2[i]);
								}
								String tempStr = tempArr2[1];
								String[] tempArr3 = tempStr.split("\\)");
								presentTempString = tempArr3[0];
								Log.d("现在温度", presentTempString);
								break;
							}
						}// for weather

						// 打印results-index-des：穿衣指南
						int count2 = 0;

						for (IndexData indexData : status.getResults().get(0)
								.getIndext()) {
							if (count2 == 0) {
								dressSuggetString = indexData.getDes();
								break;
							}
						}// for index

						Log.d("穿衣指数", dressSuggetString);

						// ui线程更新ui
						// if (getActivity() != null) {
						// getActivity().runOnUiThread(new Runnable() {
						runOnUiThread(new Runnable() {
							public void run() {
								cityNameText.setText(locationString);
								dressSuggestionText.setText(dressSuggetString);
								minTempText.setText(maxminTempString);
								txtText.setText(weatherString);
								tempratureText.setText(presentTempString);
								dateText.setText(dateString);
								RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
								mainLayout
										.setBackgroundResource(getWeatherBackground());
							}
						});
					}

					// if code

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}// getWeatherInfo

	private String api = "http://api.map.baidu.com/telematics/v3/weather?";
	private String ak = "mhn3ZNqG82zvcFInkXL39oaz";

	// 拼接请求的url，输入为中文的城市名，输出为完整的请求连接
	public String getURLString(String string) {
		// 将输入的中文转为utf-8
		try {
			string = URLEncoder.encode(string, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// http://api.map.baidu.com/telematics/v3/weather?location=苏州&output=json&ak=mhn3ZNqG82zvcFInkXL39oaz
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("location=");
		stringBuffer.append(string);
		stringBuffer.append("&output=json");
		stringBuffer.append("&ak=");
		stringBuffer.append(ak);
		String tempString = stringBuffer.toString();
		String urlString = api + tempString;
		Log.d("URL", urlString);
		return urlString;

	}// getURLString

	// 按天气更新背景
	public int getWeatherBackground() {
		if (weatherString.contains("晴"))
			return R.drawable.sunny_day;

		if (weatherString.contains("雨"))
			return R.drawable.rain_day;

		if (weatherString.contains("暴雨"))
			return R.drawable.rain_day;

		if (weatherString.contains("中雨"))
			return R.drawable.rain_day;

		if (weatherString.contains("阵雨"))
			return R.drawable.rain_day;

		if (weatherString.contains("小雨"))
			return R.drawable.rain_day;

		if (weatherString.contains("多云"))
			return R.drawable.cloudy_day;

		if (weatherString.contains("阴"))
			return R.drawable.yintian_day;

		if (weatherString.contains("雪"))
			return R.drawable.sunny_day;

		if (weatherString.contains("沙尘"))
			return R.drawable.sunny_day;

		if (weatherString.contains("雾"))
			return R.drawable.sunny_day;
		return R.drawable.sunny_day;

	}

}// class
