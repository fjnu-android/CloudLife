package com.app.cloud.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Activity.FoodDishActivity;
import com.app.cloud.Activity.FoodMenuActivity;
import com.app.cloud.Adapter.MealAdapter;
import com.app.cloud.Base.BaseFragment;
import com.app.cloud.Base.C;
import com.app.cloud.Model.DietMeal;
import com.app.cloud.Model.FoodDish;
import com.app.cloud.Model.FoodMenu;
import com.app.cloud.Ui.SegmentControl;
import com.app.cloud.Ui.SegmentControl.OnSegmentControlClickListener;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * ��ʳ
 *
 */
public class DietFragment extends BaseFragment implements View.OnClickListener {

	SegmentControl segment;
	// �л�����
	Button changeOver;

	PopupWindow pop;
	View popView;

	// Ӫ���ɷ�
	TextView power_std;
	TextView power_now;
	TextView df_std;
	TextView df_now;
	TextView carbo_std;
	TextView carbo_now;
	TextView protein_std;
	TextView protein_now;
	TextView fat_std;
	TextView fat_now;

	private ExpandableListView listView;
	private ArrayList<DietMeal> list;
	private MealAdapter listAdapter;

	DietMeal breakfast;
	DietMeal lunch;
	DietMeal dinner;

	RequestQueue mQueue;

	// meal�б�״̬ 1չ�� 0�ر�
	int[] isExpand = new int[] { 0, 0, 0 };

	// �˵���plan���� ͨ��index ��������
	JSONArray plan;
	// Ӫ���ɷ����� ͨ��index ��������
	JSONArray evaluate;
	// �˵���plan���� ͨ��index ��������
	JSONArray planMenu;
	// Ӫ���ɷ����� ͨ��index ��������
	JSONArray evaMenu;

