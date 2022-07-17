package com.ecommerce.controller;

import java.util.List;
import java.util.Scanner;

import com.ecommerce.connection.DB;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.UserLogin;
import com.ecommerce.model.CartModel;
import com.ecommerce.properites.MessageProperties;
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
		System.out.println("productId Product Name  Product Description   ");
		getProductList.forEach(it -> {
			System.out.println(
					"-------------------------------------------------------------------------------------------------------------");
			System.out.println(it.getId() + ",     " + it.getProductName() + ",  " + it.getProductDecription() + ", "
					+ it.getProductPrice());
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
		switch (input) {
		case 1:
			System.out.println("Please Login first");
			System.out.print("Please Enter Email: ");
			String userName = sc.next();
			System.out.println("Please Enter Your Password");
			String pass = sc.next();
			UserLogin loginUser = new UserLogin(userName, pass);
			String token = userService.loginUser(loginUser);
			allProducts();
			System.out.println("To Add Product in Cart, Please Select Product Id");
			int id = sc.nextInt();
			System.out.println("Please Select Product QTY");
			int oty = sc.nextInt();
			Cart cart = new Cart();
			cart.setProductId(id);
			cart.setProductQty(oty);
			int addToCart = cartService.addToCart(token, cart);
			if (addToCart == 1) {
				System.out.println(MessageProperties.PRODUCT_ADDED_SUCCESSFULLY.getMessage());
				System.out.println(MessageProperties.CART_DETAILS.getMessage());
				List<CartModel> cartDetails = cartService.getCartDetails(token);
				System.out.println(
						"____________________________________________________________________________________________________________________________________");
				System.out.println("ProductId : Product Name   : ProductPrice: TotalQty of product : TotalPrice");
				cartDetails.forEach(it -> {
					System.out.println(
							it.getProductId() + "         : " + it.getProductName() + "  : " + it.getProductPrice()
									+ "         : " + it.getProductQty() + "                : " + it.getTotalPrice());
				});
				System.out.println(
						"_____________________________________________________________________________________________________________________________________");
				
				Order order = new Order();
				System.out.println("To Place Order please Select Id of Product");
				int productId =sc.nextInt();
				System.out.println("Please Select Qty of product");
				int qty = sc.nextInt();
				order.setProductId(productId);
				order.setProductOty(qty);
				int placeOrder = orderService.placeOrder(token, order);
				if(placeOrder==1) {
					System.out.println(MessageProperties.ORDER_PLACED.getMessage());
					
				} else {
					System.out.println(MessageProperties.INTERNAL_ERROER.getMessage());
				}
				
			} else {
				System.out.println(MessageProperties.INTERNAL_ERROER.getMessage());
			}
			break;
		default:
			check = false;
			break;
		}

//	}
	}

	public static void main(String[] args) {
		run();
//		CartServiceIml cartService = new CartServiceIml(DB.connectDb());
//		List<CartModel> cartDetails = cartService.getCartDetails("bhushan@gmail.com");
//		System.out.println(
//				"____________________________________________________________________________________________________________________________________");
//		System.out.println("ProductId : Product Name   : ProductPrice: TotalQty of product : TotalPrice");
//		cartDetails.forEach(it -> {
//			System.out.println(
//					it.getProductId() + "         : " + it.getProductName() + "  : " + it.getProductPrice()
//							+ "         : " + it.getProductQty() + "                : " + it.getTotalPrice());
//		});
//		System.out.println(
//				"_____________________________________________________________________________________________________________________________________");
//		
//		Cart cart = new Cart();
//		cart.setProductId(1);
//		cart.setProductQty(2);
//		int addToCart = cartService.addToCart("dipak@gmail.com", cart);
	}
}
