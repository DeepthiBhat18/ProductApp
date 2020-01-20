package com.rakuten.training.web;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rakuten.training.domain.Product;
import com.rakuten.training.domain.Review;
import com.rakuten.training.service.NoSuchProductException;
import com.rakuten.training.service.ProductService;
import com.rakuten.training.service.ReviewService;

@RestController
public class ReviewController {
	
	ProductService service;
	ReviewService revservice;

	@Autowired
	public void setService(ProductService service) {
		this.service = service;
	}
	@Autowired
	public void setRevservice(ReviewService revservice) {
		this.revservice = revservice;
	}

	@PostMapping("/products/{productId}/review")
	public ResponseEntity<Review> addReviewToProduct(@RequestBody Review toBeAdded, @PathVariable int productId) {
		try {
			Review review = revservice.addNewReview(toBeAdded, productId);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(URI.create("/products/" + productId + "/reviews"+ review.getId()));
			return new ResponseEntity<Review>(review,headers,HttpStatus.CREATED);
		} catch (NoSuchProductException e) {
			return new ResponseEntity<Review>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/products/{productId}/reviews")
	public ResponseEntity<List<Review>> getReviewsOfProduct(@PathVariable int productId) {
		try {
			Product p = service.findById(productId);
			List<Review> reviews = p.getReviews();
			return new ResponseEntity<List<Review>>(reviews, HttpStatus.OK);
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<List<Review>>(HttpStatus.NOT_FOUND);
		}
	}

}
