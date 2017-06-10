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
import com.app.cloud.Adapter.QuanAdapter;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * Ȧ��ȫ��
 *
 */
public class CountryFragment extends BaseFragment {

	// ���� 1�붯��
	public static final int REFRESH_DELAY = 1000;
	private PullToRefreshView mPullToRefreshView;

	int firstLocation;

	private ListView listView;
	private QuanAdapter adapter;

	private View footView;
	Button load;

	RequestQueue mQueue;

	// ʱ���
	Long ts = null;

	public CountryFragment newInstance() {
		CountryFragment mFragment = new CountryFragment();
		return mFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_country, container, false);

		initView(view);

		refreshSocialData();
		return view;
	}

	private void initView(View view) {
		mQueue = SingleRequestQueue.getRequestQueue();

		mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
		mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				ts = System.currentTimeMillis();
				refreshSocialData();
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

		listView = (ListView) view.findViewById(R.id.country_list);
		adapter = new QuanAdapter(getActivity());
		listView.addFooterView(footView);
		listView.setAdapter(adapter);

		// ���ظ���
		load.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadSocialData();
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

	// ˢ��ȫ������
	private void refreshSocialData() {
		if (ts == null)
			ts = System.currentTimeMillis();

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("city", "");
		urlParams.put("begin", "0");
		urlParams.put("end", "10");
		urlParams.put("ts", ts.toString());

		Log.d("ȫ��ʱ���", ts.toString());

		// ˢ������
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getSocialData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							if (jsonObject.getString("state").equals("1")) {
								refreshData(jsonObject);
								firstLocation = 10;
							}
							if (jsonObject.getString("state").equals("-1"))
								toast("cookieʧЧ��ˢ��ʧ��");

							if (jsonObject.getString("state").equals("2"))
								toast("�Ѿ������µ���");

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						toast("�������ˢ��ʧ�ܣ�");
					}
				}, urlParams);

		if (!customer.getCookie().equals("")) {
			// �����������post����ʱ����cookie�ֶ�
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void refreshData(JSONObject json) throws JSONException {
		adapter.list.clear();

		JSONObject quan;
		JSONArray quans;
		QuanZi data;
		if (json.getString("count").equals("0")) {
			return;
		} else if (json.getString("count").equals("1")) {
			quan = json.getJSONObject("data");
			data = new QuanZi();
			try {
				data.setIcon(quan.getString("icon"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			data.setName(quan.getString("name"));
			data.setCity(quan.getString("city"));
			data.setTime(quan.getString("time").substring(0, 16));
			data.setContent(quan.getString("text"));
			data.setImage(quan.getString("img"));
			adapter.list.add(data);
		} else {
			quans = json.getJSONArray("data");
			for (int i = 0; i < quans.length(); i++) {
				quan = new JSONObject();
				quan = quans.getJSONObject(i);

				data = new QuanZi();
				try {
					data.setIcon(quan.getString("icon"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				data.setName(quan.getString("name"));
				data.setCity(quan.getString("city"));
				data.setTime(quan.getString("time").substring(0, 16));
				data.setContent(quan.getString("text"));
				data.setImage(quan.getString("img"));
				adapter.list.add(data);
			}
		}
		adapter.notifyDataSetChanged();
	}

	// ����ȫ������
	private void loadSocialData() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("city", "");
		urlParams.put("begin", firstLocation + "");
		urlParams.put("end", "10");
		urlParams.put("ts", ts.toString());
		Log.d("firstLocation", firstLocation + "");
		Log.d("ȫ��ʱ���", ts.toString());

		// ��������
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getSocialData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							if (jsonObject.getString("state").equals("1")) {
								loadData(jsonObject);
								firstLocation += 10;
								Log.d("firstLocation", firstLocation + "");
							}
							if (jsonObject.getString("state").equals("-1"))
								toast("cookieʧЧ������ʧ��");

							if (jsonObject.getString("state").equals("2"))
								toast("�Ѿ������µ���");

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						toast("������󣬼���ʧ�ܣ�");
					}
				}, urlParams);

		if (!customer.getCookie().equals("")) {
			// �����������post����ʱ����cookie�ֶ�
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void loadData(JSONObject json) throws JSONException {

		JSONObject quan;
		JSONArray quans;
		QuanZi data;
		if (json.getString("count").equals("0")) {
			return;
		} else if (json.getString("count").equals("1")) {
			quan = json.getJSONObject("data");
			data = new QuanZi();
			try {
				data.setIcon(quan.getString("icon"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			data.setName(quan.getString("name"));
			data.setCity(quan.getString("city"));
			data.setTime(quan.getString("time").substring(0, 16));
			data.setContent(quan.getString("text"));
			data.setImage(quan.getString("img"));
			adapter.list.add(data);
		} else {
			quans = json.getJSONArray("data");
			for (int i = 0; i < quans.length(); i++) {
				quan = new JSONObject();
				quan = quans.getJSONObject(i);

				data = new QuanZi();
				try {
					data.setIcon(quan.getString("icon"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				data.setName(quan.getString("name"));
				data.setCity(quan.getString("city"));
				data.setTime(quan.getString("time").substring(0, 16));
				data.setContent(quan.getString("text"));
				data.setImage(quan.getString("img"));
				adapter.list.add(data);
			}
		}
		adapter.notifyDataSetChanged();

	}

}
