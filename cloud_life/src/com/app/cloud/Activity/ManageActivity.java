package com.app.cloud.Activity;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Adapter.YueAdapter;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Model.Yue;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ͬ��Լ����
 *
 */
public class ManageActivity extends BaseUiAuth {

	ListView listView;
	YueAdapter adapter;	

	RequestQueue mQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		mQueue = SingleRequestQueue.getRequestQueue();

		initToolbar("�ҵĻ");
		initView();
		
	}
	
	public void onStart(){
		super.onStart();
		
		adapter.list.clear();
		get_MyAct();
	}

	private void initView() {

		listView = (ListView) findViewById(R.id.manage_list);
		adapter = new YueAdapter(this);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showPop(adapter.list.get(position).aid);
			}
		});

	}

	private void get_MyAct() {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "user_act");

		// ��ȡ�û����л
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("state").equals("1")) {
						initData(jsonObject);
					}
					if (jsonObject.getString("state").equals("0"))
						Log.d("get_Myact", "�գ���ȡ��������ʧ��");

					if (jsonObject.getString("state").equals("-2"))
						Log.d("get_Myact", "���������󣬻�ȡ��������ʧ��");

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

		if (!customer.getCookie().equals("")) { // �����������post����ʱ����cookie�ֶ�
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

		// ��ȡ�
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("state").equals("1")) {
						newPop(jsonObject);
					}
					if (jsonObject.getString("state").equals("0"))
						Log.d("detail", "�գ���ȡ��ϸʧ��");

					if (jsonObject.getString("state").equals("-2"))
						Log.d("detail", "���������󣬻�ȡ��ϸʧ��");

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

		if (!customer.getCookie().equals("")) { // �����������post����ʱ����cookie�ֶ�
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void newPop(JSONObject json) throws JSONException {
		Yue detail = new Yue();

		detail.aid = json.getString("aid");
		detail.name = json.getString("title");
		detail.address = json.getString("loc_name").replace("&", "\n");
		detail.time = json.getString("time_begin");
		detail.info = json.getString("info");
		detail.state = json.getString("is_over").equals("false");
		// stringתdouble
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
			
			for(int i=0;i<ones.length();i++){
				one = new JSONObject();
				one = ones.getJSONObject(i);
				
				detail.join_man += one.getString("name")+"  ";
			}	
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("yue", detail);
		
		this.overlay(ManageYueActivity.class, bundle);
		
	}
}
