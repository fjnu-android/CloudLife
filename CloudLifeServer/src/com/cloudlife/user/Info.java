package com.cloudlife.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudlife.user.db.IUserSql;
import com.cloudlife.user.db.UserSqlImp;
import com.cloudlife.utils.Check;
import com.cloudlife.utils.TextUtils;

@WebServlet(
		urlPatterns={"/user/info"},
		name="Info"
		)
public class Info extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IUserSql m_userSql = new UserSqlImp();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		String name = req.getParameter("name");
		String birthday = req.getParameter("birthday");
		String height = req.getParameter("height");
		String weight = req.getParameter("weight");
		String sex = req.getParameter("sex");
		String city = req.getParameter("city");
		String sign = req.getParameter("sign");
		String work = req.getParameter("work");
		
		PrintWriter wrt = resp.getWriter();
		if(TextUtils.isEmpty(name)==true || TextUtils.isEmpty(birthday)
				||TextUtils.isEmpty(height)==true || TextUtils.isEmpty(weight)
				||TextUtils.isEmpty(sex)==true||TextUtils.isEmpty(city)==true
				|| TextUtils.isEmpty(sign) == true) {
			wrt.write("{\"status\":\"-2\"}"); // 信息填写不完整
			return ;
		}
		
		// 获取cookie 检查用户的身份
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}"); // cookie错误
			return ;
		}
	
		if (m_userSql.modifyData(phone, new String[]{name, birthday, height, weight, city, sex, sign,work}))
			wrt.write("{\"status\":\"1\"}");
		else
			wrt.write("{\"status\":\"0\"}"); // 未知错误
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
