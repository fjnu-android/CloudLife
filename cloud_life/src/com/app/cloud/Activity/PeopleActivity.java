package com.app.cloud.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Util.BitmapCache;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ��������
 *
 */
public class PeopleActivity extends BaseUiAuth {

	ListView listView;
	PeopleAdapter adapter;

	RequestQueue mQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people);
		mQueue = SingleRequestQueue.getRequestQueue();

		initToolbar("��������(" + customer.locationCity + ")");

		initView();

		getPeople();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.people_list);
		adapter = new PeopleAdapter(this);
		listView.setAdapter(adapter);
	}

	private void getPeople() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "city_user");
		urlParams.put("city", customer.locationCity);

		// ��ȡ��������
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("state").equals("1")) {
						getData(jsonObject);
					}
					if (jsonObject.getString("state").equals("0"))
						Log.d("get_people", "�գ���ȡ��������ʧ��");

					if (jsonObject.getString("state").equals("-2"))
						Log.d("get_people", "���������󣬻�ȡ��������ʧ��");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("get_people", error.getMessage(), error);
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) { // �����������post����ʱ����cookie�ֶ�
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void getData(JSONObject json) {

		JSONObject user = null;
		JSONArray users;
		People p;

		// �������
		try {
			user = new JSONObject();
			user = json.getJSONObject("user");

			p = new People();
			p.name = user.getString("name");
			p.sign = user.getString("sign");

			try {
				p.icon = user.getString("icon_url");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			adapter.list.add(p);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// �������
		try {
			users = new JSONArray();
			users = json.getJSONArray("user");

			for (int i = 0; i < users.length(); i++) {

				p = new People();
				user = new JSONObject();
				user = users.getJSONObject(i);

				p.name = user.getString("name");
				p.sign = user.getString("sign");

				try {
					p.icon = user.getString("icon_url");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				adapter.list.add(p);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		adapter.notifyDataSetChanged();
	}

	////////////////////////////////////////
	////// �ڲ��� �б�������

	public class PeopleAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public List<People> list = new ArrayList<People>();

		public PeopleAdapter(Context context) {
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

			People p = list.get(position);

			ImageView icon;
			TextView name;
			TextView sign;

			convertView = inflater.inflate(R.layout.item_people_around, null);

			icon = (ImageView) convertView.findViewById(R.id.people_icon);
			name = (TextView) convertView.findViewById(R.id.people_name);
			sign = (TextView) convertView.findViewById(R.id.people_sign);

			if (!p.icon.equals("")) {
				// �жϱ����Ƿ����
				File f = new File(C.dir.icon + p.icon.substring(10));
				if (f.exists()) {
					// sd������ͷ����뻺��
					// "#W100#H100"Ϊ����ǰ׺
					BitmapCache.instance().putBitmap("#W100#H100" + C.api.base + "/img" + p.icon,
							decodeSampledBitmapFromFile(C.dir.icon + p.icon.substring(10), 100, 100));
				}

				// ����ͷ��
				ImageListener imageListener = ImageLoader.getImageListener(icon, R.drawable.icon_profile,
						R.drawable.icon_profile);
				ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.instance());
				imageLoader.get(C.api.base + "/img" + p.icon, imageListener, 100, 100);
			}

			name.setText(p.name);
			sign.setText(p.sign);

			return convertView;
		}

		public Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

			// ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			// �������涨��ķ�������inSampleSizeֵ
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
			// ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(path, options);
		}

		public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
			// ԴͼƬ�ĸ߶ȺͿ��
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;
			if (height > reqHeight || width > reqWidth) {
				// �����ʵ�ʿ�ߺ�Ŀ���ߵı���
				final int heightRatio = Math.round((float) height / (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);
				// ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�
				// һ��������ڵ���Ŀ��Ŀ�͸ߡ�
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}
			return inSampleSize;
		}
	}

	/////////////////////////////////////////////
	//// �û���

	public class People {

		public String icon = "";
		public String name;
		public String sign;

		public People() {

		}

	}
}
