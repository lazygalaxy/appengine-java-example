package com.example.appengine.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Preferences {
	final static public SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	@Id
	private String userId;
	private String currency;
	private String originCity;
	private Set<Date> dates = new TreeSet<Date>();
	private Set<String> todoCities = new TreeSet<String>();
	private Set<String> todoSites = new TreeSet<String>();

	@SuppressWarnings("unused")
	private Preferences() {
	}

	public Preferences(String userId) throws Exception {
		this.userId = userId;

		// TODO: get all weekends + public holidays for user country
		addDate("20160326");
		addDate("20160327");
	}

	public String getUserId() {
		return userId;
	}

	public void addDate(String date) throws Exception {
		Date dateObj = DATE_FORMAT.parse(date);
		dates.add(dateObj);
	}

	public void removeDate(String date) throws Exception {
		Date dateObj = DATE_FORMAT.parse(date);
		dates.remove(dateObj);
	}

	public Set<Date> getDates() {
		return dates;
	}
}
