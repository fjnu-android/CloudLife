package com.app.cloud.Base;

import com.app.cloud.Activity.LoginActivity;
import com.app.cloud.Model.User;

import android.os.Bundle;

/**
 * 登陆注册以外的活动基类
 *
 */
public class BaseUiAuth extends BaseUi {

	protected static User customer = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!BaseAuth.isLogin()) {
			toast("检测到你的账号未登陆");
			this.forward(LoginActivity.class);
		} else {
			customer = BaseAuth.getCustomer();
		}

	}

}
