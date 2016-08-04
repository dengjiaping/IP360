package com.truthso.ip360.utils;



import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class GsonUtil {

	private static final Gson GSON = createGson(true);

	private static final Gson GSON_NO_NULLS = createGson(false);

	public static final Gson createGson() {
		return createGson(true);
	}

	public static final Gson createGson(boolean serializeNulls) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new DateFormatter());
		builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		if (serializeNulls)
			builder.serializeNulls();
		return builder.create();
	}

	public static final Gson getGson() {
		return GSON;
	}

	public static final Gson getGson(boolean serializeNulls) {
		return serializeNulls ? GSON : GSON_NO_NULLS;
	}

	public static final String toJson(Object object) {
		return toJson(object, true);
	}

	public static final String toJson(Object object, boolean includeNulls) {
		return includeNulls ? GSON.toJson(object) : GSON_NO_NULLS
				.toJson(object);
	}

	public static final <V> V fromJson(String json, Class<V> type) {
		return GSON.fromJson(json, type);
	}

	public static final <V> V fromJson(String json, Type type) {
		return GSON.fromJson(json, type);
	}

	public static final <V> V fromJson(Reader reader, Class<V> type) {
		return GSON.fromJson(reader, type);
	}

	public static final <V> V fromJson(Reader reader, Type type) {
		return GSON.fromJson(reader, type);
	}
}
