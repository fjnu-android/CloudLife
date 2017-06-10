package com.app.cloud.Base;

import com.app.cloud.Activity.LoginActivity;
import com.app.cloud.Model.User;

import android.os.Bundle;

/**
 * ��½ע������Ļ����
 *
 */
public class BaseUiAuth extends BaseUi {

	protected static User customer = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!BaseAuth.isLogin()) {
			toast("��⵽����˺�δ��½");
			this.forward(LoginActivity.class);
		} else {
			customer = BaseAuth.getCustomer();
		}

	}

}
