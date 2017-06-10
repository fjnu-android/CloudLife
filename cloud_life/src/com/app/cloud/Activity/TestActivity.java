package com.app.cloud.Activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Adapter.TestAdapter;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Model.Test;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * ���ʲ���
 *
 */
public class TestActivity extends BaseUiAuth {

	private Toolbar mToolbar;

	private ListView listView;
	private ArrayList<Test> list;

	AlertDialog.Builder dialog;

	// �洢60��÷ֵ�����
	public int scores[] = new int[60];

	// ���ʵ÷�
	public double tizhi[] = new double[9];

	String bodytype;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_test);

		initToolbar();
		initView();

	}

	public void initToolbar() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("���ʲ���");
		mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openDialog();
			}
		});

		// menu
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.submit_test:
					if (!noChoose())
						break;
					else {
						openLoad("�����ύ�������...");
						// �����̼߳�������
						new Thread(new Runnable() {
							@Override
							public void run() {
								ListToScore();
								getConversion();
								judge();
							}
						}).start();

						submit();
						break;
					}
				default:
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	// ��ʼ���ؼ�
	private void initView() {
		listView = (ListView) findViewById(R.id.test_list);
		initData();
	}

	// ��ʼ������
	private void initData() {
		list = new ArrayList<Test>();

		Test q1 = new Test("��Ƥ����ڴ�����");
		list.add(q1);

		Test q2 = new Test("���ڴ�����ɫ��һ���˺���");
		list.add(q2);

		Test q3 = new Test("���沿��ȧ�����ƫ���� ");
		list.add(q3);

		Test q4 = new Test("���ж��֬���ڶ��������");
		list.add(q4);

		Test q5 = new Test("���������ȱ����ף�����������΢¡���������");
		list.add(q5);

		Test q6 = new Test("��ƽʱ̵�࣬�ر����ʺ��ܸе���̵������");
		list.add(q6);

		Test q7 = new Test("����̦���������̦���ĸо���");
		list.add(q7);

		Test q8 = new Test("���沿��ǲ�������л������������� ");
		list.add(q8);

		Test q9 = new Test("����ȧ����ϸ΢��˿�� ");
		list.add(q9);

		Test q10 = new Test("����ɫ���������׳��ְֺ���");
		list.add(q10);

		Test q11 = new Test("�������к���Ȧ�� ");
		list.add(q11);

		Test q12 = new Test("���ڴ���ɫƫ����");
		list.add(q12);

		Test q13 = new Test("����������ĸо���");
		list.add(q13);

		Test q14 = new Test("���е��ڿ����������ζ��");
		list.add(q14);

		Test q15 = new Test("�����򼾽ڱ仯���¶ȱ仯����ζ��ԭ����ȴ���������");
		list.add(q15);

		Test q16 = new Test("�����׹�������ҩ�ʳ���ζ�����ۻ��ڼ��ڽ��桢����仯ʱ����");
		list.add(q16);

		Test q17 = new Test("��˵���������������� ");
		list.add(q17);

		Test q18 = new Test("���ֽŷ����� ");
		list.add(q18);

		Test q19 = new Test("��θ�䲿����������ϥ��������");
		list.add(q19);

		Test q20 = new Test("���е����䡢�·��ȱ��˴��ö���");
		list.add(q20);

		Test q21 = new Test("���ȱ������׻���ð�� ");
		list.add(q21);

		Test q22 = new Test("���ԣ��ȣ����Ķ�����е�����������³ԣ��ȣ���������");
		list.add(q22);

		Test q23 = new Test("��������ԣ��ȣ����Ķ��������׸�к�������ӣ���");
		list.add(q23);

		Test q24 = new Test("�����ױ��ػ�������� ");
		list.add(q24);

		Test q25 = new Test("���е��۾���ɬ�� ");
		list.add(q25);

		Test q26 = new Test("������Դ�����׳��麹��");
		list.add(q26);

		Test q27 = new Test("���������̣��������٣��Ӳ��������� ");
		list.add(q27);

		Test q28 = new Test("�������Ļ��� ");
		list.add(q28);

		Test q29 = new Test("������ͷ�λ�վ��ʱѣ���� ");
		list.add(q29);

		Test q30 = new Test("��ϲ������������˵���� ");
		list.add(q30);

		Test q31 = new Test("���е����ƻ򸹲�������");
		list.add(q31);

		Test q32 = new Test("���е�������ز����ɻ�ˬ����");
		list.add(q32);

		Test q33 = new Test("���������������Ƥ��������������");
		list.add(q33);

		Test q34 = new Test("�������Ͳ�ˬ���нⲻ���ĸо���");
		list.add(q34);

		Test q35 = new Test("��С��ʱ����з��ȸС���ɫŨ�����");
		list.add(q35);

		Test q36 = new Test("����Ƥ���ڲ�֪�����л�����������ߣ�Ƥ�³�Ѫ����");
		list.add(q36);

		Test q37 = new Test("����������������ʹ��");
		list.add(q37);

		Test q38 = new Test("��û�и�ðʱҲ��������� ");
		list.add(q38);

		Test q39 = new Test("���е����Ʋ��֣������ͳ��� ");
		list.add(q39);

		Test q40 = new Test("�����׾�����š����ǲ�����");
		list.add(q40);

		Test q41 = new Test("������ƸС���������� ");
		list.add(q41);

		Test q42 = new Test("�����׸е����»��ܵ������� ");
		list.add(q42);

		Test q43 = new Test("����Ե�޹�̾���� ");
		list.add(q43);

		Test q44 = new Test("���ʺ�������У�����֮��������֮������");
		list.add(q44);

		Test q45 = new Test("������������ ");
		list.add(q45);

		Test q46 = new Test("������ƣ���� ");
		list.add(q46);

		Test q47 = new Test("����һ�������ܲ��˺��䣨����ĺ��䣬�������յ������ȵȣ���");
		list.add(q47);

		Test q48 = new Test("������Ӧ�����Ȼ����ỷ���ı仯��");
		list.add(q48);

		Test q49 = new Test("������ʧ���� ");
		list.add(q49);

		Test q50 = new Test("���������£���������");
		list.add(q50);

		Test q51 = new Test("��û�и�ðʱҲ��������������� ");
		list.add(q51);

		Test q52 = new Test("���е�������������ˮ�� ");
		list.add(q52);

		Test q53 = new Test("���е��ֽ��ķ�����");
		list.add(q53);

		Test q54 = new Test("���о����塢���Ϸ�����");
		list.add(q54);

		Test q55 = new Test("����������������");
		list.add(q55);

		Test q56 = new Test("�������ԣ������Ҳ�λ��ʪ����(Ů��)����ɫ�ƣ��״���ɫ���ƣ���");
		list.add(q56);

		Test q57 = new Test("����Ƥ��������ݡ������š�����顢������");
		list.add(q57);

		Test q58 = new Test("����Ƥ����������ֹ���񰣨�Ϻ�ɫ���㡢���ߣ��� ");
		list.add(q58);

		Test q59 = new Test("����Ƥ��һץ�ͺ죬������ץ���� ");
		list.add(q59);

		Test q60 = new Test("��в�߲����鷿��ʹ�� ");
		list.add(q60);

		listView.setAdapter(new TestAdapter(this, list));

		initClick();
	}

	private void initClick() {

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// TODO ���㱻��
			}
		});
	}

	private void openDialog() {
		dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("���Ի�δ��������ȷ���˳���");
		dialog.setCancelable(false);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				toMain();
			}
		});
		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	// �����ֻ��ϵ�BACK��
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			openDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void toMain() {
		this.finish();
	}

	// �޸Ĳ��ύ����
	private void submit() {

		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("bodyType", bodytype);

		RequestQueue mQueue = SingleRequestQueue.getRequestQueue();

		// �ύ��������
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.body, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					closeLoad();
					if (jsonObject.getString("status").equals("1")) {
						toast("�ύ�ɹ�");
						customer.setBodytype(bodytype);
						toAtest();
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

	private void toAtest() {
		this.forward(ATestActivity.class);
	}

	private boolean noChoose() {

		for (int i = 0; i < 60; i++) {
			if (list.get(i).getScore() == 0) {
				toast("��" + (i + 1) + "��ûѡ");
				return false;
			}
		}
		return true;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	////// �ж����������㷨

	// ���÷ִ洢������
	public void ListToScore() {

		for (int i = 0; i < list.size(); i++) {
			scores[i] = list.get(i).getScore();
		}

	}

	// ������ֵ����ʵ�ת������
	public void getConversion() {
		tizhi[0] = getA();
		tizhi[1] = getB();
		tizhi[2] = getC();
		tizhi[3] = getD();
		tizhi[4] = getE();
		tizhi[5] = getF();
		tizhi[6] = getG();
		tizhi[7] = getH();
		tizhi[8] = getI();

	}

	// �ж�����������
	public void judge() {

		if (other())
			bodytype = "A";
		else
			maxOther();

		Log.d("body", bodytype);
	}

	// ƫ�����ʵ���߷�
	private void maxOther() {
		int max = 1;
		int i;

		for (i = 2; i < 9; i++) {
			if (tizhi[i] > tizhi[max])
				max = i;
		}

		switch (max) {
		case 1:
			bodytype = "B";
			break;
		case 2:
			bodytype = "C";
			break;
		case 3:
			bodytype = "D";
			break;
		case 4:
			bodytype = "E";
			break;
		case 5:
			bodytype = "F";
			break;
		case 6:
			bodytype = "G";
			break;
		case 7:
			bodytype = "H";
			break;
		case 8:
			bodytype = "I";
			break;
		}
	}

	// ƫ�������Ƕ�С��40��
	public boolean other() {

		int i;
		for (i = 1; i < 9; i++) {
			if (tizhi[i] >= 40)
				return false;
		}

		return true;
	}

	/**
	 * ���Aƽ���ʵ�ת������ (8) 17* 39* 45 46* 47* 48 49* 50*
	 */
	public double getA() {
		int total = 0;
		total += (6 - scores[16]) + (6 - scores[38]) + (6 - scores[45]) + (6 - scores[46]) + (6 - scores[48])
				+ (6 - scores[49]);
		total += scores[44] + scores[47];
		return conversion(total, 8);
	}

	/**
	 * ���B�����ʵ�ת������ (8) 17 21 26 27 28 29 30 46
	 */
	public double getB() {
		int total = 0;
		total += scores[16] + scores[20] + scores[25] + scores[26] + scores[27] + scores[28] + scores[29] + scores[46];
		return conversion(total, 8);
	}

	/**
	 * ���C�����ʵ�ת������ (7) 18 19 20 21 22 23 47
	 */
	public double getC() {
		int total = 0;
		total += scores[17] + scores[18] + scores[19] + scores[20] + scores[21] + scores[22] + scores[46];
		return conversion(total, 7);
	}

	/**
	 * ���D�����ʵ�ת������ (8) 1 2 3 24 25 26 53 54
	 */
	public double getD() {
		int total = 0;
		total += scores[0] + scores[1] + scores[2] + scores[23] + scores[24] + scores[25] + scores[52] + scores[53];
		return conversion(total, 8);
	}

	/**
	 * ���E̵ʪ�ʵ�ת������ (8) 4 5 6 7 13 31 32 55
	 */
	public double getE() {
		int total = 0;
		total += scores[3] + scores[4] + scores[5] + scores[6] + scores[12] + scores[30] + scores[31] + scores[54];
		return conversion(total, 8);
	}

	/**
	 * ���Fʪ���ʵ�ת������ (7) 8 14 33 34 35 52 56
	 */
	public double getF() {
		int total = 0;
		total += scores[7] + scores[13] + scores[32] + scores[33] + scores[34] + scores[51] + scores[55];
		return conversion(total, 7);
	}

	/**
	 * ���GѪ���ʵ�ת������ (7) 9 10 11 12 36 37 50
	 */
	public double getG() {
		int total = 0;
		total += scores[8] + scores[9] + scores[10] + scores[11] + scores[35] + scores[36] + scores[49];
		return conversion(total, 7);
	}

	/**
	 * ���H�����ʵ�ת������ (7) 39 40 41 42 43 44 60
	 */
	public double getH() {
		int total = 0;
		total += scores[38] + scores[39] + scores[40] + scores[41] + scores[42] + scores[43] + scores[59];
		return conversion(total, 7);
	}

	/**
	 * ���I�����ʵ�ת������ (7) 15 16 38 51 57 58 59
	 */
	public double getI() {
		int total = 0;
		total += scores[14] + scores[15] + scores[37] + scores[50] + scores[56] + scores[57] + scores[58];
		return conversion(total, 7);
	}

	// ����ԭʼ�ֺ���Ŀ������ת������
	public double conversion(int score, int tip) {
		return ((double) (score - tip) / (double) (tip * 4)) * 100;
	}

}
