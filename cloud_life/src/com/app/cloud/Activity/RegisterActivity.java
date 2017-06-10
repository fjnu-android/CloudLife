package com.app.cloud.Activity;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Base.BaseAuth;
import com.app.cloud.Base.BaseUi;
import com.app.cloud.Base.C;
import com.app.cloud.Model.User;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * ע��
 *
 */
public class RegisterActivity extends BaseUi implements View.OnClickListener {

	private Toolbar mToolbar;

	TextView title;
	Button register;

	EditText user;
	EditText passwd;
	EditText repasswd;

	protected static User customer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// ��ȡΨһ�û�ʵ��
		customer = BaseAuth.getCustomer();

		initToolbar();
		init();
	}

	public void initToolbar() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("ע��");
		mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void init() {

		register = (Button) findViewById(R.id.register_btn);
		register.setOnClickListener(this);

		user = (EditText) findViewById(R.id.register_user);
		passwd = (EditText) findViewById(R.id.register_password);
		repasswd = (EditText) findViewById(R.id.register_repassword);

	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.register_btn:
				if (user.length() != 11) {
					toast("������11λ�ֻ�����");
					break;
				}
				if (passwd.length() < 6) {
					toast("������6λ��������");
					break;
				}
				if (!passwd.getText().toString().equals(repasswd.getText().toString())) {
					toast("���벻һ��");
					break;
				}
				openLoad("����ע��...");
				doRegister();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void savedata() {

		SharedPreferences.Editor editor = getSharedPreferences(C.dir.shared, MODE_PRIVATE).edit();
		editor.putString("phone", user.getText().toString());
		editor.putString("password", passwd.getText().toString());
		editor.putBoolean("defLogin", true);
		editor.commit();

		customer.setPhone(user.getText().toString());
		customer.setPassword(passwd.getText().toString());
		customer.setLogin(true);

	}

	private void doRegister() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("phone", user.getText().toString());
		urlParams.put("password", passwd.getText().toString());

		RequestQueue mQueue = SingleRequestQueue.getRequestQueue();

		// ע������
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.register, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				// �ӷ�������Ӧresponse�е�jsonObject��ȡ��cookie��ֵ���浽userʵ����cookie
				try {
					customer.setCookie(jsonObject.getString("Cookie"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					closeLoad();
					if (jsonObject.getString("status").equals("1")) {
						toast("ע��ɹ�");
						savedata();
						toMain();
					}
					if (jsonObject.getString("status").equals("-1"))
						toast("�ֻ�����ע�ᣬע��ʧ��");

					if (jsonObject.getString("status").equals("-2"))
						toast("��������ע��ʧ��");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				closeLoad();
				Log.e("TAG", error.getMessage(), error);
				toast("�������ע��ʧ�ܣ�");
			}
		}, urlParams);
		/*
		 * if(!customer.getCookie().equals("")){ //�����������post����ʱ����cookie�ֶ�
		 * post.setSendCookie(customer.getCookie()); }
		 */
		mQueue.add(post);
	}

	public void toMain() {
		this.forward(MainActivity.class);
		LoginActivity.LA.finish();
	}


}
