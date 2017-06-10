package com.app.cloud.Adapter;

import java.util.ArrayList;

import com.app.cloud.R;
import com.app.cloud.Adapter.QuanAdapter.ViewHolder;
import com.app.cloud.Model.QuanZi;
import com.app.cloud.Model.Yue;
import com.app.cloud.Ui.CircleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * �˶���Ƭ �Լ�б�������
 *
 */
public class YueAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public ArrayList<Yue> list = new ArrayList<Yue>();

	public YueAdapter(Context context, ArrayList<Yue> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;

	}

	public YueAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	// listview�ж��ٸ�item
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		final Yue yue = list.get(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_sport_yue, null);
			holder = new ViewHolder();

			holder.name = (TextView) convertView.findViewById(R.id.yue_name);
			holder.time = (TextView) convertView.findViewById(R.id.yue_time);
			holder.address = (TextView) convertView.findViewById(R.id.yue_address);
			holder.state = (ImageView) convertView.findViewById(R.id.yue_state);

			// ��viewholder�洢��view��
			convertView.setTag(holder);
		} else {
			// ���»�ȡviewholder
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(yue.name);
		holder.address.setText(yue.address);
		holder.time.setText(yue.time);

		if (yue.state != true)
			holder.state.setImageResource(R.drawable.yue_false);

		return convertView;
	}

	// ��ȡ�ؼ�ʵ���Ż�
	class ViewHolder {
		TextView name;
		TextView time;
		TextView address;
		ImageView state;
	}

}
