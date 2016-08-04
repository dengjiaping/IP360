package com.truthso.ip360.utils;



import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateFormatter implements JsonDeserializer<Date>,
		JsonSerializer<Date> {

	private final DateFormat[] formats;

	public DateFormatter() {
		this.formats = new DateFormat[3];
		this.formats[0] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		this.formats[1] = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z");
		this.formats[2] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		TimeZone timeZone = TimeZone.getTimeZone("Zulu");
		for (DateFormat format : this.formats)
			format.setTimeZone(timeZone);
	}

	public Date deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonParseException exception = null;
		String value = json.getAsString();
		for (DateFormat format : this.formats)
			try {
				synchronized (format) {
					return format.parse(value);
				}
			} catch (ParseException e) {
				exception = new JsonParseException(e);
			}
		throw exception;
	}

	public JsonElement serialize(Date date, Type type,
			JsonSerializationContext context) {
		DateFormat primary = this.formats[0];
		String formatted;
		synchronized (primary) {
			formatted = primary.format(date);
		}
		return new JsonPrimitive(formatted);
	}

}
