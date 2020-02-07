package com.example.ndege.units.models;

import java.io.Serializable;

public class Duration implements Serializable {
	private int duration;

	public void setDuration(int duration){
		this.duration = duration;
	}

	public int getDuration(){
		return duration;
	}

	@Override
 	public String toString(){
		return 
			"Duration{" + 
			"duration = '" + duration + '\'' + 
			"}";
		}
}