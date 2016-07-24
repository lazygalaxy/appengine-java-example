package com.example.appengine.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class WikipediaSourceServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(WikipediaSourceServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String title = request.getParameter("title");
		LOGGER.info("loading " + title + " wikipedia data!!!!!!!!!!!!!!");
	}
}
