package com.ecommerce.entity;

public class UserLogin {
	private String userEmail;
	private String userPass;
	
	public UserLogin(User user) {
		this.userEmail = user.getUserEmail();
		this.userPass = user.getUserPassword();
	}
	public UserLogin() {}
	public UserLogin(String userEmail, String pass) {
		this.userEmail = userEmail;
		this.userPass = pass;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	
	
	
}
