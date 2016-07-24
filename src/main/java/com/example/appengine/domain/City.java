package com.example.appengine.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class City {
	@Id
	private String name;
	// @Parent
	// private Key<Country> countryKey;
	@Index
	private String countryIso2;

	@SuppressWarnings("unused")
	private City() {
	}

	public City(String name, String countryIso2) {
		this.name = name;
		// this.countryKey = Key.create(Country.class, countryIso2);
		this.countryIso2 = countryIso2;
	}

	public String getName() {
		return name;
	}

	public String getCountryIso2() {
		return countryIso2;
	}
}
