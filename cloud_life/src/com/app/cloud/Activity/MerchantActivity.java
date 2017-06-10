package com.app.cloud.Activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.cloud.R;
import com.app.cloud.Adapter.MerchantAdapter;
import com.app.cloud.Adapter.MerchantAdapter.OnChangeListener;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Model.Merchant;
import com.app.cloud.Model.Shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

/**
 * 商家 商品购买活动
 *
 */
public class MerchantActivity extends BaseUiAuth implements OnClickListener {

	// 其他活动结束此活动
	static Activity shopB;

	CheckBox check;

	private ListView listView;
	MerchantAdapter merAdapter;

	// 订单列表
	public static ArrayList<Merchant> order;
	// 订单数目
	private int num_order = 0;

	LinearLayout detail;
	TextView number;
	TextView submit;

	JSONArray mers;
	Shop shop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant);

		getData();

		initToolbar(shop.getName());

		shopB = this;

		initView();

	}

	private void getData() {
		shop = new Shop();

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		shop = (Shop) bundle.getSerializable("shop");
		mers = new JSONArray();

		switch (shop.number) {
		case 0:
			mers = ShopActivity.a_o;
			break;
		case 1:
			mers = ShopActivity.b_o;
			break;
		case 2:
			mers = ShopActivity.c_o;
			break;
		case 3:
			mers = ShopActivity.d_o;
			break;
		case 4:
			mers = ShopActivity.e_o;
			break;
		case 5:
			mers = ShopActivity.f_o;
			break;
		}

	}

	private void initView() {

		check = (CheckBox) findViewById(R.id.mer_check);

		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					order.clear();
					order.add(new Merchant(merAdapter.list.get(0).name,1));
					order.add(new Merchant(merAdapter.list.get(1).name,1));
					order.add(new Merchant(merAdapter.list.get(2).name,1));
					overlay(SubmitActivity.class);
				}
			}
		});

		detail = (LinearLayout) findViewById(R.id.mer_detail);
		number = (TextView) findViewById(R.id.mer_number);
		submit = (TextView) findViewById(R.id.mer_submit);

		detail.setOnClickListener(this);
		submit.setOnClickListener(this);

		order = new ArrayList<Merchant>();

		listView = (ListView) findViewById(R.id.mer_list);
		merAdapter = new MerchantAdapter(this);
		listView.setAdapter(merAdapter);

		merAdapter.list.clear();
		// TODO
		JSONObject mer;
		Merchant merchant;
		try {
			for (int i = 0; i < mers.length(); i++) {
				mer = new JSONObject();
				mer = mers.getJSONObject(i);

				merchant = new Merchant(mer.getString("name"),mer.getString("flag"));
				
				merAdapter.list.add(merchant);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		merAdapter.notifyDataSetChanged();

		merAdapter.setChangeListenerListener(new OnChangeListener() {

			@Override
			public void onAdd(Merchant m) {

				num_order++;
				if (num_order == 1) {
					number.setVisibility(View.VISIBLE);
					submit.setVisibility(View.VISIBLE);
				}
				number.setText(num_order + "");

			}

			@Override
			public void onMinus(Merchant m) {

				num_order--;
				if (num_order == 0) {
					number.setVisibility(View.INVISIBLE);
					submit.setVisibility(View.INVISIBLE);
				} else
					number.setText(num_order + "");

			}

		});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.mer_detail:
			
			break;
		case R.id.mer_submit:
			order.clear();
			for (int i = 0; i < merAdapter.list.size(); i++) {
				if (merAdapter.list.get(i).num > 0)
					order.add(merAdapter.list.get(i));
			}
			this.overlay(SubmitActivity.class);
			break;
		}
	}

}
