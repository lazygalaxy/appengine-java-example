package com.example.appengine.servelt;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class StaticDataLoadServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(StaticDataLoadServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.info("loading data!!!!!!!!!!!!!!");
	}
}
