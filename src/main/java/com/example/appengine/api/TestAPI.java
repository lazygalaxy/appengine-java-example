package com.example.appengine.api;

import com.example.appengine.Constants;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.ObjectifyService;

@Api(name = "test", version = "v1", clientIds = { Constants.API_EXPLORER_CLIENT_ID })
public class TestAPI {
	@ApiMethod(name = "loadcountry", path = "loadcountry")
	public void loadCountry() throws Exception {
		final Queue queue = QueueFactory.getDefaultQueue();
		queue.add(ObjectifyService.ofy().getTransaction(), TaskOptions.Builder.withUrl("/tasks/static_data_load"));
	}

	@ApiMethod(name = "loadcity", path = "loadcity")
	public void loadCity() throws Exception {
	}

	@ApiMethod(name = "loadwikipedia", path = "loadwikipedia")
	public void loadWikipedia() throws Exception {
	}
}