package com.rakuten.training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rakuten.training.dal.ProductDAO;
import com.rakuten.training.dal.ReviewDAO;
import com.rakuten.training.domain.Product;
import com.rakuten.training.domain.Review;

@Service
@Transactional
public class ReviewServiceJpaImpl implements ReviewService {

	ReviewDAO dao;// = new ReviewDAOInMemImpl();
	ProductDAO prodDAO;
	
	@Autowired
	public void setProdDAO(ProductDAO prodDAO) {
		this.prodDAO = prodDAO;
	}
	
	@Autowired
	public void setDao(ReviewDAO dao) {
		this.dao = dao;
	}

	// Failure should be returned as Exception. Absence of an object is returned as
	// empty optional
	@Override
	public Review addNewReview(Review toBeAdded, int productId) {
		Product existing = prodDAO.findById(productId);
		if(existing  !=null) {
			toBeAdded.setProduct(existing);
			Review added = dao.save(toBeAdded);
			return added;
		}
		else {
			throw new NoSuchProductException();
		}
	}

	@Override
	public void removeReview(int id) {
		Review existing = dao.findById(id);
		if (existing != null) {
			dao.deleteById(id);
		}
		else {
			throw new NullPointerException("Review does not exist");
		}
	}

	
}
