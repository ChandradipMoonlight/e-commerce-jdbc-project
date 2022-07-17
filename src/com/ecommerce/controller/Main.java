package com.ecommerce.controller;

import java.util.List;

import com.ecommerce.connection.DB;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.UserLogin;
import com.ecommerce.model.OrderModel;
import com.ecommerce.service.CartService;
import com.ecommerce.service.CartServiceIml;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.OrderServiceImpl;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ProductServiceImpl;
import com.ecommerce.service.UserService;
import com.ecommerce.service.UserServiceImp;

public class Main {


	public static void main(String[] args) {
		UserService userService = new UserServiceImp(DB.connectDb());
		ProductService prouctService = new ProductServiceImpl(DB.connectDb());
		CartService cartService = new CartServiceIml(DB.connectDb());
		OrderService orderService = new OrderServiceImpl(DB.connectDb());
		User user = new User();
		user.setUserName("Ganesh");
		user.setUserEmail("ganesh@gmail.com");
		user.setUserPassword("12345");
		
		userService.createUserRegistration(user);
		
		UserLogin loginUser = new UserLogin("shraddha@gmail.com", "125");
		userService.loginUser(loginUser);
		
		String token = "moonlight@gmail.com";
		List<User> getUserList = userService.getAllUsers(token);
		
		getUserList.forEach(it->{
			System.out.println("--------------------------------------------------------------------------");
			System.out.println("User Name = "+it.getUserName()+", User Email = "+it.getUserEmail()+
					", Is Admin = "+ (it.isAdmin() ? "YES" : "NO"));
		});
		
		Product product = new Product();
		product.setProductName("Laptop Dell D001");
		product.setProductDecription("Laptop features -> Batary Backup : 6hr, SSD : T1B, Processor : core intel 7 11th gen");
		product.setProductPrice(90000);
		product.setProductQty(100);
		int addProducts = prouctService.addProducts(product, token);
		System.out.println(addProducts);
		
		List<Product> getProductList = prouctService.fetchAllProduct();
		System.out.println("Product Name  Product Description             ProductPrice productQty ");
		getProductList.forEach(it->{
			System.out.println("-----------------------------------------------------------------------");
			System.out.println(it.getProductName() + ",  "+it.getProductDecription() + ", "+it.getProductPrice()+", "+
		it.getProductQty());
		});
		
//		Cart cart = new Cart();
//		cart.setProductId(1);
//		cart.setProductQty(10);
//		int addToCart = cartService.addToCart(token, cart);
//		System.out.println(addToCart);
//		int cartId =1;
//		int updateQty = cartService.increaseOtyAndPriceInCartWhenProductIsAddedInCart(token, cart, cartId);
//		System.out.println(updateQty);
		
		Order order = new Order();
		order.setProductId(1);
		order.setProductOty(5);
		int placeOrder = orderService.placeOrder(token, order);
		System.out.println(placeOrder);
		
		int userId = 6;
		List<OrderModel> userOrderHistory = orderService.checkuserOrderHistory(userId, token);
		System.out.println("Product name   : Product QTY : Product Price");
		userOrderHistory.forEach(it->{
			System.out.println("----------------------------------------------------------------------");
			System.out.println(it.getProductName() + ",  " + it.getProductOty()+", "+it.getOrderPrice());
		});
	}
}