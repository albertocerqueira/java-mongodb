package com.java.mongodb;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class JSON {

	private String dado;

	public JSON() {}
	private JSON(String dado) {
		this.dado = dado;
	}

	public static JSON createFrom(String dado) {
		return new JSON(dado);
	}

	@JsonGetter("dado")
	public String dado() {
		return dado;
	}

	@JsonSetter("dado")
	public void dado(String dado) {
		this.dado = dado;
	}
}