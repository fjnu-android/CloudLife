package com.cloudlife.social;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudlife.utils.Check;

@WebServlet(
		urlPatterns="/social/my",
		name="GetMyDynamicalDataServlet")
public class GetMyDynamicalDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IUserDealSql sql = new UserDealSqlImp();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter wrt = resp.getWriter();
		
		int iBegin = Integer.parseInt(req.getParameter("begin"));
		int iEnd = Integer.parseInt(req.getParameter("end"));

		// 获取cookie 检查用户的身份
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}"); // cookie错误
			return ;
		}
		
		wrt.write(sql.getMyDynamicData(phone, iBegin, iEnd));
	}
}









