package com.rakuten.training.web;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.rakuten.training.service.ProductService;
import com.rakuten.training.service.ReviewService;

@RunWith(SpringRunner.class)
@WebMvcTest({ReviewController.class})
public class ReviewControllerUnitTest {

	@MockBean
	ProductService service;
	@MockBean
	ReviewService revservice;
	@Autowired
	public void setService(ProductService service) {
		this.service = service;
	}
	@Autowired
	public void setRevservice(ReviewService revservice) {
		this.revservice = revservice;
	}
	
	

}
