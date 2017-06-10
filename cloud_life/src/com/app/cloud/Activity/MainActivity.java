package com.app.cloud.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Fragment.HomeFragment;
import com.app.cloud.Fragment.DietFragment;
import com.app.cloud.Fragment.SportFragment;
import com.app.cloud.Ui.CircleImageView;
import com.app.cloud.Ui.PagerSlidingTabStrip;
import com.app.cloud.Util.BitmapCache;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.JsonObjectWeatherRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Raindus
 * @version 1.0
 * @since 2015.12.27 主界面活动toolbar+viewpager+tabstrip+drawlayout
 */

public class MainActivity extends BaseUiAuth implements OnClickListener {

	private Toolbar mToolbar;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	@SuppressWarnings("unused")
	private RelativeLayout drawer_bg;

	private LinearLayout drawer_info;
	private LinearLayout item_test;
	private LinearLayout item_contact;
	private LinearLayout item_about;
	private LinearLayout item_logoff;
	private LinearLayout item_data;
	private LinearLayout item_weather;
	private LinearLayout item_shop;
	private LinearLayout item_exit;

	private HomeFragment home;
	private DietFragment diet;
	private SportFragment sport;

	CircleImageView icon;
	TextView name;
	TextView sign;
	TextView body;

	// 天气部分
	public ImageView weather_icon;
	public TextView weather_temp;
	public TextView weather_city;
	public TextView weather_type;
	public TextView weather_time;

	// 获取壁纸管理器
	WallpaperManager wallpaperManager;
	// 获取当前壁纸
	Drawable wallpaperDrawable;
	// 将Drawable,转成Bitmap
	Bitmap bm;

	/**
	 * PagerSlidingTabStrip的实例
	 */
	private PagerSlidingTabStrip tabs;

	/**
	 * 获取当前屏幕的密度
	 */
	private DisplayMetrics dm;

	AlertDialog.Builder dialog;

	RequestQueue mQueue;

	/////////////////////////////////////////////////////////
	// 声明AMapLocationClient类对象
	public AMapLocationClient mlocationClient = null;

	// 声明mLocationOption对象
		public AMapLocationClientOption mLocationOption = null;
		
	// 声明定位回调监听器
	public AMapLocationListener mLocationListener = new AMapLocationListener() {

		@Override
		public void onLocationChanged(AMapLocation amapLocation) {
			if (amapLocation != null) {
				if (amapLocation.getErrorCode() == 0) {
					// 定位成功回调信息，设置相关消息
					/*
					 * amapLocation.getLocationType();//
					 * 获取当前定位结果来源，如网络定位结果，详见定位类型表 amapLocation.getLatitude();//
					 * 获取纬度 amapLocation.getLongitude();// 获取经度
					 * amapLocation.getAccuracy();// 获取精度信息 SimpleDateFormat df
					 * = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); Date date
					 * = new Date(amapLocation.getTime()); df.format(date);//
					 * 定位时间 amapLocation.getAddress();//
					 * 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，
					 * GPS定位不返回地址信息。 amapLocation.getCountry();// 国家信息
					 * amapLocation.getProvince();// 省信息
					 * amapLocation.getCity();// 城市信息
					 * amapLocation.getDistrict();// 城区信息
					 * amapLocation.getStreet();// 街道信息
					 * amapLocation.getStreetNum();// 街道门牌号信息
					 * amapLocation.getCityCode();// 城市编码
					 * amapLocation.getAdCode();// 地区编码
					 */
					customer.location = amapLocation.getAddress();
					customer.locationCity = amapLocation.getCity();
					customer.lp = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
					//TODO
					//getWeather();
					putLoacationCity();
				} else {
					// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
					Log.e("AmapError", "location Error, ErrCode:" + amapLocation.getErrorCode() + ", errInfo:"
							+ amapLocation.getErrorInfo());
				}
			}
		}

	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mQueue = SingleRequestQueue.getRequestQueue();

		initToolbar();

		initDrawer();

		setListener();

		initTabs();

		setTabsValue();
		
		wallpaperBlur();

		// 定位
		initLBS();
		
	}

