package com.example.appengine.source;

import java.io.File;
import java.io.FileReader;

import org.junit.Test;

import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonParser;

public class SkyScannerSourceTest {
	@Test
	public void testAnywhere() throws Exception {
		File file = new File(getClass().getClassLoader().getResource("skyscanner/anywhere.json").getFile());
		JsonElement element = new JsonParser().parse(new FileReader(file));
		SkyScannerSource.process(element);
	}
}
