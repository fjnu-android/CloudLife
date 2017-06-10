package com.app.cloud.Adapter;

import java.util.ArrayList;

import com.app.cloud.R;
import com.app.cloud.Model.Data;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ����ģ�� ѡ��������
 *
 */
public class DataAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public ArrayList<Data> list = new ArrayList<Data>();

	public DataAdapter(Context context, ArrayList<Data> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public DataAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

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

		final Data data = list.get(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_swipe_data, null);
			holder = new ViewHolder();
			holder.type = (TextView) convertView.findViewById(R.id.datalist_type);
			holder.kind = (TextView) convertView.findViewById(R.id.datalist_kind);
			holder.num = (TextView) convertView.findViewById(R.id.datalist_num);

			// ��viewholder�洢��view��
			convertView.setTag(holder);
		} else {
			// ���»�ȡviewholder
			holder = (ViewHolder) convertView.getTag();
		}

		holder.type.setText(data.getType());
		holder.kind.setText(data.getKind());
		holder.num.setText(data.getNum());

		return convertView;
	}

	// ��ȡ�ؼ�ʵ���Ż�
	class ViewHolder {
		TextView type;
		TextView kind;
		TextView num;
	}

}
