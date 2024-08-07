package com.example.webclient.entity;

import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable {

	private long id;
	private String name;
	
	public Customer(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return id == other.id && Objects.equals(name, other.name);
	}
	
}
