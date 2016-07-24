package com.example.appengine.source;

import java.util.Map;

import com.example.appengine.domain.Wikipedia;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.google.appengine.repackaged.com.google.gson.stream.JsonReader;

public class WikipediaSource {
	public static Wikipedia execute(JsonReader reader) {
		JsonElement element = new JsonParser().parse(reader);

		element = element.getAsJsonObject().get("query").getAsJsonObject().get("pages");

		for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
			JsonObject object = entry.getValue().getAsJsonObject();

			Wikipedia wikipedia = new Wikipedia(object.get("title").getAsString());
			String content = object.get("revisions").getAsJsonArray().get(0).getAsJsonObject().get("*").getAsString();

			// TODO: complete wikipedia page parsing

			// System.out.println(content);
			return wikipedia;
		}
		return null;
	}
}
