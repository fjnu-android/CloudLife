package com.app.cloud.Activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Adapter.MealAdapter;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Model.DietMeal;
import com.app.cloud.Model.FoodDish;
import com.app.cloud.Model.FoodMenu;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

/**
 * 搜索菜品/菜谱
 *
 */
public class SearchActivity extends BaseUiAuth {

	private SearchView search;

	private ExpandableListView listView;
	private ArrayList<DietMeal> list;
	private MealAdapter listAdapter;

	DietMeal fooddish;
	DietMeal foodmenu;

	RequestQueue mQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initToolbar("");
		initView();
		// 软键盘自动展开
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	private void initView() {
		mQueue = SingleRequestQueue.getRequestQueue();

		search = (SearchView) findViewById(R.id.search);
		search.setIconified(false);
		search.setSubmitButtonEnabled(true);
		search.setQueryHint("搜索  食材/食谱 ");
		search.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String arg0) {
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				search(arg0);
				return false;
			}
		});

		listView = (ExpandableListView) findViewById(R.id.search_diet);

		list = new ArrayList<DietMeal>();

		fooddish = new DietMeal("食材");
		foodmenu = new DietMeal("食谱");

		list.add(fooddish);
		list.add(foodmenu);

		listAdapter = new MealAdapter(this, list);

		listView.setAdapter(listAdapter);

		listView.expandGroup(0);
		listView.expandGroup(1);
		listView.setVisibility(View.GONE);

		listView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int groupPosition, int childPosition,
					long arg4) {
				if (groupPosition == 0)
					searchDish(listAdapter.getChild(groupPosition, childPosition).getName());
				if (groupPosition == 1)
					searchMenu(listAdapter.getChild(groupPosition, childPosition).getName());
				return false;
			}
		});

	}

	// 搜索全部
	private void search(final String food) {
		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "");
		urlParams.put("name", food);

		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getDietData, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						initData(jsonObject);
					}

					if (jsonObject.getString("status").equals("-2"))
						toast("找不到  " + food);

					if (jsonObject.getString("status").equals("-1"))
						toast("cookie失效，搜索失败");

					if (jsonObject.getString("status").equals("0"))
						toast("信息错误，搜索失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				toast("网络错误，搜索失败！");
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void initData(JSONObject json) {
		JSONObject object;
		JSONArray array;
		FoodMenu food;

		String body;
		int k;

		fooddish.clearAllChild();
		foodmenu.clearAllChild();

		// 食材
		try {
			object = new JSONObject();
			object = json.getJSONObject("dish").getJSONObject("dish");
			food = new FoodMenu();
			food.setName(object.getString("name"));
			food.setEffect(object.getString("effect"));
			food.setImage(object.getString("image"));

			body = new String();
			body = object.getString("bodyType").trim().replace(" ", "");

			for (k = 0; k < body.length(); k++) {
				if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
					food.setBodyType(customer.getBodytype());
					break;
				}
			}
			if (k == body.length())
				food.setBodyType("");
			fooddish.add(food);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			array = new JSONArray();
			array = json.getJSONObject("dish").getJSONArray("dish");
			for (int i = 0; i < array.length(); i++) {
				object = new JSONObject();
				object = array.getJSONObject(i);

				food = new FoodMenu();
				food.setName(object.getString("name"));
				food.setEffect(object.getString("effect"));
				food.setImage(object.getString("image"));

				body = new String();
				body = object.getString("bodyType").trim().replace(" ", "");

				for (k = 0; k < body.length(); k++) {
					if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
						food.setBodyType(customer.getBodytype());
						break;
					}
				}
				if (k == body.length())
					food.setBodyType("");
				fooddish.add(food);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// 食谱
		try {
			object = new JSONObject();
			object = json.getJSONObject("menu").getJSONObject("menu");
			food = new FoodMenu();
			food.setName(object.getString("name"));
			food.setEffect(object.getString("effect"));
			food.setImage(object.getString("image"));

			body = new String();
			body = object.getString("bodyType").trim().replace(" ", "");

			for (k = 0; k < body.length(); k++) {
				if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
					food.setBodyType(customer.getBodytype());
					break;
				}
			}
			if (k == body.length())
				food.setBodyType("");
			foodmenu.add(food);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			array = new JSONArray();
			array = json.getJSONObject("menu").getJSONArray("menu");
			for (int i = 0; i < array.length(); i++) {
				object = new JSONObject();
				object = array.getJSONObject(i);

				food = new FoodMenu();
				food.setName(object.getString("name"));
				food.setEffect(object.getString("effect"));
				food.setImage(object.getString("image"));

				body = new String();
				body = object.getString("bodyType").trim().replace(" ", "");

				for (k = 0; k < body.length(); k++) {
					if (customer.getBodyType().charAt(0) == (body.charAt(k))) {
						food.setBodyType(customer.getBodytype());
						break;
					}
				}
				if (k == body.length())
					food.setBodyType("");
				foodmenu.add(food);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (listView.getVisibility() == View.GONE)
			listView.setVisibility(View.VISIBLE);

		listAdapter.notifyDataSetChanged();
	}

	// 搜索菜品
	private void searchDish(final String search) {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "dish");
		urlParams.put("name", search);

		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getDietData, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						initDish(jsonObject);
					}
					if (jsonObject.getString("status").equals("-2"))
						toast("找不到  " + search);

					if (jsonObject.getString("status").equals("-1"))
						toast("cookie失效，搜索失败");

					if (jsonObject.getString("status").equals("0"))
						toast("信息错误，搜索失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				toast("网络错误，搜索失败！");
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	// 菜品解析，进入菜品详情
	private void initDish(JSONObject json) throws JSONException {
		FoodDish dish = new FoodDish();

		try {
			dish.setImage(json.getString("image"));
		} catch (JSONException e3) {
			e3.printStackTrace();
		}

		try {
			dish.setName(json.getString("name"));
		} catch (JSONException e3) {
			e3.printStackTrace();
		}

		try {
			dish.setIntroduction(json.getString("introduction"));
		} catch (JSONException e2) {
			e2.printStackTrace();
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

		// 获得体质字母，没有空格
		String body = json.getString("bodyType").trim().replace(" ", "");
		String type = "";
		for (int i = 0; i < body.length(); i++) {
			switch (body.charAt(i)) {
			case 'A':
				type += "平和质 ";
				break;
			case 'B':
				type += "气虚质 ";
				break;
			case 'C':
				type += "阳虚质 ";
				break;
			case 'D':
				type += "阴虚质 ";
				break;
			case 'E':
				type += "痰湿质 ";
				break;
			case 'F':
				type += "湿热质 ";
				break;
			case 'G':
				type += "血瘀质 ";
				break;
			case 'H':
				type += "气郁质 ";
				break;
			case 'I':
				type += "特禀质 ";
				break;
			}
		}
		dish.setBodyType(type);

		Bundle bundle = new Bundle();
		bundle.putSerializable("dish", dish);

		this.overlay(FoodDishActivity.class, bundle);
	}

	// 搜索菜谱
	private void searchMenu(final String search) {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("type", "menu");
		urlParams.put("name", search);

		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.getDietData, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						initMenu(jsonObject);
					}

					if (jsonObject.getString("status").equals("-2"))
						toast("找不到  " + search);

					if (jsonObject.getString("status").equals("-1"))
						toast("cookie失效，搜索失败");

					if (jsonObject.getString("status").equals("0"))
						toast("信息错误，搜索失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				toast("网络错误，搜索失败！");
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	// 食谱数据解析
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
			// 获得体质字母，没有空格
			String body = json.getString("bodyType").trim().replace(" ", "");
			String type = "";
			for (int i = 0; i < body.length(); i++) {
				switch (body.charAt(i)) {
				case 'A':
					type += "平和质 ";
					break;
				case 'B':
					type += "气虚质 ";
					break;
				case 'C':
					type += "阳虚质 ";
					break;
				case 'D':
					type += "阴虚质 ";
					break;
				case 'E':
					type += "痰湿质 ";
					break;
				case 'F':
					type += "湿热质 ";
					break;
				case 'G':
					type += "血瘀质 ";
					break;
				case 'H':
					type += "气郁质 ";
					break;
				case 'I':
					type += "特禀质 ";
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

}
