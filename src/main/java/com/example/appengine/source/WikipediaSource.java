package com.example.appengine.source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

			// remove from content items
			// TODO: overview these to ensure important info is not removed
			removeContent(wikipedia, new String[] { "<!--", "-->" });
			removeContent(wikipedia, new String[] { "<ref>", "</ref>" });

			processLinks(wikipedia);
			processInfobox(wikipedia);
			processCoords(wikipedia);

			// set introductions
			List<String> matchList = matchContentRegEx(wikipedia, new String[] { "^", "$" }, true, false, 1);
			if (matchList.size() == 1) {
				wikipedia.setIntro(matchList.get(0));
			} else {
				throw new SourceException("error retreiving introduction");
			}

			// System.out.println(wikipedia.getContent());

			return wikipedia;
		}
		return null;

	}

	private static void removeContent(Wikipedia wikipedia, String[] tokens) {
		Matcher matcher = getMatcher(tokens, wikipedia.getContent());
		while (matcher.find()) {
			wikipedia.setContent(wikipedia.getContent().replace(matcher.group(), ""));
		}
	}

	private static void processLinks(Wikipedia wikipedia) throws SourceException {
		// find all links
		Matcher matcher = getMatcher(new String[] { "[[", "]]" }, wikipedia.getContent());

		// go through each link and replace the content accordingly
		while (matcher.find()) {
			String fullMatch = matcher.group();
			String match = fullMatch.substring(2, fullMatch.length() - 2);
			String[] tokens = match.split("\\|");
			if (tokens.length == 1) {
				wikipedia.setContent(wikipedia.getContent().replace(fullMatch, tokens[0]));
			} else if (tokens.length == 2) {
				wikipedia.setContent(wikipedia.getContent().replace(fullMatch, tokens[1]));
			} else if (tokens[0].startsWith("File:") || tokens[0].startsWith("Image:")) {
				// TODO: better process files to store photo information
				wikipedia.setContent(wikipedia.getContent().replace(fullMatch, tokens[tokens.length - 1]));
			} else {
				throw new SourceException(fullMatch + " link could not be processed");
			}
			wikipedia.addLink(tokens[0]);
		}
	}

	private static void processInfobox(Wikipedia wikipedia) throws SourceException {
		String infobox = matchContentSplit(wikipedia, "\\{\\{Infobox", "\n\\}\\}", true);
		if (infobox != null) {
			String[] infoboxTokens = infobox.split("\\|");
			wikipedia.addTag(infoboxTokens[0]);
			for (int i = 1; i < infoboxTokens.length; i++) {
				String[] keyValue = infoboxTokens[i].trim().split("=");
				if (keyValue.length == 2) {
					String key = keyValue[0].toUpperCase().trim().replaceAll(" ", "_");
					String value = keyValue[1].trim();
					wikipedia.setProperty(key, value);
				} else {
					// TODO: processing of certain properties does not work as
					// expected
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
			}

			for (int i = 1; i <= 10; i++) {
				String key = "DESIGNATION" + i;
				value = wikipedia.getProperty(key);
				if (value != null) {
					wikipedia.addTag(value);
					value = wikipedia.getProperty(key + "_TYPE");
					wikipedia.addTag(value);
				} else {
					break;
				}
			}
		}
	}

	private static void processCoords(Wikipedia wikipedia) throws SourceException {
		// try to get coords from properties
		String value = wikipedia.getProperty("LATD");
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

		// try to get coords from coord block
		String coordMatch = matchContentSplit(wikipedia, "\\{\\{coord\\|", "\\}\\}", true);
		if (coordMatch != null) {
			String[] coords = coordMatch.split("\\|");
			if (coords.length <= 6) {
				float latitude = Float.parseFloat(coords[0]);
				float longitude = Float.parseFloat(coords[2]);
				wikipedia.setLocation(new GeoPt(latitude, longitude));
			} else {
				float latitude = caluclateDecDeg(coords[0], coords[1], coords[2]);
				float longitude = caluclateDecDeg(coords[4], coords[5], coords[6]);
				wikipedia.setLocation(new GeoPt(latitude, longitude));
			}
		}
	}

	private static float caluclateDecDeg(String degStr, String minStr, String secStr) {
		float deg = 0.0f;
		float min = 0.0f;
		float sec = 0.0f;

		if (degStr != null) {
			deg = Float.parseFloat(degStr);
		}

		if (minStr != null) {
			min = Float.parseFloat(minStr);
		}

		if (secStr != null) {
			sec = Float.parseFloat(secStr);
		}

		return deg + min / 60 + sec / 3600;
	}

	private static String matchContentSplit(Wikipedia wikipedia, String startToken, String endToken,
			boolean removeContent) throws SourceException {
		String[] matchStart = wikipedia.getContent().split(startToken);
		if (matchStart.length == 1) {
			return null;
		} else if (matchStart.length == 2) {
			String[] matchEnd = matchStart[1].split(endToken);
			if (matchEnd.length < 2) {
				throw new SourceException("unexpected number of end tokens");
			}
			if (removeContent) {
				// TODO: consider if this functionaity is necessary
				// wikipedia.setContent(matchStart[0]);
				// for (int i = 1; i < matchEnd.length; i++) {
				// wikipedia.setContent(wikipedia.getContent() + matchEnd[1]);
				// }

			}
			return matchEnd[0];
		} else {
			throw new SourceException("unexpected number of start tokens");
		}
	}

	private static List<String> matchContentRegEx(Wikipedia wikipedia, String[] tokens, boolean removeContent,
			boolean removeTokens, int total) {
		Matcher matcher = getMatcher(tokens, wikipedia.getContent());

		List<String> matchList = new ArrayList<String>();
		while (matcher.find() && (matchList.size() < total || total == 0)) {
			String match = matcher.group();
			if (removeTokens) {
				match = match.substring(tokens[0].length(), match.length() - tokens[tokens.length - 1].length());
			}
			matchList.add(match.trim());
			if (removeContent) {
				wikipedia.setContent(wikipedia.getContent().replace(match, ""));
			}
		}
		return matchList;
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
