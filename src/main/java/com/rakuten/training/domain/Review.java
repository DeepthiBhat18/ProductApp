package com.rakuten.training.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

// This table is created using top-down approach
@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String author;
	String content;
	float stars;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	Product product;

	public Review() {
		// TODO Auto-generated constructor stub
	}

	// Only columns of the table are arguments to constructors. Hence no need to
	// include 'product'
	public Review(String author, String content, float stars) {
		super();
		this.author = author;
		this.content = content;
		this.stars = stars;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public float getStars() {
		return stars;
	}

	public void setStars(float stars) {
		this.stars = stars;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
