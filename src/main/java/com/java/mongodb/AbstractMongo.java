package com.java.mongodb;

import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class AbstractMongo {
	
	private static MongoClient mongoClient;
	public static final String DATABASE_NAME = "albertocerqueira";

	protected AbstractMongo() {}

	public boolean isDisconnected() {
		return (AbstractMongo.mongoClient == null);
	}

	public MongoClient getDBClient() {
		initClientIfNecessary();
		return mongoClient;
	}

	protected synchronized void initClientIfNecessary() {
		System.out.println("Mongo Client precisa ser inicializado? [" + isDisconnected() + "].");
		if (isDisconnected()) {
			System.out.println("Mongo Client sendo iniciado [mongodb://127.0.0.1:27017/albertocerqueira].");
			MongoClientURI mongoClientURI = new MongoClientURI("mongodb://127.0.0.1:27017/wayhub");
			// Arrays.asList(new ServerAddress("localhost", 27017))
			mongoClient = new MongoClient(mongoClientURI);
			System.out.println("Mongo Client iniciado.");
		}
	}

	public Long currVal(String sequencia) {
		MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
		MongoCollection<Document> collection = db.getCollection("contadores");
		BasicDBObject find = new BasicDBObject();
		find.put("_id", sequencia);
		FindIterable<Document> documentosEncontrado = collection.find(find);
		Long currVal = Long.valueOf(0);
		if (documentosEncontrado.iterator().hasNext()) {
			Document documentoEncontrado = documentosEncontrado.first();
			currVal = documentoEncontrado.getLong("seq");
		}
		return currVal;
	}

	public Long nextVal(String sequencia) {
		MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
		MongoCollection<Document> collection = db.getCollection("contadores");
		BasicDBObject find = new BasicDBObject();
		find.put("_id", sequencia);
		BasicDBObject update = new BasicDBObject();
		update.put("$inc", new BasicDBObject("seq", new BsonInt64(1)));
		Document obj = collection.findOneAndUpdate(find, update);
		Long seq = null;
		if (obj == null) {
			Document d = new Document().append("_id", sequencia).append("seq", new BsonInt64(1));
			collection.insertOne(d);
			seq = Long.valueOf(1L);
		} else {
			seq = obj.getLong("seq");
		}
		return seq;
	}
	
	@SuppressWarnings("rawtypes")
	protected void persist(String key, ModeloDominio<? extends ModeloDominio> modelo, String bucketName) throws Exception {
		try {
			Document document = modelo.toBSON();
			document.put("_id", key);
			MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
			MongoCollection<Document> collection = db.getCollection(bucketName);
			collection.insertOne(document);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	@SuppressWarnings("rawtypes")
	protected void update(String key, ModeloDominio<? extends ModeloDominio> modelo, String bucketName) throws Exception {
		try {
			Document document = modelo.toBSON();
			document.put("_id", key);
			MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
			MongoCollection<Document> collection = db.getCollection(bucketName);
			collection.replaceOne(Filters.eq("_id", key), document);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	protected void delete(String key, String bucketName) throws Exception {
		try {
			MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
			MongoCollection<Document> collection = db.getCollection(bucketName);
			collection.deleteOne(Filters.eq("_id", key));
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	protected void updateArrayField(String key, String fieldToFind, Object valueToFind, String fieldToSet, Object newValue, String bucketName) throws Exception {
		MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
		MongoCollection<Document> collection = db.getCollection(bucketName);
		collection.updateOne(Filters.and(Filters.eq("_id", key), Filters.eq(fieldToFind, valueToFind)), new Document("$set", new Document(fieldToSet, newValue)));
	}

	protected void updateField(String key, String fieldToSet, Object newValue, String bucketName) throws Exception {
		MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
		MongoCollection<Document> collection = db.getCollection(bucketName);
		collection.updateOne(Filters.and(Filters.eq("_id", key)), new Document("$set", new Document(fieldToSet, newValue)));
	}

	protected void incrementField(String key, String fieldToSet, Integer value, String bucketName) throws Exception {
		incrementField(key, null, null, fieldToSet, value, bucketName);
	}

	protected void decrementField(String key, String fieldToSet, Integer value, String bucketName) throws Exception {
		incrementField(key, null, null, fieldToSet, (value * -1), bucketName);
	}

	protected void incrementField(String key, String fieldToFind, Object valueToFind, String fieldToSet, Integer value, String bucketName) throws Exception {
		MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
		MongoCollection<Document> collection = db.getCollection(bucketName);
		Bson filters = null;
		if (fieldToFind != null && valueToFind != null) {
			filters = Filters.and(Filters.eq("_id", key), Filters.eq(fieldToFind, valueToFind));
		} else {
			filters = Filters.eq("_id", key);
		}
		Document d = collection.findOneAndUpdate(filters, new Document("$inc", new Document(fieldToSet, value)));
		if (d == null) {
			// Estamos lancando um IllegalStateException pois espera-se que nesse momento ja exista o registro no banco para que seja feito o incremento,
			// nesse caso quem esta em IllegalState eh o banco de dados.
			// Nao estou colocando um ObjectNotFoundException pois a ideia de update eh diferente da ideia de busca.
			throw new IllegalStateException("O registro no bucketName [" + bucketName + "] n\u00e3o foi encontrado !!!");
		}
	}

	protected void decrementField(String key, String fieldToFind, Object valueToFind, String fieldToSet, Integer value, String bucketName) throws Exception {
		incrementField(key, fieldToFind, valueToFind, fieldToSet, (value * -1), bucketName);
	}

	public <T> T buscarPelaChave(String key, String bucketName, ICallbackResult<T> callbackResult) throws Exception {
		if (key != null) {
			MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
			MongoCollection<Document> collection = db.getCollection(bucketName);
			FindIterable<Document> documents = collection.find(Filters.eq("_id", key));
			Document obj = documents.first();
			if (obj != null) {
				return (T) callbackResult.toObject(obj.toJson());
			}
		}
		return null;
	}

	protected FindIterable<Document> findAll(String bucketName) {
		MongoDatabase db = getDBClient().getDatabase(DATABASE_NAME);
		return db.getCollection(bucketName).find();
	}
}