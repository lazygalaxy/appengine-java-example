package com.example.appengine.servlet;

import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.appengine.domain.City;
import com.example.appengine.domain.Country;
import com.example.appengine.source.StaticDataSource;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class StaticDataServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(StaticDataServlet.class.getName());

	// http://localhost:8080/tasks/static_data?data=city
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			String data = request.getParameter("data");
			if (data.equals("country")) {
				handleCountry();
			}
			if (data.equals("city")) {
				handleCity();
			}
			LOGGER.info("completed static data call");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void handleCountry() throws Exception {
		LOGGER.info("loading country static data");
		Map<String, Country> countryMap = StaticDataSource.processCountryCSV();
		for (Country country : countryMap.values()) {
			ObjectifyService.ofy().save().entity(country).now();
		}
		LOGGER.info("completed country static data");
	}

	private void handleCity() throws Exception {
		LOGGER.info("loading city static data");
		Map<String, City> cityMap = StaticDataSource.processCity();
		for (City city : cityMap.values()) {
			ObjectifyService.ofy().save().entity(city).now();
		}
		LOGGER.info("completed city static data");
	}
}
