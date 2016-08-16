package com.example.appengine.source;

public class SourceException extends Exception {

	private static final long serialVersionUID = 1L;

	public SourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public SourceException(String message) {
		super(message);
	}

	public SourceException(Throwable cause) {
		super(cause);
	}

}
