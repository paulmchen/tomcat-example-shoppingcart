package com.controller;

import com.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Cart;
import com.model.Customer;
import com.model.UserInfo;
import com.service.CartService;
import com.service.CustomerService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CartController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CartService cartService;

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public CartService getCartService() {
		return cartService;
	}

	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}
	
	@RequestMapping("cart/getCartById")
	public String getCartId(Model model, HttpSession session){
		String cartId;
		if (session.getAttribute("userInfo") != null) {
			cartId = ((UserInfo)session.getAttribute("userInfo")).cartId;
		} else {
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String emailId = user.getUsername();
			Customer customer = customerService.getCustomerByemailId(emailId);
			cartId = customer.getCart().getCartId();

			UserInfo userinfo = new UserInfo();
			userinfo.cartId = cartId;
			session.setAttribute("userInfo", userinfo);
		}
		model.addAttribute("cartId", cartId);
		return "cart";
	}
	
	@RequestMapping("/cart/getCart/{cartId}")
	public @ResponseBody Cart getCartItems(@PathVariable(value="cartId")String cartId, HttpSession session){
		if (session.getAttribute("userCart") != null) {
			return (Cart)session.getAttribute("userCart");
		} else {
			Cart cart = cartService.getCartByCartId(cartId);
			session.setAttribute("userCart", cart);
			return cart;
		}
	}
	
}
