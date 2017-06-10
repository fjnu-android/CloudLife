package com.cloudlife.resource;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		urlPatterns={"/2"},
		name="IndexServlet"
		)

public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static private String g_dirPath;
	
	static public String getRealPath() {
		return g_dirPath;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if(g_dirPath == null || g_dirPath == "") {
			g_dirPath = getServletContext().getRealPath("/");
			File file = new File (g_dirPath);
			file = new File(file.getParent()+ "/ROOT/img");
			if (file.exists() == false)
				file.mkdir();
			g_dirPath = file.getPath();
			
		System.out.println(g_dirPath);
		}
		
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		PublicData.gHasDeal = GetResource.getInstance().hasDealCount();
		PublicData.gNeedDeal = GetResource.getInstance().dealCount();
		req.getRequestDispatcher("/crawler_group.jsp").forward(req, resp);
	}

}
