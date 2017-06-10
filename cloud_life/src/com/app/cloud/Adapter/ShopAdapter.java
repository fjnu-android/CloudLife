package com.app.cloud.Adapter;

import java.util.ArrayList;

import com.app.cloud.R;
import com.app.cloud.Model.Shop;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 商家列表适配器
 *
 */
public class ShopAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public ArrayList<Shop> list;
	
	public ShopAdapter(Context context, ArrayList<Shop> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public ShopAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		list = new ArrayList<Shop>();
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

		ViewHolder holder = null;
		final Shop mer= list.get(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_shop, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.shop_name);
			holder.icon = (ImageView) convertView.findViewById(R.id.shop_icon);

			// 将viewholder存储在view中
			convertView.setTag(holder);
		} else {
			// 重新获取viewholder
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(mer.getName());
		holder.icon.setBackgroundColor(Color.parseColor(mer.getIcon()));

		return convertView;
	}
	
	// 获取控件实例优化
	class ViewHolder {
		ImageView icon;
		TextView name;
	}

}
