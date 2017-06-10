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
import com.cloudlife.utils.EncodeAndDecode;

// 利用注入直接配置网页 更方便
@WebServlet(
		urlPatterns={"/user/register"},
			name="Register"	)
public class Register extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IUserSql m_userSql = new UserSqlImp();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String phone = req.getParameter("phone");
		String passwd = req.getParameter("password");
		resp.setContentType("text/html;charset=utf-8");
		int statusCode = 1;
		// 先判断下密码是否过短
		if (phone == null | phone.length() != 11)
			statusCode = -2;
		else if (passwd == null| passwd.length()< 6)
			statusCode = 0;
		
		// 查询数据库 判断该手机号码是否已经注册了
		if(statusCode == 1) {
			if (m_userSql.isExists(phone)) {
				statusCode = -1;
			} else {
				// 先加入新的用户
				m_userSql.addUser(phone, passwd);
				// 设置cookie 
				// 第一个user的内容为手机号码+密码 进行MD5加密
				Cookie cookie = new Cookie("CLOUD_LIFE", EncodeAndDecode.md5(phone+passwd));
				// 第二个cookie为用户手机号码
				Cookie cookie2 = new Cookie("USER", phone);
				resp.addCookie(cookie);
				resp.addCookie(cookie2);
			}
		}
		// 返回状态码
		PrintWriter writer = resp.getWriter();
		writer.write("{\"status\":\""+ statusCode+ "\"}");
	//	if (statusCode == 1)
		//	req.getRequestDispatcher("/info.jsp").forward(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}





