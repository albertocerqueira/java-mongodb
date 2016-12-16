package com.java.mongodb;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bson.Document;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

@SuppressWarnings("rawtypes")
public abstract class ModeloDominio<T extends ModeloDominio> {

	private String data;
	private String usuario;

	@SuppressWarnings("unchecked")
	@JsonSetter("data")
	public T data(String data) {
		this.data = data;
		return (T) this;
	}

	@JsonGetter("data")
	public String data() {
		return data;
	}

	@SuppressWarnings("unchecked")
	@JsonSetter("usuario")
	public T usuario(String usuario) {
		this.usuario = usuario;
		return (T) this;
	}

	@JsonGetter("usuario")
	public String usuario() {
		return usuario;
	}
	
	@JsonIgnore
	public static String getCurrentISO8601Timestamp() {
		return getCurrentISO8601Timestamp(new Date());
	}

	@JsonIgnore
	public static String getCurrentISO8601Timestamp(Date date) {
		String formattedDate = null;
		if (date != null) {
			TimeZone tz = TimeZone.getTimeZone("UTC");
			// Java Dates don't have microsecond resolution :(
			// Pad out to microseconds to match other examples.
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'000'");
			df.setTimeZone(tz);
			formattedDate = df.format(date);
		}
		return formattedDate;
	}

	public JSON toJSON() throws Exception {
		return JSONEngine.getInstance().toJSON(this);
	}

	public Document toBSON() {
		Document c = new Document();
		c = deepToBSON(c, this);
		return c;
	}

	public Document toBSONSearch() {
		Document c = new Document();
		c = deepToBSONSearch(c, this, "");
		return c;
	}

	private Document deepToBSONSearch(Document d, Object o, String previousDot) {
		Class<?> currentClass = o.getClass();
		while (currentClass != null && currentClass.getPackage().getName().startsWith("com.java.mongodb")) {
			Field[] fields = currentClass.getDeclaredFields();
			try {
				for (int i = 0; i < fields.length; i++) {
					Field f = fields[i];
					f.setAccessible(true);
					Object valorO = f.get(o);
					if (valorO instanceof List) {
						List ls = (List) valorO;
						if (!ls.isEmpty()) {
							List<Document> ld = new ArrayList<Document>();
							for (Object lo : ls) {
								Document dInner = deepToBSONSearch(new Document(), lo, previousDot);
								ld.add(dInner);
								System.out.println("Inserindo: " + f.getName());
							}
							d.put(f.getName(), ld);
						}
					} else if (valorO instanceof String) {
						if (valorO != null) {
							d.append(previousDot + f.getName(), valorO);
						}
					} else if (valorO instanceof BigDecimal) {
						if (valorO != null) {
							d.append(previousDot + f.getName(), valorO);
						}
					} else if (valorO instanceof Number) {
						if (valorO != null) {
							d.append(previousDot + f.getName(), valorO);
						}
					} else if (valorO instanceof Boolean) {
						if (valorO != null) {
							d.append(previousDot + f.getName(), valorO);
						}
					} else if (valorO instanceof Enum) {
						if (valorO != null) {
							d.append(previousDot + f.getName(), valorO);
						}
					} else if (valorO instanceof Object) {
						if (valorO != null) {
							String oldPreviousDot = previousDot;
							if (previousDot.equals("")) {
								previousDot = f.getName() + ".";
							} else {
								previousDot = previousDot + "." + f.getName() + ".";
							}
							deepToBSONSearch(d, valorO, previousDot);
							previousDot = oldPreviousDot;
							// d.put(f.getName(), dInner);
						}
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			currentClass = currentClass.getSuperclass();
		}
		return d;
	}

	private Document deepToBSON(Document d, Object o) {
		Class<?> currentClass = o.getClass();
		while (currentClass != null && currentClass.getPackage().getName().startsWith("com.java.mongodb")) {
			Field[] fields = currentClass.getDeclaredFields();
			try {
				for (int i = 0; i < fields.length; i++) {
					Field f = fields[i];
					f.setAccessible(true);
					Object valorO = f.get(o);
					if (valorO instanceof List) {
						List ls = (List) valorO;
						List<Document> ld = new ArrayList<Document>();
						for (Object lo : ls) {
							Document dInner = deepToBSON(new Document(), lo);
							ld.add(dInner);
							System.out.println("Inserindo: " + f.getName());
						}
						d.put(f.getName(), ld);
					} else if (valorO instanceof String) {
						d.append(f.getName(), valorO);
					} else if (valorO instanceof BigDecimal) {
						d.append(f.getName(), valorO);
					} else if (valorO instanceof Number) {
						d.append(f.getName(), valorO);
					} else if (valorO instanceof Boolean) {
						d.append(f.getName(), valorO);
					} else if (valorO instanceof Enum) {
						d.append(f.getName(), valorO);
					} else if (valorO instanceof Object) {
						Document dInner = deepToBSON(new Document(), valorO);
						System.out.println("Inserindo: " + f.getName());
						d.put(f.getName(), dInner);
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			currentClass = currentClass.getSuperclass();
		}
		return d;
	}
}