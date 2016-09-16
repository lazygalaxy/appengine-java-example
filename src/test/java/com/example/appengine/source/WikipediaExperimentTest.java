package com.example.appengine.source;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

import org.junit.Test;

import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.google.appengine.repackaged.com.google.gson.stream.JsonReader;

import info.bliki.wiki.model.WikiModel;

public class WikipediaExperimentTest {
	@Test
	public void testFlorence() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Florence.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		JsonElement element = new JsonParser().parse(reader);

		element = element.getAsJsonObject().get("query").getAsJsonObject().get("pages");

		for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
			JsonObject object = entry.getValue().getAsJsonObject();

			String TEST = object.get("revisions").getAsJsonArray().get(0).getAsJsonObject().get("*").getAsString();

			// WikiModel wikiModel = new
			// WikiModel("https://www.mywiki.com/wiki/${image}",
			// "https://www.mywiki.com/wiki/${title}");
			// String plainStr = wikiModel.render(new Plain§HTMLConverter(),
			// TEST);
			String plainStr = WikiModel.toHtml(TEST);
			System.out.print(plainStr);
		}

	}
}
