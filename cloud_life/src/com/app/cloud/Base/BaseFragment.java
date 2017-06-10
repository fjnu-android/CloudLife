package com.app.cloud.Base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.app.cloud.Model.User;

import android.content.Intent;
import android.os.Bundle;

/**
 * 碎片基类
 *
 */
public class BaseFragment extends Fragment {

	protected static User customer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		customer = BaseAuth.getCustomer();

	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	////////
	
	// 消息提示
	public void toast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	// fragment to activity
	public void overlay(Class<?> classObj) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClass(getActivity(), classObj);
		getActivity().startActivity(intent);
	}

	// 带数据
	public void overlay(Class<?> classObj, Bundle params) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClass(getActivity(), classObj);
		intent.putExtras(params);
		startActivity(intent);
	}

}
