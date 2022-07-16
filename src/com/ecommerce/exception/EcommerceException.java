package com.ecommerce.exception;

public class EcommerceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public enum ExceptionType {
        USER_ALREADY_PRESENT,
        EMAIL_NOT_FOUND,
        PASSWORD_INCORRECT,
        EMAIL_NOT_VERIFIED,
        UNAUTHORISED_USER,
        PRODUCT_NOT_FOUND,
        INTERNAL_ERROR,
    }
	public EcommerceException.ExceptionType type;
	
	public EcommerceException(String msg, EcommerceException.ExceptionType type) {
		super(msg);
		this.type=type;
	}
	
	public EcommerceException(String msg) {
		super(msg);
	}
}
