package com.app.cloud.Adapter;

import java.util.ArrayList;

import com.app.cloud.R;
import com.app.cloud.Model.Merchant;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public ArrayList<Merchant> list;

	private OnChangeListener onChangeListener;
	
	public MerchantAdapter(Context context, ArrayList<Merchant> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public MerchantAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		list = new ArrayList<Merchant>();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Merchant mer = list.get(position);
		
		TextView name;
		final TextView num;
		ImageView icon;
		final ImageButton minus;
		ImageButton add;

		convertView = inflater.inflate(R.layout.item_merchant, null);
		name = (TextView) convertView.findViewById(R.id.mer_name);
		icon = (ImageView) convertView.findViewById(R.id.mer_icon);
		minus = (ImageButton) convertView.findViewById(R.id.mer_minus);
		add = (ImageButton) convertView.findViewById(R.id.mer_add);
		num = (TextView) convertView.findViewById(R.id.mer_num);

		name.setText(mer.name);

		// 增加
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mer.num++;
				num.setText(mer.num + "");
				minus.setVisibility(View.VISIBLE);		
				if (onChangeListener != null) {
					onChangeListener.onAdd(mer);
				}
			}
		});

		// 减少
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mer.num--;
				if(mer.num != 0)
					num.setText(mer.num + "");				
				else {
					num.setText("");
					minus.setVisibility(View.INVISIBLE);
				}
				if (onChangeListener != null) {
					onChangeListener.onMinus(mer);
				} 
			}
		});
		
		switch(position % 6){
		case 0:
			icon.setBackgroundColor(Color.parseColor("#FF0000"));
			break;
		case 1:
			icon.setBackgroundColor(Color.parseColor("#FFA500"));
			break;
		case 2:
			icon.setBackgroundColor(Color.parseColor("#FFFF00"));
			break;
		case 3:
			icon.setBackgroundColor(Color.parseColor("#00FF00"));
			break;
		case 4:
			icon.setBackgroundColor(Color.parseColor("#008B8B"));
			break;
		case 5:
			icon.setBackgroundColor(Color.parseColor("#9B30FF"));
			break;
		}
		
		return convertView;
	}
	
	/**
	 * 自定义监听器
	 * 监听列表子项中增加和减少的动作
	 */		
	public void setChangeListenerListener(OnChangeListener onChangeListener) {
		this.onChangeListener = onChangeListener;
	}
	
	/**
	 * 监听器接口
	 * 数据变动
	 */
	public interface OnChangeListener {		
		public void onAdd(Merchant m);
		public void onMinus(Merchant m);
	}

}
