package com.example.appengine.source;

import java.util.logging.Logger;

import com.google.appengine.repackaged.com.google.gson.JsonElement;

public class SkyScannerSource {
	private static final Logger LOGGER = Logger.getLogger(SkyScannerSource.class.getName());

	public static void process(JsonElement element) throws SourceException {
		JsonElement places = element.getAsJsonObject().get("Places");
		for (JsonElement place : places.getAsJsonArray()) {
			String type = place.getAsJsonObject().get("Type").getAsString();
			if (type.equals("Station")) {
				String skyscannerPlaceId = place.getAsJsonObject().get("PlaceId").getAsString();
				String iataCode = place.getAsJsonObject().get("IataCode").getAsString();
				String airportName = place.getAsJsonObject().get("Name").getAsString();
				String skyscannerCode = place.getAsJsonObject().get("SkyscannerCode").getAsString();
				String cityName = place.getAsJsonObject().get("CityName").getAsString();
				String cityId = place.getAsJsonObject().get("CityId").getAsString();
				String countryName = place.getAsJsonObject().get("CountryName").getAsString();
				// LOGGER.info(place.toString());
			}
		}

		JsonElement quotes = element.getAsJsonObject().get("Quotes");
		for (JsonElement quote : quotes.getAsJsonArray()) {
			String direct = quote.getAsJsonObject().get("Direct").getAsString();
			if (direct.equals("true")) {
				String price = quote.getAsJsonObject().get("MinPrice").getAsString();
				LOGGER.info(quote.toString());
			}
		}
	}
}
