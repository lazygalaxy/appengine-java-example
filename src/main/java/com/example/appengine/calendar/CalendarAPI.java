package com.example.appengine.calendar;

import org.joda.time.DateTime;

import com.example.appengine.Constants;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

@Api(name = "calendar", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
		Constants.IOS_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })
public class CalendarAPI {
	@ApiMethod(name = "get", path = "get")
	public Calendar get(User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Calendar calendar = new Calendar();
		return calendar;
	}

	@ApiMethod(name = "set", path = "set", httpMethod = "post")
	public Calendar set(User user, Day day) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Calendar calendar = new Calendar();
		calendar.add(day);
		calendar.add(Day.get(new DateTime(2016, 3, 31, 0, 0)));
		return calendar;
	}

}
