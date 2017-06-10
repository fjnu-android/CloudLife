package com.app.cloud.Activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 体质测试之前
 *
 */
public class BTestActivity extends BaseUiAuth implements View.OnClickListener {

	Button toTest;
	Button choose;
	Button t_A;
	Button t_B;
	Button t_C;
	Button t_D;
	Button t_E;
	Button t_F;
	Button t_G;
	Button t_H;
	Button t_I;

	String body;

	RequestQueue mQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_btest);

		mQueue = SingleRequestQueue.getRequestQueue();

		initToolbar("我的体质");
		initView();

	}

	// 初始化控件
	private void initView() {
		toTest = (Button) findViewById(R.id.btest);
		choose = (Button) findViewById(R.id.btest_choose);

		toTest.setOnClickListener(this);
		choose.setOnClickListener(this);

		t_A = (Button) findViewById(R.id.b_A);
		t_B = (Button) findViewById(R.id.b_B);
		t_C = (Button) findViewById(R.id.b_C);
		t_D = (Button) findViewById(R.id.b_D);
		t_E = (Button) findViewById(R.id.b_E);
		t_F = (Button) findViewById(R.id.b_F);
		t_G = (Button) findViewById(R.id.b_G);
		t_H = (Button) findViewById(R.id.b_H);
		t_I = (Button) findViewById(R.id.b_I);

		t_A.setOnClickListener(this);
		t_B.setOnClickListener(this);
		t_C.setOnClickListener(this);
		t_D.setOnClickListener(this);
		t_E.setOnClickListener(this);
		t_F.setOnClickListener(this);
		t_G.setOnClickListener(this);
		t_H.setOnClickListener(this);
		t_I.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.btest:
				this.forward(TestActivity.class);
				break;
			case R.id.btest_choose:
				openLoad("正在提交体质...");
				submit();
				break;
			case R.id.b_A:
				body = "A";
				change(body);
				break;
			case R.id.b_B:
				body = "B";
				change(body);
				break;
			case R.id.b_C:
				body = "C";
				change(body);
				break;
			case R.id.b_D:
				body = "D";
				change(body);
				break;
			case R.id.b_E:
				body = "E";
				change(body);
				break;
			case R.id.b_F:
				body = "F";
				change(body);
				break;
			case R.id.b_G:
				body = "G";
				change(body);
				break;
			case R.id.b_H:
				body = "H";
				change(body);
				break;
			case R.id.b_I:
				body = "I";
				change(body);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void change(String b) {
		if (!choose.isEnabled())
			choose.setEnabled(true);

		switch (b) {
		case "A":
			choose.setText("我是 平和质");
			break;
		case "B":
			choose.setText("我是 气虚质");
			break;
		case "C":
			choose.setText("我是 阳虚质");
			break;
		case "D":
			choose.setText("我是 阴虚质");
			break;
		case "E":
			choose.setText("我是 痰湿质");
			break;
		case "F":
			choose.setText("我是 湿热质");
			break;
		case "G":
			choose.setText("我是 血瘀质");
			break;
		case "H":
			choose.setText("我是 气郁质");
			break;
		case "I":
			choose.setText("我是 特禀质");
			break;
		}

	}

	// 修改并提交体质
	private void submit() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("bodyType", body);

		// 提交体质请求
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.body, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					closeLoad();
					if (jsonObject.getString("status").equals("1")) {
						toast("提交成功");
						customer.setBodytype(body);
						toMain();
					}
					if (jsonObject.getString("status").equals("-1"))
						toast("cookie失效，提交失败");

					if (jsonObject.getString("status").equals("-2"))
						toast("信息不完整，提交失败");

					if (jsonObject.getString("status").equals("0"))
						toast("服务器异常，提交失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				closeLoad();
				Log.e("TAG", error.getMessage(), error);
				toast("网络错误，提交失败！");
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void toMain() {
		this.finish();
	}

}
