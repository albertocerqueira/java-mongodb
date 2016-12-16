package com.java.mongodb.cliente;

public class ClienteRepositoryFactory {

	public static IClienteRepository clienteRepository() {
		return new ClienteRepository();
	}
}