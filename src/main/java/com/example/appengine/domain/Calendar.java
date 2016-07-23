package com.example.appengine.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Calendar {
	final static public SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	@Id
	private String userId;
	private Date fromDate;
	private Date toDate;

	@SuppressWarnings("unused")
	private Calendar() {
	}

	public Calendar(String userId) throws Exception {
		this.userId = userId;

		// TODO: get all weekends + public holidays for user country
		this.fromDate = DATE_FORMAT.parse("20160326");
		this.toDate = DATE_FORMAT.parse("20160327");
	}

	public String getUserId() {
		return userId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
