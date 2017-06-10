package com.cloudlife.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.cloudlife.user.db.IUserSql;
import com.cloudlife.user.db.UserSqlImp;
import com.cloudlife.utils.EncodeAndDecode;
import com.cloudlife.utils.TextUtils;

@WebServlet (
		urlPatterns={"/user/login"},
		name="Login"
		)
public class Login extends HttpServlet {

	private IUserSql m_userSql = new UserSqlImp();
	
	private static final long serialVersionUID = 1L;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		String phone = req.getParameter("phone");
		String pass = req.getParameter("password");
		PrintWriter wrt = resp.getWriter();
		
		if (TextUtils.isEmpty(phone)==true ||
				TextUtils.isEmpty(pass)==true ||
				m_userSql.isExists(phone) == false) {
			wrt.write("{\"status\":\"-2\"}");
			return;
		}
		int nRes =  m_userSql.login(phone, pass);
		if (nRes == 1) {
			// 登录成功
			// 设置cookie 
			// 第一个user的内容为手机号码+密码 进行MD5加密
			Cookie cookie = new Cookie("CLOUD_LIFE", EncodeAndDecode.md5(phone+pass));
			cookie.setPath("/CloudLife/");
			cookie.setMaxAge(30*24*60*60);
			// 第二个cookie为用户手机号码
			Cookie cookie2 = new Cookie("USER", phone);
			cookie2.setPath("/CloudLife/");
			cookie2.setMaxAge(30*24*60*60);
			resp.addCookie(cookie);
			resp.addCookie(cookie2);
			
			// 登录成功返回用户的各个数据 
			wrt.write(m_userSql.getData(phone));
		} else
			wrt.write("{\"status\":\""+ nRes+"\"}");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doPost(req, resp);
	}
}
