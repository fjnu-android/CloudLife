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
 * 选择职业对话框
 *
 */
public class ChangeWorkDialog extends Dialog implements android.view.View.OnClickListener {

	private WheelView wvWork;

	private View lyChangeWork;
	private View lyChangeWorkChild;

	private TextView btnSure;
	private TextView btnCancel;

	private Context context;
	private String[] mWorkDatas;

	private ArrayList<String> arrWork = new ArrayList<String>();

	private WorkTextAdapter workAdapter;

	private String strWork = "学生";

	private OnWorkCListener onWorkCListener;

	private int maxsize = 24;
	private int minsize = 14;

	public ChangeWorkDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changework);

		wvWork = (WheelView) findViewById(R.id.wv_work);

		lyChangeWork = findViewById(R.id.ly_myinfo_changework);
		lyChangeWorkChild = findViewById(R.id.ly_myinfo_changework_child);

		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		lyChangeWork.setOnClickListener(this);
		lyChangeWorkChild.setOnClickListener(this);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		initDatas();
		initWork();

		workAdapter = new WorkTextAdapter(context, arrWork, getWorkItem(strWork), maxsize, minsize);
		wvWork.setVisibleItems(5);
		wvWork.setViewAdapter(workAdapter);
		wvWork.setCurrentItem(getWorkItem(strWork));

		wvWork.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) workAdapter.getItemText(wheel.getCurrentItem());
				strWork = currentText;
				setTextviewSize(currentText, workAdapter);
			}
		});

		wvWork.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) workAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, workAdapter);
			}
		});

	}

	private class WorkTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected WorkTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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

	public void setTextviewSize(String curriteItemText, WorkTextAdapter adapter) {
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

	public void setWorkkListener(OnWorkCListener onWorkCListener) {
		this.onWorkCListener = onWorkCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onWorkCListener != null) {
				onWorkCListener.onClick(strWork);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeWorkChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	public interface OnWorkCListener {
		public void onClick(String work);
	}

	private void initDatas() {
		mWorkDatas = new String[] { "计算机/互联网/通信", "生产/工艺/制造", "医疗/护理/制药", "金融/银行/投资/保险", "商业/服务业/个体经营", "文化/广告/传媒",
				"娱乐/艺术/表演", "律师/法务", "教育/培训", "公务员/行政/事业单位", "模特", "空姐", "学生", "其它高劳动力工作", "其它低劳动力工作" };
	}

	public void initWork() {
		int length = mWorkDatas.length;
		for (int i = 0; i < length; i++) {
			arrWork.add(mWorkDatas[i]);
		}
	}

	public void setWork(String work) {
		if (work != null && work.length() > 0) {
			this.strWork = work;
		}
	}

	public int getWorkItem(String work) {
		int size = arrWork.size();
		int workIndex = 0;
		boolean nowork = true;
		for (int i = 0; i < size; i++) {
			if (work.equals(arrWork.get(i))) {
				nowork = false;
				return workIndex;
			} else {
				workIndex++;
			}
		}
		if (nowork) {
			strWork = "学生";
			return 0;
		}
		return workIndex;
	}

}
