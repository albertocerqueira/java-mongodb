package com.java.mongodb;

public interface ICallbackResult<T> {

	public T toObject(Object object);

}