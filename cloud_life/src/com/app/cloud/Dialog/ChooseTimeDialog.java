package com.app.cloud.Dialog;

import java.util.ArrayList;
import java.util.Calendar;

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
 * 选择 日时分 对话框
 *
 */
public class ChooseTimeDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;

	private WheelView wvDay;
	private WheelView wvHour;
	private WheelView wvMinute;

	private View vChooseTime;
	private View vChooseTimeChild;

	private TextView btnSure;
	private TextView btnCancel;

	private String[] mDays;
	private String[] mHours;
	private String[] mMinutes;

	private ArrayList<String> arry_days = new ArrayList<String>();
	private ArrayList<String> arry_hours = new ArrayList<String>();
	private ArrayList<String> arry_minutes = new ArrayList<String>();

	private TimeTextAdapter mDayAdapter;
	private TimeTextAdapter mHourAdapter;
	private TimeTextAdapter mMinuteAdapter;
	// TODO
	private String strDay = "";
	private String strHour = "12";
	private String strMinute = "0";

	private OnTimeCListener onTimeCListener;

	private int maxsize = 24;
	private int minsize = 14;

	// 当前的年月日
	Calendar c = Calendar.getInstance();
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH) + 1;
	int monthDay = c.get(Calendar.DAY_OF_MONTH);

	public ChooseTimeDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_choose_time);

		wvDay = (WheelView) findViewById(R.id.wv_choose_day);
		wvHour = (WheelView) findViewById(R.id.wv_choose_hour);
		wvMinute = (WheelView) findViewById(R.id.wv_choose_minute);

		vChooseTime = findViewById(R.id.choose_time);
		vChooseTimeChild = findViewById(R.id.choose_time_child);

		btnSure = (TextView) findViewById(R.id.btn_sure);
		btnCancel = (TextView) findViewById(R.id.btn_cancel);

		vChooseTime.setOnClickListener(this);
		vChooseTimeChild.setOnClickListener(this);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		// TODO
		initDatas();
		initDay();
		initHour();
		initMinute();

		mDayAdapter = new TimeTextAdapter(context, arry_days, getDayItem(strDay), maxsize, minsize);
		wvDay.setVisibleItems(5);
		wvDay.setViewAdapter(mDayAdapter);
		wvDay.setCurrentItem(getDayItem(strDay));
		wvDay.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
				strDay = currentText;
				setTextviewSize(currentText, mDayAdapter);
			}
		});
		wvDay.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDayAdapter);
			}
		});

		mHourAdapter = new TimeTextAdapter(context, arry_hours, getHourItem(strHour), maxsize, minsize);
		wvHour.setVisibleItems(5);
		wvHour.setViewAdapter(mHourAdapter);
		wvHour.setCurrentItem(getHourItem(strHour));
		wvHour.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
				strHour = currentText;
				setTextviewSize(currentText, mHourAdapter);
			}
		});
		wvHour.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mHourAdapter);
			}
		});

		mMinuteAdapter = new TimeTextAdapter(context, arry_minutes, getMinuteItem(strMinute), maxsize, minsize);
		wvMinute.setVisibleItems(5);
		wvMinute.setViewAdapter(mMinuteAdapter);
		wvMinute.setCurrentItem(getMinuteItem(strMinute));
		wvMinute.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
				strMinute = currentText;
				setTextviewSize(currentText, mMinuteAdapter);
			}
		});
		wvMinute.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMinuteAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mMinuteAdapter);
			}
		});

	}

	private class TimeTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected TimeTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
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

	public void setTextviewSize(String curriteItemText, TimeTextAdapter adapter) {
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

	public void setTimekListener(OnTimeCListener onTimeCListener) {
		this.onTimeCListener = onTimeCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onTimeCListener != null) {
				// TODO
				onTimeCListener.onClick(strDay, strHour, strMinute);
			}
		} else if (v == btnCancel) {

		} else if (v == vChooseTimeChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();
	}

	public interface OnTimeCListener {
		public void onClick(String day, String hour, String minute);
	}

	private void initDatas() {

		mDays = new String[28];
		for (int i = 0; i < 28; i++) {
			mDays[i] = howDay(i);
		}

		mHours = new String[] { "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
				"21", "22" };

		mMinutes = new String[] { "0", "10", "20", "30", "40", "50" };
	}

	////////////////////////////////////
	// 返回当前日期加i天后的月日
	public String howDay(int i) {

		int n_year;
		int n_month;
		int n_monthDay;

		if ((monthDay + i) <= lastDay()) {
			n_month = month;
			n_monthDay = monthDay + i;
			n_year = year;
		} else {

			if (month > 11) {
				n_month = 1;
				n_year = year + 1;
			} else {
				n_month = month + 1;
				n_year = year;
			}

			n_monthDay = monthDay + i - lastDay();
		}

		return (n_year - 2000) + "-" + n_month + "-" + n_monthDay;
	}

	public int lastDay() {

		int last = 0;

		boolean leayyear = false;
		if (year % 4 == 0 && year % 100 != 0) {
			leayyear = true;
		} else {
			leayyear = false;
		}

		for (int i = 1; i <= 12; i++) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				last = 31;
				break;
			case 2:
				if (leayyear) {
					last = 29;
				} else {
					last = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				last = 30;
				break;
			}
		}

		return last;
	}

	////////////////////////////////////

	public void initDay() {
		int length = mDays.length;
		for (int i = 0; i < length; i++) {
			arry_days.add(mDays[i]);
		}
	}

	public void setDay(String day) {
		if (day != null && day.length() > 0) {
			this.strDay = day;
		}
	}

	public int getDayItem(String day) {
		int size = arry_days.size();
		int dayIndex = 0;
		boolean noday = true;
		for (int i = 0; i < size; i++) {
			if (day.equals(arry_days.get(i))) {
				noday = false;
				return dayIndex;
			} else {
				dayIndex++;
			}
		}
		if (noday) {
			strDay = "";// TODO
			return 0;
		}
		return dayIndex;
	}

	public void initHour() {
		int length = mHours.length;
		for (int i = 0; i < length; i++) {
			arry_hours.add(mHours[i]);
		}
	}

	public void setHour(String hour) {
		if (hour != null && hour.length() > 0) {
			this.strHour = hour;
		}
	}

	public int getHourItem(String hour) {
		int size = arry_hours.size();
		int hourIndex = 0;
		boolean nohour = true;
		for (int i = 0; i < size; i++) {
			if (hour.equals(arry_hours.get(i))) {
				nohour = false;
				return hourIndex;
			} else {
				hourIndex++;
			}
		}
		if (nohour) {
			strHour = "12";
			return 0;
		}
		return hourIndex;
	}

	public void initMinute() {
		int length = mMinutes.length;
		for (int i = 0; i < length; i++) {
			arry_minutes.add(mMinutes[i]);
		}
	}

	public void setMinute(String minute) {
		if (minute != null && minute.length() > 0) {
			this.strMinute = minute;
		}
	}

	public int getMinuteItem(String minute) {
		int size = arry_minutes.size();
		int minuteIndex = 0;
		boolean nominute = true;
		for (int i = 0; i < size; i++) {
			if (minute.equals(arry_minutes.get(i))) {
				nominute = false;
				return minuteIndex;
			} else {
				minuteIndex++;
			}
		}
		if (nominute) {
			strMinute = "0";
			return 0;
		}
		return minuteIndex;
	}

}
