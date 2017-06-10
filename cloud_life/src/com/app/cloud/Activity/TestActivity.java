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
 * 体质测试
 *
 */
public class TestActivity extends BaseUiAuth {

	private Toolbar mToolbar;

	private ListView listView;
	private ArrayList<Test> list;

	AlertDialog.Builder dialog;

	// 存储60题得分的数组
	public int scores[] = new int[60];

	// 体质得分
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
		mToolbar.setTitle("体质测试");
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
						openLoad("正在提交你的体质...");
						// 开启线程计算体质
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

	// 初始化控件
	private void initView() {
		listView = (ListView) findViewById(R.id.test_list);
		initData();
	}

	// 初始化数据
	private void initData() {
		list = new ArrayList<Test>();

		Test q1 = new Test("您皮肤或口唇干吗？");
		list.add(q1);

		Test q2 = new Test("您口唇的颜色比一般人红吗？");
		list.add(q2);

		Test q3 = new Test("您面部两颧潮红或偏红吗？ ");
		list.add(q3);

		Test q4 = new Test("您有额部油脂分泌多的现象吗？");
		list.add(q4);

		Test q5 = new Test("您上眼睑比别人肿（上眼睑有轻微隆起的现象）吗？");
		list.add(q5);

		Test q6 = new Test("您平时痰多，特别是咽喉部总感到有痰堵着吗？");
		list.add(q6);

		Test q7 = new Test("您舌苔厚腻或有舌苔厚厚的感觉吗？");
		list.add(q7);

		Test q8 = new Test("您面部或鼻部有油腻感或者油亮发光吗？ ");
		list.add(q8);

		Test q9 = new Test("您两颧部有细微红丝吗？ ");
		list.add(q9);

		Test q10 = new Test("您面色晦黯或容易出现褐斑吗？");
		list.add(q10);

		Test q11 = new Test("您容易有黑眼圈吗？ ");
		list.add(q11);

		Test q12 = new Test("您口唇颜色偏黯吗？");
		list.add(q12);

		Test q13 = new Test("您嘴里有黏黏的感觉吗？");
		list.add(q13);

		Test q14 = new Test("您感到口苦或嘴里有异味吗？");
		list.add(q14);

		Test q15 = new Test("您有因季节变化、温度变化或异味等原因而咳喘的现象吗？");
		list.add(q15);

		Test q16 = new Test("您容易过敏（对药物、食物、气味、花粉或在季节交替、气候变化时）吗？");
		list.add(q16);

		Test q17 = new Test("您说话声音低弱无力吗？ ");
		list.add(q17);

		Test q18 = new Test("您手脚发凉吗？ ");
		list.add(q18);

		Test q19 = new Test("您胃脘部、背部或腰膝部怕冷吗？");
		list.add(q19);

		Test q20 = new Test("您感到怕冷、衣服比别人穿得多吗？");
		list.add(q20);

		Test q21 = new Test("您比别人容易患感冒吗？ ");
		list.add(q21);

		Test q22 = new Test("您吃（喝）凉的东西会感到不舒服或者怕吃（喝）凉东西吗？");
		list.add(q22);

		Test q23 = new Test("你受凉或吃（喝）凉的东西后，容易腹泻（拉肚子）吗？");
		list.add(q23);

		Test q24 = new Test("您容易便秘或大便干燥吗？ ");
		list.add(q24);

		Test q25 = new Test("您感到眼睛干涩吗？ ");
		list.add(q25);

		Test q26 = new Test("您活动量稍大就容易出虚汗吗？");
		list.add(q26);

		Test q27 = new Test("您容易气短（呼吸急促，接不上气）吗？ ");
		list.add(q27);

		Test q28 = new Test("您容易心慌吗？ ");
		list.add(q28);

		Test q29 = new Test("您容易头晕或站起时眩晕吗？ ");
		list.add(q29);

		Test q30 = new Test("您喜欢安静、懒得说话吗？ ");
		list.add(q30);

		Test q31 = new Test("您感到胸闷或腹部胀满吗？");
		list.add(q31);

		Test q32 = new Test("您感到身体沉重不轻松或不爽快吗？");
		list.add(q32);

		Test q33 = new Test("您脸上容易生痤疮或皮肤容易生疮疖吗？");
		list.add(q33);

		Test q34 = new Test("您大便黏滞不爽、有解不尽的感觉吗？");
		list.add(q34);

		Test q35 = new Test("您小便时尿道有发热感、尿色浓（深）吗？");
		list.add(q35);

		Test q36 = new Test("您的皮肤在不知不觉中会出现青紫瘀斑（皮下出血）吗？");
		list.add(q36);

		Test q37 = new Test("您身体上有哪里疼痛吗？");
		list.add(q37);

		Test q38 = new Test("您没有感冒时也会打喷嚏吗？ ");
		list.add(q38);

		Test q39 = new Test("您感到闷闷不乐，情绪低沉吗？ ");
		list.add(q39);

		Test q40 = new Test("您容易精神紧张、焦虑不安吗？");
		list.add(q40);

		Test q41 = new Test("您多愁善感、感情脆弱吗？ ");
		list.add(q41);

		Test q42 = new Test("您容易感到害怕或受到惊吓吗？ ");
		list.add(q42);

		Test q43 = new Test("您无缘无故叹气吗？ ");
		list.add(q43);

		Test q44 = new Test("您咽喉部有异物感，且吐之不出、咽之不下吗？");
		list.add(q44);

		Test q45 = new Test("您精力充沛吗？ ");
		list.add(q45);

		Test q46 = new Test("您容易疲乏吗？ ");
		list.add(q46);

		Test q47 = new Test("您比一般人耐受不了寒冷（冬天的寒冷，夏天的冷空调、电扇等）吗？");
		list.add(q47);

		Test q48 = new Test("您能适应外界自然和社会环境的变化吗？");
		list.add(q48);

		Test q49 = new Test("您容易失眠吗？ ");
		list.add(q49);

		Test q50 = new Test("您容易忘事（健忘）吗？");
		list.add(q50);

		Test q51 = new Test("您没有感冒时也会鼻塞、流鼻涕吗？ ");
		list.add(q51);

		Test q52 = new Test("您感到口舌干燥总想喝水吗？ ");
		list.add(q52);

		Test q53 = new Test("您感到手脚心发热吗？");
		list.add(q53);

		Test q54 = new Test("您感觉身体、脸上发热吗？");
		list.add(q54);

		Test q55 = new Test("您腹部肥满松软吗？");
		list.add(q55);

		Test q56 = new Test("您（男性）的阴囊部位潮湿吗？您(女性)带下色黄（白带颜色发黄）吗？");
		list.add(q56);

		Test q57 = new Test("您的皮肤容易起荨麻疹（风团、风疹块、风疙瘩）吗？");
		list.add(q57);

		Test q58 = new Test("您的皮肤因过敏出现过紫癜（紫红色瘀点、瘀斑）吗？ ");
		list.add(q58);

		Test q59 = new Test("您的皮肤一抓就红，并出现抓痕吗？ ");
		list.add(q59);

		Test q60 = new Test("您胁肋部或乳房胀痛吗？ ");
		list.add(q60);

		listView.setAdapter(new TestAdapter(this, list));

		initClick();
	}

