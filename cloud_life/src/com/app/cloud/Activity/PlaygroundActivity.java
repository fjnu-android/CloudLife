package com.app.cloud.Activity;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapException;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.NaviPara;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 附近运动场所 高德地图sdk
 * 
 * 定位+POI周边搜索
 */
public class PlaygroundActivity extends BaseUiAuth implements OnClickListener, LocationSource, AMapLocationListener,
		OnMarkerClickListener, InfoWindowAdapter, OnPoiSearchListener {

	private Toolbar mToolbar;

	// 声明变量
	private MapView mapView;
	// 在onCreat方法中给aMap对象赋值
	private AMap aMap;

	// 定位坐标经纬度，搜索中心点
	private LatLonPoint lp;
	private String city;

	/**
	 * 定位
	 */
	private AMapLocation Location;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;

	/**
	 * 搜索
	 */
	private int currentPage = 0;// 当前页面，从0开始计数
	private int totalPage = 1;
	private PoiSearch.Query query;// Poi查询条件类

	PoiAdapter adapter;
	ListView listView;

	// 图片标记
	int[] poi_marker = new int[] { R.drawable.b_poi_1, R.drawable.b_poi_2, R.drawable.b_poi_3, R.drawable.b_poi_4,
			R.drawable.b_poi_5, R.drawable.b_poi_6, R.drawable.b_poi_7, R.drawable.b_poi_8, R.drawable.b_poi_9,
			R.drawable.b_poi_10 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playground);

		initToolbar();
		initView();

		openLoad("正在搜索...");

		initMap(savedInstanceState);

	}

	private void initToolbar() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("附近的运动场所");
		mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// up Button
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// menu
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.pg_up:
					if (currentPage == 0)
						toast("当前已是第一页");
					if (currentPage > 0) {
						openLoad("正在搜索...");
						currentPage--;
						doSearchQuery();
					}
					break;
				case R.id.pg_down:
					if (totalPage <= 1 || (currentPage + 1) == totalPage)
						toast("没有下一页了");
					else {
						openLoad("正在搜索...");
						currentPage++;
						doSearchQuery();
					}

					break;
				default:
				}
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_playground, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	/**
	 * 初始化AMap对象
	 */
	private void initMap(Bundle savedInstanceState) {
		mapView = (MapView) findViewById(R.id.pg_map);
		// 必须要写
		mapView.onCreate(savedInstanceState);
		aMap = mapView.getMap();

		/**
		 * 定位
		 */
		// 自定义系统定位蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		// 自定义定位蓝点图标
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
		// 自定义精度范围的圆形区域填充颜色
		myLocationStyle.radiusFillColor(Color.parseColor("#301e90ff"));
		// 自定义精度范围的圆形边框颜色
		myLocationStyle.strokeColor(Color.parseColor("#1e90ff"));
		// 自定义精度范围的圆形边框宽度
		myLocationStyle.strokeWidth(2);

		// 将自定义的 myLocationStyle 对象添加到地图上
		aMap.setMyLocationStyle(myLocationStyle);
		// 设置定位资源。如果不设置此定位资源则定位按钮不可点击。
		aMap.setLocationSource(this);
		// 设置默认定位按钮是否显示
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		aMap.setMyLocationEnabled(true);
		
		
		/**
		 * POI搜索
		 */
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doSearchQuery();
			}
		}, 1000);

	}

	private void initView() {
		listView = (ListView) findViewById(R.id.pg_list);
		adapter = new PoiAdapter(this);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}
	}

	///////////////////////////////////////////////////
	///// 地图定位
	/**
	 * 启动定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();

			mlocationClient.setLocationListener(this);
			// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 给定位客户端对象设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;

	}

	/**
	 ** 定位成功后回调函数  
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				// 显示系统小蓝点
				//mListener.onLocationChanged(amapLocation);
				
				Location = amapLocation;
				lp = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
				city = amapLocation.getCity();
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
			}
		}
	}

	/////////////////////////////////////////
	//////// 高德poi搜索 ,运动场所080100
	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {

		query = new PoiSearch.Query("运动场馆", "体育休闲服务", city);
		// keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
		// 共分为以下20种：汽车服务|汽车销售|
		// 汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
		// 住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
		// 金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
		// cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第?页
		PoiSearch poiSearch = new PoiSearch(this, query);
		poiSearch.setBound(new SearchBound(lp, 30000));// 设置周边搜索的中心点以及区域,单位m
		poiSearch.setOnPoiSearchListener(this);// 设置数据返回的监听器
		poiSearch.searchPOIAsyn();// 开始搜索
	}

	/**
	 * POI详情查询回调方法
	 */
	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {

	}

	/**
	 * POI信息查询回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					totalPage = result.getPageCount();

					if (poiItems != null && poiItems.size() > 0) {
						// 加载到列表并标记地图
						adapter.list.clear();
						adapter.list = poiItems;
						adapter.notifyDataSetChanged();

						aMap.clear();	
						//TODO 定位图标
						mListener.onLocationChanged(Location);

						for (int i = 0; i < poiItems.size(); i++) {

							aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
									.icon(BitmapDescriptorFactory.fromResource(poi_marker[i]))
									.position(new LatLng(poiItems.get(i).getLatLonPoint().getLatitude(),
											poiItems.get(i).getLatLonPoint().getLongitude())));

						}
					} else {
						toast("附近没有运动场所");
					}
				}
			} else {
				toast("附近没有运动场所");
			}
		} else if (rCode == 27) {
			toast("未知的主机");
		} else if (rCode == 32) {
			toast("key 鉴权验证失败，请检查key绑定的sha1值、packageName与apk是否对应");
		} else {
			toast("其他错误" + rCode);
		}

		closeLoad();

	}

	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	private void openAMapNavi(final LatLng marker) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("是否利用高德地图进行导航");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startAMapNavi(marker);
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	/**
	 * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
	 */
	public void startAMapNavi(LatLng marker) {
		// 构造导航参数
		NaviPara naviPara = new NaviPara();
		// 设置终点位置
		naviPara.setTargetPoint(marker);
		// 设置导航策略，这里是避免拥堵
		naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);
		try {
			// 调起高德地图导航
			AMapUtils.openAMapNavi(naviPara, getApplicationContext());
		} catch (AMapException e) {
			// 如果没安装会进入异常，调起下载页面
			openDialog();
		}
	}

	private void openDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("你手机未安装高德地图，是否下载并进行导航");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AMapUtils.getLatestAMapApp(getApplicationContext());
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	/**
	 * 获取当前app的应用名字
	 */
	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	/////////////////////////////////////////
	////// mapView及活动的生命周期

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	////////////////////////////////////////
	////// 内部类 列表适配器

	public class PoiAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public List<PoiItem> list = new ArrayList<PoiItem>();

		public PoiAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		// listview有多少个item
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final PoiItem poi = list.get(position);

			TextView title;
			TextView address;
			TextView distance;
			LinearLayout show;
			Button navi;

			convertView = inflater.inflate(R.layout.item_pg_poi, null);

			title = (TextView) convertView.findViewById(R.id.pg_title);
			address = (TextView) convertView.findViewById(R.id.pg_address);
			show = (LinearLayout) convertView.findViewById(R.id.pg_show);
			distance = (TextView) convertView.findViewById(R.id.pg_distance);
			navi = (Button) convertView.findViewById(R.id.pg_navi);

			title.setText((position + 1) + "." + poi.getTitle());
			address.setText(poi.getSnippet());
			distance.setText(poi.getDistance() + "m");

			show.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					aMap.moveCamera(new CameraUpdateFactory().changeLatLng(
							new LatLng(poi.getLatLonPoint().getLatitude(), poi.getLatLonPoint().getLongitude())));
				}
			});

			navi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openAMapNavi(new LatLng(poi.getLatLonPoint().getLatitude(), poi.getLatLonPoint().getLongitude()));
				}
			});

			return convertView;
		}

	}

}
