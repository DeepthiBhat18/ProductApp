package com.rakuten.training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rakuten.training.dal.ProductDAO;
//import com.rakuten.training.dal.ProductDAOInMemImpl;
import com.rakuten.training.domain.Product;
import com.rakuten.training.domain.Review;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	ProductDAO dao;// = new ProductDAOInMemImpl();

	@Autowired
	public void setDao(ProductDAO dao) {
		this.dao = dao;
	}

	// Failure should be returned as Exception. Absence of an object is returned as
	// empty optional
	@Override
	public int addNewProduct(Product toBeAdded) {
		if (toBeAdded.getPrice() * toBeAdded.getQoh() >= 10000) {
			Product added = dao.save(toBeAdded);
			return added.getId();
		} else {
			throw new IllegalArgumentException("The monetory value is less than 10000!");
		}
	}

	@Override
	public void removeProduct(int id) {
		Product existing = dao.findById(id);
		if (existing != null) {
			if (existing.getPrice() * existing.getQoh() >= 100000) {
				throw new IllegalStateException("Monetory  value >100000. Cannot delete");
			} else {
				dao.deleteById(id);
			}
		}
		else {
			throw new NullPointerException("Product does not exist");
		}
	}

	@Override
	public List<Product> findAll() {
		return dao.findAll();
	}

	@Override
	public Product findById(int id) {
		return dao.findById(id);
	}
	
	public List<Review> getReviews(int productId)
	{
		Product p = dao.findById(productId);
		return p.getReviews();
	}

}
