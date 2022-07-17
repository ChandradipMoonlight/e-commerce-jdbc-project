package com.ecommerce.controller;

import java.util.List;
import java.util.Scanner;

import com.ecommerce.connection.DB;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.UserLogin;
import com.ecommerce.service.CartService;
import com.ecommerce.service.CartServiceIml;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.OrderServiceImpl;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ProductServiceImpl;
import com.ecommerce.service.UserService;
import com.ecommerce.service.UserServiceImp;

public class EcommerceApplication {
	public static void allProducts() {
		ProductService prouctService = new ProductServiceImpl(DB.connectDb());
		List<Product> getProductList = prouctService.fetchAllProduct();
		System.out.println(
				"________________________________________________________________________________________________________________________");
		System.out.println("productId Product Name  Product Description             ProductPrice productQty ");
		getProductList.forEach(it -> {
			System.out.println(
					"-------------------------------------------------------------------------------------------------------------");
			System.out.println(it.getId() + ",     " + it.getProductName() + ",  " + it.getProductDecription() + ", "
					+ it.getProductPrice() + ", " + it.getProductQty());
		});
		System.out.println(
				"__________________________________________________________________________________________________________________________");
	}

	public static void run() {
		Scanner sc = new Scanner(System.in);
		UserService userService = new UserServiceImp(DB.connectDb());
		ProductService prouctService = new ProductServiceImpl(DB.connectDb());
		CartService cartService = new CartServiceIml(DB.connectDb());
		OrderService orderService = new OrderServiceImpl(DB.connectDb());
		boolean check = true;
//		while(check) {

		allProducts();
		System.out.println("Do you want to Purchaser product! Enter 1 for puchase if not 2");
		int input = sc.nextInt();
		switch(input) {
		case 1:
			System.out.println("Please Login first");
			System.out.print("Please Enter Email: ");
			String userName = sc.next();
			System.out.println("Please Enter Your Password");
			String pass = sc.next();
			UserLogin loginUser = new UserLogin(userName, pass);
			String token = userService.loginUser(loginUser);
			allProducts();
			System.out.println("To Buy Product, Please Select Product Id");
			int id = sc.nextInt();
			System.out.println("Please Select Product QTY");
			int oty = sc.nextInt();
			Cart cart = new Cart();
			cart.setProductId(id);
			cart.setProductQty(oty);
			int addToCart = cartService.addToCart(token, cart);
			if(addToCart==1) {
				System.out.println("added");
			}
			break;
		default :
			check =false;
			break;
		}

	}
//	}

	public static void main(String[] args) {
		run();
	}
}
