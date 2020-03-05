package com.example.ndege.units.product_reviews.models;

import java.io.Serializable;

public class ProductRating implements Serializable {
	private float points__avg;

	public float getPoints__avg() {
		return points__avg;
	}

	public void setPoints__avg(float points__avg) {
		this.points__avg = points__avg;
	}

	@Override
	public String toString() {
		return "ProductRating{" +
				"points__avg=" + points__avg +
				'}';
	}
}