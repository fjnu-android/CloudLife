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
import com.cloudlife.utils.TextUtils;

@WebServlet(
		urlPatterns="/getSocialData",
		name="GetSocialDataServlet")
public class GetSocialDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IUserDealSql sql = new UserDealSqlImp();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter wrt = resp.getWriter();

		// 获取cookie 检查用户的身份
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}"); // cookie错误
			return ;
		}
		
		String city = req.getParameter("city");
		int iBegin = Integer.parseInt(req.getParameter("begin"));
		int iEnd = Integer.parseInt(req.getParameter("end"));
		long ts =  Long.parseLong(req.getParameter("ts"));

		if (ts == 0) {
			ts = System.currentTimeMillis();
		}
		
		if (TextUtils.isEmpty(city)) {
			// 如果城市为空  则返回全国的记录
			wrt.write(sql.getCityBynamic(iBegin, iEnd, ts));
		} else {
			wrt.write(sql.getCityBynamic(city, iBegin, iEnd, ts));
		}
		
	}
}






