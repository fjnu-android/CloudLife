package com.app.cloud.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Adapter.ShopAdapter;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Model.Shop;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 附近购买
 *
 */
public class ShopActivity extends BaseUiAuth {

	TextView address;

	// 其他活动结束此活动
	static Activity shopA;

	private ListView listView;
	private ArrayList<Shop> list;

	// 6个店铺的菜品
	static JSONArray a_o;
	static JSONArray b_o;
	static JSONArray c_o;
	static JSONArray d_o;
	static JSONArray e_o;
	static JSONArray f_o;

	RequestQueue mQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		mQueue = SingleRequestQueue.getRequestQueue();

		initToolbar("附近购<模拟>");

		shopA = this;

		initView();

		getStore();
	}

	private void initView() {
		address = (TextView) findViewById(R.id.shop_address);
		address.setText(customer.location);

		listView = (ListView) findViewById(R.id.shop_list);

		// TODO
		list = new ArrayList<Shop>();

		Shop a = new Shop("食尚宅配", "#FF0000",0);
		Shop b = new Shop("有家厨房", "#FFA500",1);
		Shop c = new Shop("三言两语", "#FFFF00",2);
		Shop d = new Shop("饭米粒", "#00FF00",3);
		Shop e = new Shop("盘中餐", "#008B8B",4);
		Shop f = new Shop("一碗情深", "#9B30FF",5);

		list.add(a);
		list.add(b);
		list.add(c);
		list.add(d);
		list.add(e);
		list.add(f);

		listView.setAdapter(new ShopAdapter(this, list));

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Bundle bundle = new Bundle() ;
				bundle.putSerializable("shop", list.get(position));
				overlay(MerchantActivity.class,bundle);
			}
		});
	}

	private void getStore() {
		HashMap<String, String> urlParams = new HashMap<String, String>();

		// 获取附近
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getStoreMenu, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject json) {
				try {
					a_o = new JSONArray();
					a_o = json.getJSONArray("store").getJSONObject(0).getJSONArray("menu");

					b_o = new JSONArray();
					b_o = json.getJSONArray("store").getJSONObject(1).getJSONArray("menu");

					c_o = new JSONArray();
					c_o = json.getJSONArray("store").getJSONObject(2).getJSONArray("menu");

					d_o = new JSONArray();
					d_o = json.getJSONArray("store").getJSONObject(3).getJSONArray("menu");

					e_o = new JSONArray();
					e_o = json.getJSONArray("store").getJSONObject(4).getJSONArray("menu");

					f_o = new JSONArray();
					f_o = json.getJSONArray("store").getJSONObject(5).getJSONArray("menu");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("get_Store", error.getMessage(), error);
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) { // 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

}
