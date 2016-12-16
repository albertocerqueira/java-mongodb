package com.java.mongodb.cliente;

import com.java.mongodb.AbstractMongo;
import com.java.mongodb.ICallbackResult;
import com.java.mongodb.JSON;

public class ClienteRepository extends AbstractMongo implements IClienteRepository {

	public static final String BUCKET_NAME_CLIENTE = "cliente";

	public Long clienteNextVal() {
		return nextVal("cliente");
	}

	public void persist(Cliente cliente) throws Exception {
		String key = clienteNextVal() + "";
		cliente.data(Cliente.getCurrentISO8601Timestamp());
		cliente.usuario("0123456789");
		persist(key, cliente, BUCKET_NAME_CLIENTE);
		System.out.println("Efetuado a grava\u00e7\u00e3o no Mongo do Produto com a key [" + key + "].");
	}

	public void update(Cliente cliente) throws Exception {
		String key = cliente.codigo();
		cliente.data(Cliente.getCurrentISO8601Timestamp());
		cliente.usuario("0123456789");
		update(key, cliente, BUCKET_NAME_CLIENTE);
		System.out.println("Efetuado a altera\u00e7\u00e3o no Mongo do Produto com a key [" + key + "].");
	}
	
	public void remove(String key) throws Exception {
		
	}

	public Cliente buscarPelaChave(String key) throws Exception {
		Cliente cliente = null;
		if (key != null) {
			cliente = (Cliente) super.buscarPelaChave(key, BUCKET_NAME_CLIENTE, new ICallbackResult<Cliente>() {
				public Cliente toObject(Object value) {
					try {
						return Cliente.create(JSON.createFrom(String.valueOf(value)));
					} catch (Exception e) {
						return null;
					}
				}
			});
		}
		return cliente;
	}

	public Cliente buscarPeloNome(String key) throws Exception {
		return null;
	}
	
}