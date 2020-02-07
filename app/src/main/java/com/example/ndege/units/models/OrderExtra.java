package com.example.ndege.units.models;

import java.io.Serializable;

public class OrderExtra implements Serializable {
	private String customer_name;
	private String client_phone;
	private double margin;
	private boolean reseller_paid;

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getClient_phone() {
		return client_phone;
	}

	public void setClient_phone(String client_phone) {
		this.client_phone = client_phone;
	}

	public double getMargin() {
		return margin;
	}

	public void setMargin(double margin) {
		this.margin = margin;
	}

	public boolean isReseller_paid() {
		return reseller_paid;
	}

	public void setReseller_paid(boolean reseller_paid) {
		this.reseller_paid = reseller_paid;
	}

	@Override
	public String toString() {
		return "OrderExtra{" +
				"customer_name='" + customer_name + '\'' +
				", client_phone='" + client_phone + '\'' +
				", margin=" + margin +
				", reseller_paid=" + reseller_paid +
				'}';
	}
}