package com.app.cloud.Activity;

import java.util.ArrayList;

import com.app.cloud.R;
import com.app.cloud.Adapter.SubmitAdapter;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Model.Merchant;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SubmitActivity extends BaseUiAuth {

	TextView main;
	
	//订单列表
	private ArrayList<Merchant> order = new ArrayList<Merchant>();
	
	ListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit);

		initToolbar("提交订单");	
		
		initView();
		
	}
	
	private void initView(){
		
		order = MerchantActivity.order;		
		
		main = (TextView)findViewById(R.id.sub_main);
		main.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				MerchantActivity.shopB.finish();
				ShopActivity.shopA.finish();
				finish();
			}
			
		});
		
		listView = (ListView)findViewById(R.id.sub_list);
		listView.setAdapter(new SubmitAdapter(this,order));
		
		
	}
}
