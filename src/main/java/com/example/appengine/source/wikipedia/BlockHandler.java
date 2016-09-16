package com.example.appengine.source.wikipedia;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.example.appengine.domain.Wikipedia;
import com.example.appengine.source.SourceException;

public abstract class BlockHandler {
	public static List<BlockHandler> commonHandlerList = new ArrayList<BlockHandler>();

	private static List<BlockHandler> EMPTY_BLOCK_HANDLERS = new ArrayList<BlockHandler>();

	protected Wikipedia wikipedia;

	public abstract boolean isStart(String line);

	public abstract boolean isBody(String line);

	public abstract boolean isEnd(String line);

	protected abstract void processStart(String line) throws SourceException;

	protected abstract int processEnd(String line, int index) throws SourceException;

	protected abstract void processLine(String line) throws SourceException;

	protected List<BlockHandler> getBlockHandlerList() {
		return EMPTY_BLOCK_HANDLERS;
	}

	public int hanlde(Wikipedia wikipedia, String[] lines, int index) throws SourceException {
		this.wikipedia = wikipedia;

		int i = index;
		for (; i < lines.length; i++) {
			String line = lines[i].trim();
			if (StringUtils.isNotBlank(line)) {
				processLinks(line);
				String normalizeLine = line.toUpperCase();

				boolean found = false;
				for (BlockHandler handler : getBlockHandlerList()) {
					if (handler.isStart(normalizeLine)) {
						if (handler.mustRemove()) {
							getBlockHandlerList().remove(handler);
						}
						i = handler.hanlde(wikipedia, lines, i);
						found = true;
						break;
					}
				}

				if (!found) {
					if (isEnd(normalizeLine)) {
						return processEnd(line, i);
					} else if (isBody(normalizeLine)) {
						processLine(line);
					} else if (isStart(normalizeLine)) {
						processStart(line);
					} else {
						throw new SourceException("unexpected wikipedia line: " + line);
					}
				}
			}
		}
		return i;
	}

	public boolean mustRemove() {
		return true;
	}

	protected static float caluclateDecDeg(String degStr, String minStr, String secStr) {
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

	private void processLinks(String line) throws SourceException {
		// find all links
		Matcher matcher = getMatcher(new String[] { "[[", "]]" }, line);

		// go through each link and replace the content accordingly
		while (matcher.find()) {
			String fullMatch = matcher.group();
			String match = fullMatch.substring(2, fullMatch.length() - 2);
			String[] tokens = match.split("\\|");
			// if (tokens.length == 1) {
			// wikipedia.setContent(wikipedia.getContent().replace(fullMatch,
			// tokens[0]));
			// } else if (tokens.length == 2) {
			// wikipedia.setContent(wikipedia.getContent().replace(fullMatch,
			// tokens[1]));
			// } else if (tokens[0].startsWith("File:") ||
			// tokens[0].startsWith("Image:")) {
			// // TODO: better process files to store photo information
			// wikipedia.setContent(wikipedia.getContent().replace(fullMatch,
			// tokens[tokens.length - 1]));
			// } else {
			// throw new SourceException(fullMatch + " link could not be
			// processed");
			// }
			this.wikipedia.addLink(tokens[0]);
		}
	}

	private Matcher getMatcher(String regEx, String input) {
		Pattern patern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = patern.matcher(input);
		return matcher;
	}

	private Matcher getMatcher(String[] tokens, String input) {
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
