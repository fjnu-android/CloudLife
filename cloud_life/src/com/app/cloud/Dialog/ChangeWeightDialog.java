package com.app.cloud.Dialog;

import java.util.ArrayList;

import com.app.cloud.R;
import com.app.cloud.PickWheel.Adapter.AbstractWheelTextAdapter;
import com.app.cloud.PickWheel.View.OnWheelChangedListener;
import com.app.cloud.PickWheel.View.OnWheelScrollListener;
import com.app.cloud.PickWheel.View.WheelView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 选择体重对话框
 *
 */
public class ChangeWeightDialog extends Dialog implements android.view.View.OnClickListener {

	private WheelView wvWeight;

	private View lyChangeWeight;
	private View lyChangeWeightChild;

	private TextView btnSure;
	private TextView btnCancel;

	private Context context;
	private String[] mWeightDatas;

	private ArrayList<String> arrWeight = new ArrayList<String>();

	private WeightTextAdapter weightAdapter;

	private String strWeight = "60";

	private OnWeightCListener onWeightCListener;

	private int maxsize = 24;
	private int minsize = 14;

	public ChangeWeightDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changeweight);

		wvWeight = (WheelView) findViewById(R.id.wv_weight);

		lyChangeWeight = findViewById(R.id.ly_myinfo_changeweight);
		lyChangeWeightChild = findViewById(R.id.ly_myinfo_changeweight_child);

		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		lyChangeWeight.setOnClickListener(this);
		lyChangeWeightChild.setOnClickListener(this);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		initDatas();
		initWeight();

		weightAdapter = new WeightTextAdapter(context, arrWeight, getWeightItem(strWeight), maxsize, minsize);
		wvWeight.setVisibleItems(5);
		wvWeight.setViewAdapter(weightAdapter);
		wvWeight.setCurrentItem(getWeightItem(strWeight));

		wvWeight.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) weightAdapter.getItemText(wheel.getCurrentItem());
				strWeight = currentText;
				setTextviewSize(currentText, weightAdapter);
			}
		});

		wvWeight.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) weightAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, weightAdapter);
			}
		});

	}

	private class WeightTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected WeightTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize,
				int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	public void setTextviewSize(String curriteItemText, WeightTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(24);
			} else {
				textvew.setTextSize(14);
			}
		}
	}

	public void setWeightkListener(OnWeightCListener onWeightCListener) {
		this.onWeightCListener = onWeightCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onWeightCListener != null) {
				onWeightCListener.onClick(strWeight);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeWeightChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	public interface OnWeightCListener {
		public void onClick(String height);
	}

	private void initDatas() {
		mWeightDatas = new String[60];
		int j = 39;
		for (int i = 0; i < 60; i++) {
			j++;
			mWeightDatas[i] = j + "";
		}
	}

	public void initWeight() {
		int length = mWeightDatas.length;
		for (int i = 0; i < length; i++) {
			arrWeight.add(mWeightDatas[i]);
		}
	}

	public void setWeight(String weight) {
		if (weight != null && weight.length() > 0) {
			this.strWeight = weight;
		}
	}

	public int getWeightItem(String weight) {
		int size = arrWeight.size();
		int weightIndex = 0;
		boolean noweight = true;
		for (int i = 0; i < size; i++) {
			if (weight.equals(arrWeight.get(i))) {
				noweight = false;
				return weightIndex;
			} else {
				weightIndex++;
			}
		}
		if (noweight) {
			strWeight = "60";
			return 0;
		}
		return weightIndex;
	}
}
