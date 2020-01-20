package com.rakuten.training.service;

import java.util.List;

import com.rakuten.training.domain.Review;

public interface ReviewService {

	Review addNewReview(Review toBeAdded,  int productId);

	void removeReview(int id);

}
