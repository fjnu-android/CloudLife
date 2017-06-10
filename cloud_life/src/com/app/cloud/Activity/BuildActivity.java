package com.app.cloud.Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Dialog.ChooseTimeDialog;
import com.app.cloud.Dialog.ChooseTimeDialog.OnTimeCListener;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 发起活动
 *
 */
public class BuildActivity extends BaseUiAuth implements View.OnClickListener, OnPoiSearchListener {

	Calendar c = Calendar.getInstance();
	String nextDay = (c.get(Calendar.YEAR) - 2000) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
			+ c.get(Calendar.DAY_OF_MONTH);

	LatLonPoint build_point;

	EditText name;
	EditText sign;
	TextView time;
	TextView address1;
	TextView address2;
	Button build;

	PopupWindow pop;
	View popView;

	PopAdapter adapter;
	ListView listView;
	Button pg_up;
	Button pg_down;

	RequestQueue mQueue;
	/**
	 * 搜索
	 */
	private int currentPage = 0;// 当前页面，从0开始计数
	private int totalPage = 1;
	private PoiSearch.Query query;// Poi查询条件类

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_build);
		mQueue = SingleRequestQueue.getRequestQueue();

		initToolbar("发起活动");
		initView();
		initPop();

	}

	private void initView() {

		name = (EditText) findViewById(R.id.build_name);
		sign = (EditText) findViewById(R.id.build_sign);
		time = (TextView) findViewById(R.id.build_time);
		address1 = (TextView) findViewById(R.id.build_address1);
		address2 = (TextView) findViewById(R.id.build_address2);
		build = (Button) findViewById(R.id.build_sure);

		time.setOnClickListener(this);
		address1.setOnClickListener(this);
		build.setOnClickListener(this);
	}

	@SuppressLint("InflateParams")
	private void initPop() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popup_playground, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		pop = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		pop.setFocusable(true);
		// 设置弹出和隐藏动画
		pop.setAnimationStyle(R.style.popBottom_anim);

		listView = (ListView) popView.findViewById(R.id.pop_pg_list);
		adapter = new PopAdapter(this);
		listView.setAdapter(adapter);

		pg_up = (Button) popView.findViewById(R.id.pop_up);
		pg_down = (Button) popView.findViewById(R.id.pop_down);
		pg_up.setOnClickListener(this);
		pg_down.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.build_time:
			ChooseTimeDialog mChooseTimeDialog = new ChooseTimeDialog(this);
			mChooseTimeDialog.setDay(nextDay);
			mChooseTimeDialog.setHour("12");
			mChooseTimeDialog.setMinute("0");
			mChooseTimeDialog.show();
			mChooseTimeDialog.setTimekListener(new OnTimeCListener() {
				@Override
				public void onClick(String day, String hour, String minute) {
					time.setText("20" + day + " " + hour + ":" + minute);
				}
			});
			break;
		case R.id.build_address1:
			doSearchQuery();
			pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.build_sure:

			if (name.getText().toString().equals(""))
				toast("请输入活动名称");
			else if (time.getText().toString().equals(""))
				toast("请选择活动时间");
			else if (sign.getText().toString().equals(""))
				toast("请选择活动简介");
			else if (address1.getText().toString().equals(""))
				toast("请选择活动地点");
			else
				put_act();

			break;
		case R.id.pop_up:
			if (currentPage == 0)
				toast("当前已是第一页");
			if (currentPage > 0) {
				currentPage--;
				doSearchQuery();
			}
			break;
		case R.id.pop_down:
			if (totalPage <= 1 || (currentPage + 1) == totalPage)
				toast("没有下一页了");
			else {
				currentPage++;
				doSearchQuery();
			}
			break;
		}

	}

	private void put_act() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "build");
		urlParams.put("title", name.getText().toString());
		urlParams.put("info", sign.getText().toString());
		urlParams.put("time_begin", time.getText().toString());
		//TODO &
		urlParams.put("locate", address1.getText().toString() + "&" + address2.getText().toString());
		urlParams.put("lng", build_point.getLongitude() + "");
		urlParams.put("lat", build_point.getLatitude() + "");
		urlParams.put("city", customer.locationCity);

		// 发表活动
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						toast("发表成功");
						finish();
					}
					if (jsonObject.getString("status").equals("0"))
						Log.d("put_act", "空，发表失败");

					if (jsonObject.getString("status").equals("-2"))
						Log.d("put_act", "服务器错误，发表失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("put_act", error.getMessage(), error);
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) { // 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	/////////////////////////////////////////
	//////// 高德poi搜索 ,运动场所080100
	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {

		query = new PoiSearch.Query("运动场馆", "体育休闲服务", customer.locationCity);
		// keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
		// 共分为以下20种：汽车服务|汽车销售|
		// 汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
		// 住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
		// 金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
		// cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第?页
		PoiSearch poiSearch = new PoiSearch(this, query);
		poiSearch.setBound(new SearchBound(customer.lp, 30000));// 设置周边搜索的中心点以及区域,单位m
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
						// 加载到列表
						adapter.list.clear();
						adapter.list = poiItems;
						adapter.notifyDataSetChanged();

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
	}

	////////////////////////////////////////
	////// 内部类 列表适配器

	public class PopAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public List<PoiItem> list = new ArrayList<PoiItem>();

		public PopAdapter(Context context) {
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
			TextView snippet;
			TextView distance;
			LinearLayout item;

			convertView = inflater.inflate(R.layout.item_pop_pg, null);

			title = (TextView) convertView.findViewById(R.id.pop_title);
			snippet = (TextView) convertView.findViewById(R.id.pop_address);
			distance = (TextView) convertView.findViewById(R.id.pop_distance);
			item = (LinearLayout) convertView.findViewById(R.id.pop_item);

			title.setText((position + 1) + "." + poi.getTitle());
			snippet.setText(poi.getSnippet());
			distance.setText(poi.getDistance() + "m");

			item.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					build_point = poi.getLatLonPoint();
					address1.setText(poi.getTitle());
					address2.setText(poi.getSnippet());
					pop.dismiss();
				}
			});

			return convertView;
		}

	}
}
