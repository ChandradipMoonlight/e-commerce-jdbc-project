package com.ecommerce.controller;

import java.util.List;

import com.ecommerce.connection.DB;
import com.ecommerce.entity.User;
import com.ecommerce.entity.UserLogin;
import com.ecommerce.service.UserService;
import com.ecommerce.service.UserServiceImp;

public class Main {


	public static void main(String[] args) {
		UserService userService = new UserServiceImp(DB.connectDb());
		User user = new User();
		user.setUserName("Chandradip");
		user.setUserEmail("moonlight@gmail.com");
		user.setUserPassword("dipak@11");
		
//		userService.createUserRegistration(user);
//		
//		UserLogin loginUser = new UserLogin("shraddha@gmail.com", "125");
//		userService.loginUser(loginUser);
		String token = "moonlight@gmail.com";
		List<User> getUserList = userService.getAllUsers(token);
		
		getUserList.forEach(it->{
			System.out.println("--------------------------------------------------------------------------");
			System.out.println("User Name = "+it.getUserName()+", User Email = "+it.getUserEmail()+
					", Is Admin = "+ (it.isAdmin() ? "YES" : "NO"));
		});
	}
}
