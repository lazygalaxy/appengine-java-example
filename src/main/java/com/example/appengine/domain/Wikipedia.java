package com.example.appengine.domain;

import com.google.appengine.api.datastore.GeoPt;

public class Wikipedia {
	private String title;
	private GeoPt location;
	private String intro;

	@SuppressWarnings("unused")
	private Wikipedia() {
	}

	public Wikipedia(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public GeoPt getLocation() {
		return location;
	}

	public void setLocation(GeoPt location) {
		this.location = location;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
}
