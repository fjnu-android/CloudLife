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
 * 选择模型对话框
 */
public class ChangeDataDialog extends Dialog implements android.view.View.OnClickListener {

	private WheelView wvType;
	private WheelView wvKind;
	private WheelView wvNum;

	private View ChangeData;
	private View ChangeDataChild;

	private TextView btnSure;
	private TextView btnCancel;

	private Context context;

	private String[] mTypeDatas;
	private String[] mKindDatas;
	private String[] mNumDatas;

	private ArrayList<String> arrType = new ArrayList<String>();
	private ArrayList<String> arrKind = new ArrayList<String>();
	private ArrayList<String> arrNum = new ArrayList<String>();

	private DataTextAdapter typeAdapter;
	private DataTextAdapter kindAdapter;
	private DataTextAdapter numAdapter;

	private String strType = "蔬菜";
	private String strKind = "1";
	private String strNum = "100";

	private OnDataCListener onDataCListener;

	private int maxsize = 24;
	private int minsize = 14;

	public ChangeDataDialog(Context context, String[] type) {
		super(context, R.style.ShareDialog);
		this.context = context;
		this.mTypeDatas = type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_data_change);

		wvType = (WheelView) findViewById(R.id.wv_type);
		wvKind = (WheelView) findViewById(R.id.wv_kind);
		wvNum = (WheelView) findViewById(R.id.wv_num);

		ChangeData = findViewById(R.id.data_change);
		ChangeDataChild = findViewById(R.id.data_change_child);

		btnSure = (TextView) findViewById(R.id.btn_data_sure);
		btnCancel = (TextView) findViewById(R.id.btn_data_cancel);

		ChangeData.setOnClickListener(this);
		ChangeDataChild.setOnClickListener(this);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		// TODO
		initDatas();
		initType();
		initKind();
		initNum();

		typeAdapter = new DataTextAdapter(context, arrType, getTypeItem(strType), maxsize, minsize);
		wvType.setVisibleItems(5);
		wvType.setViewAdapter(typeAdapter);
		wvType.setCurrentItem(getTypeItem(strType));
		wvType.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) typeAdapter.getItemText(wheel.getCurrentItem());
				strType = currentText;
				setTextviewSize(currentText, typeAdapter);
			}
		});
		wvType.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) typeAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, typeAdapter);
			}
		});

		kindAdapter = new DataTextAdapter(context, arrKind, getKindItem(strKind), maxsize, minsize);
		wvKind.setVisibleItems(5);
		wvKind.setViewAdapter(kindAdapter);
		wvKind.setCurrentItem(getKindItem(strKind));
		wvKind.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) kindAdapter.getItemText(wheel.getCurrentItem());
				strKind = currentText;
				setTextviewSize(currentText, kindAdapter);
			}
		});
		wvKind.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) kindAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, kindAdapter);
			}
		});

		numAdapter = new DataTextAdapter(context, arrNum, getNumItem(strNum), maxsize, minsize);
		wvNum.setVisibleItems(5);
		wvNum.setViewAdapter(numAdapter);
		wvNum.setCurrentItem(getNumItem(strNum));
		wvNum.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) numAdapter.getItemText(wheel.getCurrentItem());
				strNum = currentText;
				setTextviewSize(currentText, numAdapter);
			}
		});
		wvNum.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) numAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, numAdapter);
			}
		});

	}

	private class DataTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected DataTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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

	public void setTextviewSize(String curriteItemText, DataTextAdapter adapter) {
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

	public void setDatakListener(OnDataCListener onDataCListener) {
		this.onDataCListener = onDataCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onDataCListener != null) {
				onDataCListener.onClick(strType, strKind, strNum);
			}
		} else if (v == btnCancel) {

		} else if (v == ChangeDataChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	public interface OnDataCListener {
		public void onClick(String type, String kind, String num);
	}

	private void initDatas() {
		int i;

		mKindDatas = new String[5];
		for (i = 0; i < mKindDatas.length; i++)
			mKindDatas[i] = (i + 1) + "";

		mNumDatas = new String[10];
		for (i = 0; i < mNumDatas.length; i++)
			mNumDatas[i] = (i * 50 + 50) + "";
	}

	public void initType() {
		int length = mTypeDatas.length;
		for (int i = 0; i < length; i++) {
			arrType.add(mTypeDatas[i]);
		}
	}

	public void setType(String type) {
		if (type != null && type.length() > 0) {
			this.strType = type;
		}
	}

	public int getTypeItem(String type) {
		int size = arrType.size();
		int typeIndex = 0;
		boolean notype = true;
		for (int i = 0; i < size; i++) {
			if (type.equals(arrType.get(i))) {
				notype = false;
				return typeIndex;
			} else {
				typeIndex++;
			}
		}
		if (notype) {
			strType = "蔬菜";
			return 0;
		}
		return typeIndex;
	}

	public void initKind() {
		int length = mKindDatas.length;
		for (int i = 0; i < length; i++) {
			arrKind.add(mKindDatas[i]);
		}
	}

	public void setKind(String kind) {
		if (kind != null && kind.length() > 0) {
			this.strKind = kind;
		}
	}

	public int getKindItem(String kind) {
		int size = arrKind.size();
		int kindIndex = 0;
		boolean nokind = true;
		for (int i = 0; i < size; i++) {
			if (kind.equals(arrKind.get(i))) {
				nokind = false;
				return kindIndex;
			} else {
				kindIndex++;
			}
		}
		if (nokind) {
			strKind = "1";
			return 0;
		}
		return kindIndex;
	}

	public void initNum() {
		int length = mNumDatas.length;
		for (int i = 0; i < length; i++) {
			arrNum.add(mNumDatas[i]);
		}
	}

	public void setNum(String num) {
		if (num != null && num.length() > 0) {
			this.strNum = num;
		}
	}

	public int getNumItem(String num) {
		int size = arrKind.size();
		int numIndex = 0;
		boolean nonum = true;
		for (int i = 0; i < size; i++) {
			if (num.equals(arrNum.get(i))) {
				nonum = false;
				return numIndex;
			} else {
				numIndex++;
			}
		}
		if (nonum) {
			strNum = "1";
			return 0;
		}
		return numIndex;
	}
}
