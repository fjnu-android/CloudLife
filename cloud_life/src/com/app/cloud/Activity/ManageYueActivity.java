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
 * ͬ��Լ����
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

		initToolbar("�����");
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

		// ������Ϣ
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						toast("���ĳɹ�");
						manage_sign.setText(resign.getText().toString());
						resign.setText("");
						resign.setVisibility(View.INVISIBLE);
						sure.setVisibility(View.INVISIBLE);
					}
					if (jsonObject.getString("status").equals("0"))
						Log.d("get_Myact", "�գ�������Ϣʧ��");

					if (jsonObject.getString("status").equals("-2"))
						Log.d("get_Myact", "���������󣬸�����Ϣʧ��");

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

	private void delete() {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "delete");
		urlParams.put("aid", detail.aid);

		// ɾ���
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.activity, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						toast("ɾ���ɹ�");
						finish();
					}
					if (jsonObject.getString("status").equals("0"))
						Log.d("get_Myact", "�գ� ɾ���ʧ��");

					if (jsonObject.getString("status").equals("-2"))
						Log.d("get_Myact", "���������� ɾ���ʧ��");

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

	private void openDelete() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("�Ƿ�ɾ���û");
		dialog.setCancelable(false);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				delete();
			}
		});
		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	///////////////////////////
	//// ����
	private void openAMapNavi(final LatLng marker) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("�Ƿ����øߵµ�ͼ���е���");
		dialog.setCancelable(false);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startAMapNavi(marker);
			}
		});
		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	/**
	 * ����ߵµ�ͼ�������ܣ����û��װ�ߵµ�ͼ��������쳣���������쳣�д�������ߵµ�ͼapp������ҳ��
	 */
	public void startAMapNavi(LatLng marker) {
		// ���쵼������
		NaviPara naviPara = new NaviPara();
		// �����յ�λ��
		naviPara.setTargetPoint(marker);
		// ���õ������ԣ������Ǳ���ӵ��
		naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);
		try {
			// ����ߵµ�ͼ����
			AMapUtils.openAMapNavi(naviPara, getApplicationContext());
		} catch (AMapException e) {
			// ���û��װ������쳣����������ҳ��
			openDialog();
		}
	}

	private void openDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("���ֻ�δ��װ�ߵµ�ͼ���Ƿ����ز����е���");
		dialog.setCancelable(false);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AMapUtils.getLatestAMapApp(getApplicationContext());
			}
		});
		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	/**
	 * ��ȡ��ǰapp��Ӧ������
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
