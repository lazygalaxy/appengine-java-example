package com.example.appengine.domain;

import com.google.appengine.api.datastore.GeoPt;

public class Wikipedia {
	private String title;
	private String[] links;
	private GeoPt location;
	private String intro;

	private String infobox;
	private String content;

	@SuppressWarnings("unused")
	private Wikipedia() {
	}

	public Wikipedia(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String[] getLinks() {
		return links;
	}

	public void setLinks(String[] links) {
		this.links = links;
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

	public String getInfobox() {
		return infobox;
	}

	public void setInfobox(String infobox) {
		this.infobox = infobox.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content.trim();
	}
}
