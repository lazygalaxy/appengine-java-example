package com.example.appengine.calendar;

import org.joda.time.DateTime;

public class Day {
	private int year;
	private int month;
	private int day;

	private Day() {
	}

	private Day(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	protected static Day get(DateTime dateTime) {
		return new Day(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth());
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}
}
