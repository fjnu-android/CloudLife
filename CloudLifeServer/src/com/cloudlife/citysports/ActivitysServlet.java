package com.cloudlife.citysports;

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
		urlPatterns="/activity",
		name="ActivitysServlet")
public class ActivitysServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IUserSql m_userSql = new UserSqlImp();
	private IActivitySql m_actSql = new ActivitySqlImp();
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter wrt = resp.getWriter();

		System.out.println("-----------");
		// 获取cookie 检查用户的身份
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}"); // cookie错误
			return ;
		}
		
		String type = req.getParameter("type");
		System.out.println(type);
		if (TextUtils.isEmpty(type)) {
			wrt.write("{\"status\":\"-3\"}"); 
			return;
		}
		
		if (type.equals("build")) {
			String title = req.getParameter("title");
			float lng = Float.parseFloat(req.getParameter("lng"));
			float lat = Float.parseFloat(req.getParameter("lat"));
			String info = req.getParameter("info");
			String time_begin = req.getParameter("time_begin");
			String location_name = req.getParameter("locate");
			String city = req.getParameter("city");
			boolean b = m_actSql.bulidActivity(phone, title, lng, lat, info, time_begin, location_name,city);
		    if (b)
				wrt.write("{\"status\":\"1\"}"); 
		    else
				wrt.write("{\"status\":\"-2\"}"); 
			return;	
		} else if (type.equals("locate")) {
			System.out.println("==========");
			String city = req.getParameter("city");
			System.out.println(city);
			boolean b = m_actSql.updateLocation(phone, city);
		    if (b)
				wrt.write("{\"status\":\"1\"}"); 
		    else
				wrt.write("{\"status\":\"-2\"}"); 
		} else if (type.equals("delete")) {
			
			String aid = req.getParameter("aid");
			boolean b = m_actSql.deleteActivity(phone, aid);
		    if (b)
				wrt.write("{\"status\":\"1\"}"); 
		    else
				wrt.write("{\"status\":\"-2\"}"); 
		} else if (type.equals("update")) {
			
			String aid = req.getParameter("aid");
			String info = req.getParameter("info");
			boolean b = m_actSql.updateActivityInfo(phone, aid, info);
		    if (b)
				wrt.write("{\"status\":\"1\"}"); 
		    else
				wrt.write("{\"status\":\"-2\"}"); 
		} else if (type.equals("city_user")) {
			String city = req.getParameter("city");
			wrt.write(m_actSql.getSameCityUser(city));
		} else if (type.equals("city_act")) {
			String city = req.getParameter("city");
			wrt.write(m_actSql.getSameCityActivitys(city));
		} else if (type.equals("user_act")) {
			wrt.write(m_actSql.getUserActivitys(phone));
		} else if (type.equals("detail")) {
			String aid = req.getParameter("aid");
			wrt.write(m_actSql.getActivityDetailData(aid));
		} else if (type.equals("join")) {
			String aid = req.getParameter("aid");
			boolean b = m_actSql.joinActivity(phone, aid);
		    if (b)
				wrt.write("{\"status\":\"1\"}"); 
		    else
				wrt.write("{\"status\":\"0\"}"); 
		}
	}
}

















