package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.User;
import com.ecommerce.entity.UserLogin;

public interface UserService {

	public boolean createUserRegistration(User user);

	public String loginUser(UserLogin loginUser);

	public List<User> getAllUsers(String token);

	public int makeUserAsAdmin(String token, int userId);

}
