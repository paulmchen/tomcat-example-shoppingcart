package com.dao;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.model.CartItem;
import com.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.model.Cart;
import com.service.CustomerOrderService;

@Repository
@Transactional
public class CartDaoImpl implements CartDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private CustomerOrderService customerOrderService;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Cart getCartByCartId(String CartId) {
		Session session = sessionFactory.openSession();
		Cart cart = (Cart) session.get(Cart.class, CartId);
		System.out.println(cart.getCartId() + " " + cart.getCartItem());
		List<CartItem> cartItems = cart.getCartItem();
		Collections.sort(cartItems, new Comparator() {
			public int compare(Object a, Object b) {
				String one = ((CartItem)a).getProduct ().getProductName();
				String two = ((CartItem)b).getProduct ().getProductName();
				return one.compareTo(two) ;
			}
		});
		cart.setCartItem(cartItems);
		System.out.println(cart);
		session.close();
		return cart;

	}

	public Cart validate(String cartId) throws IOException {
		Cart cart = getCartByCartId(cartId);
		if (cart == null || cart.getCartItem().size() == 0) {
			throw new IOException(cartId + "");
		}
		update(cart);
		return cart;
	}

	public void update(Cart cart) {

		String cartId = cart.getCartId();
		double grandTotal = customerOrderService.getCustomerOrderGrandTotal(cartId);
		cart.setTotalPrice(grandTotal);

		Session session = sessionFactory.openSession();
		session.saveOrUpdate(cart);
		session.flush();
		session.close();
	}

}
