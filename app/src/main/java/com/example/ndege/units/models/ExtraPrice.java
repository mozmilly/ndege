package com.example.ndege.units.models;

import java.io.Serializable;

public class ExtraPrice implements Serializable {
	private String name;
	private double amount;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
 	public String toString(){
		return 
			"ExtraPrice{" + 
			"name = '" + name + '\'' + 
			",amount = '" + amount + '\'' + 
			"}";
		}
}