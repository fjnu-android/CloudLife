package com.app.cloud.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Adapter.DataAdapter;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Dialog.ChangeDataDialog;
import com.app.cloud.Dialog.ChangeDataDialog.OnDataCListener;
import com.app.cloud.Model.Data;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * 数据模型
 *
 */
public class DataActivity extends BaseUiAuth implements View.OnClickListener {

	private Toolbar mToolbar;
	Context context = this;

	// 早餐
	private SwipeMenuListView b_listView;
	private DataAdapter b_adapter;
	Button b_add;

	// 午餐
	private SwipeMenuListView l_listView;
	private DataAdapter l_adapter;
	Button l_add;

	// 晚餐
	private SwipeMenuListView d_listView;
	private DataAdapter d_adapter;
	Button d_add;

	EditText data_bowls;
	
	RequestQueue mQueue;

	String[] current;
	String[] remain = new String[] {};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_data);

		mQueue = SingleRequestQueue.getRequestQueue();

		initToolbar();
		initView();
		initSwipe();
		getModel();
	}

	public void initToolbar() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("数据模型");
		mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// menu
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.submit_data:
					openLoad("正在提交模型...");
					try {
						submit();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
				}
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_data, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	private void initView() {

		b_listView = (SwipeMenuListView) findViewById(R.id.b_swipelist);
		b_adapter = new DataAdapter(this);
		b_listView.setAdapter(b_adapter);
		b_add = (Button) findViewById(R.id.b_add);
		b_add.setOnClickListener(this);

		l_listView = (SwipeMenuListView) findViewById(R.id.l_swipelist);
		l_adapter = new DataAdapter(this);
		l_listView.setAdapter(l_adapter);
		l_add = (Button) findViewById(R.id.l_add);
		l_add.setOnClickListener(this);

		d_listView = (SwipeMenuListView) findViewById(R.id.d_swipelist);
		d_adapter = new DataAdapter(this);
		d_listView.setAdapter(d_adapter);
		d_add = (Button) findViewById(R.id.d_add);
		d_add.setOnClickListener(this);
		
		data_bowls = (EditText)findViewById(R.id.data_bowls);
		
	}

	private void initSwipe() {
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "change" item
				SwipeMenuItem changeItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				changeItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
				// set item width
				changeItem.setWidth(dp2px(90));
				// set item title
				changeItem.setTitle("更改");
				// set item title fontsize
				changeItem.setTitleSize(18);
				// set item title font color
				changeItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(changeItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a title
				deleteItem.setTitle("删除");
				deleteItem.setTitleSize(18);
				deleteItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		b_listView.setMenuCreator(creator);
		l_listView.setMenuCreator(creator);
		d_listView.setMenuCreator(creator);

		// step 2. listener item click event
		b_listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
				Data item = (Data) b_adapter.getItem(position);
				switch (index) {
				case 0:
					current = new String[] { item.getType() };
					ChangeDataDialog mChangeDataDialog = new ChangeDataDialog(context, current);
					mChangeDataDialog.setType(item.getType());
					mChangeDataDialog.setKind(item.getKind());
					mChangeDataDialog.setNum(item.getNum());
					mChangeDataDialog.show();
					mChangeDataDialog.setDatakListener(new OnDataCListener() {
						@Override
						public void onClick(String type, String kind, String num) {
							b_adapter.list.set(position, new Data(type, kind, num));
							b_adapter.notifyDataSetChanged();
						}
					});
					break;
				case 1:
					b_adapter.list.remove(position);
					b_adapter.notifyDataSetChanged();
					setListHeight(b_listView, b_adapter);
					break;
				}
			}
		});

		l_listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
				Data item = (Data) l_adapter.getItem(position);
				switch (index) {
				case 0:
					current = new String[] { item.getType() };
					ChangeDataDialog mChangeDataDialog = new ChangeDataDialog(context, current);
					mChangeDataDialog.setType(item.getType());
					mChangeDataDialog.setKind(item.getKind());
					mChangeDataDialog.setNum(item.getNum());
					mChangeDataDialog.show();
					mChangeDataDialog.setDatakListener(new OnDataCListener() {
						@Override
						public void onClick(String type, String kind, String num) {
							l_adapter.list.set(position, new Data(type, kind, num));
							l_adapter.notifyDataSetChanged();
						}
					});
					break;
				case 1:
					l_adapter.list.remove(position);
					l_adapter.notifyDataSetChanged();
					setListHeight(l_listView, l_adapter);
					break;
				}
			}
		});

		d_listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(final int position, SwipeMenu menu, int index) {
				Data item = (Data) d_adapter.getItem(position);
				switch (index) {
				case 0:
					current = new String[] { item.getType() };
					ChangeDataDialog mChangeDataDialog = new ChangeDataDialog(context, current);
					mChangeDataDialog.setType(item.getType());
					mChangeDataDialog.setKind(item.getKind());
					mChangeDataDialog.setNum(item.getNum());
					mChangeDataDialog.show();
					mChangeDataDialog.setDatakListener(new OnDataCListener() {
						@Override
						public void onClick(String type, String kind, String num) {
							d_adapter.list.set(position, new Data(type, kind, num));
							d_adapter.notifyDataSetChanged();
						}
					});
					break;
				case 1:
					d_adapter.list.remove(position);
					d_adapter.notifyDataSetChanged();
					setListHeight(d_listView, d_adapter);
					break;
				}
			}
		});

		// set SwipeListener
		b_listView.setOnSwipeListener(new OnSwipeListener() {
			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		l_listView.setOnSwipeListener(new OnSwipeListener() {
			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		d_listView.setOnSwipeListener(new OnSwipeListener() {
			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.b_add:
				if (b_adapter.list.size() == 5) {
					toast("你已经添加完了");
					break;
				}
				remain = setTypeData(b_adapter).clone();
				ChangeDataDialog breakfast = new ChangeDataDialog(this, remain);
				breakfast.setType(remain[0]);
				breakfast.setKind("1");
				breakfast.setNum("100");
				breakfast.show();
				breakfast.setDatakListener(new OnDataCListener() {
					@Override
					public void onClick(String type, String kind, String num) {
						b_adapter.list.add(new Data(type, kind, num));
						b_adapter.notifyDataSetChanged();
						setListHeight(b_listView, b_adapter);
					}
				});
				break;
			case R.id.l_add:
				if (l_adapter.list.size() == 5) {
					toast("你已经添加完了");
					break;
				}
				remain = setTypeData(l_adapter).clone();
				ChangeDataDialog lunch = new ChangeDataDialog(this, remain);
				lunch.setType(remain[0]);
				lunch.setKind("1");
				lunch.setNum("100");
				lunch.show();
				lunch.setDatakListener(new OnDataCListener() {
					@Override
					public void onClick(String type, String kind, String num) {
						l_adapter.list.add(new Data(type, kind, num));
						l_adapter.notifyDataSetChanged();
						setListHeight(l_listView, l_adapter);
					}
				});
				break;
			case R.id.d_add:
				if (d_adapter.list.size() == 5) {
					toast("你已经添加完了");
					break;
				}
				remain = setTypeData(d_adapter).clone();
				ChangeDataDialog dinner = new ChangeDataDialog(this, remain);
				dinner.setType(remain[0]);
				dinner.setKind("1");
				dinner.setNum("100");
				dinner.show();
				dinner.setDatakListener(new OnDataCListener() {
					@Override
					public void onClick(String type, String kind, String num) {
						d_adapter.list.add(new Data(type, kind, num));
						d_adapter.notifyDataSetChanged();
						setListHeight(d_listView, d_adapter);
					}
				});
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void submit() throws JSONException {
		int i;
		JSONObject model = new JSONObject();
		JSONObject breakfast = new JSONObject();
		JSONObject lunch = new JSONObject();
		JSONObject dinner = new JSONObject();
		JSONObject dish;

		for (i = 0; i < b_adapter.list.size(); i++) {
			dish = new JSONObject();
			dish.put("type", b_adapter.list.get(i).getType());
			dish.put("count", b_adapter.list.get(i).getKind());
			dish.put("weight", b_adapter.list.get(i).getNum());
			breakfast.accumulate("dish", dish);
		}

		for (i = 0; i < l_adapter.list.size(); i++) {
			dish = new JSONObject();
			dish.put("type", l_adapter.list.get(i).getType());
			dish.put("count", l_adapter.list.get(i).getKind());
			dish.put("weight", l_adapter.list.get(i).getNum());
			lunch.accumulate("dish", dish);
		}

		for (i = 0; i < d_adapter.list.size(); i++) {
			dish = new JSONObject();
			dish.put("type", d_adapter.list.get(i).getType());
			dish.put("count", d_adapter.list.get(i).getKind());
			dish.put("weight", d_adapter.list.get(i).getNum());
			dinner.accumulate("dish", dish);
		}

		model.put("breakfast", breakfast);
		model.put("lunch", lunch);
		model.put("dinner", dinner);
		model.put("rice", data_bowls.getText().toString());
		model.put("status", "1");

		Log.d("m", model.toString());

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("model", model.toString());

		// 提交数据模型请求
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.setModel, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					closeLoad();
					if (jsonObject.getString("status").equals("1")) {
						toast("提交成功");
					}
					if (jsonObject.getString("status").equals("-1")) {
						toast("cookie有误,提交失败");
					}
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

	private void getModel() {

		// 获得 数据模型请求
		JsonObjectPostRequest get = new JsonObjectPostRequest(C.api.getModel, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					if (jsonObject.getString("status").equals("1")) {
						initModel(jsonObject);
						toast("模型初始化成功");
					}
					if (jsonObject.getString("status").equals("-1")) {
						toast("cookie有误,获取模型失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("VolleyError", error.getMessage(), error);
				toast("网络错误，获取失败！");
			}
		}, null);

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			get.setSendCookie(customer.getCookie());
		}

		mQueue.add(get);
	}

	// 模型初始化
	private void initModel(JSONObject json) throws JSONException {
		int i;
		JSONObject breakfast = new JSONObject();
		JSONObject lunch = new JSONObject();
		JSONObject dinner = new JSONObject();
		
		try {
			data_bowls.setText(json.getString("rice"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		JSONArray dish;
		JSONObject mdish;
		JSONObject b;

		breakfast = json.getJSONObject("breakfast");
		lunch = json.getJSONObject("lunch");
		dinner = json.getJSONObject("dinner");

		// 早餐数据
		dish = new JSONArray();
		mdish = new JSONObject();
		try {
			mdish = breakfast.getJSONObject("dish");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			dish = breakfast.getJSONArray("dish");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (i = 0; i < dish.length(); i++) {
			b = new JSONObject();
			b = dish.getJSONObject(i);
			b_adapter.list.add(new Data(b.getString("type"), b.getString("count"), b.getString("weight")));
		}
		if (!(mdish.length() == 0))
			b_adapter.list.add(new Data(mdish.getString("type"), mdish.getString("count"), mdish.getString("weight")));
		b_adapter.notifyDataSetChanged();
		setListHeight(b_listView, b_adapter);

		// 午餐数据
		dish = new JSONArray();
		mdish = new JSONObject();
		try {
			mdish = lunch.getJSONObject("dish");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			dish = lunch.getJSONArray("dish");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (i = 0; i < dish.length(); i++) {
			b = new JSONObject();
			b = dish.getJSONObject(i);
			l_adapter.list.add(new Data(b.getString("type"), b.getString("count"), b.getString("weight")));
		}
		if (!(mdish.length() == 0))
			l_adapter.list.add(new Data(mdish.getString("type"), mdish.getString("count"), mdish.getString("weight")));
		l_adapter.notifyDataSetChanged();
		setListHeight(l_listView, l_adapter);

		// 晚餐数据
		dish = new JSONArray();
		mdish = new JSONObject();
		try {
			mdish = dinner.getJSONObject("dish");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			dish = dinner.getJSONArray("dish");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (i = 0; i < dish.length(); i++) {
			b = new JSONObject();
			b = dish.getJSONObject(i);
			d_adapter.list.add(new Data(b.getString("type"), b.getString("count"), b.getString("weight")));
		}
		if (!(mdish.length() == 0))
			d_adapter.list.add(new Data(mdish.getString("type"), mdish.getString("count"), mdish.getString("weight")));
		d_adapter.notifyDataSetChanged();
		setListHeight(d_listView, d_adapter);

	}

	// 调整dialog里的type数组
	private String[] setTypeData(DataAdapter data) {
		int i;
		int num = data.list.size();
		String[] now = new String[num];
		// 将列表里已有的保存到数组now
		for (i = 0; i < num; i++) {
			now[i] = data.list.get(i).getType();
		}
		// 用数组all去减now生成新数组remain
		String[] all = new String[] { "蔬菜", "水果", "五谷", "海鲜", "肉类" };
		String[] remain = new String[5 - data.list.size()];

		List<String> ls = new ArrayList<String>();
		// 双重循环
		for (String s1 : all) {
			boolean flag = false;
			for (String s2 : now) {
				if (s1.equals(s2)) {
					flag = true;
				}
			}
			if (flag == false) {
				ls.add(s1);
			}
		}
		remain = ls.toArray(new String[ls.size()]);
		return remain;
	}

	// 动态设置list高度
	public void setListHeight(SwipeMenuListView listView, DataAdapter adapter) {

		if (adapter == null) {
			return;
		}
		int totalHeight = 0;

		for (int i = 0; i < adapter.getCount(); i++) {
			View itemView = adapter.getView(i, null, listView);
			itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			totalHeight += itemView.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
