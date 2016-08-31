package com.example.appengine.domain;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.appengine.api.datastore.GeoPt;

public class Wikipedia {
	private String title;
	private Set<String> links = new HashSet<String>();
	private Set<String> tags = new HashSet<String>();
	private Map<String, String> properties = new LinkedHashMap<>();
	private GeoPt location;
	private String intro;
	private String content;

	@SuppressWarnings("unused")
	private Wikipedia() {
	}

	public Wikipedia(String title) {
		this.title = title.trim();
	}

	public String getTitle() {
		return title;
	}

	public Set<String> getLinks() {
		return links;
	}

	public void addLink(String link) {
		this.links.add(link.trim());
	}

	public void setLinks(Set<String> links) {
		this.links = links;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void addTag(String tag) {
		tag = tag.trim().toUpperCase();

		// normalize the value of certain tags
		if (tag.equals("WORLD HERITAGE SITE")) {
			tag = "WHS";
		}

		this.tags.add(tag);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public String getProperty(String key) {
		return properties.get(key);
	}

	public void setProperty(String key, String value) {
		this.properties.put(key, value);
	}

	public void removeProperty(String key) {
		this.properties.remove(key);
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
		this.intro = intro.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content.trim();
	}
}
