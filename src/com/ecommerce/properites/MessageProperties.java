package com.ecommerce.properites;

public enum MessageProperties {
	REGISTRATION_SUCCESSFUL("Registration Successful."),
	LOGIN_SUCCESSFUL("Login Successful."),
	INTERNAL_ERROER("Somthing is went Wrong."),
	PLEASE_LOGIN("Please Login First."),
	GET_PERMISSION("Sorry! you don't have access. Please contact with the Admin"),
	PRODUCT_NOT_FOUND("Product not found."),
	CART_NOT_FOUND("cart not found"),
	SELECT_PRODUCT("Please Select at least one Product to place order"),
	USER_ALREADY_EXIST("User Already Exist.");
	
	private String message;

    MessageProperties(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
