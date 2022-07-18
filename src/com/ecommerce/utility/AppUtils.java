package com.ecommerce.utility;

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

public class AppUtils {
	
	public static void allProducts() {
		ProductService prouctService = new ProductServiceImpl(DB.connectDb());
		List<Product> getProductList = prouctService.fetchAllProduct();
		System.out.println(
				"________________________________________________________________________________________________________________________");
		System.out.println("productId : Product Name   Product Description    Proudct price ");
		getProductList.forEach(it -> {
			System.out.println(
					"-------------------------------------------------------------------------------------------------------------");
			System.out.println(it.getId() + ",     " + it.getProductName() + ",  " + it.getProductDecription() + ", "
					+ it.getProductPrice());
		});
		System.out.println(
				"__________________________________________________________________________________________________________________________");
	}
	
	public static void getCartDetailsOfUser(String token) {
		System.out.println(MessageProperties.CART_DETAILS.getMessage());
		CartService cartService = new CartServiceIml(DB.connectDb());
		List<CartModel> cartDetails = cartService.getCartDetails(token);
		System.out.println(
				"____________________________________________________________________________________________________________________________________");
		System.out.println("ProductId : Product Name   : ProductPrice: TotalQty of product : TotalPrice");
		cartDetails.forEach(it -> {
			System.out.println(it.getProductId() + "         : " + it.getProductName() + "  : " + it.getProductPrice()
					+ "         : " + it.getProductQty() + "                : " + it.getTotalPrice());
		});
		System.out.println(
				"_____________________________________________________________________________________________________________________________________");

	}
	
	public static void operation(String token, Scanner sc) {
		CartService cartService = new CartServiceIml(DB.connectDb());
		OrderService orderService = new OrderServiceImpl(DB.connectDb());
		System.out.println("***************************************Proudct Details**************************************");
		AppUtils.allProducts();
		System.out.println("To Add Product in Cart, Please Select Product Id");
		int id = sc.nextInt();
		System.out.println("Please Select Product QTY");
		int oty = sc.nextInt();
		Cart cart = new Cart();
		cart.setProductId(id);
		cart.setProductQty(oty);
		int addToCart = cartService.addToCart(token, cart);
		if (addToCart == 1) {
			System.out.println("*********************************Your Cart Detils****************************************");
			AppUtils.getCartDetailsOfUser(token);
			Order order = new Order();
			System.out.println("To Place Order please Select Id of Product");
			int productId = sc.nextInt();
			System.out.println("Please Select Qty of product");
			int qty = sc.nextInt();
			order.setProductId(productId);
			order.setProductOty(qty);
			int placeOrder = orderService.placeOrder(token, order);
			if (placeOrder == 1) {
				System.out.println("************************Your Cart Datails After Order is placed**********************");
				AppUtils.getCartDetailsOfUser(token);
			} else {
				System.out.println(MessageProperties.INTERNAL_ERROER.getMessage());
			}

		} else {
			System.out.println(MessageProperties.INTERNAL_ERROER.getMessage());
		}

	}
	
	public static String login(String token, Scanner sc) {
		UserService userService = new UserServiceImp(DB.connectDb());
		System.out.println("Please Login first");
		System.out.print("Please Enter Email: ");
		String userName = sc.next();
		System.out.println("Please Enter Your Password");
		String pass = sc.next();
		UserLogin loginUser = new UserLogin(userName, pass);
		
		try {
			token = userService.loginUser(loginUser);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return token;
	}
	
	public static void AllUser(String token) {
		UserServiceImp userService = new UserServiceImp(DB.connectDb());
		System.out.println("____________________________________________________________________________________________________________________________");
		System.out.println("UserId : Name         : Email                : IsAmdmin  " );
		System.out.println("--------------------------------------------------------------------------------------------------------------");
		userService.getAllUsers(token).forEach(it->{
			System.out.println(it.getUserId() +"      : "+it.getUserName()+"  : "+it.getUserEmail()+" : " +(it.isAdmin() ? "YES" : "NO"));
		});
		System.out.println("____________________________________________________________________________________________________________________________________________");
	}
	
	public static void orderHistoryOfUser(String token, int userId) {
		OrderServiceImpl orderService = new OrderServiceImpl(DB.connectDb());
		System.out.println("________________________________________________________________________________________________________________________________");
		System.out.println("OrderId : Product name   : proudctQTY : OrderPrice");
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		orderService.checkuserOrderHistory(userId, token).forEach(it->{
			System.out.println(it.getOrderId()+"       : "+it.getProductName()+"   :  "+it.getProductOty()+" : "+it.getOrderPrice());
		});
		System.out.println("____________________________________________________________________________________________________________________________________");
	}
		
	
}
