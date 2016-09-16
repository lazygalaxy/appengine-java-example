package com.example.appengine.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class Country {
	@Id
	private String iso2;
	@Index
	private String name;
	private String iso3;
	private String num;

	@SuppressWarnings("unused")
	private Country() {
	}

	public Country(String iso2, String name, String iso3, String num) {
		this.iso2 = iso2;
		this.name = name;
		this.iso3 = iso3;
		this.num = num;
	}

	public String getIso2() {
		return iso2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIso3() {
		return iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
}
