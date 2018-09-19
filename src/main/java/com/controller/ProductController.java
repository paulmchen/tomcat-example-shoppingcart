
package com.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.model.Product;
import com.service.ProductService;


@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ServletContext servletContext;

	//Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "/WEB-INF/resource/uploadimages/";

	// Getters and Setters

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	// Configuration for MultiPartResolver
	// Multipart resolver is for uploading images and other media
	// maxupload size is for image size should not be maximum than 10240000

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(10240000);
		return multipartResolver;
	}

	// Request Mapping

	// which displays the list of products to the productList page

	/* Product List using Angular
	* @RequestMapping("/getAllProducts")
	* public ModelAndView getAllProducts() {
	*	List<Product> products = productService.getAllProducts();
	*	return new ModelAndView("productListAngular", "products", products);
	*}
	*/
	//		Normal ProductList view 
	  @RequestMapping("/getAllProducts") public ModelAndView getAllProducts() {
	  List<Product> products = productService.getAllProducts(); return new
	  ModelAndView("productList", "products", products); }
	 
	
	// this is used for getting the product by productId

	@RequestMapping("getProductById/{productId}")
	public ModelAndView getProductById(@PathVariable(value = "productId") String productId, HttpSession session) {
		Product product = productService.getProductById(productId);
		if (session.getAttribute("viewHistory") != null) {
			List<Product> products = (List<Product>)session.getAttribute("viewHistory");
			if (products.isEmpty() || products.get(0).getProductId() != product.getProductId()) {
				products.add(0, product);
				session.setAttribute("viewHistory", products);
			}
		} else {
			List<Product> products = new ArrayList<Product>(Arrays.asList(product));
			session.setAttribute("viewHistory", products);
		}

		return new ModelAndView("productPage", "productObj", product);
	}

	@RequestMapping("/admin/delete/{productId}")
	public String deleteProduct(@PathVariable(value = "productId") String productId) {

		// Here the Path class is used to refer the path of the file

		Path path = Paths.get(servletContext.getRealPath(UPLOADED_FOLDER
				+ productId + ".jpg"));

		if (Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		productService.deleteProduct(productId);
		// http://localhost:8080/shoppingCart/getAllProducts
		return "redirect:/getAllProducts";
	}

	@RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.GET)
	public String getProductForm(Model model) {
		Product product = new Product();
		// New Arrivals
		// set the category as 1 for the Book book
		product.setProductCategory("Android");
		model.addAttribute("productFormObj", product);
		return "addProduct";

	}

	@RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.POST)
	public String addProduct(@Valid @ModelAttribute(value = "productFormObj") Product product, BindingResult result) {
		// Binding Result is used if the form that has any error then it will
		// redirect to the same page without performing any functions
		if (result.hasErrors())
			return "addProduct";
		productService.addProduct(product);
		System.out.println(servletContext.getRealPath(UPLOADED_FOLDER
				+ product.getProductId() + ".jpg"));
		MultipartFile image = product.getProductImage();
		if (image != null && !image.isEmpty()) {
			Path path = Paths
					.get(servletContext.getRealPath(UPLOADED_FOLDER
							+ product.getProductId() + ".jpg"));

			try {
				image.transferTo(new File(path.toString()));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "redirect:/getAllProducts";
	}

	@RequestMapping(value = "/admin/product/editProduct/{productId}")
	public ModelAndView getEditForm(@PathVariable(value = "productId") String productId) {
		Product product = productService.getProductById(productId);
		return new ModelAndView("editProduct", "editProductObj", product);
	}

	@RequestMapping(value = "/admin/product/editProduct", method = RequestMethod.POST)
	public String editProduct(@ModelAttribute(value = "editProductObj") Product product) {
		productService.editProduct(product);
		return "redirect:/getAllProducts";
	}

	@RequestMapping("/getProductsList")
	public @ResponseBody List<Product> getProductsListInJson() {
		return productService.getAllProducts();
	}

	@RequestMapping("/productsListAngular")
	public String getProducts() {
		return "productListAngular";
	}

	//		viewed ProductList view
	@RequestMapping("/getViewedHistory")
	public ModelAndView getViewedHistory(HttpSession session) {
		List<Product> products = new ArrayList<Product>();
		// load customer viewed product history from http session
		if (session.getAttribute("viewHistory") != null) {
			products = (List<Product>)session.getAttribute("viewHistory");
		}
		return new ModelAndView("viewedProductList", "products", products);
	}


}
