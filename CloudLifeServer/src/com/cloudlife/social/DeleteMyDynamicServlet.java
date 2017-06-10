
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
		urlPatterns="/social/delete",
		name="DeleteMyDynamicServlet")
public class DeleteMyDynamicServlet extends HttpServlet {


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
		
		String sid = req.getParameter("sid");
		if (TextUtils.isEmpty(sid)) {
			wrt.write("{\"status\":\"0\"}"); 
			return;
		}
		
		boolean bl = sql.deleteMyDynamicData(phone, sid);
		if (bl == true) {
			wrt.write("{\"status\":\"1\"}"); 
		}else {
			wrt.write("{\"status\":\"-2\"}"); 
		}
	}
}






