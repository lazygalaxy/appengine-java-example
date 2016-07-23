package com.example.appengine.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class City {
	@Id
	private String name;
	@Parent
	private Key<Country> countryKey;

	public City(String name, String countryKey) {
		this.name = name;
		this.countryKey = Key.create(Country.class, countryKey);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
