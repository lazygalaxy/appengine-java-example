package com.example.appengine;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.example.appengine.domain.City;
import com.example.appengine.domain.Country;
import com.example.appengine.domain.Preferences;
import com.example.appengine.domain.Wikipedia;
import com.googlecode.objectify.ObjectifyService;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP
 * is run. This is required to let JSP's access Ofy.
 **/
public class OfyHelper implements ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(OfyHelper.class.getName());

	public void contextInitialized(ServletContextEvent event) {
		LOGGER.info("registering entities");

		// This will be invoked as part of a warmup request, or the first user
		// request if no warmup request.
		ObjectifyService.register(Preferences.class);
		ObjectifyService.register(City.class);
		ObjectifyService.register(Country.class);
		ObjectifyService.register(Wikipedia.class);
	}

	public void contextDestroyed(ServletContextEvent event) {
		// App Engine does not currently invoke this method.
	}
}