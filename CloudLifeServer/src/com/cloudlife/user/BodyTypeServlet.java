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
		urlPatterns="/user/bodyType",
		name="BodyTypeServlet"
		)
public class BodyTypeServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private IUserSql m_userSql = new UserSqlImp();
	private final String type="ABCDEFGHI";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PrintWriter wrt = resp.getWriter();
		String bodyType = req.getParameter("bodyType");
		if (TextUtils.isEmpty(bodyType) == true || bodyType.length() != 1||type.contains(bodyType) == false) {
			wrt.write("{\"status\":\"-2\"}");
			return;
		}
		
		// 获取cookie 检查用户的身份
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}");
			return ;
		}
		if(m_userSql.updateBodyType(phone, bodyType))
			wrt.write("{\"status\":\"1\"}");
		else
			wrt.write("{\"status\":\"0\"}");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}
}





