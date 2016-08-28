package com.example.appengine.source;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.appengine.domain.Wikipedia;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.google.appengine.repackaged.com.google.gson.stream.JsonReader;

public class WikipediaSource {
	public static Wikipedia process(JsonReader reader) throws SourceException {
		JsonElement element = new JsonParser().parse(reader);

		element = element.getAsJsonObject().get("query").getAsJsonObject().get("pages");

		for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
			JsonObject object = entry.getValue().getAsJsonObject();

			Wikipedia wikipedia = new Wikipedia(object.get("title").getAsString());
			// set full content
			wikipedia.setContent(
					object.get("revisions").getAsJsonArray().get(0).getAsJsonObject().get("*").getAsString());

			// remove from content comments and other garbage
			removeContent(wikipedia, new String[] { "<!--", "-->" });
			// removeContent(wikipedia, new String[] { "<small>", "</small>" });

			// get all links in the content
			wikipedia.setLinks(getLinks(wikipedia));

			// process the infobox
			processInfobox(wikipedia);

			return wikipedia;
		}
		return null;

	}

	private static Set<String> getLinks(Wikipedia wikipedia) {
		List<String> allMatch = matchContent(wikipedia, new String[] { "[[", "]]" }, false, true);
		Set<String> matchSet = new HashSet<>();
		for (String match : allMatch) {
			String[] tokens = match.split("\\|");
			matchSet.add(tokens[0]);
		}

		return matchSet;
	}

	private static void removeContent(Wikipedia wikipedia, String[] tokens) {
		Matcher matcher = getMatcher(tokens, wikipedia.getContent());
		while (matcher.find()) {
			wikipedia.setContent(wikipedia.getContent().replace(matcher.group(), ""));
		}
	}

	private static List<String> matchContent(Wikipedia wikipedia, String[] tokens, boolean removeContent,
			boolean removeTokens) {
		Matcher matcher = getMatcher(tokens, wikipedia.getContent());

		List<String> matchList = new ArrayList<String>();
		while (matcher.find()) {
			String match = matcher.group();
			if (removeTokens) {
				match = match.substring(tokens[0].length(), match.length() - tokens[tokens.length - 1].length());
			}
			matchList.add(match.trim());
			if (removeContent) {
				wikipedia.setContent(wikipedia.getContent().replace(matcher.group(), ""));
			}
		}
		return matchList;
	}

	private static void processInfobox(Wikipedia wikipedia) throws SourceException {
		List<String> matchList = matchContent(wikipedia, new String[] { "{{Infobox", "}}" }, true, true);
		if (matchList.size() != 1) {
			throw new SourceException("only one infobox expected");
		} else {
			String infobox = matchList.get(0);

			Matcher matcher = getMatcher(new String[] { "|", "=", "$" }, infobox);
			List<String> parametersList = new ArrayList<String>();
			while (matcher.find()) {
				String match = matcher.group();
				match = match.substring(1);
				parametersList.add(match.trim());
				infobox = infobox.replace(matcher.group(), "");
			}

			wikipedia.addTag(infobox);
			for (String parameter : parametersList) {
				String[] keyValue = parameter.split("=");
				if (keyValue.length > 1) {
					String key = keyValue[0].toUpperCase().trim();
					String value = keyValue[1].trim();
					wikipedia.setProperty(key, value);
				}
			}

			String value = wikipedia.getProperty("NAME");
			if (value != null && !value.equals(wikipedia.getTitle())) {
				throw new SourceException("page conflict name");
			} else {
				wikipedia.removeProperty("NAME");
			}

			value = wikipedia.getProperty("TYPE");
			if (value != null) {
				wikipedia.addTag(value);
				wikipedia.removeProperty("TYPE");
			}

			value = wikipedia.getProperty("LATNS");
			if (value != null) {
				try {
					int latd = Integer.parseInt(wikipedia.getProperty("LATD"));
					int latm = Integer.parseInt(wikipedia.getProperty("LATM"));
					int lats = Integer.parseInt(wikipedia.getProperty("LATS"));
					// wikipedia.getProperty("LATNS");
					int longd = Integer.parseInt(wikipedia.getProperty("LONGD"));
					int longm = Integer.parseInt(wikipedia.getProperty("LONGM"));
					int longs = Integer.parseInt(wikipedia.getProperty("LONGS"));
					// wikipedia.getProperty("LONGEW");

					float latitude = latd + latm / 60.0f + lats / 3600.0f;
					float longitude = longd + longm / 60.0f + longs / 3600.0f;

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

			System.out.println(wikipedia.getProperties());
		}
	}

	private static Matcher getMatcher(String regEx, String input) {
		Pattern patern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = patern.matcher(input);
		return matcher;
	}

	private static Matcher getMatcher(String[] tokens, String input) {
		String[] regExpTokens = new String[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			regExpTokens[i] = tokens[i];
			regExpTokens[i] = regExpTokens[i].replace("|", "\\|");
			regExpTokens[i] = regExpTokens[i].replace("[", "\\[");
			regExpTokens[i] = regExpTokens[i].replace("]", "\\]");
			regExpTokens[i] = regExpTokens[i].replace("{", "\\{");
			regExpTokens[i] = regExpTokens[i].replace("}", "\\}");
		}

		String regEx = regExpTokens[0];

		for (int i = 1; i < regExpTokens.length; i++) {
			regEx += "(.|\n)*?" + regExpTokens[i];
		}

		return getMatcher(regEx, input);
	}
}
