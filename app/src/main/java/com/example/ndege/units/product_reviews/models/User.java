package com.example.ndege.units.product_reviews.models;

import java.io.Serializable;

public class User implements Serializable {
	private String username;
	private String first_name;

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", first_name='" + first_name + '\'' +
				'}';
	}
}