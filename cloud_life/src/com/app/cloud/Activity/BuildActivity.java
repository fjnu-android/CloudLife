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
 * ����
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
	 * ����
	 */
	private int currentPage = 0;// ��ǰҳ�棬��0��ʼ����
	private int totalPage = 1;
	private PoiSearch.Query query;// Poi��ѯ������

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_build);
		mQueue = SingleRequestQueue.getRequestQueue();

		initToolbar("����");
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
		// ����layoutInflater���View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popup_playground, null);
		// ���������ַ����õ���Ⱥ͸߶� getWindow().getDecorView().getWidth()
		pop = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// ����popWindow��������ɵ������仰������ӣ�������true
		pop.setFocusable(true);
		// ���õ��������ض���
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
				toast("����������");
			else if (time.getText().toString().equals(""))
				toast("��ѡ��ʱ��");
			else if (sign.getText().toString().equals(""))
				toast("��ѡ�����");
			else if (address1.getText().toString().equals(""))
				toast("��ѡ���ص�");
			else
				put_act();

			break;
		case R.id.pop_up:
			if (currentPage == 0)
				toast("��ǰ���ǵ�һҳ");
			if (currentPage > 0) {
				currentPage--;
				doSearchQuery();
			}
			break;
		case R.id.pop_down:
			if (totalPage <= 1 || (currentPage + 1) == totalPage)
				toast("û����һҳ��");
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

		// ����
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						toast("����ɹ�");
						finish();
					}
					if (jsonObject.getString("status").equals("0"))
						Log.d("put_act", "�գ�����ʧ��");

					if (jsonObject.getString("status").equals("-2"))
						Log.d("put_act", "���������󣬷���ʧ��");

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

		if (!customer.getCookie().equals("")) { // �����������post����ʱ����cookie�ֶ�
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	/////////////////////////////////////////
	//////// �ߵ�poi���� ,�˶�����080100
	/**
	 * ��ʼ����poi����
	 */
	protected void doSearchQuery() {

		query = new PoiSearch.Query("�˶�����", "�������з���", customer.locationCity);
		// keyWord��ʾ�����ַ������ڶ���������ʾPOI�������ͣ�Ĭ��Ϊ��������񡢲�����������סլ
		// ����Ϊ����20�֣���������|��������|
		// ����ά��|Ħ�г�����|��������|�������|�������|�������з���|ҽ�Ʊ�������|
		// ס�޷���|�羰��ʤ|����סլ|�����������������|�ƽ��Ļ�����|��ͨ��ʩ����|
		// ���ڱ��շ���|��˾��ҵ|��·������ʩ|������ַ��Ϣ|������ʩ
		// cityCode��ʾPOI�������򣬣�������Դ����ַ��������ַ�������ȫ����ȫ����Χ�ڽ���������
		query.setPageSize(10);// ����ÿҳ��෵�ض�����poiitem
		query.setPageNum(currentPage);// ���ò��?ҳ
		PoiSearch poiSearch = new PoiSearch(this, query);
		poiSearch.setBound(new SearchBound(customer.lp, 30000));// �����ܱ����������ĵ��Լ�����,��λm
		poiSearch.setOnPoiSearchListener(this);// �������ݷ��صļ�����
		poiSearch.searchPOIAsyn();// ��ʼ����
	}

	/**
	 * POI�����ѯ�ص�����
	 */
	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {

	}

	/**
	 * POI��Ϣ��ѯ�ص�����
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// ����poi�Ľ��
				if (result.getQuery().equals(query)) {// �Ƿ���ͬһ��

					// ȡ����������poiitems�ж���ҳ
					List<PoiItem> poiItems = result.getPois();// ȡ�õ�һҳ��poiitem���ݣ�ҳ��������0��ʼ
					totalPage = result.getPageCount();

					if (poiItems != null && poiItems.size() > 0) {
						// ���ص��б�
						adapter.list.clear();
						adapter.list = poiItems;
						adapter.notifyDataSetChanged();

					} else {
						toast("����û���˶�����");
					}
				}
			} else {
				toast("����û���˶�����");
			}
		} else if (rCode == 27) {
			toast("δ֪������");
		} else if (rCode == 32) {
			toast("key ��Ȩ��֤ʧ�ܣ�����key�󶨵�sha1ֵ��packageName��apk�Ƿ��Ӧ");
		} else {
			toast("��������" + rCode);
		}
	}

	////////////////////////////////////////
	////// �ڲ��� �б�������

	public class PopAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public List<PoiItem> list = new ArrayList<PoiItem>();

		public PopAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		// listview�ж��ٸ�item
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
