package com.example.appengine.source.wikipedia;

import java.util.List;

import com.example.appengine.source.SourceException;

public class IntroductionBlockHandler extends BlockHandler {
	@Override
	protected List<BlockHandler> getBlockHandlerList() {
		return commonHandlerList;
	}

	@Override
	public boolean isStart(String line) {
		return true;
	}

	@Override
	public boolean isBody(String line) {
		return true;
	}

	@Override
	public boolean isEnd(String line) {
		return line.startsWith("=") && line.endsWith("=");
	}

	@Override
	protected void processStart(String line) throws SourceException {
		throw new SourceException("not required");
	}

	@Override
	protected int processEnd(String line, int index) throws SourceException {
		return index - 1;
	}

	@Override
	protected void processLine(String line) throws SourceException {
		if (wikipedia.getIntro() == null) {
			wikipedia.setIntro("");
		}
		wikipedia.setIntro(wikipedia.getIntro() + line + "\r\n");
	}

}
