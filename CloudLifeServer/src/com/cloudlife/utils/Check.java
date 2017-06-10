package com.cloudlife.utils;

import javax.servlet.http.Cookie;

import com.cloudlife.user.db.IUserSql;
import com.cloudlife.user.db.UserSqlImp;

public class Check {

	/**
	 * @breif 检查cookie的有效性 cookie正确则返回用户的手机号码
	 * 			否则返回null
	 *  @author wuyi
	 */
	
	static private IUserSql m_userSql = new UserSqlImp();
	
	static public String isCookieValid(Cookie []cookies) {
		
		String phone = null,  cloud_life = null;
		for (int i =0; i< cookies.length; ++i) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals("USER"))
				phone = cookie.getValue();
			if (cookie.getName().equals("CLOUD_LIFE"))
				cloud_life = cookie.getValue();
		}
		if (TextUtils.isEmpty(phone)== true || TextUtils.isEmpty(cloud_life)==true) {
			return null;
		}
		
		if (m_userSql.check(phone, cloud_life) == false) {
			return null;
		}
		return phone;
	}
}
