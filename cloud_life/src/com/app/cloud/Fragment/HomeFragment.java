package com.app.cloud.Fragment;

import com.app.cloud.R;
import com.app.cloud.Base.BaseFragment;
import com.app.cloud.Ui.SegmentControl;
import com.app.cloud.Ui.SegmentControl.OnSegmentControlClickListener;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 圈子
 *
 */
public class HomeFragment extends BaseFragment {

	SegmentControl segment;
	private FragmentManager mFragmentManager;

	private CountryFragment country;
	private CityFragment province;
	private PersonFragment person;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		initSegment(view);
		setTabSelection(0);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	private void initSegment(View view) {
		segment = (SegmentControl) view.findViewById(R.id.segment_control);

		segment.setOnSegmentControlClickListener(new OnSegmentControlClickListener() {
			@Override
			public void onSegmentControlClick(int index) {
				setTabSelection(index);
			}
		});
	}

	/**
	 * 根据传入的index参数来设置选中的tab页
	 * 
	 * @param index
	 * 
	 */
	private void setTabSelection(int i) {
		mFragmentManager = getChildFragmentManager();
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		hideFragments(transaction);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		switch (i) {
		case 0:
			if (country == null) {
				country = new CountryFragment().newInstance();
				transaction.add(R.id.home_frame, country);
			} else {
				transaction.show(country);
			}
			break;
		case 1:
			if (province == null) {
				province = new CityFragment().newInstance();
				transaction.add(R.id.home_frame, province);
			} else {
				transaction.show(province);
			}
			break;
		case 2:
			if (person == null) {
				person = new PersonFragment().newInstance();
				transaction.add(R.id.home_frame, person);
			} else {
				transaction.show(person);
			}
			break;
		}
		transaction.commit();

	}

	/**
	 * 将所有的Fragment都置为隐藏状态
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (country != null) {
			transaction.hide(country);
		}
		if (province != null) {
			transaction.hide(province);
		}
		if (person != null) {
			transaction.hide(person);
		}
	}

}
