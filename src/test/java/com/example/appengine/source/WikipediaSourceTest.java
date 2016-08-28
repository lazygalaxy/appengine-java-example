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
		Wikipedia wikipedia = WikipediaSource.process(reader);

		Assert.assertEquals("Heraion of Samos", wikipedia.getTitle());
		Assert.assertEquals(42, wikipedia.getLinks().size());
		Assert.assertEquals(2, wikipedia.getTags().size());
		Assert.assertEquals(17, wikipedia.getProperties().size());

		Assert.assertEquals(37.67194747924805, wikipedia.getLocation().getLatitude(), 0);
		Assert.assertEquals(26.885555267333984, wikipedia.getLocation().getLongitude(), 0);
	}

	// @Test
	// public void testFlorence() throws Exception {
	// File file = new
	// File(getClass().getClassLoader().getResource("wikipedia/Florence.json").getFile());
	// JsonReader reader = new JsonReader(new FileReader(file));
	// Wikipedia wikipedia = WikipediaSource.process(reader);
	//
	// Assert.assertEquals("Florence", wikipedia.getTitle());
	// }

	// @Test
	// public void testHeraionOfSamos() throws Exception {
	// File file = new
	// File(getClass().getClassLoader().getResource("wikipedia/Heraion_of_Samos.json").getFile());
	// JsonReader reader = new JsonReader(new FileReader(file));
	// Wikipedia wikipedia = WikipediaSource.process(reader);
	//
	// Assert.assertEquals("Heraion of Samos", wikipedia.getTitle());
	// }

	// @Test
	// public void testPythagoreion() throws Exception {
	// File file = new
	// File(getClass().getClassLoader().getResource("wikipedia/Pythagoreion.json").getFile());
	// JsonReader reader = new JsonReader(new FileReader(file));
	// Wikipedia wikipedia = WikipediaSource.process(reader);
	//
	// Assert.assertEquals("Pythagoreion", wikipedia.getTitle());
	// }

	// @Test
	// public void testUffizi() throws Exception {
	// File file = new
	// File(getClass().getClassLoader().getResource("wikipedia/Uffizi.json").getFile());
	// JsonReader reader = new JsonReader(new FileReader(file));
	// Wikipedia wikipedia = WikipediaSource.process(reader);
	//
	// Assert.assertEquals("Uffizi", wikipedia.getTitle());
	// }
	//
	// @Test
	// public void testWordHeritageSites() throws Exception {
	// File file = new File(getClass().getClassLoader()
	// .getResource("wikipedia/List_of_World_Heritage_Sites_by_year_of_inscription.json").getFile());
	// JsonReader reader = new JsonReader(new FileReader(file));
	// Wikipedia wikipedia = WikipediaSource.process(reader);
	//
	// Assert.assertEquals("List of World Heritage Sites by year of
	// inscription", wikipedia.getTitle());
	// }
}
