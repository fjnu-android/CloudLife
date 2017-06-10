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
		urlPatterns="/getDietData",
		name="GetDietDataServlet")
public class GetDietDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IUserSql m_userSql = new UserSqlImp();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		String name = req.getParameter("name");
		String type = req.getParameter("type");

		PrintWriter wrt = resp.getWriter();
		
		if (TextUtils.isEmpty(name)== true) {
			wrt.write("{\"status\":\"0\"}"); 
			return ;
		}

		String data ="" ;
		if (TextUtils.isEmpty(type))
			data = m_userSql.getDietDataByName(name);
		else {
			data = m_userSql.getDishDataByName(type, name);
		}
		if (data != null)
			wrt.write(data);
		else
			wrt.write("{\"status\":\"-2\"}");
	}
}












