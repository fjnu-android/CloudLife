package com.app.cloud.Activity;

import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;

import android.os.Bundle;

/**
 * �������
 *
 */
public class AboutActivity extends BaseUiAuth {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		initToolbar("����������");

	}

}
