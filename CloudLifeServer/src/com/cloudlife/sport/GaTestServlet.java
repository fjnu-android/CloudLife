package com.cloudlife.sport;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		urlPatterns="/sportTest",
		name="GaTestServlet")
public class GaTestServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		UserModel model = new UserModel();
		DataFactory fac = DataFactory.getInstance();
		Population pop = new Population();
		pop.createPop(20,  model);
		Ga ga = new Ga();
		ga.startRevolution(pop, model);
	}
}









