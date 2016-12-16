package com.java.mongodb.cliente;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCliente {

	private static Cliente cliente = new Cliente();;
	private static String nome = "Alberto";
	private static String sobrenome = "Cerqueira";
	private static Integer idade = 26;
	
	@Test
	public void test() {

	}

	@Test
	public void test_1_inserir() {
		cliente.nome(nome);
		cliente.sobrenome(sobrenome);
		cliente.idade(idade);

		try {
			ClienteRepository.getInstance().inserir(cliente);
		} catch (Exception e) {
			Assert.fail("Stack de erro [" + e.getMessage() + "].");
		}
	}

	@Test
	public void test_2_buscarPeloNome() {
		try {
			cliente = ClienteRepository.getInstance().buscarPeloNome(nome);
			System.out.println(cliente.toJSON().dado());
			Assert.assertEquals(cliente.nome(), nome);
		} catch (Exception e) {
			Assert.fail("Stack de erro [" + e.getMessage() + "].");
		}
	}
	
	@Test
	public void test_3_deletar() {
		try {
			ClienteRepository.getInstance().deletar(cliente.codigo());
		} catch (Exception e) {
			Assert.fail("Stack de erro [" + e.getMessage() + "].");
		}
	}
	
	@Test
	public void test_4_buscarPeloNome() {
		try {
			cliente = ClienteRepository.getInstance().buscarPeloNome(nome);
			if (cliente != null) {
				Assert.fail("N\u00e3o deveria retornar nenhum dado.");
			}
		} catch (Exception e) {
			Assert.fail("Stack de erro [" + e.getMessage() + "].");
		}
	}
}