package com.example.appengine.source;

import java.io.File;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Test;

import com.example.appengine.domain.Wikipedia;
import com.example.appengine.source.wikipedia.WikipediaSource;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonParser;

public class WikipediaSource2Test {
	@Test
	public void testHeraionOfSamos() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Example.json").getFile());
		JsonElement element = new JsonParser().parse(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(element);

		test(wikipedia, "Heraion of Samos", 42, new String[] { "ANCIENT SITE", "SANCTUARY", "WHS", "CULTURAL" }, 19,
				37.67194747924805f, 26.885555267333984f, 6539, "The '''Heraion of Samos''' was a large sanctuary",
				"Kienast took charge of the excavations in 1976.");
	}

	@Test
	public void testPythagoreion() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Pythagoreion.json").getFile());
		JsonElement element = new JsonParser().parse(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(element);

		test(wikipedia, "Pythagoreion", 17, new String[] { "CULTURAL", "WHS" }, 10, 37.70121f, 26.868784f, 479,
				"The '''Pythagoreion''' is the archaeological site",
				"Pythagoreion was registered as a [[UNESCO]] [[World Heritage Site]] in 1992.");
	}

	@Test
	public void testFlorence() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Florence.json").getFile());
		JsonElement element = new JsonParser().parse(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(element);

		test(wikipedia, "Florence", 505, new String[] { "ITALIAN COMUNE" }, 24, 43.78333282470703f, 11.25f, 4067,
				"{{Hatnote|", "Texto en cursiva ''</ref>");
	}

	@Test
	public void testUffizi() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Uffizi.json").getFile());
		JsonElement element = new JsonParser().parse(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(element);

		test(wikipedia, "Uffizi", 124,
				new String[] { "MUSEUM", "ART MUSEUM", "DESIGN/TEXTILE MUSEUM", "HISTORIC SITE" }, 8, 43.76864f,
				11.255214f, 769, "{{More references|date=January 2016}}", "region of Tuscany, Italy.");
	}

	@Test
	public void testWordHeritageSites() throws Exception {
		File file = new File(getClass().getClassLoader()
				.getResource("wikipedia/List_of_World_Heritage_Sites_by_year_of_inscription.json").getFile());
		JsonElement element = new JsonParser().parse(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(element);

		test(wikipedia, "List of World Heritage Sites by year of inscription", 1192, new String[] {}, 0, null, null,
				214, "This is a list of the [[United Nations Educational", "denotes the country's first inscription.");
	}

	private void test(Wikipedia wikipedia, String title, long totalLinks, String[] tags, long totalProps, Float lat,
			Float lon, int introLength, String introStart, String introEnd) {
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

		Assert.assertEquals(introLength, wikipedia.getIntro().length());
		Assert.assertTrue(introStart, wikipedia.getIntro().startsWith(introStart));
		Assert.assertTrue(introStart, wikipedia.getIntro().endsWith(introEnd));
	}
}
