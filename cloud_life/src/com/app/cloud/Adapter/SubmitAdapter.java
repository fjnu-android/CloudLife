package com.app.cloud.Adapter;

import java.util.ArrayList;

import com.app.cloud.R;
import com.app.cloud.Model.Merchant;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubmitAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public ArrayList<Merchant> list;

	public SubmitAdapter(Context context, ArrayList<Merchant> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public SubmitAdapter(Context context) {
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

		ViewHolder holder = null;
		final Merchant m = list.get(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_submit, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.sub_name);
			holder.num = (TextView) convertView.findViewById(R.id.sub_num);

			// 将viewholder存储在view中
			convertView.setTag(holder);
		} else {
			// 重新获取viewholder
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(m.name);
		holder.num.setText(m.num+"");
		
		return convertView;
	}

	// 获取控件实例优化
	class ViewHolder {
		TextView name;
		TextView num;
	}
}
