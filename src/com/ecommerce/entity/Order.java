package com.ecommerce.entity;

public class Order {
	private int orderId;
	private int userId;
	private int productId;
	private int productOty;
	private int orderPrice;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductOty() {
		return productOty;
	}
	public void setProductOty(int productOty) {
		this.productOty = productOty;
	}
	public int getProductPrice() {
		return orderPrice;
	}
	public void setProductPrice(int productPrice) {
		this.orderPrice = productPrice;
	}

}
