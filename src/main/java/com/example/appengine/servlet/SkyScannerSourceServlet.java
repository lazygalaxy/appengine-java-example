package com.example.appengine.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class SkyScannerSourceServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(SkyScannerSourceServlet.class.getName());

	// http://localhost:8080/tasks/skyscanner_source
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			URL url = new URL(
					"http://partners.api.skyscanner.net/apiservices/browsequotes/v1.0/CH/CHF/en-GB/CH/anywhere/2016-10/2016-11?apiKey=la915271491354291553495841285271");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");

			int respCode = conn.getResponseCode();
			if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				JsonElement element = new JsonParser().parse(reader);
				LOGGER.info(element.toString());
				reader.close();
			} else {
				LOGGER.log(Level.SEVERE, "error " + conn.getResponseCode() + " " + conn.getResponseMessage());
			}

			LOGGER.info("get skyscanner data");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
