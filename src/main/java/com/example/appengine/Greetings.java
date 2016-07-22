package com.example.appengine;

import java.util.ArrayList;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

/**
 * Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(name = "helloworld", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
		Constants.IOS_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })
public class Greetings {

	public static ArrayList<HelloGreeting> greetings = new ArrayList<HelloGreeting>();

	static {
		greetings.add(new HelloGreeting("hello world!"));
		greetings.add(new HelloGreeting("goodbye world!"));
	}

	public HelloGreeting getGreeting(@Named("id") Integer id) throws NotFoundException {
		try {
			return greetings.get(id);
		} catch (IndexOutOfBoundsException e) {
			throw new NotFoundException("Greeting not found with an index: " + id);
		}
	}

	public ArrayList<HelloGreeting> listGreeting() {
		return greetings;
	}

	@ApiMethod(name = "greetings.multiply", httpMethod = "post")
	public HelloGreeting insertGreeting(@Named("times") Integer times, HelloGreeting greeting) {
		HelloGreeting response = new HelloGreeting();
		StringBuilder responseBuilder = new StringBuilder();
		for (int i = 0; i < times; i++) {
			responseBuilder.append(greeting.getMessage());
		}
		response.setMessage(responseBuilder.toString());
		return response;
	}

	@ApiMethod(name = "greetings.authed", path = "hellogreeting/authed")
	public HelloGreeting authedGreeting(User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		HelloGreeting response = new HelloGreeting("hello " + user.getEmail());
		return response;
	}
}
