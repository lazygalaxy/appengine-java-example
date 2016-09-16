package com.example.appengine.source.wikipedia;

import java.util.Map;

import com.example.appengine.domain.Wikipedia;
import com.example.appengine.source.SourceException;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;

public class WikipediaSource {

	public static Wikipedia process(JsonElement element) throws SourceException {
		element = element.getAsJsonObject().get("query").getAsJsonObject().get("pages");

		for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
			JsonObject object = entry.getValue().getAsJsonObject();

			Wikipedia wikipedia = new Wikipedia(object.get("title").getAsString());
			String originalContent = object.get("revisions").getAsJsonArray().get(0).getAsJsonObject().get("*")
					.getAsString();

			// set original content
			// wikipedia.setOriginalContent(
			// object.get("revisions").getAsJsonArray().get(0).getAsJsonObject().get("*").getAsString());

			BlockHandler.commonHandlerList.clear();
			BlockHandler.commonHandlerList.add(new InfoboxBlockHandler());
			BlockHandler.commonHandlerList.add(new CoordBlockHandler());
			BlockHandler.commonHandlerList.add(new IntroductionBlockHandler());

			String lines[] = originalContent.split("\\r?\\n");
			MainBlockHandler mainBlockHandler = new MainBlockHandler();
			int index = mainBlockHandler.hanlde(wikipedia, lines, 0);
			if (index != lines.length) {
				throw new SourceException("page has not been completely handled");
			}

			return wikipedia;
		}
		return null;

	}

	// private static void removeContent(Wikipedia wikipedia, String[] tokens) {
	// Matcher matcher = getMatcher(tokens, wikipedia.getContent());
	// while (matcher.find()) {
	// wikipedia.setContent(wikipedia.getContent().replace(matcher.group(),
	// ""));
	// }
	// }

	// private static String matchContentSplit(Wikipedia wikipedia, String
	// startToken, String endToken,
	// boolean removeContent) throws SourceException {
	// String[] matchStart = wikipedia.getContent().split(startToken);
	// if (matchStart.length == 1) {
	// return null;
	// } else if (matchStart.length == 2) {
	// String[] matchEnd = matchStart[1].split(endToken);
	// if (matchEnd.length < 2) {
	// throw new SourceException("unexpected number of end tokens");
	// }
	// if (removeContent) {
	// // TODO revisit this removal logi
	// // wikipedia.setContent(matchStart[0]);
	// // for (int i = 1; i < matchEnd.length; i++) {
	// // wikipedia.setContent(wikipedia.getContent() + matchEnd[1]);
	// // }
	// }
	// return matchEnd[0];
	// } else {
	// throw new SourceException("unexpected number of start tokens");
	// }
	// }

	// private static List<String> matchContentRegEx(Wikipedia wikipedia,
	// String[] tokens, boolean removeContent,
	// boolean removeTokens, int total) {
	// Matcher matcher = getMatcher(tokens, wikipedia.getContent());
	//
	// List<String> matchList = new ArrayList<String>();
	// while (matcher.find() && (matchList.size() < total || total == 0)) {
	// String match = matcher.group();
	// if (removeTokens) {
	// match = match.substring(tokens[0].length(), match.length() -
	// tokens[tokens.length - 1].length());
	// }
	// matchList.add(match.trim());
	// if (removeContent) {
	// wikipedia.setContent(wikipedia.getContent().replace(match, ""));
	// }
	// }
	// return matchList;
	// }

}
