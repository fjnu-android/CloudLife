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
 * ���ʲ���֮ǰ
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

		initToolbar("�ҵ�����");
		initView();

	}

	// ��ʼ���ؼ�
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
				openLoad("�����ύ����...");
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
			choose.setText("���� ƽ����");
			break;
		case "B":
			choose.setText("���� ������");
			break;
		case "C":
			choose.setText("���� ������");
			break;
		case "D":
			choose.setText("���� ������");
			break;
		case "E":
			choose.setText("���� ̵ʪ��");
			break;
		case "F":
			choose.setText("���� ʪ����");
			break;
		case "G":
			choose.setText("���� Ѫ����");
			break;
		case "H":
			choose.setText("���� ������");
			break;
		case "I":
			choose.setText("���� ������");
			break;
		}

	}

	// �޸Ĳ��ύ����
	private void submit() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("bodyType", body);

		// �ύ��������
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.body, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					closeLoad();
					if (jsonObject.getString("status").equals("1")) {
						toast("�ύ�ɹ�");
						customer.setBodytype(body);
						toMain();
					}
					if (jsonObject.getString("status").equals("-1"))
						toast("cookieʧЧ���ύʧ��");

					if (jsonObject.getString("status").equals("-2"))
						toast("��Ϣ���������ύʧ��");

					if (jsonObject.getString("status").equals("0"))
						toast("�������쳣���ύʧ��");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				closeLoad();
				Log.e("TAG", error.getMessage(), error);
				toast("��������ύʧ�ܣ�");
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) {
			// �����������post����ʱ����cookie�ֶ�
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void toMain() {
		this.finish();
	}

}
