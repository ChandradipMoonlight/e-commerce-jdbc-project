package com.ecommerce.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.entity.User;
import com.ecommerce.entity.UserLogin;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.properites.MessageProperties;


public class UserServiceImp implements UserService {
	private Connection con;
	
	public UserServiceImp(Connection con) {
		this.con = con;
	}
	
	public ResultSet findUserByEmail(String email) {
		PreparedStatement findUserByEmail = null;
		ResultSet set = null;
		try {
			 findUserByEmail = con.prepareStatement("select user_password, user_email, is_admin, user_id from user where user_email=?");
			findUserByEmail.setString(1, email);
			set = findUserByEmail.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return set;
	}
	/**
	 * This is method to Register as new user or new Admin
	 * @param user
	 * @return
	 */
	public boolean createUserRegistration(User user) {
		boolean isUserRegistred = false;
		
		try {
			if(findUserByEmail(user.getUserEmail()).next()){
				throw new EcommerceException("This User Name is already registerd, Please Login!", 
						EcommerceException.ExceptionType.USER_ALREADY_PRESENT);
			}
			PreparedStatement statemet = con.prepareStatement("INSERT INTO user(is_admin, user_name, user_email, user_password) VALUES (?,?,?,?)");
			statemet.setBoolean(1, user.isAdmin());
			statemet.setString(2, user.getUserName());
			statemet.setString(3, user.getUserEmail());
			statemet.setString(4, user.getUserPassword());
			isUserRegistred = statemet.execute();
			statemet.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isUserRegistred;
	}
	
	public String loginUser(UserLogin userLogin) {
		String token = "";
		try {
			ResultSet userByEmail = findUserByEmail(userLogin.getUserEmail());
			if(!userByEmail.next()) {
				throw new EcommerceException("User with this email is not registered, Please signUp First ", EcommerceException.ExceptionType.EMAIL_NOT_FOUND);
			}
			boolean password = userLogin.getUserPass().equals(userByEmail.getString(1));
			if(!password) {
				throw new EcommerceException("Incorrect password", EcommerceException.ExceptionType.PASSWORD_INCORRECT);
			}
			token = userByEmail.getString(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(MessageProperties.LOGIN_SUCCESSFUL.getMessage());
		return token;
	}
	
	@Override
	public List<User> getAllUsers(String token) {
		List<User> userList = new ArrayList<>();
		User user = null;
		try {
			ResultSet userByEmail = findUserByEmail(token);
			if(!userByEmail.next()) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			boolean isAdmin = userByEmail.getBoolean(3);
			if(!isAdmin) {
				throw new EcommerceException(MessageProperties.GET_PERMISSION.getMessage(),
						EcommerceException.ExceptionType.UNAUTHORISED_USER );
			}
			PreparedStatement state = con.prepareStatement("select user_name, user_email, is_admin from user");
			ResultSet rs = state.executeQuery();
			while(rs.next()) {
				user = new User();
				user.setUserName(rs.getString(1));
				user.setUserEmail(rs.getString(2));
				user.setAdmin(rs.getBoolean(3));
				userList.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userList;
	}
	
}