	// 更新个人信息
	@Override
	public void onStart() {
		super.onStart();

		getIcon();
		name.setText(customer.getName());
		sign.setText(customer.getSign());
		body.setText(customer.getBodytype());
	}

	public void onDestory() {
		super.onDestroy();
		if (null != mlocationClient) {
			// 停止定位
			mlocationClient.stopLocation();
			// 销毁定位客户端。
			mlocationClient.onDestroy();
		}
	}

	public void initToolbar() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("云生活");
		mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}

	public void initDrawer() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
				R.string.drawer_close);

		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public void setListener() {
		drawer_info = (LinearLayout) findViewById(R.id.drawer_info);

		item_test = (LinearLayout) findViewById(R.id.item_test);
		item_logoff = (LinearLayout) findViewById(R.id.item_logoff);
		item_contact = (LinearLayout) findViewById(R.id.item_contact);
		item_about = (LinearLayout) findViewById(R.id.item_about);
		item_data = (LinearLayout) findViewById(R.id.item_data);
		item_weather = (LinearLayout) findViewById(R.id.item_weather);
		item_shop = (LinearLayout) findViewById(R.id.item_shop);
		item_exit = (LinearLayout) findViewById(R.id.item_exit);

		weather_icon = (ImageView) findViewById(R.id.weather_icon);
		weather_temp = (TextView) findViewById(R.id.weather_temp);
		weather_city = (TextView) findViewById(R.id.weather_city);
		weather_type = (TextView) findViewById(R.id.weather_type);
		weather_time = (TextView) findViewById(R.id.weather_time);

		drawer_bg = (RelativeLayout) findViewById(R.id.drawer_bg);
		drawer_info.setOnClickListener(this);

		item_test.setOnClickListener(this);
		item_logoff.setOnClickListener(this);
		item_contact.setOnClickListener(this);
		item_about.setOnClickListener(this);
		item_data.setOnClickListener(this);
		item_weather.setOnClickListener(this);
		item_shop.setOnClickListener(this);
		item_exit.setOnClickListener(this);

		name = (TextView) findViewById(R.id.user_name);
		sign = (TextView) findViewById(R.id.user_sign);
		body = (TextView) findViewById(R.id.user_body);
		icon = (CircleImageView) findViewById(R.id.user_icon);
	}

	public void initTabs() {
		dm = getResources().getDisplayMetrics();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		// 加载旁边的两个碎片
		pager.setOffscreenPageLimit(2);
		tabs.setViewPager(pager);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.main_add:
			overlay(PublishActivity.class);
			break;
		case R.id.main_search:
			overlay(SearchActivity.class);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 打开延迟
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.drawer_info:
			mDrawerLayout.closeDrawers();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					overlay(InfoActivity.class);
				}
			}, 500);
			break;
		case R.id.item_test:
			mDrawerLayout.closeDrawers();
			if (!customer.getBodyType().equals(""))
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						overlay(ATestActivity.class);
					}
				}, 500);
			else
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						overlay(BTestActivity.class);
					}
				}, 500);
			break;
		case R.id.item_data:
			mDrawerLayout.closeDrawers();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					overlay(DataActivity.class);
				}
			}, 500);
			break;
		case R.id.item_shop:
			mDrawerLayout.closeDrawers();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					overlay(ShopActivity.class);
				}
			}, 500);
			break;
		case R.id.item_logoff:
			openDialog();
			break;
		case R.id.item_exit:
			this.doFinish();
			System.exit(0);
			break;
		case R.id.item_contact:
			mDrawerLayout.closeDrawers();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					overlay(ContactActivity.class);
				}
			}, 500);
			break;
		case R.id.item_about:
			mDrawerLayout.closeDrawers();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					overlay(AboutActivity.class);
				}
			}, 500);
			break;
		case R.id.item_weather:
			// TODO 温馨提示还是不错的
			break;
		}
	}

	private void getWeather() {
		String url;
		/*
		// 对于特别地区进行处理
		switch (customer.getCity().substring(0, customer.getCity().indexOf("-"))) {
		case "北京":
			url = "北京";
			break;
		case "天津":
			url = "天津";
			break;
		case "上海":
			url = "上海";
			break;
		case "重庆":
			url = "重庆";
			break;
		case "香港":
			url = "香港";
			break;
		case "澳门":
			url = "澳门";
			break;
		default:
			// 截取“-”之后的市名
			url = customer.getCity().substring(customer.getCity().indexOf("-") + 1);
		}
		*/
		url = customer.locationCity;
		try {
			url = java.net.URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		JsonObjectWeatherRequest get = new JsonObjectWeatherRequest(C.weather + url + C.weather_key,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject json) {
						try {
							if (json.getString("error_code").equals("0")) {
								Log.d("getWeather", json.getString("reason").toString());

								JSONObject data = json.getJSONObject("result").getJSONObject("data")
										.getJSONObject("realtime");
								weather_time.setText("更新  " + data.getString("time").substring(0, 5));
								weather_city.setText(data.getString("city_name"));

								JSONObject weather = data.getJSONObject("weather");
								weather_temp.setText(weather.getString("temperature") + " °");
								weather_type.setText(weather.getString("info"));

								changeWeatherIcon(weather.getString("info"));

								customer.weather = json.getJSONObject("result").getJSONObject("data");
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						toast("网络错误，天气更新失败！");
					}
				});

		mQueue.add(get);
	}

	// 换天气图标
	private void changeWeatherIcon(String type) {

		if (type.equals("霾")) {
			weather_icon.setImageResource(R.drawable.w_mai);
			return;
		}

		if (type.equals("雾")) {
			weather_icon.setImageResource(R.drawable.w_wu);
			return;
		}

		if (type.equals("晴")) {
			weather_icon.setImageResource(R.drawable.w_qing);
			return;
		}

		if (type.equals("多云")) {
			weather_icon.setImageResource(R.drawable.w_duoyun);
			return;
		}

		if (type.equals("阴")) {
			weather_icon.setImageResource(R.drawable.w_yin);
			return;
		}

		for (int i = 0; i < type.length(); i++) {
			if (type.charAt(i) == '雨') {
				weather_icon.setImageResource(R.drawable.w_yu);
				return;
			}
		}

		for (int i = 0; i < type.length(); i++) {
			if (type.charAt(i) == '雪') {
				weather_icon.setImageResource(R.drawable.w_xue);
				return;
			}
		}

		weather_icon.setImageResource(R.drawable.w_other);

	}
	
	private void putLoacationCity(){
		
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type","locate");
		urlParams.put("city", customer.locationCity);

		// 发送定位
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						Log.d("up_locate", "发送定位成功");
					}
					if (jsonObject.getString("status").equals("0"))
						Log.d("up_locate", "空，发送定位失败");

					if (jsonObject.getString("status").equals("-2"))
						Log.d("up_locate", "服务器错误，发送定位失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("up_locate", error.getMessage(), error);
			}
		}, urlParams);
		
		if(!customer.getCookie().equals("")){ //向服务器发起post请求时加上cookie字段
			 post.setSendCookie(customer.getCookie()); }
		
		mQueue.add(post);
	}

	private void openDialog() {
		dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("你确定退出当前账号并清除信息吗？");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clearShared();
				toLogin();
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	// 清除shared里的数据
	private void clearShared() {
		SharedPreferences.Editor editor = getSharedPreferences(C.dir.shared, MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();

	}

	private void getIcon() {
		// 头像存在加载头像
		if (!customer.getIcon().equals("")) {
			File f = new File(C.dir.icon + customer.getIcon().substring(10));
			if (f.exists()) {
				// sd卡本地头像放入缓存
				// "#W100#H100"为缓存前缀
				BitmapCache.instance().putBitmap("#W100#H100" + C.api.base + "/img" + customer.getIcon(),
						decodeSampledBitmapFromFile(C.dir.icon + customer.getIcon().substring(10), 100, 100));
				icon.setImageBitmap(
						BitmapCache.instance().getBitmap("#W100#H100" + C.api.base + "/img" + customer.getIcon()));
			} else {
				// 下载头像 保存
				ImageRequest imageRequest = new ImageRequest(C.api.base + "/img" + customer.getIcon(),
						new Response.Listener<Bitmap>() {
							@Override
							public void onResponse(Bitmap response) {
								try {
									saveBitmapIcon(customer.getIcon().substring(10), response);
									BitmapCache.instance().putBitmap(
											"#W100#H100" + C.api.base + "/img" + customer.getIcon(),
											decodeSampledBitmapFromFile(C.dir.icon + customer.getIcon().substring(10),
													100, 100));
									icon.setImageBitmap(BitmapCache.instance()
											.getBitmap("#W100#H100" + C.api.base + "/img" + customer.getIcon()));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}, 0, 0, Config.ARGB_8888, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
							}
						});
				mQueue.add(imageRequest);
			}

		}
	}

	public void toLogin() {
		this.forward(LoginActivity.class);
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(Color.parseColor("#1E90FF"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		tabs.setSelectedTextColor(Color.parseColor("#1E90FF"));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		private final String[] titles = { "圈子", "饮食", "运动" };

		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (home == null) {
					home = new HomeFragment();
				}
				return home;
			case 1:
				if (diet == null) {
					diet = new DietFragment();
				}
				return diet;
			case 2:
				if (sport == null) {
					sport = new SportFragment();
				}
				return sport;
			default:
				return null;
			}
		}

	}

	// 开线程获取桌面壁纸高斯模糊
	private void wallpaperBlur() {

		new Thread(new Runnable() {
			@Override
			public void run() {

				// 获取壁纸管理器
				wallpaperManager = WallpaperManager.getInstance(getContext());
				// 获取当前壁纸
				wallpaperDrawable = wallpaperManager.getDrawable();
				// 将Drawable,转成Bitmap
				bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();

				customer.wallBlurs = blur(bm);
			}
		}).start();

	}

	// 背景虚化
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private Bitmap blur(Bitmap bkg) {
		long startMs = System.currentTimeMillis();
		float radius = 15;

		bkg = small(bkg);
		Bitmap bitmap = bkg.copy(bkg.getConfig(), true);

		final RenderScript rs = RenderScript.create(this.getContext());
		final Allocation input = Allocation.createFromBitmap(rs, bkg, Allocation.MipmapControl.MIPMAP_NONE,
				Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		script.setRadius(radius);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(bitmap);

		rs.destroy();
		Log.d("wallPaper", "blur take away:" + (System.currentTimeMillis() - startMs) + "ms");
		return bitmap;
	}

	private static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.20f, 0.20f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	/**
	 * 监听手机上的BACK键 先关闭侧边栏 不退出应用，直接返回桌面
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
				mDrawerLayout.closeDrawers();
			} else {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 * @param path
	 *            根据sd卡路径加载图片并压缩
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 保存头像jpg格式 不压缩 到sd卡
	public void saveBitmapIcon(String resourceId, Bitmap bitmap) throws IOException {

		// 检查文件夹存在否
		File imageDir = new File(C.dir.icon);
		if (!imageDir.exists())
			imageDir.mkdirs();

		// 路径加名字
		File f = new File(C.dir.icon + resourceId);
		if (f.exists())
			return;

		// 开始保存
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 配置定位参数，启动定位
	private void initLBS() {
		// 初始化定位
		mlocationClient = new AMapLocationClient(getApplicationContext());
		// 设置定位回调监听
		mlocationClient.setLocationListener(mLocationListener);

		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(true);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);

		// 给定位客户端对象设置定位参数
		mlocationClient.setLocationOption(mLocationOption);
		// 启动定位
		mlocationClient.startLocation();

	}

}
