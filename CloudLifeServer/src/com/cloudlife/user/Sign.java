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

@WebServlet(
		urlPatterns="/user/sign",
		name="Sign"
		)

public class Sign extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IUserSql m_userSql = new UserSqlImp();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		String sign = req.getParameter("sign");
		PrintWriter wrt = resp.getWriter();
		if (sign == null) {
			wrt.write("{\"status\":\"false\"}");
			return;
		}
	
		// 获取cookie 检查用户的身份
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		if (phone == null) {
			wrt.write("{\"status\":\"false\", \"reason\":\"Invalid cookie\"}");
			return ;
		}
		
		m_userSql.modifySign(phone, sign);
		wrt.write("{\"status\":\"success\"}");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doPost(req, resp);
	}
}











