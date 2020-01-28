package com.rakuten.training.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rakuten.training.domain.Product;
import com.rakuten.training.domain.Review;
import com.rakuten.training.service.NoSuchProductException;
import com.rakuten.training.service.ProductService;
import com.rakuten.training.service.ReviewService;

@RunWith(SpringRunner.class)
@WebMvcTest({ ReviewController.class })
public class ReviewControllerUnitTest {

	MockMvc mockMvc;

	@Autowired
	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

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

	@Test
	public void addNewReview_Returns_Created_If_Review_Is_Created() throws Exception {
		// Arrange
		Review toBeAdded = new Review("test", "test", 5);
		Product p = new Product("test", (float) 1234.0, 10);
		int id = 1;
		int productId = 1;
		p.setId(productId);
		toBeAdded.setId(id);
		toBeAdded.setProduct(p);
		Mockito.when(revservice.addNewReview(Mockito.any(Review.class), Mockito.eq(productId))).thenReturn(toBeAdded);
		// Act//Assert
		mockMvc.perform(post("/products/{productId}/review", productId).contentType(MediaType.APPLICATION_JSON)
				.content(objToJson(toBeAdded))).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION,
						"/products/" + productId + "/reviews/" + id));
		Mockito.verify(revservice).addNewReview(Mockito.any(Review.class), Mockito.eq(productId));
	}

	@Test
	public void addNewReview_Returns_BadRequest_If_Product_Is_Not_Found() throws Exception {
		// Arrange
		Review toBeAdded = new Review("test", "test", 5);
		int id = 1;
		int productId = 1;
		Mockito.when(revservice.addNewReview(Mockito.any(Review.class), Mockito.eq(productId)))
				.thenThrow(new NoSuchProductException());
		// Act//Assert
		mockMvc.perform(post("/products/{productId}/review", productId).contentType(MediaType.APPLICATION_JSON)
				.content(objToJson(toBeAdded))).andExpect(MockMvcResultMatchers.status().isBadRequest());
		Mockito.verify(revservice).addNewReview(Mockito.any(Review.class), Mockito.eq(productId));
	}

	@Test
	public void getReviewByProduct_Returns_Ok_WithList_If_Product_Exists() throws Exception {
		// Arrange
		Product found = new Product("test", 1234.0f, 6);
		int id = 1;
		found.setId(id);
		List<Review> reviews = new ArrayList<Review>();
		Review r1 = new Review("test1", "test1", 1);
		Review r2 = new Review("test1", "test1", 1);
		reviews.add(r1);
		r1.setProduct(found);
		reviews.add(r2);
		r2.setProduct(found);
		found.setReviews(reviews);

		Mockito.when(service.findById(id)).thenReturn(found);
		// Act//Assert
		mockMvc.perform(get("/products/{productId}/reviews", id)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[*].product.id", CoreMatchers.hasItem(id)));
		Mockito.verify(service).findById(id);

	}

	@Test
	public void getReviewByProduct_Returns_NotFound_If_Review_Does_Not_Exists() throws Exception {
		// Arrange
		int id = 1;
		Mockito.doThrow(new IllegalArgumentException()).when(service).findById(id);
		// Act//Assert
		mockMvc.perform(get("/products/{productId}/reviews", id)).andExpect(MockMvcResultMatchers.status().isNotFound());
		Mockito.verify(service).findById(id);

	}

	public static String objToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(obj);
		return requestJson;
	}
}
