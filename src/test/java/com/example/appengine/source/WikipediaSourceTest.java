package com.example.appengine.source;

import java.io.File;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Test;

import com.example.appengine.domain.Wikipedia;
import com.google.appengine.repackaged.com.google.gson.stream.JsonReader;

public class WikipediaSourceTest {
	@Test
	public void testHeraionOfSamos() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Example.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource2.process(reader);

		test(wikipedia, "Heraion of Samos", 39, new String[] { "ANCIENT SITE", "SANCTUARY", "WHS", "CULTURAL" }, 18,
				37.67194747924805f, 26.885555267333984f, "{{Infobox ancient site");
	}

	@Test
	public void testFlorence() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Florence.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource2.process(reader);

		test(wikipedia, "Florence", 499, new String[] { "ITALIAN COMUNE" }, 22, 43.78333282470703f, 11.25f,
				"{{Hatnote|\"Firenze\" redirects here. For other uses, see Florence (disambiguation), Florentin (disambiguation), Florentine (disambiguation) or Firenze (disambiguation).}}");
	}

	@Test
	public void testPythagoreion() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Pythagoreion.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource2.process(reader);

		test(wikipedia, "Pythagoreion", 17, new String[] { "CULTURAL", "WHS" }, 10, 37.70121f, 26.868784f,
				"{{Infobox World Heritage Site");
	}

	@Test
	public void testUffizi() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Uffizi.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource2.process(reader);

		test(wikipedia, "Uffizi", 123,
				new String[] { "MUSEUM", "ART MUSEUM", "DESIGN/TEXTILE MUSEUM", "HISTORIC SITE" }, 7, 43.76864f,
				11.255214f, "{{More references|date=January 2016}}");
	}

	@Test
	public void testWordHeritageSites() throws Exception {
		File file = new File(getClass().getClassLoader()
				.getResource("wikipedia/List_of_World_Heritage_Sites_by_year_of_inscription.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource2.process(reader);

		test(wikipedia, "List of World Heritage Sites by year of inscription", 1192, new String[] {}, 0, null, null,
				"This is a list of the UNESCO World Heritage Sites around the world by year of inscription. (F) denotes the country's first inscription.");
	}

	@Test
	public void testExample() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Example.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource2.process(reader);

		test(wikipedia, "Heraion of Samos", 39, new String[] { "ANCIENT SITE", "SANCTUARY", "WHS", "CULTURAL" }, 18,
				37.67194747924805f, 26.885555267333984f, "{{Infobox ancient site");
	}

	private void test(Wikipedia wikipedia, String title, long totalLinks, String[] tags, long totalProps, Float lat,
			Float lon, String introduction) {
		Assert.assertEquals(title, wikipedia.getTitle());

		Assert.assertEquals(totalLinks, wikipedia.getLinks().size());

		Assert.assertEquals(tags.length, wikipedia.getTags().size());
		for (String tag : tags) {
			Assert.assertEquals(true, wikipedia.getTags().contains(tag));
		}

		Assert.assertEquals(totalProps, wikipedia.getProperties().size());

		if (lat != null) {
			Assert.assertEquals(lat, wikipedia.getLocation().getLatitude(), 0);
		} else {
			Assert.assertNull(wikipedia.getLocation());
		}
		if (lon != null) {
			Assert.assertEquals(lon, wikipedia.getLocation().getLongitude(), 0);
		} else {
			Assert.assertNull(wikipedia.getLocation());
		}

		Assert.assertEquals(introduction, wikipedia.getIntro());
	}
}
