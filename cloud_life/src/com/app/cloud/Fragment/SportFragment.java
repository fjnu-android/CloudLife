package com.app.cloud.Fragment;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.maps2d.AMapException;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.NaviPara;
import com.amap.api.services.core.LatLonPoint;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Activity.BuildActivity;
import com.app.cloud.Activity.ManageActivity;
import com.app.cloud.Activity.PeopleActivity;
import com.app.cloud.Activity.PlaygroundActivity;
import com.app.cloud.Adapter.YueAdapter;
import com.app.cloud.Base.BaseFragment;
import com.app.cloud.Base.C;
import com.app.cloud.Model.Yue;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 运动(同城约)
 *
 */
public class SportFragment extends BaseFragment implements View.OnClickListener {

	RequestQueue mQueue;

	TextView yundong_grade;
	TextView yundong_content;

	LinearLayout s_people;
	LinearLayout s_address;
	LinearLayout s_publish;
	LinearLayout s_manage;

	ListView listView;
	YueAdapter adapter;

	Yue detail;

	PopupWindow pop;
	View popView;

	Button pop_back;
	Button pop_join;
	TextView pop_name;
	TextView pop_time;
	ImageView pop_state;
	TextView pop_address;
	Button pop_navi;
	TextView pop_sign;
	TextView pop_one;
	TextView pop_ones;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mQueue = SingleRequestQueue.getRequestQueue();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sport, container, false);

		initView(view);
		initPop();

		return view;
	}

	private void initView(View view) {
		yundong_grade = (TextView) view.findViewById(R.id.yundong_grade);
		yundong_content = (TextView) view.findViewById(R.id.yundong_content);
		
		//TODO
		/**
		// 天气延时5秒
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				JSONObject life;
				JSONArray info;
				try {
					life = new JSONObject();
					// TODO 经常错
					life = customer.weather.getJSONObject("life").getJSONObject("info");
					info = new JSONArray();
					info = life.getJSONArray("yundong");
					yundong_grade.setText(info.getString(0));
					yundong_content.setText(info.getString(1));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, 3500);
		 
		*/ 
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// 定位需要时间
				adapter.list.clear();
				getAct();
			}
		}, 2000);

		s_people = (LinearLayout) view.findViewById(R.id.s_people);
		s_address = (LinearLayout) view.findViewById(R.id.s_address);
		s_publish = (LinearLayout) view.findViewById(R.id.s_publish);
		s_manage = (LinearLayout) view.findViewById(R.id.s_manage);

		s_people.setOnClickListener(this);
		s_address.setOnClickListener(this);
		s_publish.setOnClickListener(this);
		s_manage.setOnClickListener(this);

		listView = (ListView) view.findViewById(R.id.sport_yue_list);
		adapter = new YueAdapter(getActivity());
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO 列表显示到popup
				showPop(adapter.list.get(position).aid);
			}
		});

	}

	@SuppressLint("InflateParams")
	private void initPop() {
		// 利用layoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popup_yue, null);
		// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
		pop = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		pop.setFocusable(true);
		// 设置弹出和隐藏动画
		pop.setAnimationStyle(R.style.popBottom_anim);

		pop_back = (Button) popView.findViewById(R.id.pop_yue_back);
		pop_join = (Button) popView.findViewById(R.id.pop_yue_join);
		pop_back.setOnClickListener(this);
		pop_join.setOnClickListener(this);

		pop_name = (TextView) popView.findViewById(R.id.pop_yue_name);
		pop_time = (TextView) popView.findViewById(R.id.pop_yue_time);
		pop_state = (ImageView) popView.findViewById(R.id.pop_yue_state);
		pop_address = (TextView) popView.findViewById(R.id.pop_yue_address);
		pop_navi = (Button) popView.findViewById(R.id.pop_yue_navi);
		pop_navi.setOnClickListener(this);
		pop_sign = (TextView) popView.findViewById(R.id.pop_yue_sign);
		pop_one = (TextView) popView.findViewById(R.id.pop_yue_one);
		pop_ones = (TextView) popView.findViewById(R.id.pop_yue_ones);

	}

	private void getAct() {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "city_act");
		urlParams.put("city", customer.locationCity);

		// 获取城市所有活动
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("state").equals("1")) {
						initData(jsonObject);
					}
					if (jsonObject.getString("state").equals("0"))
						Log.d("get_act", "空，获取活动失败");

					if (jsonObject.getString("state").equals("-2"))
						Log.d("get_act", "服务器错误，获取活动失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("get_act", error.getMessage(), error);
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) { // 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void initData(JSONObject json) {

		JSONObject yue;
		JSONArray yues;
		Yue y;

		try {
			yue = new JSONObject();
			yue = json.getJSONObject("activity");

			y = new Yue();
			y.aid = yue.getString("aid");
			y.name = yue.getString("title");
			y.time = yue.getString("time");
			y.address = yue.getString("locate").substring(0, yue.getString("locate").indexOf("&"));
			y.state = yue.getString("is_over").equals("false");

			adapter.list.add(y);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			yues = new JSONArray();
			yues = json.getJSONArray("activity");

			for (int i = 0; i < yues.length(); i++) {

				yue = new JSONObject();
				yue = yues.getJSONObject(i);

				y = new Yue();
				y.aid = yue.getString("aid");
				y.name = yue.getString("title");
				y.time = yue.getString("time");
				y.address = yue.getString("locate").substring(0, yue.getString("locate").indexOf("&"));
				y.state = yue.getString("is_over").equals("false");

				adapter.list.add(y);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
	}

	private void showPop(String aid) {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "detail");
		urlParams.put("aid", aid);

		// 获取活动
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("state").equals("1")) {
						newPop(jsonObject);
					}
					if (jsonObject.getString("state").equals("0"))
						Log.d("detail", "空，获取详细失败");

					if (jsonObject.getString("state").equals("-2"))
						Log.d("detail", "服务器错误，获取详细失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("detail", error.getMessage(), error);
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) { // 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void newPop(JSONObject json) throws JSONException {
		detail = new Yue();

		detail.aid = json.getString("aid");
		detail.name = json.getString("title");
		detail.address = json.getString("loc_name").replace("&", "\n");
		detail.time = json.getString("time_begin");
		detail.info = json.getString("info");
		detail.state = json.getString("is_over").equals("false");
		// string转double
		detail.lat = Double.valueOf(json.getString("lat")).doubleValue();
		detail.lng = Double.valueOf(json.getString("lng")).doubleValue();
		detail.publish_man = json.getString("who_build");

		JSONObject one;
		JSONArray ones;

		try {
			one = new JSONObject();
			one = json.getJSONObject("user");
			detail.join_man = one.getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			ones = new JSONArray();
			ones = json.getJSONArray("user");

			for (int i = 0; i < ones.length(); i++) {
				one = new JSONObject();
				one = ones.getJSONObject(i);

				detail.join_man += one.getString("name") + "  ";
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		pop_name.setText(detail.name);
		;
		pop_time.setText(detail.time);

		if (detail.state != true)
			pop_state.setImageResource(R.drawable.yue_false);

		pop_address.setText(detail.address);
		pop_sign.setText(detail.info);
		pop_one.setText(detail.publish_man);
		pop_ones.setText(detail.join_man);
		pop.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);

	}

	private void joinAct() {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "join");
		urlParams.put("aid", detail.aid);

		// 加入活动
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						toast("加入成功");
					}
					if (jsonObject.getString("status").equals("0"))
						toast("你已经加入该活动");

					if (jsonObject.getString("status").equals("-2"))
						Log.d("detail", "服务器错误，加入失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("detail", error.getMessage(), error);
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) { // 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.s_people:
			overlay(PeopleActivity.class);
			break;
		case R.id.s_address:
			overlay(PlaygroundActivity.class);
			break;
		case R.id.s_publish:
			overlay(BuildActivity.class);
			break;
		case R.id.s_manage:
			overlay(ManageActivity.class);
			break;
		case R.id.pop_yue_back:
			pop.dismiss();
			break;
		case R.id.pop_yue_join:
			// TODO 判断是不是自己的
			if (detail.publish_man.equals(customer.getPhone()))
				toast("不能加入自己的活动");
			else
				joinAct();
			break;
		case R.id.pop_yue_navi:
			openAMapNavi(new LatLng(detail.lat, detail.lng));
			break;
		}

	}

	///////////////////////////
	//// 导航
	private void openAMapNavi(final LatLng marker) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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
			AMapUtils.openAMapNavi(naviPara, getActivity().getApplicationContext());
		} catch (AMapException e) {
			// 如果没安装会进入异常，调起下载页面
			openDialog();
		}
	}

	private void openDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setMessage("你手机未安装高德地图，是否下载并进行导航");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AMapUtils.getLatestAMapApp(getActivity().getApplicationContext());
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
			packageManager = getActivity().getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(getActivity().getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		return applicationName;
	}

}
