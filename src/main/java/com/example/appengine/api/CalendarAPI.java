package com.example.appengine.api;

import javax.inject.Named;

import com.example.appengine.Constants;
import com.example.appengine.domain.Calendar;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

@Api(name = "calendar", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
		Constants.IOS_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })
public class CalendarAPI {
	@ApiMethod(name = "get", path = "get")
	public Calendar get(User user) throws Exception {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Key<Calendar> key = Key.create(Calendar.class, user.getUserId());
		Calendar calendar = ObjectifyService.ofy().load().key(key).now();
		if (calendar == null) {
			calendar = new Calendar(user.getUserId());
			ObjectifyService.ofy().save().entity(calendar).now();
		}

		return calendar;
	}

	@ApiMethod(name = "set", path = "set", httpMethod = "post")
	public Calendar set(User user, @Named("fromDate") String fromDate, @Named("toDate") String toDate)
			throws Exception {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Key<Calendar> key = Key.create(Calendar.class, user.getUserId());
		Calendar calendar = ObjectifyService.ofy().load().key(key).now();
		if (calendar == null) {
			calendar = new Calendar(user.getUserId());
		}

		if (fromDate != null) {
			calendar.setFromDate(Calendar.DATE_FORMAT.parse(fromDate));
		}

		if (toDate != null) {
			calendar.setToDate(Calendar.DATE_FORMAT.parse(toDate));
		}

		ObjectifyService.ofy().save().entity(calendar).now();

		return calendar;
	}

}
