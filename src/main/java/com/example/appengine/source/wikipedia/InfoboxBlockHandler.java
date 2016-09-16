package com.example.appengine.source.wikipedia;

import org.apache.commons.lang3.StringUtils;

import com.example.appengine.source.SourceException;
import com.google.appengine.api.datastore.GeoPt;

public class InfoboxBlockHandler extends BlockHandler {
	@Override
	public boolean isStart(String line) {
		return line.startsWith("{{INFOBOX");
	}

	@Override
	public boolean isBody(String line) {
		return line.startsWith("|");
	}

	@Override
	public boolean isEnd(String line) {
		return line.equals("}}");
	}

	@Override
	protected void processStart(String line) throws SourceException {
		wikipedia.addTag(line.substring(9));
	}

	@Override
	protected int processEnd(String line, int index) throws SourceException {
		String value = wikipedia.getProperty("NAME");
		if (value != null && !value.equals(wikipedia.getTitle())) {
			throw new SourceException("page conflict name");
		} else {
			wikipedia.removeProperty("NAME");
		}

		value = wikipedia.getProperty("TYPE");
		if (value != null) {
			wikipedia.addTag(value);
		}

		for (int j = 1; j <= 10; j++) {
			String key = "DESIGNATION" + j;
			value = wikipedia.getProperty(key);
			if (value != null) {
				wikipedia.addTag(value);
				value = wikipedia.getProperty(key + "_TYPE");
				wikipedia.addTag(value);
			} else {
				break;
			}
		}

		value = wikipedia.getProperty("LATD");
		if (value != null) {
			try {
				float latitude = caluclateDecDeg(wikipedia.getProperty("LATD"), wikipedia.getProperty("LATM"),
						wikipedia.getProperty("LATS"));
				float longitude = caluclateDecDeg(wikipedia.getProperty("LONGD"), wikipedia.getProperty("LONGM"),
						wikipedia.getProperty("LONGS"));

				wikipedia.setLocation(new GeoPt(latitude, longitude));

				wikipedia.removeProperty("LATD");
				wikipedia.removeProperty("LATM");
				wikipedia.removeProperty("LATS");
				wikipedia.removeProperty("LATNS");
				wikipedia.removeProperty("LONGD");
				wikipedia.removeProperty("LONGM");
				wikipedia.removeProperty("LONGS");
				wikipedia.removeProperty("LONGEW");

			} catch (Exception e) {
				throw new SourceException("invalid coordination definition");
			}
		}
		return index;
	}

	@Override
	protected void processLine(String line) throws SourceException {
		String[] tokens = line.split("\\|");
		for (String token : tokens) {
			String[] keyValue = token.trim().split("=");
			if (keyValue.length == 2 && StringUtils.isNotEmpty(keyValue[1])) {
				String key = keyValue[0].trim().toUpperCase();
				String value = keyValue[1].trim();
				wikipedia.setProperty(key, value);
			}
		}
	}
}
