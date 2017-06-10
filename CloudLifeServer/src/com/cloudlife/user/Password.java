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

@WebServlet (
		urlPatterns={"/user/password"},
		name="Password"
		)
public class Password extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IUserSql m_UserSql = new UserSqlImp();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String prePass = req.getParameter("prePass");
		String newPass = req.getParameter("newPass");
		PrintWriter wrt = resp.getWriter();
		if (TextUtils.isEmpty(prePass) == true
				|| TextUtils.isEmpty(newPass) == true) {
			wrt.write("{\"status\":\"false\"}");
			return;
		}
		
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		if (TextUtils.isEmpty(phone)) {
			wrt.write("{\"status\":\"false\", \"reason\":\"Invalid cookie\"}");;
			return;
		}
		
		if (m_UserSql.changePass(phone, prePass, newPass)) {
			wrt.write("{\"status\":\"success\"}");
			return;
		}
		wrt.write("{\"status\":\"false\"}");	
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
}
