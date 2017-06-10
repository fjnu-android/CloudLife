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
		urlPatterns="/getModelData",
		name="GetPlanModelDataServlet")
public class GetPlanModelDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IUserSql m_userSql = new UserSqlImp();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 获取cookie 检查用户的身份
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		PrintWriter wrt = resp.getWriter();
		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}"); // cookie错误
			return ;
		}
		
		String str = m_userSql.getModelData(phone);
		wrt.write(str);

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 获取cookie 检查用户的身份
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		PrintWriter wrt = resp.getWriter();
		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}"); // cookie错误
			return ;
		}

		String str = m_userSql.getModelData(phone);
		wrt.write(str);

	}
}







