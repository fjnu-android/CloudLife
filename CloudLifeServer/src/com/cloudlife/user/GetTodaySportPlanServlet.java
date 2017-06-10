package com.cloudlife.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudlife.user.db.IUserPlanSql;
import com.cloudlife.user.db.UserPlanSqlImp;
import com.cloudlife.utils.Check;

@WebServlet(
		urlPatterns="/getTodaySportRmd",
		name="GetTodaySportPlanServlet")
public class GetTodaySportPlanServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IUserPlanSql m_userPlanSql = new UserPlanSqlImp();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		PrintWriter wrt = resp.getWriter();

		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}"); // cookie错误
			return ;
		}

		// 获取用户运动方案推荐
		String str = m_userPlanSql.isTodaySportPlanExits(phone);
		wrt.write(str);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doPost(req, resp);
	}
}







