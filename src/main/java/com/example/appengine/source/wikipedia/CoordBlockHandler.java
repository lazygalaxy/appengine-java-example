package com.example.appengine.source.wikipedia;

import com.example.appengine.source.SourceException;
import com.google.appengine.api.datastore.GeoPt;

public class CoordBlockHandler extends BlockHandler {

	@Override
	public boolean isStart(String line) {
		return line.startsWith("{{COORD|") && line.endsWith("}}");
	}

	@Override
	public boolean isBody(String line) {
		return false;
	}

	@Override
	public boolean isEnd(String line) {
		return isStart(line);
	}

	@Override
	protected void processStart(String line) throws SourceException {
		throw new SourceException("not required");

	}

	@Override
	protected int processEnd(String line, int index) throws SourceException {
		String coord = line.substring(8, line.length() - 2);
		String[] coords = coord.split("\\|");
		if (coords.length <= 6) {
			float latitude = Float.parseFloat(coords[0]);
			float longitude = Float.parseFloat(coords[2]);
			wikipedia.setLocation(new GeoPt(latitude, longitude));
		} else {
			float latitude = caluclateDecDeg(coords[0], coords[1], coords[2]);
			float longitude = caluclateDecDeg(coords[4], coords[5], coords[6]);
			wikipedia.setLocation(new GeoPt(latitude, longitude));
		}
		return index;
	}

	@Override
	protected void processLine(String line) throws SourceException {
		throw new SourceException("not required");
	}
}
