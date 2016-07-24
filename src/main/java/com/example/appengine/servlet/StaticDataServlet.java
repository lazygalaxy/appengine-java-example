package com.example.appengine.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class StaticDataServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(StaticDataServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String data = request.getParameter("data");
		LOGGER.info("loading " + data + " static data!!!!!!!!!!!!!!");
	}
}
