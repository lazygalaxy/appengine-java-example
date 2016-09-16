package com.example.appengine.api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import com.example.appengine.Constants;
import com.example.appengine.domain.City;
import com.example.appengine.domain.Country;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

@Api(name = "travel", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
		Constants.IOS_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })

public class TravelAPI {
	@ApiMethod(name = "getCountry", path = "getCountry", httpMethod = ApiMethod.HttpMethod.GET)
	public List<Country> getCountry(@Named("countries") List<String> countries) throws Exception {
		if (countries == null || countries.size() == 0) {
			Query<Country> query = ObjectifyService.ofy().load().type(Country.class);
			return query.list();
		} else {
			List<Country> countryList = new ArrayList<Country>();
			for (String countryId : countries) {
				Key<Country> key = Key.create(Country.class, countryId);
				Country country = ObjectifyService.ofy().load().key(key).now();
				if (country != null) {
					countryList.add(country);
				}
			}
			return countryList;
		}
	}

	@ApiMethod(name = "getCity", path = "getCity", httpMethod = ApiMethod.HttpMethod.GET)
	public List<City> getCity(@Named("cities") List<String> cities) throws Exception {
		if (cities == null || cities.size() == 0) {
			Query<City> query = ObjectifyService.ofy().load().type(City.class);
			return query.list();
		} else {
			List<City> cityList = new ArrayList<City>();
			for (String cityId : cities) {
				Key<City> key = Key.create(City.class, cityId);
				City city = ObjectifyService.ofy().load().key(key).now();
				if (city != null) {
					cityList.add(city);
				}
			}
			return cityList;
		}
	}

	@ApiMethod(name = "getCityInCountry", path = "getCityInCountry", httpMethod = ApiMethod.HttpMethod.GET)
	public List<City> getCityInCountry(@Named("country") String country) throws Exception {
		if (country == null) {
			throw new BadRequestException("no country provided");
		}

		System.out.println(country);

		Query<City> query = ObjectifyService.ofy().load().type(City.class);
		query = query.filter(new FilterPredicate("countryIso2", FilterOperator.EQUAL, country));
		return query.list();
	}

	@ApiMethod(name = "test", path = "test", httpMethod = ApiMethod.HttpMethod.GET)
	public void test(@Named("title") String title) throws Exception {

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/tasks/wikipedia_source").param("title", title));
	}
}
