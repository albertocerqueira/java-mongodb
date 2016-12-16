package com.java.mongodb.cliente;

import java.util.List;

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
	public void test_0() {

	}

	@Test
	public void test_1_inserir() {
		System.out.println("Executando m\u00e9todo de teste: inserir()");
		
		try {
			cliente.nome(nome);
			cliente.sobrenome(sobrenome);
			cliente.idade(idade);
			
			ClienteRepository.getInstance().inserir(cliente);
		} catch (Exception e) {
			Assert.fail("Stack de erro [" + e.getMessage() + "].");
		}
	}

	@Test
	public void test_2_buscarPeloNome() {
		System.out.println("Executando m\u00e9todo de teste: buscarPeloNome()");
		
		try {
			cliente = ClienteRepository.getInstance().buscarPeloNome(nome);
			System.out.println(cliente.toJSON().dado());
			Assert.assertEquals(cliente.nome(), nome);
		} catch (Exception e) {
			Assert.fail("Stack de erro [" + e.getMessage() + "].");
		}
	}
	
	@Test
	public void test_3_buscarPelaIdade() {
		System.out.println("Executando m\u00e9todo de teste: buscarPorIdades()");
		
		try {
			Cliente cliente = ClienteRepository.getInstance().buscarPelaIdade(26);
			System.out.println(cliente.toJSON().dado());
			Assert.assertEquals(cliente.nome(), nome);
		} catch (Exception e) {
			Assert.fail("Stack de erro [" + e.getMessage() + "].");
		}
	}
	
	@Test
	public void test_4_buscarPorIdades() {
		System.out.println("Executando m\u00e9todo de teste: buscarPorIdades()");
		
		try {
			List<Cliente> clientes = ClienteRepository.getInstance().buscarPorIdades(20, 21, 22, 23, 24, 25, 26, 27, 28, 29);
			
			if (clientes.size() == 0) {
				Assert.fail("Nenhum cliente encontrado dentro de uma busca prevista.");
			}
			
			for (Cliente cliente : clientes) {
				System.out.println(cliente.toJSON().dado());
			}
		} catch (Exception e) {
			Assert.fail("Stack de erro [" + e.getMessage() + "].");
		}
	}
	
	@Test
	public void test_5_deletar() {
		System.out.println("Executando m\u00e9todo de teste: deletar()");
		
		try {
			ClienteRepository.getInstance().deletar(cliente.codigo());
		} catch (Exception e) {
			Assert.fail("Stack de erro [" + e.getMessage() + "].");
		}
	}
	
	@Test
	public void test_6_buscarPeloNome() {
		System.out.println("Executando m\u00e9todo de teste: buscarPeloNome()");
		
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