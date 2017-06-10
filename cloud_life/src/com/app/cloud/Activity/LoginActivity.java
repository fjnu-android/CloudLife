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
import com.app.cloud.Dialog.ChangeUrlDialog;
import com.app.cloud.Dialog.ChangeUrlDialog.OnChangeUrlListener;
import com.app.cloud.Model.User;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 登陆
 *
 */
public class LoginActivity extends BaseUi implements View.OnClickListener {

	private long mExitTime = 0;

	private Toolbar mToolbar;

	Button login;
	Button toRegister;

	EditText user;
	EditText passwd;
	
	TextView change;
	
	//注册活动关闭该活动
	static Activity LA;
	
	protected static User customer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		LA = this;
		
		// 获取唯一用户实例
		customer = BaseAuth.getCustomer();

		initToolbar();

		init();

	}

	public void initToolbar() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("登陆");
		mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}

	public void init() {

		login = (Button) findViewById(R.id.login_btn);
		login.setOnClickListener(this);

		toRegister = (Button) findViewById(R.id.register);
		toRegister.setOnClickListener(this);

		user = (EditText) findViewById(R.id.login_user);
		passwd = (EditText) findViewById(R.id.login_password);
		
		change = (TextView) findViewById(R.id.change_url);
		change.setOnClickListener(this);
		change.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.register:
				this.overlay(RegisterActivity.class);
				break;
			case R.id.login_btn:
				if (user.length() != 11) {
					toast("请输入11位手机号码");
					break;
				}
				if (passwd.length() < 6) {
					toast("请输入6为以上密码");
					break;
				}
				openLoad("正在登录");
				doLogin();
				break;
			case R.id.change_url:
				ChangeUrlDialog mChangeUrlDialog = new ChangeUrlDialog(this);
				mChangeUrlDialog.show();
				mChangeUrlDialog.setonChangeUrlListener(new OnChangeUrlListener(){
					@Override
					public void onClick(String url){
						C.api.base = url;
						SharedPreferences.Editor editor = getSharedPreferences(C.dir.shared, MODE_PRIVATE).edit();
						editor.putString("url", url);
						editor.commit();
						toast("修改成功");
					}
				});
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doLogin() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("phone", user.getText().toString());
		urlParams.put("password", passwd.getText().toString());

		RequestQueue mQueue = SingleRequestQueue.getRequestQueue();

		// 登陆请求
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.login, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				// 从服务器响应response中的jsonObject中取出cookie的值，存到user实例的cookie
				try {
					customer.setCookie(jsonObject.getString("Cookie"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					closeLoad();
					if (jsonObject.getString("status").equals("1")) {
						toast("登陆成功");
						saveData(jsonObject);
						toMain();
					}
					if (jsonObject.getString("status").equals("-1"))
						toast("手机号未注册，登陆失败");

					if (jsonObject.getString("status").equals("0"))
						toast("密码错误，登陆失败");

					if (jsonObject.getString("status").equals("-3"))
						toast("服务器异常，登陆失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				closeLoad();
				Log.e("TAG", error.getMessage(), error);
				toast("网络错误，登陆失败！");
			}
		}, urlParams);

		mQueue.add(post);

	}

	private void saveData(JSONObject json) throws JSONException {

		SharedPreferences.Editor editor = getSharedPreferences(C.dir.shared, MODE_PRIVATE).edit();
		editor.putString("phone", user.getText().toString());
		editor.putString("password", passwd.getText().toString());
		editor.putBoolean("defLogin", true);
		editor.commit();

		customer.setPhone(user.getText().toString());
		customer.setPassword(passwd.getText().toString());
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

	public void toMain() {
		this.forward(MainActivity.class);
	}
	
	// 监听手机上的BACK键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 判断两次点击的时间间隔（默认设置为2秒）
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				this.toast("再按一次退出云生活");
				mExitTime = System.currentTimeMillis();
			} else {
				mExitTime = 0;
				this.doFinish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
