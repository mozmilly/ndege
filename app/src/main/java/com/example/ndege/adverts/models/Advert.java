package com.example.ndege.adverts.models;

import java.io.Serializable;

public class Advert implements Serializable {
	private String my_image;
	private String title;
	private String text;
	private String long_description;


	public String getMy_image() {
		return my_image;
	}

	public void setMy_image(String my_image) {
		this.my_image = my_image;
	}

	public String getLong_description() {
		return long_description;
	}

	public void setLong_description(String long_description) {
		this.long_description = long_description;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}


	@Override
	public String toString() {
		return "Advert{" +
				"my_image='" + my_image + '\'' +
				", title='" + title + '\'' +
				", text='" + text + '\'' +
				", long_description='" + long_description + '\'' +
				'}';
	}
}