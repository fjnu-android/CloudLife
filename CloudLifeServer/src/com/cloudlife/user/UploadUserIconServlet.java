package com.cloudlife.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.cloudlife.user.db.IUserSql;
import com.cloudlife.user.db.UserSqlImp;
import com.cloudlife.utils.Check;
import com.cloudlife.utils.EncodeAndDecode;

@WebServlet(
		urlPatterns="/user/icon_update",
		name="UploadUserIconServlet")
@MultipartConfig
public class UploadUserIconServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IUserSql m_userSql = new UserSqlImp();
	
	public void init(ServletConfig config)throws ServletException{  
		super.init(config);  
	}  
	
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
		
		String img = "";
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);  
		if (isMultipart == true) {
			Part part = req.getPart("file");
			if (part == null) {
				wrt.write("{\"status\":\"0\"}");
				return;
			} else {
				// 图片命名方式: 用户手机号码（id）进行md5加密后+ 时间戳
				img = "/user/icon/"+EncodeAndDecode.md5(phone)+ System.currentTimeMillis()+".jpg";
				// 获取当前应用存放用户发帖图片的目录
				String dir = getServletContext().getRealPath("/")+"img";
				System.out.println("传图片地址："+ dir);
				part.write(dir+img);
				m_userSql.uploadUserIcon(phone, img);
			}
			wrt.write("{\"status\":\"1\"}");
			return;
		}
		wrt.write("{\"status\":\"0\"}");
	}
}











