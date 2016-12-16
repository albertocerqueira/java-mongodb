package com.java.mongodb.cliente;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.java.mongodb.AbstractMongo;
import com.java.mongodb.ICallbackResult;
import com.java.mongodb.JSON;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class ClienteRepository extends AbstractMongo {

	public static final String BUCKET_NAME_CLIENTE = "cliente";
	public static ClienteRepository instance = new ClienteRepository();

	public static ClienteRepository getInstance() {
		return instance;
	}

	public Long clienteNextVal() {
		return nextVal("cliente");
	}

	public void inserir(Cliente cliente) throws Exception {
		String key = clienteNextVal() + "";
		cliente.codigo(key);
		cliente.data(Cliente.getCurrentISO8601Timestamp());
		cliente.usuario("0123456789");
		persist(key, cliente, BUCKET_NAME_CLIENTE);
		System.out.println("Efetuado a grava\u00e7\u00e3o no Mongo do Cliente com a key [" + key + "].");
	}

	public void alterar(Cliente cliente) throws Exception {
		String key = cliente.codigo();
		cliente.data(Cliente.getCurrentISO8601Timestamp());
		cliente.usuario("0123456789");
		update(key, cliente, BUCKET_NAME_CLIENTE);
		System.out.println("Efetuado a altera\u00e7\u00e3o no Mongo do Cliente com a key [" + key + "].");
	}

	public void deletar(String key) throws Exception {
		delete(key, BUCKET_NAME_CLIENTE);
		System.out.println("Efetuado a remo\u00e7\u00e3o no Mongo do Cliente com a key [" + key + "].");
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

	/**
	 * 
	 * @query db.cliente.find({"nome": <"value">})
	 * 
	 * @param nome
	 * @return
	 * @throws Exception
	 */
	public Cliente buscarPeloNome(String nome) throws Exception {
		MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
		MongoCollection<Document> collection = db.getCollection(BUCKET_NAME_CLIENTE);
		FindIterable<Document> documents = collection.find(Filters.and(Filters.eq("nome", nome)));
		Document obj = documents.first();
		if (obj != null) {
			return Cliente.create(JSON.createFrom(String.valueOf(obj.toJson())));
		}
		return null;
	}
	
	/**
	 * @query db.cliente.find({"idade": { $in: [<"value">, <"value">] }})
	 * 
	 * @param idades
	 * @return
	 * @throws Exception
	 */
	public List<Cliente> buscarPorIdades(Integer ... idades) throws Exception {
		MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
		MongoCollection<Document> collection = db.getCollection(BUCKET_NAME_CLIENTE);
		
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0, l = idades.length; i < l; i++) {
			list.add(idades[i]);
		}
		
		BasicDBList docIdades = new BasicDBList();
		docIdades.addAll(list);
		DBObject inClause = new BasicDBObject("$in", docIdades);
		BasicDBObject query = new BasicDBObject("idade", inClause);
		
		Iterable<Document> clientesRaw = collection.find(Filters.and(query));
		
		List<Cliente> clientes = new ArrayList<Cliente>();
		for (Document clienteRow : clientesRaw) {
			Cliente cliente = Cliente.create(JSON.createFrom(String.valueOf(clienteRow.toJson())));
			clientes.add(cliente);
		}
		
		return clientes;
	}
}