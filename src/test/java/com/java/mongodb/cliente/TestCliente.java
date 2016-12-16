package com.java.mongodb.cliente;

import org.junit.Assert;
import org.junit.Test;

public class TestCliente {
	
	@Test
	public void test() {
		
	}
	
	@Test
	public void test_persist() {
		Cliente cliente = new Cliente();
		cliente.codigo("albertocerqueira");
		cliente.nome("Alberto");
		cliente.sobrenome("Cerqueira");
		cliente.idade(26);
		
		try {
			IClienteRepository repository = ClienteRepositoryFactory.clienteRepository();
			repository.persist(cliente);
		} catch (Exception e) {
			Assert.fail();
		}
	}
}