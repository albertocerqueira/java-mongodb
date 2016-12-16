package com.java.mongodb.cliente;

import org.junit.Assert;
import org.junit.Test;

public class TestCliente {
	
	@Test
	public void test() {
		
	}
	
	// @Test
	public void test_persist() {
		Cliente cliente = new Cliente();
		cliente.codigo("albertocerqueira");
		cliente.nome("Alberto");
		cliente.sobrenome("Cerqueira");
		cliente.idade(26);
		
		try {
			ClienteRepository.getInstance().persist(cliente);
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void test_buscarPeloNome() {
		try {
			Cliente cliente = ClienteRepository.getInstance().buscarPeloNome("Alberto");
			System.out.println(cliente.toJSON().dado());
			Assert.assertEquals(cliente.nome(), "Alberto");
		} catch (Exception e) {
			Assert.fail();
		}
	}
}