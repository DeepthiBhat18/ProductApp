package com.rakuten.training;

import java.util.Iterator;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.rakuten.training.dal.ProductDAO;
import com.rakuten.training.dal.ReviewDAO;
import com.rakuten.training.domain.Product;
import com.rakuten.training.domain.Review;
import com.rakuten.training.ui.ProductConsoleUI;

@SpringBootApplication
public class ProductAppApplication {

	public static void main(String[] args) {
		// ApplicationContext springContainer =
		SpringApplication.run(ProductAppApplication.class, args);
		/*
		 * ProductConsoleUI ui = springContainer.getBean(ProductConsoleUI.class);
		 * ui.createProductWithUI();
		 */

		/*
		 * ReviewDAO reviewDAO = springContainer.getBean(ReviewDAO.class); Review sample
		 * = new Review("self", "this is good", 5); Review saved =
		 * reviewDAO.save(sample,2); System.out.println("Created review with id: " +
		 * saved.getId());
		 */

		/*
		 * ProductDAO productDAO = springContainer.getBean(ProductDAO.class);
		 * List<Product> products = productDAO.findAll();
		 * System.out.println("The products are:"); for (Product p : products) {
		 * System.out.println(p.getName()); }
		 * 
		 * productDAO.deleteById(1);
		 */

		// System.out.println("This product has "+p.getReviews().size()+" reviews");
	}

}
