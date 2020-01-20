/*
 * The main aim of test cases writing should be to minimise the time taken to execute.
 * Hence we do  not bring up Spring in the test cases.
 * So we use test doubles to replicate only the functionalities that are required foe testing. 
 * By using  mock objects that mimics the behaviour of the  original implementation.
 * To do this, we use mock object libraries like Mockito. 
 */

package com.rakuten.training.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.Mockito;

import com.rakuten.training.dal.ProductDAO;
import com.rakuten.training.domain.Product;

public class ProductServiceImplTest {

	@Test
	public void addNewProduct_Returns_Valid_Id_When_ProductValue_GTEQ_MinValue() {
		//Arrange
		ProductServiceImpl service = new ProductServiceImpl();
		Product toBeAdded = new Product("test", 10000, 1); //notice 10000*1 >=10000
		Product saved = new Product("test", 10000, 1);
		saved.setId(1);
		ProductDAO mockDAO = Mockito.mock(ProductDAO.class);
		Mockito.when(mockDAO.save(toBeAdded)).thenReturn(saved);
		service.setDao(mockDAO);
		//Act
		int id = service.addNewProduct(toBeAdded);
		//Assert
		assertTrue(id>0);
	}
	
	       //Assert
	@Test(expected = IllegalArgumentException.class) 
	public void addNewProduct_ThrowsException_When_ProductValue_LT_MinValue() {
		ProductServiceImpl service = new ProductServiceImpl();
		Product toBeAdded = new Product("test", 9999, 1); //notice 9999*1 <10000
		//Act
		int id = service.addNewProduct(toBeAdded);
	}
	//Act
	@Test(expected = IllegalStateException.class) 
	public void removeProduct_ThrowsException_When_ProductValue_GT_MinValue() {
		//Arrange
		ProductServiceImpl service = new ProductServiceImpl();
		Product existing = new Product("test", 10999, 1); //notice 10000*1 >=10000
		existing.setId(1);
		ProductDAO mockDAO = Mockito.mock(ProductDAO.class);
		Mockito.when(mockDAO.findById(1)).thenReturn(existing);
		service.setDao(mockDAO);
		//Mockito.verify(mockDAO).deleteById(1);
		//Act
		service.removeProduct(1);
	}
	
	@Test
	public void removeProduct_Removes_When_ProductValue_LT_MinValue() {
		//Arrange
		ProductServiceImpl service = new ProductServiceImpl();
		Product existing = new Product("test", 9999, 1); //notice 999*1 <10000
		existing.setId(1);
		ProductDAO mockDAO = Mockito.mock(ProductDAO.class);
		Mockito.when(mockDAO.findById(1)).thenReturn(existing);
		service.setDao(mockDAO);
		int id=1;
		service.removeProduct(1);
		Mockito.verify(mockDAO).deleteById(id);
	}
	
	@Test
	public void removeProduct_Does_Not_React_If_Product_Does_Not_Exist() {
		ProductServiceImpl service = new ProductServiceImpl();
		ProductDAO mockDAO = Mockito.mock(ProductDAO.class);
		Mockito.when(mockDAO.findById(1)).thenReturn(null);
		service.setDao(mockDAO);
		try {
			service.removeProduct(1);
			Mockito.verify(mockDAO, Mockito.times(0)).deleteById(1);;
		} catch (Exception e) {
			fail("Exception occured !!!!!"+e);
		}
		
		
	}

}
