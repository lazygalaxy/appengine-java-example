package com.example.appengine.source;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.appengine.domain.Wikipedia;
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

			List<String> match;
			// get the infobox and set it
			match = matchContent(wikipedia, new String[] { "{{Infobox", "}}" }, true, true);
			if (match.size() != 1) {
				throw new SourceException("only one infobox expected");
			} else {
				wikipedia.setInfobox(match.get(0));
			}

			// process the infobox
			match = matchInfobox(wikipedia, new String[] { "|", "=", "\n" }, true, true);

			System.out.println(wikipedia.getInfobox());

			return wikipedia;
		}
		return null;

	}

	private static String[] getLinks(Wikipedia wikipedia) {
		List<String> allMatch = matchContent(wikipedia, new String[] { "[[", "]]" }, false, true);
		Set<String> matchSet = new HashSet<>();
		for (String match : allMatch) {
			String[] tokens = match.split("\\|");
			matchSet.add(tokens[0]);
		}

		return matchSet.toArray(new String[matchSet.size()]);
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

	private static List<String> matchInfobox(Wikipedia wikipedia, String[] tokens, boolean removeContent,
			boolean removeTokens) {
		Matcher matcher = getMatcher(tokens, wikipedia.getInfobox());

		List<String> matchList = new ArrayList<String>();
		while (matcher.find()) {
			String match = matcher.group();
			if (removeTokens) {
				match = match.substring(tokens[0].length(), match.length() - tokens[tokens.length - 1].length());
			}
			matchList.add(match.trim());
			if (removeContent) {
				wikipedia.setInfobox(wikipedia.getInfobox().replace(matcher.group(), ""));
			}
		}
		return matchList;
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

		String strPattern = regExpTokens[0];

		for (int i = 1; i < regExpTokens.length; i++) {
			strPattern += "(.|\n)*?" + regExpTokens[i];
		}

		Pattern patern = Pattern.compile(strPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = patern.matcher(input);
		return matcher;
	}
}
