package com.java.mongodb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONEngine {

	private ObjectMapper om = null;
	public static JSONEngine jsonEngine = new JSONEngine();

	private JSONEngine() {
		om = new ObjectMapper();
		om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static JSONEngine getInstance() {
		return jsonEngine;
	}

	public JSON toJSON(Object o) throws Exception {
		JSON json = null;
		try {
			String sJson = om.writeValueAsString(o);
			json = JSON.createFrom(sJson);
		} catch (JsonProcessingException e) {
			String message = "N\u00e3o foi poss\u00edvel fazer a transforma\u00e7\u00e3o para JSON.";
			System.out.println(message);
			throw new Exception(message);
		}
		return json;
	}

	public <T> T createFrom(JSON json, Class<T> clazz) throws Exception {
		try {
			return om.readValue(json.dado(), clazz);
		} catch (JsonMappingException e) {
			String message = "N\u00e3o foi poss\u00edvel criar o objeto a partir do JSON [" + json.dado() + "]";
			System.out.println(message);
			throw new Exception(e);
		} catch (JsonParseException e) {
			String message = "N\u00e3o foi poss\u00edvel criar o objeto a partir do JSON [" + json.dado() + "]";
			System.out.println(message);
			throw new Exception(e);
		} catch (IOException e) {
			String message = "N\u00e3o foi poss\u00edvel criar o objeto a partir do JSON [" + json.dado() + "]";
			System.out.println(message);
			throw new Exception(e);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public String toJsonArray(List lista) throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		om.writeValue(out, lista);
		final byte[] data = out.toByteArray();
		return new String(data);
	}

	public <T> T fromJsonArray(final TypeReference<T> type, final String jsonPacket) throws Exception {
		T data = null;
		try {
			data = om.readValue(jsonPacket, type);
		} catch (JsonParseException e) {
			throw new Exception(e);
		} catch (JsonMappingException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		}
		return data;
	}
}