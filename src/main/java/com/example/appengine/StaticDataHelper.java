package com.example.appengine;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.example.appengine.domain.Country;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

/**
 * StaticDataHelper, a ServletContextListener, is setup in web.xml to run before
 * a JSP is run.
 **/
public class StaticDataHelper implements ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(StaticDataHelper.class.getName());

	public void contextInitialized(ServletContextEvent event) {
		LOGGER.info("loading static data");

		Key<Country> key = Key.create(Country.class, "GR");
		Country country = ObjectifyService.ofy().load().key(key).now();
		if (country == null) {

		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		// App Engine does not currently invoke this method.
	}
}
