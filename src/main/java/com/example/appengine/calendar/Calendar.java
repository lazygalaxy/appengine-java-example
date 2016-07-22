package com.example.appengine.calendar;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Calendar {
	public List<Day> freeDays = new ArrayList<Day>();

	public Calendar() {
		this.freeDays.add(Day.get(new DateTime(2016, 3, 25, 0, 0)));
		this.freeDays.add(Day.get(new DateTime(2016, 3, 26, 0, 0)));
		this.freeDays.add(Day.get(new DateTime(2016, 3, 27, 0, 0)));
		this.freeDays.add(Day.get(new DateTime(2016, 3, 28, 0, 0)));
	}

	public void add(Day day) {
		freeDays.add(day);
	}
}
