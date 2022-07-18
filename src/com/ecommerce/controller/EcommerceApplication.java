package com.ecommerce.controller;

import java.util.Scanner;

import com.ecommerce.connection.DB;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.properites.MessageProperties;
import com.ecommerce.service.ProductServiceImpl;
import com.ecommerce.service.UserServiceImp;
import com.ecommerce.utility.AppUtils;

public class EcommerceApplication {

	public static void run() {
		UserServiceImp userService = new UserServiceImp(DB.connectDb());
		ProductServiceImpl productService = new ProductServiceImpl(DB.connectDb());
		Scanner sc = new Scanner(System.in);
		String token = null;
		token = AppUtils.login(token, sc);
		if (token == null) {
			System.out.println("Do you wan to register with us, Plese enter 1 for Yes else enter any number");
			int option = sc.nextInt();
			if (option == 1) {
				User user = new User();
				System.out.print("Please Enter Your Name : ");
				String name = sc.next();
				System.out.print("please Enter Your Email: ");
				String email = sc.next();
				System.out.print("Please Set Password : ");
				String password = sc.next();
				user.setUserName(name);
				user.setUserEmail(email);
				user.setUserPassword(password);
				userService.createUserRegistration(user);
				token = AppUtils.login(token, sc);
				AppUtils.operation(token, sc);
			} else {
				System.out.println(MessageProperties.VISITE_AGAIN.getMessage());
			}

		} else {
			AppUtils.allProducts();
			System.out.println("Select Option from following : ");
			System.out.println("To know all Users : 1");
			System.out.println("To check order history of user : 2");
			System.out.println("To add products: 3");
			int select = sc.nextInt();
			switch (select) {
			case 1:
				AppUtils.AllUser(token);
				break;
			case 2:
				AppUtils.AllUser(token);
				System.out.println("Please Select Id of User to check Order History: ");
				int id = sc.nextInt();
				AppUtils.orderHistoryOfUser(token, id);
				break;
			case 3:
				Product pr = new Product();
				System.out.println("Please Enter Product Name: ");
				String pName = sc.next();
				pr.setProductName(pName);
				System.out.println("Please set Product price :");
				int pPrice = sc.nextInt();
				pr.setProductPrice(pPrice);
				System.out.println("Please Enter Product Description : ");
				String desc = sc.next();
				pr.setProductDecription(desc);
				System.out.println("Please Set Qty Of prodcut : ");
				int qty = sc.nextInt();
				pr.setProductQty(qty);
				productService.addProducts(pr, token);
				break;
			default:
				boolean flag = true;
				while (flag) {
					System.out.println("do you want ot purchase, please enter 1 else enter any number :");
					int res = sc.nextInt();
					flag = res == 1 ? true : false;
					if (!flag) {
						System.out.println(MessageProperties.VISITE_AGAIN.getMessage());
						break;
					}
					AppUtils.operation(token, sc);

				}
			}
			

		}
		System.out.println(MessageProperties.VISITE_AGAIN.getMessage());
	}

	public static void main(String[] args) {
		run();
	}
}
