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
 * 选择身高对话框
 *
 */
public class ChangeHeightDialog extends Dialog implements android.view.View.OnClickListener {

	private WheelView wvHeight;

	private View lyChangeHeight;
	private View lyChangeHeightChild;

	private TextView btnSure;
	private TextView btnCancel;

	private Context context;
	private String[] mHeightDatas;

	private ArrayList<String> arrHeight = new ArrayList<String>();

	private HeightTextAdapter heightAdapter;

	private String strHeight = "170";

	private OnHeightCListener onHeightCListener;

	private int maxsize = 24;
	private int minsize = 14;

	public ChangeHeightDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changeheight);

		wvHeight = (WheelView) findViewById(R.id.wv_height);

		lyChangeHeight = findViewById(R.id.ly_myinfo_changeheight);
		lyChangeHeightChild = findViewById(R.id.ly_myinfo_changeheight_child);

		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		lyChangeHeight.setOnClickListener(this);
		lyChangeHeightChild.setOnClickListener(this);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		initDatas();
		initHeight();

		heightAdapter = new HeightTextAdapter(context, arrHeight, getHeightItem(strHeight), maxsize, minsize);
		wvHeight.setVisibleItems(5);
		wvHeight.setViewAdapter(heightAdapter);
		wvHeight.setCurrentItem(getHeightItem(strHeight));

		wvHeight.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) heightAdapter.getItemText(wheel.getCurrentItem());
				strHeight = currentText;
				setTextviewSize(currentText, heightAdapter);
			}
		});

		wvHeight.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) heightAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, heightAdapter);
			}
		});

	}

	private class HeightTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected HeightTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize,
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

	public void setTextviewSize(String curriteItemText, HeightTextAdapter adapter) {
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

	public void setHeightkListener(OnHeightCListener onHeightCListener) {
		this.onHeightCListener = onHeightCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onHeightCListener != null) {
				onHeightCListener.onClick(strHeight);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeHeightChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	public interface OnHeightCListener {
		public void onClick(String height);
	}

	private void initDatas() {
		mHeightDatas = new String[60];
		int j = 139;
		for (int i = 0; i < 60; i++) {
			j++;
			mHeightDatas[i] = j + "";
		}
	}

	public void initHeight() {
		int length = mHeightDatas.length;
		for (int i = 0; i < length; i++) {
			arrHeight.add(mHeightDatas[i]);
		}
	}

	public void setHeight(String height) {
		if (height != null && height.length() > 0) {
			this.strHeight = height;
		}
	}

	public int getHeightItem(String height) {
		int size = arrHeight.size();
		int heightIndex = 0;
		boolean noheight = true;
		for (int i = 0; i < size; i++) {
			if (height.equals(arrHeight.get(i))) {
				noheight = false;
				return heightIndex;
			} else {
				heightIndex++;
			}
		}
		if (noheight) {
			strHeight = "170";
			return 0;
		}
		return heightIndex;
	}

}
