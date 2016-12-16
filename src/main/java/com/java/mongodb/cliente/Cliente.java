package com.java.mongodb.cliente;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.java.mongodb.JSON;
import com.java.mongodb.JSONEngine;
import com.java.mongodb.ModeloDominio;

public class Cliente extends ModeloDominio<Cliente> {

	private String codigo;
	private String nome;
	private String sobrenome;
	private Integer idade;
	
	public static Cliente create() {
		return new Cliente();
	}
	
	public static Cliente create(JSON jCliente) throws Exception {
		return JSONEngine.getInstance().createFrom(jCliente, Cliente.class);
	}
	
	@JsonSetter("codigo")
	public Cliente codigo(String codigo) {
		this.codigo = codigo;
		return this;
	}
	
	@JsonGetter("codigo")
	public String codigo() {
		return codigo;
	}
	
	@JsonSetter("nome")
	public Cliente nome(String nome) {
		this.nome = nome;
		return this;
	}
	
	@JsonGetter("nome")
	public String nome() {
		return nome;
	}
	
	@JsonSetter("sobrenome")
	public Cliente sobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
		return this;
	}
	
	@JsonGetter("sobrenome")
	public String sobrenome() {
		return sobrenome;
	}
	
	@JsonSetter("idade")
	public Cliente idade(Integer idade) {
		this.idade = idade;
		return this;
	}
	
	@JsonGetter("idade")
	public Integer idade() {
		return idade;
	}
}