	// ��¼��ǰ��Ʒ/������plan��index
	int index = 0;
	// ��¼��ǰ��ʳ�Ļ���ʳƷ
	// 0Ĭ��ʳ�� 1ʳ��
	int food = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_diet, container, false);

		initView(view);
		initPop();
		//TODO
		/**
		// ��ȡʳ��
		initTodayDiet("dish");
		initTodayDietEva("dish");

		// ��ȡʳ��
		initTodayDiet("menu");
		initTodayDietEva("menu");
		 */
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@SuppressLint("InflateParams")
	private void initPop() {
		// ����layoutInflater���View
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popView = inflater.inflate(R.layout.popup_diet, null);
		// ���������ַ����õ���Ⱥ͸߶� getWindow().getDecorView().getWidth()
		pop = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		// ����popWindow��������ɵ������仰������ӣ�������true
		pop.setFocusable(true);
		// ���ñ���Ϊ��͸��
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		pop.setBackgroundDrawable(dw);
		// ���õ��������ض���
		pop.setAnimationStyle(R.style.popBottom_anim);

		popView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����ʳ��
				index++;
				if (food == 0) {
					if (index == plan.length())
						index = 0;
					try {
						changeTodayIndex(index);
						changeTodayEva(index);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (food == 1) {
					if (index == plan.length())
						index = 0;
					try {
						changeTodayIndex(index);
						changeTodayEva(index);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
				toast("����   " + (index + 1) + "");
				pop.dismiss();
			}
		});
	}

	private void initView(View view) {

		power_std = (TextView) view.findViewById(R.id.power_std);
		power_now = (TextView) view.findViewById(R.id.power_now);
		df_std = (TextView) view.findViewById(R.id.df_std);
		df_now = (TextView) view.findViewById(R.id.df_now);
		carbo_std = (TextView) view.findViewById(R.id.carbo_std);
		carbo_now = (TextView) view.findViewById(R.id.carbo_now);
		protein_std = (TextView) view.findViewById(R.id.protein_std);
		protein_now = (TextView) view.findViewById(R.id.protein_now);
		fat_std = (TextView) view.findViewById(R.id.fat_std);
		fat_now = (TextView) view.findViewById(R.id.fat_now);

		changeOver = (Button) view.findViewById(R.id.meal_changeover);
		changeOver.setOnClickListener(this);

		segment = (SegmentControl) view.findViewById(R.id.segment_control);

		segment.setOnSegmentControlClickListener(new OnSegmentControlClickListener() {
			@Override
			public void onSegmentControlClick(int i) {
				if (food != i && i == 0) {

					try {
						index = 0;
						food = 0;
						changeTodayIndex(0);
						changeTodayEva(0);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else if (food != i && i == 1) {

					try {
						index = 0;
						food = 1;
						changeTodayIndex(0);
						changeTodayEva(0);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		});
		listView = (ExpandableListView) view.findViewById(R.id.diet_meal);

		mQueue = SingleRequestQueue.getRequestQueue();
		initData();
	}

	private void initData() {
		list = new ArrayList<DietMeal>();

		breakfast = new DietMeal("���");
		lunch = new DietMeal("���");
		dinner = new DietMeal("���");

		list.add(breakfast);
		list.add(lunch);
		list.add(dinner);

		listAdapter = new MealAdapter(getActivity(), list);

		listView.setAdapter(listAdapter);

		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int groupPosition, int childPosition,
					long arg4) {
				if (food == 0)
					searchDish("dish", listAdapter.getChild(groupPosition, childPosition).getName());

				if (food == 1)
					searchDish("menu", listAdapter.getChild(groupPosition, childPosition).getName());

				return false;
			}

		});

		// ͨ������չ���͹ر��¼���̬���ø߶�
		listView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				isExpand[groupPosition] = 1;
				setListHeight(listView, listAdapter);
			}

		});
		listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {
				isExpand[groupPosition] = 0;
				setListHeight(listView, listAdapter);
			}
		});
	}

	// ������Ʒ
	private void searchDish(final String type, String search) {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", type);
		urlParams.put("name", search);

		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getDietData, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						if (type.equals("dish"))
							initDish(jsonObject);

						if (type.equals("menu"))
							initMenu(jsonObject);
					}
					if (jsonObject.getString("status").equals("-2"))
						toast("�Ҳ��� search");

					if (jsonObject.getString("status").equals("-1"))
						toast("cookieʧЧ����ȡʧ��");

					if (jsonObject.getString("status").equals("0"))
						toast("��Ϣ���󣬻�ȡʧ��");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				toast("������󣬻�ȡʧ�ܣ�");
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) {
			// �����������post����ʱ����cookie�ֶ�
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	// ��Ʒ�����������Ʒ����
	private void initDish(JSONObject json) throws JSONException {
		FoodDish dish = new FoodDish();

		try {
			dish.setImage(json.getString("image"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			dish.setName(json.getString("name"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			dish.setIntroduction(json.getString("introduction"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			dish.setMan_suit(json.getString("man_suit"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			dish.setMan_unsuit(json.getString("man_unsuit"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			dish.setHow_eat(json.getString("how_eat"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			dish.setSave(json.getString("save"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			dish.setHow_select(json.getString("select"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			dish.setEffect(json.getString("effect"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// ���������ĸ��û�пո�
		String body = json.getString("bodyType").trim().replace(" ", "");
		String type = "";
		for (int i = 0; i < body.length(); i++) {
			switch (body.charAt(i)) {
			case 'A':
				type += "ƽ���� ";
				break;
			case 'B':
				type += "������ ";
				break;
			case 'C':
				type += "������ ";
				break;
			case 'D':
				type += "������ ";
				break;
			case 'E':
				type += "̵ʪ�� ";
				break;
			case 'F':
				type += "ʪ���� ";
				break;
			case 'G':
				type += "Ѫ���� ";
				break;
			case 'H':
				type += "������ ";
				break;
			case 'I':
				type += "������ ";
				break;
			}
		}
		dish.setBodyType(type);

		Bundle bundle = new Bundle();
		bundle.putSerializable("dish", dish);

		this.overlay(FoodDishActivity.class, bundle);
	}

	// ʳ�����ݽ���
	private void initMenu(JSONObject json) throws JSONException {

		FoodMenu menu = new FoodMenu();

		try {
			menu.setImage(json.getString("image"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			menu.setName(json.getString("name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			menu.setEffect(json.getString("effect"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			menu.setHow_make(json.getString("how_make"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			menu.setFlavor(json.getString("flavor"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			menu.setMaterial_main(json.getString("material_main"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			menu.setMaterial_assist(json.getString("material_assist"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			// ���������ĸ��û�пո�
			String body = json.getString("bodyType").trim().replace(" ", "");
			String type = "";
			for (int i = 0; i < body.length(); i++) {
				switch (body.charAt(i)) {
				case 'A':
					type += "ƽ���� ";
					break;
				case 'B':
					type += "������ ";
					break;
				case 'C':
					type += "������ ";
					break;
				case 'D':
					type += "������ ";
					break;
				case 'E':
					type += "̵ʪ�� ";
					break;
				case 'F':
					type += "ʪ���� ";
					break;
				case 'G':
					type += "Ѫ���� ";
					break;
				case 'H':
					type += "������ ";
					break;
				case 'I':
					type += "������ ";
					break;
				}
			}
			menu.setBodyType(type);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Bundle bundle = new Bundle();
		bundle.putSerializable("menu", menu);

		this.overlay(FoodMenuActivity.class, bundle);
	}

	// ��ȡ����ʳ������
	private void initTodayDiet(final String type) {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", type);

		// �ύ����
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getaTodayDiet,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							if (jsonObject.getString("status").equals("1")) {
								if (type.equals("dish")) {
									plan = new JSONArray();
									plan = jsonObject.getJSONObject("data").getJSONArray("plan");
									changeTodayIndex(0);
								}

								if (type.equals("menu")) {
									planMenu = new JSONArray();
									planMenu = jsonObject.getJSONObject("data").getJSONArray("plan");
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						toast("������󣬻�ȡʧ�ܣ�");
					}
				}, urlParams);

		//������ʱ,����
		post.setRetryPolicy(new DefaultRetryPolicy(
				120000,//Ĭ�ϳ�ʱʱ��2minutes
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//Ĭ������Դ���
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
				){			
		});
		
		if (!customer.getCookie().equals("")) {
			// �����������post����ʱ����cookie�ֶ�
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	// ͨ��plan�������±����
	private void changeTodayIndex(int i) throws JSONException {
		int j;
		int k;
		String key = null;
		JSONObject index;// plan����±�

		JSONArray b = new JSONArray();// index���breakfastd��dish��array
		JSONArray l = new JSONArray();// index���lunch��dish��array
		JSONArray d = new JSONArray();// index���breakfast��dish��array

		JSONObject bO;// index���breakfastd��dish��Object
		JSONObject lO;// index���lunch��dish��Object
		JSONObject dO;// index���breakfast��dish��Object

		JSONObject dish;// dish��Ķ���

		// ��Ȼ��dish��������menu���Ͼ�ֻҪ���֣����ʣ�Ч����ͼƬ
		FoodMenu menu;

		String body;
		// ͨ��index json����plan ȡ��json����
		index = new JSONObject();

		if (food == 0) {
			index = plan.getJSONObject(i);
			key = "dish";
		}
		if (food == 1) {
			index = planMenu.getJSONObject(i);
			key = "menu";
		}

		breakfast.clearAllChild();
		lunch.clearAllChild();
		dinner.clearAllChild();

		// ����п����Ƕ��������
		try {
			b = index.getJSONObject("breakfast").getJSONArray(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			bO = index.getJSONObject("breakfast").getJSONObject(key);

			menu = new FoodMenu();
			menu.setName(bO.getString("name"));
			menu.setEffect(bO.getString("effect"));

			try {
				menu.setImage(bO.getString("img"));
			} catch (JSONException e2) {
				e2.printStackTrace();
			}

			body = new String();
			body = bO.getString("bodyType").trim().replace(" ", "");

			for (k = 0; k < body.length(); k++) {
				if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
					menu.setBodyType(customer.getBodytype());
					break;
				}
			}
			if (k == body.length())
				menu.setBodyType("");

			breakfast.add(menu);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		// ����п����Ƕ��������
		try {
			l = index.getJSONObject("lunch").getJSONArray(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			lO = index.getJSONObject("lunch").getJSONObject(key);

			menu = new FoodMenu();
			menu.setName(lO.getString("name"));
			menu.setEffect(lO.getString("effect"));
			try {
				menu.setImage(lO.getString("img"));
			} catch (JSONException e2) {
				e2.printStackTrace();
			}

			body = new String();
			body = lO.getString("bodyType").trim().replace(" ", "");

			for (k = 0; k < body.length(); k++) {
				if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
					menu.setBodyType(customer.getBodytype());
					break;
				}
			}
			if (k == body.length())
				menu.setBodyType("");

			lunch.add(menu);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		// ����п����Ƕ��������
		try {
			d = index.getJSONObject("dinner").getJSONArray(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			dO = index.getJSONObject("dinner").getJSONObject(key);

			menu = new FoodMenu();
			menu.setName(dO.getString("name"));
			menu.setEffect(dO.getString("effect"));
			try {
				menu.setImage(dO.getString("img"));
			} catch (JSONException e2) {
				e2.printStackTrace();
			}

			body = new String();
			body = dO.getString("bodyType").trim().replace(" ", "");

			for (k = 0; k < body.length(); k++) {
				if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
					menu.setBodyType(customer.getBodytype());
					break;
				}
			}
			if (k == body.length())
				menu.setBodyType("");

			dinner.add(menu);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		// ��ȡ���
		for (j = 0; j < b.length(); j++) {
			dish = new JSONObject();
			dish = b.getJSONObject(j);

			menu = new FoodMenu();
			menu.setName(dish.getString("name"));
			try {
				menu.setEffect(dish.getString("effect"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				menu.setImage(dish.getString("img"));
			} catch (JSONException e2) {
				e2.printStackTrace();
			}

			body = new String();
			body = dish.getString("bodyType").trim().replace(" ", "");

			for (k = 0; k < body.length(); k++) {
				if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
					menu.setBodyType(customer.getBodytype());
					break;
				}
			}
			if (k == body.length())
				menu.setBodyType("");

			breakfast.add(menu);
		}

		// ��ȡ���
		for (j = 0; j < l.length(); j++) {
			dish = new JSONObject();
			dish = l.getJSONObject(j);

			menu = new FoodMenu();
			menu.setName(dish.getString("name"));
			try {
				menu.setEffect(dish.getString("effect"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				menu.setImage(dish.getString("img"));
			} catch (JSONException e2) {
				e2.printStackTrace();
			}

			body = new String();
			body = dish.getString("bodyType").trim().replace(" ", "");

			for (k = 0; k < body.length(); k++) {
				if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
					menu.setBodyType(customer.getBodytype());
					break;
				}
			}
			if (k == body.length())
				menu.setBodyType("");

			lunch.add(menu);
		}

		// ��ȡ���
		for (j = 0; j < d.length(); j++) {
			dish = new JSONObject();
			dish = d.getJSONObject(j);

			menu = new FoodMenu();
			menu.setName(dish.getString("name"));
			try {
				menu.setEffect(dish.getString("effect"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				menu.setImage(dish.getString("img"));
			} catch (JSONException e2) {
				e2.printStackTrace();
			}

			body = new String();
			body = dish.getString("bodyType").trim().replace(" ", "");

			for (k = 0; k < body.length(); k++) {
				if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
					menu.setBodyType(customer.getBodytype());
					break;
				}
			}
			if (k == body.length())
				menu.setBodyType("");

			dinner.add(menu);
		}

		listAdapter.notifyDataSetChanged();
		// ʱ��չ��
		expandOfTime(listView);

	}

	private void initTodayDietEva(final String type) {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", type);

		// �ύ����
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getaTodayDietEva,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							if (jsonObject.getString("status").equals("1")) {
								if (type.equals("dish")) {
									evaluate = new JSONArray();
									evaluate = jsonObject.getJSONObject("data").getJSONArray("evaluate");

									// ����Ԫ�ر�׼ֵ
									JSONObject std = jsonObject.getJSONObject("data").getJSONObject("std");

									if (std.getString("power").length() > 5)
										power_std.setText(std.getString("power").substring(0,
												std.getString("power").indexOf(".")));
									else
										power_std.setText(std.getString("power"));

									if (std.getString("DF").length() > 5)
										df_std.setText(std.getString("DF").substring(0,
												std.getString("DF").indexOf(".")));
									else
										df_std.setText(std.getString("DF"));
									
									if (std.getString("carbohydrate").length() > 5)
										carbo_std.setText(std.getString("carbohydrate").substring(0,
												std.getString("carbohydrate").indexOf(".")));
									else
										carbo_std.setText(std.getString("carbohydrate"));
									
									if (std.getString("protein").length() > 5)
										protein_std.setText(std.getString("protein").substring(0,
												std.getString("protein").indexOf(".")));
									else
										protein_std.setText(std.getString("protein"));
									
									if (std.getString("fat").length() > 5)
										fat_std.setText(std.getString("fat").substring(0,
												std.getString("fat").indexOf(".")));
									else
										fat_std.setText(std.getString("fat"));
									
									changeTodayEva(0);
								}

								if (type.equals("menu")) {
									evaMenu = new JSONArray();
									evaMenu = jsonObject.getJSONObject("data").getJSONArray("evaluate");
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						toast("������󣬻�ȡʧ�ܣ�");
					}
				}, urlParams);
		
		// ������ʱ,����
		post.setRetryPolicy(new DefaultRetryPolicy(
				120000, // Ĭ�ϳ�ʱʱ��2minutes
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Ĭ������Դ���
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
		});

		if (!customer.getCookie().equals("")) {
			// �����������post����ʱ����cookie�ֶ�
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void changeTodayEva(int i) throws JSONException {
		JSONObject eva = new JSONObject();

		if (food == 0)
			eva = evaluate.getJSONObject(i);

		if (food == 1)
			eva = evaMenu.getJSONObject(i);

		Float f;
		f=Float.parseFloat(eva.getString("power"));
		
		power_now.setText(f.intValue()+"");
		if (Integer.parseInt((String) power_now.getText()) > Integer.parseInt((String) power_std.getText()))
			power_now.setTextColor(Color.parseColor("#FF0000"));
		else
			power_now.setTextColor(Color.parseColor("#00FF00"));

		f=Float.parseFloat(eva.getString("df"));
		df_now.setText(f.intValue()+"");
		if (Integer.parseInt((String) df_now.getText()) > Integer.parseInt((String) df_std.getText()))
			df_now.setTextColor(Color.parseColor("#FF0000"));
		else
			df_now.setTextColor(Color.parseColor("#00FF00"));

		f=Float.parseFloat(eva.getString("carbohydrate"));
		carbo_now.setText(f.intValue()+"");
		if (Integer.parseInt((String) carbo_now.getText()) > Integer.parseInt((String) carbo_std.getText()))
			carbo_now.setTextColor(Color.parseColor("#FF0000"));
		else
			carbo_now.setTextColor(Color.parseColor("#00FF00"));

		f=Float.parseFloat(eva.getString("protein"));
		protein_now.setText(f.intValue()+"");
		if (Integer.parseInt((String) protein_now.getText()) > Integer.parseInt((String) protein_std.getText()))
			protein_now.setTextColor(Color.parseColor("#FF0000"));
		else
			protein_now.setTextColor(Color.parseColor("#00FF00"));

		f=Float.parseFloat(eva.getString("fat"));
		fat_now.setText(f.intValue()+"");
		if (Integer.parseInt((String) fat_now.getText()) > Integer.parseInt((String) fat_std.getText()))
			fat_now.setTextColor(Color.parseColor("#FF0000"));
		else
			fat_now.setTextColor(Color.parseColor("#00FF00"));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.meal_changeover:
			pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			break;
		}
	}

	/**
	 * expandListView����Ӧ�߶� ������������
	 */
	public void setListHeight(ExpandableListView listView, MealAdapter listAdapter) {

		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		int total = 0;

		View listItem;

		for (int i = 0; i < listAdapter.getGroupCount(); i++) {
			listItem = listAdapter.getGroupView(i, false, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
			total += (listAdapter.getChildrenCount(0) - 1);
		}

		if (isExpand[0] == 1)
			for (int i = 0; i < listAdapter.getChildrenCount(0); i++) {
				listItem = listAdapter.getChildView(0, i, false, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
				total += (listAdapter.getChildrenCount(0) - 1);
			}

		if (isExpand[1] == 1)
			for (int i = 0; i < listAdapter.getChildrenCount(1); i++) {
				listItem = listAdapter.getChildView(1, i, false, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
				total += (listAdapter.getChildrenCount(1) - 1);
			}

		if (isExpand[2] == 1)
			for (int i = 0; i < listAdapter.getChildrenCount(2); i++) {
				listItem = listAdapter.getChildView(2, i, false, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
				total += (listAdapter.getChildrenCount(2) - 1);
			}

		ViewGroup.LayoutParams params = listView.getLayoutParams();

		params.height = totalHeight + (listView.getDividerHeight() * total);

		listView.setLayoutParams(params);
	}

	/**
	 * ����ʱ��չ����
	 */
	private void expandOfTime(ExpandableListView listView) {

		// ��ȡʱ��
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);

		if (hour <= 10)
			listView.expandGroup(0);
		else if (hour <= 14)
			listView.expandGroup(1);
		else
			listView.expandGroup(2);
	}

}
