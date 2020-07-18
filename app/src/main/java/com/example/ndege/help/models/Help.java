package com.example.ndege.help.models;

import java.io.Serializable;

public class Help implements Serializable {
	private String title;
	private String content;
	private String video;
	private String file_image;
	private Category category;

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getFile_image() {
		return file_image;
	}

	public void setFile_image(String file_image) {
		this.file_image = file_image;
	}

	public void setCategory(Category category){
		this.category = category;
	}

	public Category getCategory(){
		return category;
	}

	@Override
	public String toString() {
		return "Help{" +
				"title='" + title + '\'' +
				", content='" + content + '\'' +
				", video='" + video + '\'' +
				", file_image='" + file_image + '\'' +
				", category=" + category +
				'}';
	}
}