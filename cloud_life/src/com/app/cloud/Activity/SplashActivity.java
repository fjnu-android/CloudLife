package com.app.cloud.Activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.view.animation.textsurface.TextSurface;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Base.BaseAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Model.User;
import com.app.cloud.Ui.CookieThumper;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/***
 * @app cloud life
 * @author Raindus
 * @school FJNU
 * @time 2015.12.27 ��ӭ����,���
 */

public class SplashActivity extends Activity {

	protected static User customer = null;

	@SuppressWarnings("unused")
	private Context context;
	private TextSurface tv_surface;

	SharedPreferences pref;

	RequestQueue mQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		mQueue = SingleRequestQueue.getRequestQueue();

		pref = getSharedPreferences(C.dir.shared, MODE_PRIVATE);

		// ��ȡΨһ�û�ʵ��
		customer = BaseAuth.getCustomer();

		context = SplashActivity.this;

		tv_surface = (TextSurface) findViewById(R.id.tv_surface);
		tv_surface.postDelayed(new Runnable() {
			@Override
			public void run() {
				showAnim();
			}
		}, 500);

	}

	private void showAnim() {
		tv_surface.reset();
		CookieThumper.play(tv_surface, getAssets());

		new Handler().postDelayed(new Runnable() {

			// �жϵ�¼״̬
			@Override
			public void run() {

				//ȷ��������·��·��
				if( pref.getString("url","false")!= "false")
					C.api.base = pref.getString("url","");
				
				if (!pref.getBoolean("defLogin", false))
					toLogin();
				else
					autoLogin();

			}
		}, 3500);

	}

	private void toLogin() {
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	private void toMain() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();
	}

	private void autoLogin() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("phone", pref.getString("phone", ""));
		urlParams.put("password", pref.getString("password", ""));

		// ��½����
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.login, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				// �ӷ�������Ӧresponse�е�jsonObject��ȡ��cookie��ֵ���浽userʵ����cookie
				try {
					customer.setCookie(jsonObject.getString("Cookie"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if (jsonObject.getString("status").equals("1")) {
						toast("�Զ���½�ɹ�");
						saveData(jsonObject);
						toMain();
					}
					if (jsonObject.getString("status").equals("-3")) {
						toast("�������쳣���Զ���½ʧ��");
						toLogin();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				toast("��������Զ���½ʧ�ܣ�");
				toLogin();
			}
		}, urlParams);

		mQueue.add(post);
	}

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void saveData(JSONObject json) throws JSONException {

		customer.setPhone(pref.getString("phone", ""));
		customer.setPassword(pref.getString("password", ""));
		customer.setLogin(true);

		customer.setIcon(json.getString("img"));
		customer.setName(json.getString("name"));
		customer.setSign(json.getString("sign"));
		customer.setBirth(json.getString("birthday"));
		customer.setCity(json.getString("city"));
		customer.setWeight(json.getString("weight"));
		customer.setHeight(json.getString("height"));
		customer.setSex(json.getString("sex"));
		customer.setBodytype(json.getString("bodyType"));
		customer.setWork(json.getString("work"));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
