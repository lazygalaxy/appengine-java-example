package com.example.appengine.api;

import javax.inject.Named;

import com.example.appengine.Constants;
import com.example.appengine.domain.Preferences;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

@Api(name = "preferences", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
		Constants.IOS_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })
public class PreferencesAPI {
	@ApiMethod(name = "get", path = "get")
	public Preferences get(User user) throws Exception {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Key<Preferences> key = Key.create(Preferences.class, user.getUserId());
		Preferences preferences = ObjectifyService.ofy().load().key(key).now();
		if (preferences == null) {
			preferences = new Preferences(user.getUserId());
			ObjectifyService.ofy().save().entity(preferences).now();
		}

		return preferences;
	}

	@ApiMethod(name = "add_date", path = "add_date", httpMethod = "post")
	public Preferences addDate(User user, @Named("date") String date) throws Exception {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Key<Preferences> key = Key.create(Preferences.class, user.getUserId());
		Preferences preferences = ObjectifyService.ofy().load().key(key).now();
		if (preferences == null) {
			preferences = new Preferences(user.getUserId());
		}

		if (date != null) {
			preferences.addDate(date);
		}

		ObjectifyService.ofy().save().entity(preferences).now();

		return preferences;
	}

	@ApiMethod(name = "remove_date", path = "remove_date", httpMethod = "post")
	public Preferences removeDate(User user, @Named("date") String date) throws Exception {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Key<Preferences> key = Key.create(Preferences.class, user.getUserId());
		Preferences preferences = ObjectifyService.ofy().load().key(key).now();
		if (preferences == null) {
			preferences = new Preferences(user.getUserId());
		}

		if (date != null) {
			preferences.removeDate(date);
		}

		ObjectifyService.ofy().save().entity(preferences).now();

		return preferences;
	}

}
