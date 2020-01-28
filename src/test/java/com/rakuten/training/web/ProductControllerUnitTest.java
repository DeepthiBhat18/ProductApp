package com.rakuten.training.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
import com.rakuten.training.service.ProductService;
import com.rakuten.training.service.ReviewService;

//  To enable JUnit to be able to stimulate and process the Request-Response, we take the help of Spring
@RunWith(SpringRunner.class)
@WebMvcTest({ ProductController.class })
public class ProductControllerUnitTest {

	MockMvc mockMvc;

	@Autowired
	public void setMockMvc(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	// Request Spring to Mock this bean for us.
	@MockBean
	ProductService service;
	@MockBean
	ReviewService revService;

	@Test
	public void getProductById_Returns_OK_With_Correct_Product_If_Found() throws Exception {
		// Arrange
		Product found = new Product("test", 1234.0f, 100);
		int id = 1;
		found.setId(id);
		Mockito.when(service.findById(id)).thenReturn(found);
		// Act//Assert
		mockMvc.perform(get("/products/{id}", id)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)));

	}

	@Test
	public void getProductById_Returns_NotFound_If_Product_Does_Not_Exist() throws Exception {
		// Arrange
		int id = 1;
		Mockito.when(service.findById(id)).thenReturn(null);
		// Act//Assert
		mockMvc.perform(get("/products/{id}", id)).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void addProduct_Returns_Created_If_Product_Is_Created() throws Exception {
		// Arrange
		Product toBeAdded = new Product("test", 123465.0f, 100);
		int id = 1;
		Mockito.when(service.addNewProduct(Mockito.any(Product.class))).thenReturn(id);
		// Act//Assert
		mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(objToJson(toBeAdded)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "/products/" + id));
		Mockito.verify(service).addNewProduct(Mockito.any(Product.class));

	}

	@Test
	public void addProduct_Returns_BadRequest_If_ProductValue_Is_LTMinValue() throws Exception {
		// Arrange
		Product toBeAdded = new Product("test", 12.0f, 100);
		int id = 1;
		Mockito.when(service.addNewProduct(Mockito.any())).thenThrow(new IllegalArgumentException());
		// Act//Assert
		mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(objToJson(toBeAdded)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
		Mockito.verify(service).addNewProduct(Mockito.any(Product.class));

	}

	@Test
	public void deleteProduct_If_ProductId_Exists_And_LT_MinValue() throws Exception {
		// Arrange
		int id = 1;
		Mockito.doNothing().when(service).removeProduct(id);
		// Act//Assert
		mockMvc.perform(delete("/products/{id}", id)).andExpect(MockMvcResultMatchers.status().isNoContent());
		Mockito.verify(service).removeProduct(id);
	}

	@Test
	public void deleteProduct_If_ProductId_Exists_And_GT_MinValue() throws Exception {
		// Arrange
		int id = 1;
		// Act//Assert
		Mockito.doThrow(new IllegalStateException()).when(service).removeProduct(id);
		mockMvc.perform(delete("/products/{id}", id)).andExpect(MockMvcResultMatchers.status().isConflict());
		Mockito.verify(service).removeProduct(id);
		//mockMvc.perform(delete("/products/{id}", id)).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void deleteProduct_If_ProductId_Does_Not_Exists() throws Exception {
		// Arrange
		int id = 1;
		// Act//Assert
		Mockito.doThrow(new NullPointerException()).when(service).removeProduct(id);
		mockMvc.perform(delete("/products/{id}", id)).andExpect(MockMvcResultMatchers.status().isNotFound());
		Mockito.verify(service).removeProduct(id);
		//mockMvc.perform(delete("/products/{id}", id)).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	public static String objToJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(obj);
		return requestJson;
	}
}
