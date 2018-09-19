package com.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

import com.model.Product;

@Repository(value = "productDao")
@EnableCaching
public class ProductDaoImpl implements ProductDao {

	// this class is wired with the sessionFactory to do some operation in the
	// database

	@Autowired
	private SessionFactory sessionFactory;

	// this will create one sessionFactory for this class
	// there is only one sessionFactory should be created for the applications
	// we can create multiple sessions for a sessionFactory
	// each session can do some functions

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Cacheable("products")
	public List<Product> getAllProducts() {
		// Reading the records from the table
		Session session = sessionFactory.openSession();
		// List<Product> products = session.createQuery("from Product").list();
		List<Product> products = session.createCriteria(Product.class).list();
		Collections.sort(products, new Comparator() {
			public int compare(Object a, Object b) {
				String one = ((Product)a).getProductName ();
				String two = ((Product)b).getProductName ();
				return one.compareTo(two) ;
			}
		});
		System.out.println("----- List of Products-----");
		System.out.println(products);
		// session.flush is used for clear cache in the session
		session.flush();
		// it will close the particular session after completing the process
		session.close();
		return products;
	}

	@Cacheable("products")
	public Product getProductById(String productId) {

		// Reading the records from the table
		Session session = sessionFactory.openSession();
		// select * from Product where isbn=i
		Product product = (Product) session.get(Product.class, productId);
		session.close();
		return product;
	}


	@CacheEvict(value="products", allEntries=true)
	public void deleteProduct(String productId) {
		Session session = sessionFactory.openSession();
		Product product = (Product) session.get(Product.class, productId);
		session.delete(product);
		session.flush();
		session.close();// close the session
	}


	@CacheEvict(value="products", allEntries=true)
	public void addProduct(Product product) {
		Session session = sessionFactory.openSession();
		session.save(product);
		session.flush();
		session.close();
	}

	@CachePut(value="products")
	public void editProduct(Product product) {
		Session session = sessionFactory.openSession();
		session.update(product);
		session.flush();
		session.close();
	}

}
