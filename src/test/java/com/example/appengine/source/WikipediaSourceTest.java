package com.example.appengine.source;

import java.io.File;
import java.io.FileReader;

import org.junit.Assert;
import org.junit.Test;

import com.example.appengine.domain.Wikipedia;
import com.google.appengine.repackaged.com.google.gson.stream.JsonReader;

public class WikipediaSourceTest {
	@Test
	public void testFlorence() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Florence.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(reader);

		Assert.assertEquals("Florence", wikipedia.getTitle());
	}

	@Test
	public void testHeraionOfSamos() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Heraion_of_Samos.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(reader);

		Assert.assertEquals("Heraion of Samos", wikipedia.getTitle());
	}

	@Test
	public void testPythagoreion() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Pythagoreion.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(reader);

		Assert.assertEquals("Pythagoreion", wikipedia.getTitle());
	}

	@Test
	public void testUffizi() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("wikipedia/Uffizi.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(reader);

		Assert.assertEquals("Uffizi", wikipedia.getTitle());
	}

	@Test
	public void testWordHeritageSites() throws Exception {
		File file = new File(getClass().getClassLoader()
				.getResource("wikipedia/List_of_World_Heritage_Sites_by_year_of_inscription.json").getFile());
		JsonReader reader = new JsonReader(new FileReader(file));
		Wikipedia wikipedia = WikipediaSource.process(reader);

		Assert.assertEquals("List of World Heritage Sites by year of inscription", wikipedia.getTitle());
	}
}
