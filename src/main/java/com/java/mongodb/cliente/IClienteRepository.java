package com.java.mongodb.cliente;

import com.java.mongodb.IRepository;

public interface IClienteRepository extends IRepository {
	
	public Long clienteNextVal();
	public void persist(Cliente cliente) throws Exception;
	public void update(Cliente cliente) throws Exception;
	public Cliente buscarPelaChave(String key) throws Exception;
	public Cliente buscarPeloNome(String key) throws Exception;
}