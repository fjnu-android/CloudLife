package com.app.cloud.Adapter;

import java.util.ArrayList;
import com.app.cloud.R;
import com.app.cloud.Model.Test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 中医体质测试题目适配器
 *
 */

public class TestAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<Test> list;

	public TestAdapter(Context context, ArrayList<Test> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	// listview有多少个item
	@Override
	public int getCount() {
		return list.size();
	}

	//
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 每个子项滚动到屏幕内调用
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		final Test test = list.get(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_test, null);
			holder = new ViewHolder();
			holder.questioin = (TextView) convertView.findViewById(R.id.test_qst);

			holder.btn1 = (RadioButton) convertView.findViewById(R.id.test_btn1);
			holder.btn2 = (RadioButton) convertView.findViewById(R.id.test_btn2);
			holder.btn3 = (RadioButton) convertView.findViewById(R.id.test_btn3);
			holder.btn4 = (RadioButton) convertView.findViewById(R.id.test_btn4);
			holder.btn5 = (RadioButton) convertView.findViewById(R.id.test_btn5);

			holder.radioGroup = (RadioGroup) convertView.findViewById(R.id.test_rg);
			// 将viewholder存储在view中
			convertView.setTag(holder);
		} else {
			// 重新获取viewholder
			holder = (ViewHolder) convertView.getTag();
		}

		holder.questioin.setText((position + 1) + "." + test.getQuestion());
		holder.radioGroup.setTag(position);

		// 点击后显示选中
		if (1 == test.getScore()) {
			holder.radioGroup.check(holder.btn1.getId());
		} else if (2 == test.getScore()) {
			holder.radioGroup.check(holder.btn2.getId());
		} else if (3 == test.getScore()) {
			holder.radioGroup.check(holder.btn3.getId());
		} else if (4 == test.getScore()) {
			holder.radioGroup.check(holder.btn4.getId());
		} else if (5 == test.getScore()) {
			holder.radioGroup.check(holder.btn5.getId());
		} else {
			holder.radioGroup.clearCheck();
		}

		// radiogroup中button点击事件
		final RadioGroup radioGroup = holder.radioGroup;
		holder.radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				Test test = list.get(((Integer) group.getTag()));

				if (group == radioGroup) {
					switch (checkedId) {
					case R.id.test_btn1:
						test.setScore(1);
						break;
					case R.id.test_btn2:
						test.setScore(2);
						break;
					case R.id.test_btn3:
						test.setScore(3);
						break;
					case R.id.test_btn4:
						test.setScore(4);
						break;
					case R.id.test_btn5:
						test.setScore(5);
						break;
					}
				}
			}
		});

		return convertView;
	}

	// 获取控件实例优化
	class ViewHolder {
		TextView questioin;
		RadioButton btn1;
		RadioButton btn2;
		RadioButton btn3;
		RadioButton btn4;
		RadioButton btn5;
		RadioGroup radioGroup;
	}

}
