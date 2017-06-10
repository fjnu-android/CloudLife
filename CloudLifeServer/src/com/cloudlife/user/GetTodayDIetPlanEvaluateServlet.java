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
import com.cloudlife.utils.TextUtils;

@WebServlet(
		urlPatterns="/getTodayDietRmdEva",
		name="GetTodayDIetPlanEvaluateServlet")
public class GetTodayDIetPlanEvaluateServlet extends HttpServlet {

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
		String type = req.getParameter("type");
		if (TextUtils.isEmpty(type)) {
			wrt.write("{\"status\":\"0\"}"); // ²ÎÊýÓÐ´í
			return;
		}
		
		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}"); // cookie´íÎó
			return ;
		}
		
		System.out.println("type:"+ type);
		String ret = m_userPlanSql.getTodayDietPlanEvaluate(phone,type.equals("dish")?0:1);
		if (ret == null)
			ret = "";
		wrt.write(ret);
	}
	
}
