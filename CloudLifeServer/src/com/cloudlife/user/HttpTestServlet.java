package com.cloudlife.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

@WebServlet(
		urlPatterns="/test",
		name="HttpTestServlet")
public class HttpTestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		JSONObject json = new JSONObject();
		
		JSONObject dish = new JSONObject();
		dish.put("type", "蔬菜类");
		dish.put("count", "2");
		dish.put("weight", "2");
		
		JSONObject bf = new JSONObject();
		bf.accumulate("dish", dish);
		dish.remove("type");
		dish.put("type", "水果/干果");
		bf.accumulate("dish", dish);
		
		json.put("breakfast", bf);
		
		JSONObject ln = new JSONObject();
		dish.remove("type");
		dish.put("type", "水果/干果");
		ln.accumulate("dish", dish);
		
		json.put("lunch", ln);
		
		JSONObject dn = new JSONObject();
		dish.remove("type");
		dish.put("type", "水果/干果");
		dn.accumulate("dish", dish);
		
		json.put("dinner", dn);
		
		System.out.println(json.toString());
	}
}










