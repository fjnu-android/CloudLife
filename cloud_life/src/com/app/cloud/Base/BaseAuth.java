package com.app.cloud.Base;

import com.app.cloud.Model.User;

/**
 * ��֤�û��Ƿ��½
 */
public class BaseAuth {

	static public boolean isLogin() {
		User customer = User.getInstance();
		if (customer.getLogin() == true) {
			return true;
		}
		return false;
	}

	static public void setLogin(Boolean status) {
		User customer = User.getInstance();
		customer.setLogin(status);
	}

	// ע��
	static public void setCustomer(User mc) {
		User customer = User.getInstance();
		customer.setPassword(mc.getPassword());
	}

	static public User getCustomer() {
		return User.getInstance();
	}
}