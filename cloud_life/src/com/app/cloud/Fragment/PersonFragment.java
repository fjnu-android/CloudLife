package com.app.cloud.Fragment;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Activity.QuanDetailActivity;
import com.app.cloud.Adapter.PersonAdapter;
import com.app.cloud.Base.BaseFragment;
import com.app.cloud.Base.C;
import com.app.cloud.Model.QuanZi;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;
import com.yalantis.taurus.PullToRefreshView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 圈子个人
 *
 */
public class PersonFragment extends BaseFragment {

	// 下拉 1秒动画
	public static final int REFRESH_DELAY = 1000;
	private PullToRefreshView mPullToRefreshView;

	int firstLocation;

	private ListView listView;
	private PersonAdapter adapter;

	private View footView;
	Button load;

	RequestQueue mQueue;

	public PersonFragment newInstance() {
		PersonFragment mFragment = new PersonFragment();
		return mFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_person, container, false);

		initView(view);

		refreshPersonData();

		return view;
	}

	private void initView(View view) {
		mQueue = SingleRequestQueue.getRequestQueue();

		mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
		mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshPersonData();
				mPullToRefreshView.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPullToRefreshView.setRefreshing(false);
					}
				}, REFRESH_DELAY);
			}
		});
		@SuppressWarnings("static-access")
		LayoutInflater layoutInflater = (LayoutInflater) getActivity()
				.getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		footView = layoutInflater.inflate(R.layout.listview_footer, null);

		load = (Button) footView.findViewById(R.id.footer_load);

		listView = (ListView) view.findViewById(R.id.person_list);
		adapter = PersonAdapter.instance(getActivity());
		listView.addFooterView(footView);
		listView.setAdapter(adapter);

		// 加载更多
		load.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadPersonData();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("quanzi", adapter.list.get(position));
				overlay(QuanDetailActivity.class, bundle);
			}

		});

	}

	// 刷新个人数据
	private void refreshPersonData() {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("begin", "0");
		urlParams.put("end", "10");

		// 刷新请求
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getPersonData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							if (jsonObject.getString("status").equals("1")) {
								refreshData(jsonObject);
								firstLocation = 10;
							}
							if (jsonObject.getString("status").equals("-1"))
								toast("cookie失效，刷新失败");

							if (jsonObject.getString("status").equals("2"))
								toast("已经是最新的了");

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						toast("网络错误，刷新失败！");
					}
				}, urlParams);

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void refreshData(JSONObject json) {
		adapter.list.clear();

		JSONObject quan;
		JSONArray quans;
		QuanZi data;

		try {
			quan = json.getJSONObject("data");
			data = new QuanZi();
			data.setName(customer.getName());
			data.setTime(quan.getString("time").substring(0, 16));
			data.setContent(quan.getString("text"));
			data.setImage(quan.getString("iamge"));
			data.setSid(quan.getString("sid"));
			adapter.list.add(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			quans = json.getJSONArray("data");
			for (int i = 0; i < quans.length(); i++) {
				quan = new JSONObject();
				quan = quans.getJSONObject(i);

				data = new QuanZi();
				data.setName(customer.getName());
				data.setTime(quan.getString("time").substring(0, 16));
				data.setContent(quan.getString("text"));
				data.setImage(quan.getString("iamge"));
				data.setSid(quan.getString("sid"));
				adapter.list.add(data);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
	}

	// 加载全国数据
	private void loadPersonData() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("begin", firstLocation + "");
		urlParams.put("end", "10");

		// 加载请求
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getPersonData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							if (jsonObject.getString("status").equals("1")) {
								loadData(jsonObject);
								firstLocation += 10;
							}
							if (jsonObject.getString("status").equals("-1"))
								toast("cookie失效，加载失败");

							if (jsonObject.getString("status").equals("2"))
								toast("已经是最新的了");

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						toast("网络错误，加载失败！");
					}
				}, urlParams);

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void loadData(JSONObject json) throws JSONException {

		JSONObject quan;
		JSONArray quans;
		QuanZi data;

		try {
			quan = json.getJSONObject("data");
			data = new QuanZi();
			data.setName(customer.getName());
			data.setTime(quan.getString("time").substring(0, 16));
			data.setContent(quan.getString("text"));
			data.setImage(quan.getString("iamge"));
			data.setSid(quan.getString("sid"));
			adapter.list.add(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			quans = json.getJSONArray("data");
			for (int i = 0; i < quans.length(); i++) {
				quan = new JSONObject();
				quan = quans.getJSONObject(i);

				data = new QuanZi();
				data.setName(customer.getName());
				data.setTime(quan.getString("time").substring(0, 16));
				data.setContent(quan.getString("text"));
				data.setImage(quan.getString("iamge"));
				data.setSid(quan.getString("sid"));
				adapter.list.add(data);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();

	}

}
