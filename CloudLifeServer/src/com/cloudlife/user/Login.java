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
			// ��¼�ɹ�
			// ����cookie 
			// ��һ��user������Ϊ�ֻ�����+���� ����MD5����
			Cookie cookie = new Cookie("CLOUD_LIFE", EncodeAndDecode.md5(phone+pass));
			cookie.setPath("/CloudLife/");
			cookie.setMaxAge(30*24*60*60);
			// �ڶ���cookieΪ�û��ֻ�����
			Cookie cookie2 = new Cookie("USER", phone);
			cookie2.setPath("/CloudLife/");
			cookie2.setMaxAge(30*24*60*60);
			resp.addCookie(cookie);
			resp.addCookie(cookie2);
			
			// ��¼�ɹ������û��ĸ������� 
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
