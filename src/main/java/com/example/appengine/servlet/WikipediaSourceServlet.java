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

import com.example.appengine.domain.Wikipedia;
import com.example.appengine.source.wikipedia.WikipediaSource;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.googlecode.objectify.ObjectifyService;

@SuppressWarnings("serial")
public class WikipediaSourceServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(WikipediaSourceServlet.class.getName());

	// http://localhost:8080/tasks/wikipedia_source?title=Pythagoreion
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			String title = request.getParameter("title");
			LOGGER.info("start process wikipedia " + title);

			URL url = new URL(
					"https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=json&titles="
							+ title);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");

			int respCode = conn.getResponseCode();
			if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				JsonElement element = new JsonParser().parse(reader);
				Wikipedia wikipedia = WikipediaSource.process(element);
				ObjectifyService.ofy().save().entity(wikipedia).now();
				LOGGER.info(wikipedia.getIntro());

				reader.close();
			} else {
				LOGGER.log(Level.SEVERE, "error " + conn.getResponseCode() + " " + conn.getResponseMessage());
			}

			LOGGER.info("end process wikipedia " + title);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
