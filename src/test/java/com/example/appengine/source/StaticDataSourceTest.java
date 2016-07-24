package com.example.appengine.source;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.example.appengine.domain.Country;

public class StaticDataSourceTest {
	@Test
	public void testCountry() throws Exception {
		Map<String, Country> countryMap = StaticDataSource.processCountry();

		Assert.assertEquals(249, countryMap.size());
		Assert.assertEquals("Taiwan, Province of China", countryMap.get("TW").getName());

	}
}
