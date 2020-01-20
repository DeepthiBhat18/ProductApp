package com.rakuten.training.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakuten.training.domain.Product;

@Repository
@Transactional
public class ProductDAOJpaImpl implements ProductDAO {

	@Autowired
	EntityManager em;

	@Override
	public Product save(Product toBeSaved) {
		em.persist(toBeSaved); // persist is equivalent to insert.
		return toBeSaved;
	}

	@Override
	public Product findById(int id) {
		Product p = em.find(Product.class, id);
		// System.out.println("This product has "+p.getReviews().size()+"reviews");
		return p;
	}

	@Override
	public List<Product> findAll() {
		Query q = em.createQuery("select p from Product as  p");
		List<Product> all = q.getResultList();
		return all;
	}

	@Override
	public void deleteById(int id) {
		//Instead of .find(), we can use .getref() which will only return a proxy and avoid a select operation.
		//If we try to access any other property, it will trigger lazy  evaluation.
		Product p = em.getReference(Product.class, id); 
		em.remove(p);
		/*//JPQL simply translates into SQL queries, hence Cascade and Fetch will not work with queries. 
		 *  //If te cascading was configured on teh database side, then query  based operations can be used.
		 * Query q = em.createQuery("delete from Product as p where p.id = :idparam");
		 * q.setParameter("idparam", id); q.executeUpdate();
		 */
	}

}
