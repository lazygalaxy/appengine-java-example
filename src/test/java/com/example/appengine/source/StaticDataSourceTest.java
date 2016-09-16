package com.example.appengine.source;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.example.appengine.domain.Country;

public class StaticDataSourceTest {
	@Test
	public void testCountryCSV() throws Exception {
		Map<String, Country> countryMap = StaticDataSource.processCountryCSV();

		Assert.assertEquals(249, countryMap.size());
		Assert.assertEquals("Taiwan, Province of China", countryMap.get("TW").getName());

	}

	@Test
	public void testCountryJSON() throws Exception {
		Map<String, Country> countryMap = StaticDataSource.processCountryJSON();

		Assert.assertEquals(234, countryMap.size());
		Assert.assertEquals("Greece", countryMap.get("GR").getName());
	}
}
