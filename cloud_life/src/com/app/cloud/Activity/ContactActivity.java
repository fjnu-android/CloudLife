package com.app.cloud.Activity;

import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;

import android.os.Bundle;

/**
 * 联系我们(反馈)
 *
 */
public class ContactActivity extends BaseUiAuth {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		initToolbar("联系我们");

	}

}
