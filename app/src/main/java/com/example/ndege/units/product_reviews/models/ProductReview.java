package com.example.ndege.units.product_reviews.models;

import java.io.Serializable;

public class ProductReview implements Serializable {
	private String comment;
	private String date;
	private User user;
	private String media;

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setMedia(String media){
		this.media = media;
	}

	public String getMedia(){
		return media;
	}

	@Override
 	public String toString(){
		return 
			"ProductReview{" + 
			"comment = '" + comment + '\'' + 
			",date = '" + date + '\'' + 
			",user = '" + user + '\'' + 
			",media = '" + media + '\'' + 
			"}";
		}
}