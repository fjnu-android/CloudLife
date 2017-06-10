package com.cloudlife.resource;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		urlPatterns={"/get"},
		name="GetResourceServlet"
		)
public class GetResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		String url = req.getParameter("url");
		String type = req.getParameter("type");
		int page = Integer.parseInt(req.getParameter("page"));
		PrintWriter wrt = resp.getWriter();
		if (url.equals(null) || url == "") {
			
			wrt.write("<script type=\"text/javascript\">window.location.href='/Crawler';</script>");
			return;
		}
		System.out.println("type:" + type);
		GetResource getRes = GetResource.getInstance();
		boolean bl = getRes.addResource(url, type, page);
		
		if (bl)
			wrt.write("<script type=\"text/javascript\">confirm(\"add crawler queue success\"); "+
				"window.location.href='/data';</script>");
		else
			wrt.write("<script type=\"text/javascript\">confirm(\"add crawler queue false\"); "+
					"window.location.href='/data';</script>");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doPost(req, resp);
	}
	

}