	private void initClick() {

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// TODO 焦点被抢
			}
		});
	}

	private void openDialog() {
		dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("测试还未结束，你确定退出吗？");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				toMain();
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	// 监听手机上的BACK键
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

	// 修改并提交体质
	private void submit() {

		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("bodyType", bodytype);

		RequestQueue mQueue = SingleRequestQueue.getRequestQueue();

		// 提交体质请求
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.body, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					closeLoad();
					if (jsonObject.getString("status").equals("1")) {
						toast("提交成功");
						customer.setBodytype(bodytype);
						toAtest();
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

	private void toAtest() {
		this.forward(ATestActivity.class);
	}

	private boolean noChoose() {

		for (int i = 0; i < 60; i++) {
			if (list.get(i).getScore() == 0) {
				toast("第" + (i + 1) + "项没选");
				return false;
			}
		}
		return true;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	////// 判断体质类型算法

	// 将得分存储进数组
	public void ListToScore() {

		for (int i = 0; i < list.size(); i++) {
			scores[i] = list.get(i).getScore();
		}

	}

	// 计算九种的体质的转化分数
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

	// 判断是哪种体质
	public void judge() {

		if (other())
			bodytype = "A";
		else
			maxOther();

		Log.d("body", bodytype);
	}

	// 偏颇体质的最高分
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

	// 偏颇体制是都小于40分
	public boolean other() {

		int i;
		for (i = 1; i < 9; i++) {
			if (tizhi[i] >= 40)
				return false;
		}

		return true;
	}

	/**
	 * 获得A平和质的转化分数 (8) 17* 39* 45 46* 47* 48 49* 50*
	 */
	public double getA() {
		int total = 0;
		total += (6 - scores[16]) + (6 - scores[38]) + (6 - scores[45]) + (6 - scores[46]) + (6 - scores[48])
				+ (6 - scores[49]);
		total += scores[44] + scores[47];
		return conversion(total, 8);
	}

	/**
	 * 获得B气虚质的转化分数 (8) 17 21 26 27 28 29 30 46
	 */
	public double getB() {
		int total = 0;
		total += scores[16] + scores[20] + scores[25] + scores[26] + scores[27] + scores[28] + scores[29] + scores[46];
		return conversion(total, 8);
	}

	/**
	 * 获得C阳虚质的转化分数 (7) 18 19 20 21 22 23 47
	 */
	public double getC() {
		int total = 0;
		total += scores[17] + scores[18] + scores[19] + scores[20] + scores[21] + scores[22] + scores[46];
		return conversion(total, 7);
	}

	/**
	 * 获得D阴虚质的转化分数 (8) 1 2 3 24 25 26 53 54
	 */
	public double getD() {
		int total = 0;
		total += scores[0] + scores[1] + scores[2] + scores[23] + scores[24] + scores[25] + scores[52] + scores[53];
		return conversion(total, 8);
	}

	/**
	 * 获得E痰湿质的转化分数 (8) 4 5 6 7 13 31 32 55
	 */
	public double getE() {
		int total = 0;
		total += scores[3] + scores[4] + scores[5] + scores[6] + scores[12] + scores[30] + scores[31] + scores[54];
		return conversion(total, 8);
	}

	/**
	 * 获得F湿热质的转化分数 (7) 8 14 33 34 35 52 56
	 */
	public double getF() {
		int total = 0;
		total += scores[7] + scores[13] + scores[32] + scores[33] + scores[34] + scores[51] + scores[55];
		return conversion(total, 7);
	}

	/**
	 * 获得G血瘀质的转化分数 (7) 9 10 11 12 36 37 50
	 */
	public double getG() {
		int total = 0;
		total += scores[8] + scores[9] + scores[10] + scores[11] + scores[35] + scores[36] + scores[49];
		return conversion(total, 7);
	}

	/**
	 * 获得H气郁质的转化分数 (7) 39 40 41 42 43 44 60
	 */
	public double getH() {
		int total = 0;
		total += scores[38] + scores[39] + scores[40] + scores[41] + scores[42] + scores[43] + scores[59];
		return conversion(total, 7);
	}

	/**
	 * 获得I特禀质的转化分数 (7) 15 16 38 51 57 58 59
	 */
	public double getI() {
		int total = 0;
		total += scores[14] + scores[15] + scores[37] + scores[50] + scores[56] + scores[57] + scores[58];
		return conversion(total, 7);
	}

	// 传入原始分和条目数返回转化分数
	public double conversion(int score, int tip) {
		return ((double) (score - tip) / (double) (tip * 4)) * 100;
	}

}
