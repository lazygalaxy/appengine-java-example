package com.example.appengine.servlet;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.appengine.domain.Country;
import com.example.appengine.source.StaticDataSource;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class StaticDataServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(StaticDataServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			String data = request.getParameter("data");
			LOGGER.info("loading " + data + " static data");
			if (data.equals("country")) {
				handleCountry();
			}
			if (data.equals("city")) {
				handleCity();
			}
			LOGGER.info("completed " + data + " static data");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void handleCountry() throws Exception {
		Map<String, Country> countryMap = StaticDataSource.processCountry();
		for (Country country : countryMap.values()) {
			ObjectifyService.ofy().save().entity(country).now();
		}
	}

	private void handleCity() throws Exception {
		Map<String, Country> countryMap = StaticDataSource.processCountry();
		for (Country country : countryMap.values()) {
			ObjectifyService.ofy().save().entity(country).now();
		}
	}
}
