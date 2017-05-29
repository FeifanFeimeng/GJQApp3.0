package com.baidumap;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.ustc.gjqapp.R;


/**
 * 演示poi搜索功能
 */
public class PoiSearchFragmentActivity extends FragmentActivity implements
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private BaiduMap mBaiduMap = null;
	/**
	 * 搜索关键字输入窗口
	 */
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;
	 
		// 定位相关
	    LocationClient mLocClient;
	    public MyLocationListenner myListener = new MyLocationListenner();
	    private LocationMode mCurrentMode;
	    BitmapDescriptor mCurrentMarker;
	    double myLatitude= 31.32;
	    double myLongitude= 120.62;

		TextureMapView mMapView;
//	    BaiduMap mBaiduMap;

	    // UI相关
 
	    Button requestLocButton;
	    boolean isFirstLoc = true; // 是否首次定位

	  @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_poisearch);
		 requestLocButton = (Button) findViewById(R.id.button2);
	        mCurrentMode = LocationMode.NORMAL;
	        requestLocButton.setText("定位");
	        OnClickListener btnClickListener = new OnClickListener() {
	            @Override
				public void onClick(View v) {
	                switch (mCurrentMode) {
	                    case NORMAL:
	                        requestLocButton.setText("跟随");
	                        mCurrentMode = LocationMode.FOLLOWING;
	                        mBaiduMap
	                                .setMyLocationConfigeration(new MyLocationConfiguration(
	                                        mCurrentMode, true, mCurrentMarker));
	                        break;
	                    case COMPASS:
	                        requestLocButton.setText("定位");
	                        mCurrentMode = LocationMode.NORMAL;
	                        mBaiduMap
	                                .setMyLocationConfigeration(new MyLocationConfiguration(
	                                        mCurrentMode, true, mCurrentMarker));
	                        break;
	                    case FOLLOWING:
	                        requestLocButton.setText("罗盘");
	                        mCurrentMode = LocationMode.COMPASS;
	                        mBaiduMap
	                                .setMyLocationConfigeration(new MyLocationConfiguration(
	                                        mCurrentMode, true, mCurrentMarker));
	                        break;
	                    default:
	                        break;
	                }
	            }
	        };
	        requestLocButton.setOnClickListener(btnClickListener);
	     // 初始化地图
	        mMapView = (TextureMapView) findViewById(R.id.map);
	        mBaiduMap = mMapView.getMap();
			init();
		     // 开启定位图层
	        mBaiduMap.setMyLocationEnabled(true);
			// 定位初始化
	        mLocClient = new LocationClient(this);
	        mLocClient.registerLocationListener(myListener);
	        LocationClientOption option = new LocationClientOption();
	        option.setOpenGps(true); // 打开gps
	        option.setCoorType("bd09ll"); // 设置坐标类型
	        option.setScanSpan(1000);
	        mLocClient.setLocOption(option);
	        mLocClient.start();
	     // 初始化搜索模块，注册搜索事件监听
			mPoiSearch = PoiSearch.newInstance();
			mPoiSearch.setOnGetPoiSearchResultListener(this);
			mSuggestionSearch = SuggestionSearch.newInstance();
			mSuggestionSearch.setOnGetSuggestionResultListener(this);
			keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
			sugAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line);
			keyWorldsView.setAdapter(sugAdapter);
//			mBaiduMap = ((SupportMapFragment) (getSupportFragmentManager()
//					.findFragmentById(R.id.map))).getBaiduMap();
			  
			/**
			 * 当输入关键字变化时，动态更新建议列表
			 */
			keyWorldsView.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable arg0) {

				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
											  int arg2, int arg3) {

				}

				@Override
				public void onTextChanged(CharSequence cs, int arg1, int arg2,
										  int arg3) {
					if (cs.length() <= 0) {
						return;
					}
					String city = ((EditText) findViewById(R.id.city)).getText()
							.toString();
					/**
					 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
					 */
					mSuggestionSearch
							.requestSuggestion((new SuggestionSearchOption())
									.keyword(cs.toString()).city(city));
				}
			});

		}

		@Override
		protected void onPause() {
			super.onPause();
		}

		@Override
		protected void onResume() {
			super.onResume();
		}

		@Override
		protected void onDestroy() {
			mPoiSearch.destroy();
			mSuggestionSearch.destroy();
			super.onDestroy();
		}

		@Override
		protected void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
		}

		@Override
		protected void onRestoreInstanceState(Bundle savedInstanceState) {
			super.onRestoreInstanceState(savedInstanceState);
		}

	private void init(){
        //经纬度(纬度，经度) 我们这里设置苏州市的位置
        LatLng latlng = new LatLng(31.32,120.62);
        MapStatusUpdate mapStatusUpdate_circle = MapStatusUpdateFactory.newLatLng(latlng);
        mBaiduMap.setMapStatus(mapStatusUpdate_circle);
    }
 

	/**
	 * 影响搜索按钮点击事件
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {
		EditText editCity = (EditText) findViewById(R.id.city);
		EditText editSearchKey = (EditText) findViewById(R.id.searchkey);
		if(!editCity.getText().toString().contains("附近")){
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(editCity.getText().toString())
				.keyword(editSearchKey.getText().toString())
				.pageNum(load_Index));
		}
		else{
		// 这是范围
		LatLngBounds.Builder b = new LatLngBounds.Builder();
		b.include(new LatLng(myLatitude, myLongitude));
		 LatLngBounds build = b.build();
		// 第四步，发起检索请求； 
		mPoiSearch.searchInBound(new PoiBoundSearchOption().bound(build).keyword(editSearchKey.getText().toString()).pageNum(load_Index)); //检索附近
		}
		}

	public void goToNextPage(View v) {
		load_Index++;
		searchButtonProcess(null);
	}

	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(PoiSearchFragmentActivity.this, "未找到结果", Toast.LENGTH_LONG)
			.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果";
			Toast.makeText(PoiSearchFragmentActivity.this, strInfo, Toast.LENGTH_LONG)
					.show();
		}
	}

	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PoiSearchFragmentActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(PoiSearchFragmentActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
			.show();
		}
	}

	@Override
	public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			// }
			return true;
		}
	}
	/**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
		
        @Override
        public void onReceiveLocation( BDLocation location) {
//        	new Thread() {   			 
//    			@Override
//    			public void run() {
    				 // map view 销毁后不在处理新接收的位置
        	myLatitude=location.getLatitude();
            myLongitude=location.getLongitude();
    	            if (location == null || mMapView == null) {
    	                return;
    	            }
    	            MyLocationData locData = new MyLocationData.Builder()
    	                    .accuracy(location.getRadius())
    	                            // 此处设置开发者获取到的方向信息，顺时针0-360
    	                    .direction(100).latitude(location.getLatitude())
    	                    .longitude(location.getLongitude()).build();
    	            
    	            mBaiduMap.setMyLocationData(locData);
    	            if (isFirstLoc) {
    	                isFirstLoc = false;
    	                LatLng ll = new LatLng(location.getLatitude(),
    	                        location.getLongitude());
    	                MapStatus.Builder builder = new MapStatus.Builder();
    	                builder.target(ll).zoom(18.0f);
    	                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    	            }
    				
    			}
//    		}.start();
//           
//        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}

