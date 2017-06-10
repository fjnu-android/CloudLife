package com.cloudlife.social;

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

import com.cloudlife.utils.Check;
import com.cloudlife.utils.EncodeAndDecode;
import com.cloudlife.utils.TextUtils;

@WebServlet(
		urlPatterns="/user/social",
		name="PostDynamicServlet")
@MultipartConfig
public class PostDynamicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IUserDealSql sql = new UserDealSqlImp();

	public void init(ServletConfig config)throws ServletException{  
		super.init(config);  
	}  
	   
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter wrt = resp.getWriter();

		// ��ȡcookie ����û������
		Cookie[] cookies = req.getCookies();
		String phone = Check.isCookieValid(cookies);
		if (phone == null) {
			wrt.write("{\"status\":\"-1\"}"); // cookie����
			return ;
		}

		String content= req.getParameter("text");
		if (TextUtils.isEmpty(content)) {
			wrt.write("{\"status\":\"0\"}");
			return;
		}

		String img = "";
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);  
		if (isMultipart == true) {
			Part part = req.getPart("file");
			if (part == null)
				img = "";
			else {
				// ͼƬ������ʽ: �û��ֻ����루id������md5���ܺ�+ ʱ���
				img = "/user/dynamic/"+EncodeAndDecode.md5(phone)+ System.currentTimeMillis()+".jpg";
				// ��ȡ��ǰӦ�ô���û�����ͼƬ��Ŀ¼
				String dir = getServletContext().getRealPath("/")+"/img";
				part.write(dir+img);
			}
		}
		boolean bl = sql.postDynamic(phone, content, img);
		if (bl == true) {
			wrt.write("{\"status\":\"1\"}"); // ����̬�ɹ�
		} else {
			wrt.write("{\"status\":\"-2\"}"); // ����̬ʧ��  ����������
		}
	}
}

















