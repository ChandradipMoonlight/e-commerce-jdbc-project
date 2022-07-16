package com.ecommerce.properites;

public enum MessageProperties {
	REGISTRATION_SUCCESSFUL("Registration Successful."),
	LOGIN_SUCCESSFUL("Login Successful."),
	INTERNAL_ERROER("Somthing is went Wrong."),
	PLEASE_LOGIN("Please Login First."),
	GET_PERMISSION("Sorry! you don't have access. Please contact with the Admin"),
	USER_ALREADY_EXIST("User Already Exist.");
	
	private String message;

    MessageProperties(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
