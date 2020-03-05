package com.example.ndege.help.models;

import java.io.Serializable;

public class Category implements Serializable {
	private String name_of_cat;

	public String getName_of_cat() {
		return name_of_cat;
	}

	public void setName_of_cat(String name_of_cat) {
		this.name_of_cat = name_of_cat;
	}

	@Override
 	public String toString(){
		return 
			"Category{" + 
			"name_of_cat = '" + name_of_cat + '\'' +
			"}";
		}
}