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
 * 选择性别对话框
 *
 */
public class ChangeSexDialog extends Dialog implements android.view.View.OnClickListener {

	private WheelView wvSex;

	private View lyChangeSex;
	private View lyChangeSexChild;

	private TextView btnSure;
	private TextView btnCancel;

	private Context context;
	private String[] mSexDatas;

	private ArrayList<String> arrSex = new ArrayList<String>();

	private SexTextAdapter sexAdapter;

	private String strSex = "男";

	private OnSexCListener onSexCListener;

	private int maxsize = 24;
	private int minsize = 14;

	public ChangeSexDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changesex);

		wvSex = (WheelView) findViewById(R.id.wv_sex);

		lyChangeSex = findViewById(R.id.ly_myinfo_changesex);
		lyChangeSexChild = findViewById(R.id.ly_myinfo_changesex_child);

		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		lyChangeSex.setOnClickListener(this);
		lyChangeSexChild.setOnClickListener(this);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		initDatas();
		initSex();

		sexAdapter = new SexTextAdapter(context, arrSex, getSexItem(strSex), maxsize, minsize);
		wvSex.setVisibleItems(5);
		wvSex.setViewAdapter(sexAdapter);
		wvSex.setCurrentItem(getSexItem(strSex));

		wvSex.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) sexAdapter.getItemText(wheel.getCurrentItem());
				strSex = currentText;
				setTextviewSize(currentText, sexAdapter);
			}
		});

		wvSex.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) sexAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, sexAdapter);
			}
		});

	}

	private class SexTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected SexTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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

	public void setTextviewSize(String curriteItemText, SexTextAdapter adapter) {
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

	public void setSexkListener(OnSexCListener onSexCListener) {
		this.onSexCListener = onSexCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onSexCListener != null) {
				onSexCListener.onClick(strSex);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeSexChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	public interface OnSexCListener {
		public void onClick(String sex);
	}

	private void initDatas() {
		mSexDatas = new String[2];
		mSexDatas[0] = "男";
		mSexDatas[1] = "女";
	}

	public void initSex() {
		int length = mSexDatas.length;
		for (int i = 0; i < length; i++) {
			arrSex.add(mSexDatas[i]);
		}
	}

	public void setSex(String sex) {
		if (sex != null && sex.length() > 0) {
			this.strSex = sex;
		}
	}

	public int getSexItem(String sex) {
		int size = arrSex.size();
		int sexIndex = 0;
		boolean nosex = true;
		for (int i = 0; i < size; i++) {
			if (sex.equals(arrSex.get(i))) {
				nosex = false;
				return sexIndex;
			} else {
				sexIndex++;
			}
		}
		if (nosex) {
			strSex = "男";
			return 0;
		}
		return sexIndex;
	}

}
