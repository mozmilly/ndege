package com.example.ndege.units.maincategories.models;

import java.io.Serializable;

public class MainCategory implements Serializable {
	private String category_name;
	private String image;
	private int id;

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"MainCategory{" + 
			"category_name = '" + category_name + '\'' +
			",image = '" + image + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}