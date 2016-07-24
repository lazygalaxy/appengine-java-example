package com.example.appengine.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.example.appengine.domain.Country;

public class StaticDataSource {
	private static final Logger LOGGER = Logger.getLogger(StaticDataSource.class.getName());

	public static Map<String, Country> processCountry() throws Exception {
		File file = new File(StaticDataSource.class.getClassLoader().getResource("staticdata/country.csv").getFile());

		Map<String, Country> countryMap = new HashMap<String, Country>();

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		while ((line = br.readLine()) != null) {
			String[] info = line.split(",");
			if (line.contains("\"")) {
				info = processDoubleQuotes(info);
			}

			if (info.length != 4) {
				LOGGER.warning("malformated csv file row: " + Arrays.toString(info));
				continue;
			}

			Country country = new Country(info[0], info[1], info[2], info[3]);
			countryMap.put(country.getIso2(), country);
		}

		br.close();
		return countryMap;
	}

	private static String[] processDoubleQuotes(String[] data) {
		List<String> newData = new ArrayList<String>();
		String newElement = null;
		for (String element : data) {
			// something strange with the data, return it unchanged
			if ((element.startsWith("\"") && newElement != null) || (element.endsWith("\"") && newElement == null)) {
				return data;
			}

			if (element.startsWith("\"")) {
				newElement = element.substring(1, element.length());
			} else if (element.endsWith("\"")) {
				newElement += "," + element.substring(0, element.length() - 1);
				newData.add(newElement);
				newElement = null;
			} else if (newElement != null) {
				newElement += element;
			} else {
				newData.add(element);
			}
		}
		return newData.toArray(new String[newData.size()]);
	}
}
