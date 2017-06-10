package com.app.cloud.Activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.maps2d.AMapException;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.NaviPara;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Model.Yue;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 同城约管理
 *
 */
public class ManageYueActivity extends BaseUiAuth implements View.OnClickListener {

	Button manage_change;
	Button manage_delete;
	TextView manage_name;
	TextView manage_time;
	ImageView manage_state;
	TextView manage_address;
	Button manage_navi;
	TextView manage_sign;
	TextView manage_one;
	TextView manage_ones;

	EditText resign;
	Button sure;

	RequestQueue mQueue;

	Yue detail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yue_manage);
		mQueue = SingleRequestQueue.getRequestQueue();

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		detail = (Yue) bundle.getSerializable("yue");

		initToolbar("活动管理");
		initView();

	}

	private void initView() {

		manage_change = (Button) findViewById(R.id.manage_change);
		manage_delete = (Button) findViewById(R.id.manage_delete);
		manage_change.setOnClickListener(this);
		manage_delete.setOnClickListener(this);

		manage_name = (TextView) findViewById(R.id.manage_name);
		manage_time = (TextView) findViewById(R.id.manage_time);
		manage_state = (ImageView) findViewById(R.id.manage_state);
		manage_address = (TextView) findViewById(R.id.manage_address);
		manage_navi = (Button) findViewById(R.id.manage_navi);
		manage_navi.setOnClickListener(this);
		manage_sign = (TextView) findViewById(R.id.manage_sign);
		manage_one = (TextView) findViewById(R.id.manage_one);
		manage_ones = (TextView) findViewById(R.id.manage_ones);

		resign = (EditText) findViewById(R.id.manage_resign);
		sure = (Button) findViewById(R.id.manage_sure);
		sure.setOnClickListener(this);

		manage_name.setText(detail.name);
		;
		manage_time.setText(detail.time);

		if (detail.state != true)
			manage_state.setImageResource(R.drawable.yue_false);

		manage_address.setText(detail.address);

		manage_sign.setText(detail.info);

		manage_one.setText(detail.publish_man); 
		manage_ones.setText(detail.join_man);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.manage_change:
			resign.setVisibility(View.VISIBLE);
			sure.setVisibility(View.VISIBLE);
			break;
		case R.id.manage_delete:
			openDelete();			
			break;
		case R.id.manage_navi:
			openAMapNavi(new LatLng(detail.lat,detail.lng));
			break;
		case R.id.manage_sure:
			changeInfo();
			break;
		}

	}

	private void changeInfo() {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "update");
		urlParams.put("aid", detail.aid);
		urlParams.put("info", resign.getText().toString());

		// 更改信息
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						toast("更改成功");
						manage_sign.setText(resign.getText().toString());
						resign.setText("");
						resign.setVisibility(View.INVISIBLE);
						sure.setVisibility(View.INVISIBLE);
					}
					if (jsonObject.getString("status").equals("0"))
						Log.d("get_Myact", "空，更改信息失败");

					if (jsonObject.getString("status").equals("-2"))
						Log.d("get_Myact", "服务器错误，更改信息失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("get_Myact", error.getMessage(), error);
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) { // 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void delete() {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "delete");
		urlParams.put("aid", detail.aid);

		// 删除活动
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						toast("删除成功");
						finish();
					}
					if (jsonObject.getString("status").equals("0"))
						Log.d("get_Myact", "空， 删除活动失败");

					if (jsonObject.getString("status").equals("-2"))
						Log.d("get_Myact", "服务器错误， 删除活动失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("get_Myact", error.getMessage(), error);
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) { // 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void openDelete() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("是否删除该活动");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				delete();
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	///////////////////////////
	//// 导航
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
}
